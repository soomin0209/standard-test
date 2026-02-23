package com.example.sparta.service;

import com.example.sparta.entity.Order;
import com.example.sparta.entity.Product;
import com.example.sparta.repository.OrderRepository;
import com.example.sparta.util.ProductServiceSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final OrderRepository orderRepository;

    // 최근 일주일동안 가장 많이 팔린 상품 탑 쓰리 가져오기.
    public List<Product> findHit3Products() {
        // 조회 시작 기간 : 일주일 전
        LocalDateTime from = LocalDateTime.now().minusDays(7);

        // 최근 일주일 간 발생한 주문
        List<Order> orderList = orderRepository.findByCreatedAtIsAfter(from);

        return ProductServiceSupport.extractFromOrderList(orderList);
    }
}