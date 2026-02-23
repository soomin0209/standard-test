package com.example.sparta.util;

import com.example.sparta.entity.Order;
import com.example.sparta.entity.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductServiceSupport {

    public static List<Product> extractFromOrderList(List<Order> orderList) {
        // 상품 판매 횟수 맵 -> Key: 상품, Value: 해당 상품의 판매 횟수
        Map<Product, Long> saleCountMap = new HashMap<>();

        // order.OrderLine 을 확인하여 상품과 해당 상품의 주문량을 saleCountMap에 합산
        orderList.forEach(
                order -> order.getOrderLines().forEach(
                        ol -> saleCountMap.put(
                                ol.getProduct(),
                                saleCountMap.getOrDefault(ol.getProduct(), 0L) + ol.getAmount()
                        )
                )
        );

        return saleCountMap.entrySet().stream()  				// saleCountMap 에서
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))   // Value 로 내림차순
                .limit(3)    // 그중 1, 2, 3번째를 가져옴 (탑 쓰리)
                .map(Map.Entry::getKey)   // 해당하는 맵의 Key 만 가져옴 (Product)
                .toList();    // 리스트로 변환 (List<Product>)
    }
}
