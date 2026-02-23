package com.example.sparta.test5;

import com.example.sparta.entity.Order;
import com.example.sparta.entity.OrderLine;
import com.example.sparta.entity.Product;
import com.example.sparta.repository.OrderRepository;
import com.example.sparta.repository.ProductRepository;
import com.example.sparta.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        Product p1 = productRepository.save(new Product(null, "티셔츠", "무지 티", 10000L, 100L, 0L));
        Product p2 = productRepository.save(new Product(null, "바지", "긴 바지", 15000L, 100L, 0L));
        Product p3 = productRepository.save(new Product(null, "신발", "컨버스", 20000L, 100L, 0L));
        Product p4 = productRepository.save(new Product(null, "악세서리", "목걸이", 5000L, 100L, 0L));

        Order order = orderRepository.save(new Order(100000L));
        new OrderLine(order, p1, 50L);
        new OrderLine(order, p2, 30L);
        new OrderLine(order, p3, 10L);
        new OrderLine(order, p4, 40L);
    }

    @Test
    void 최근일주일간_판매량_TOP3상품조회() {
        // when
        List<Product> top3 = productService.findHit3Products();

        // then
        assertThat(top3).hasSize(3);
        assertThat(top3.get(0).getName()).isEqualTo("티셔츠");
        assertThat(top3.get(1).getName()).isEqualTo("악세서리");
        assertThat(top3.get(2).getName()).isEqualTo("바지");
    }
}