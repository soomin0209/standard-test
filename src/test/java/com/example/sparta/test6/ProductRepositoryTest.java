package com.example.sparta.test6;

import com.example.sparta.entity.Product;
import com.example.sparta.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // JPA 관련 컴포넌트만 로드 (가볍고 빠름)
@ActiveProfiles("test") // application-test.yml 설정 적용 (H2 DB 등)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // [중요] 내가 설정한 DB 설정을 덮어쓰지 마!
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 저장 및 조회 테스트")
    void saveAndFindProduct() {
        // given
        Product product = new Product(null, "맥북 프로", "애플 노트북", 3000000L, 10L, 0L);

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull(); // ID 자동 생성 확인
        assertThat(savedProduct.getName()).isEqualTo("맥북 프로");
        assertThat(savedProduct.getAmount()).isEqualTo(10L);
        assertThat(savedProduct.getSaleCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("상품명으로 조회 (쿼리 메서드 동작 확인)")
    void findByName() {
        // given
        productRepository.save(new Product(null, "아이폰 15", "스마트폰", 1500000L, 100L, 0L));
        productRepository.save(new Product(null, "아이폰 15", "스마트폰(리퍼)", 1300000L, 50L, 0L));
        productRepository.save(new Product(null, "갤럭시 S24", "삼성 스마트폰", 1200000L, 100L, 0L));

        // when
        List<Product> iphones = productRepository.findByName("아이폰 15");

        // then
        assertThat(iphones).hasSize(2); // 이름이 같은 상품 2개가 조회되어야 함
        assertThat(iphones.get(0).getName()).isEqualTo("아이폰 15");
    }

    @Test
    @DisplayName("상품 구매 시 재고 감소 및 판매량 증가가 DB에 반영되는지 확인")
    void verifyUpdateLogic() {
        // given
        Product product = new Product(null, "에어팟", "무선 이어폰", 300000L, 50L, 0L);
        Product savedProduct = productRepository.save(product);

        // when
        // [중요] 엔티티의 비즈니스 메서드 호출 (재고 50 -> 45, 판매량 0 -> 5)
        savedProduct.purchased(5L);

        // JPA의 변경 감지(Dirty Checking)가 동작하여 Update 쿼리가 발생함
        // 확실한 검증을 위해 다시 DB에서 조회 (flush & clear는 @DataJpaTest 환경에서 자동 처리되기도 하지만 명시적으로 조회)
        Product foundProduct = productRepository.findById(savedProduct.getId())
                .orElseThrow();

        // then
        assertThat(foundProduct.getAmount()).isEqualTo(45L);
        assertThat(foundProduct.getSaleCount()).isEqualTo(5L);
    }
}