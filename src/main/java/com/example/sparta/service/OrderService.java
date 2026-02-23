package com.example.sparta.service;

import com.example.sparta.dto.OrderLineRequest;
import com.example.sparta.dto.PaymentCreateRequest;
import com.example.sparta.entity.Order;
import com.example.sparta.entity.OrderLine;
import com.example.sparta.entity.Product;
import com.example.sparta.payment.PaymentClient;
import com.example.sparta.repository.OrderLineRepository;
import com.example.sparta.repository.OrderRepository;
import com.example.sparta.repository.ProductRepository;
import com.example.sparta.dto.OrderCreateRequest;
import com.example.sparta.util.OrderServiceSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;
    private final PaymentClient paymentClient;

    @Transactional
    public Order create(OrderCreateRequest request) {
        // 주문 생성
        Order order = orderRepository.save(new Order(request.getTotalPrice()));

        // 주문 요청한 상품의 ID 리스트
        List<Long> productIds = request.getOrderLines().stream()
                .map(OrderLineRequest::getProductId)
                .toList();

        // 주문 요청한 상품 리스트 조회
        List<Product> products = productRepository.findByIdIn(productIds);

        List<OrderLine> orderLineList = OrderServiceSupport.buildOrderLines(products, request.getOrderLines(), order);
        orderLineRepository.saveAll(orderLineList);
        return order;
    }

    @Transactional
    public Long placeOrder(PaymentCreateRequest request) {
        // 1. 외부 PG사에 결제 유효성 검증 요청
        boolean isValid = paymentClient.validatePayment(request.getPaymentKey(), request.getTotalAmount());

        if (!isValid) {
            throw new IllegalArgumentException("결제 검증 실패: 유효하지 않은 결제입니다.");
        }

        // 2. 검증 토오가 시 주문 저장 로직 실행
        Order order = new Order(request.getTotalAmount());
        return orderRepository.save(order).getId();
    }
}