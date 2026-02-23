package com.example.sparta.repository;

import com.example.sparta.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIdIn(List<Long> productIds);

    // 상품명으로 조회하는 쿼리 메서드 (실습용)
    List<Product> findByName(String name);
}
