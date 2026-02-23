package com.example.sparta.test2;

import com.example.sparta.entity.Order;
import com.example.sparta.entity.OrderLine;
import com.example.sparta.entity.Product;
import com.example.sparta.util.ProductServiceSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ProductServiceSupportTest {

    @Test
    public void extractFromOrderList() {
        // given
        Product shirts = new Product(1L, "티셔츠", "무지 티 입니다.", 10000L, 100L, 0L);
        Product pants = new Product(2L, "바지", "긴 바지 입니다.", 15000L, 50L, 0L);
        Product shoes = new Product(3L, "신발", "컨버스 신발입니다.", 18000L, 200L, 0L);
        Product acc = new Product(4L, "악세서리", "목걸이입니다.", 1000L, 1000L, 0L);
        Order order = new Order(10000L);
        OrderLine orderLine1 = new OrderLine(order, shirts, 50L);  // 구매량 1등
        OrderLine orderLine2 = new OrderLine(order, pants, 30L);  // 구매량 3등
        OrderLine orderLine3 = new OrderLine(order, shoes, 10L);  // 꼴등
        OrderLine orderLine4 = new OrderLine(order, acc, 40L);  // 구매량 2등

        // when
        List<Product> products = ProductServiceSupport.extractFromOrderList(List.of(order));

        // then
        assertThat(products.size()).isEqualTo(3); // 상품 리스트 크기 검증
        assertThat(products.get(0).getId()).isEqualTo(1L); // 첫번째 상품이 구매량 1등인 shirts 인지 검증
        assertThat(products.get(1).getId()).isEqualTo(4L); // 두번째 상품이 구매량 2등인 acc 인지 검증
        assertThat(products.get(2).getId()).isEqualTo(2L); // 마지막 상품이 구매량 3등인 pants 인지 검증
    }
}