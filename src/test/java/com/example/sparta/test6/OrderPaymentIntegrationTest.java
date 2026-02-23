package com.example.sparta.test6;

import com.example.sparta.dto.PaymentCreateRequest;
import com.example.sparta.payment.PaymentClient;
import com.example.sparta.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderPaymentIntegrationTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean // 실제 PaymentClient 대신 가짜 객체 주입
    private PaymentClient paymentClient;

    @Test
    void 외부_PG사_검증_성공_시_주문_성공() {
        // given
        String paymentKey = "imp_123456";
        Long amount = 15000L;
        PaymentCreateRequest request = new PaymentCreateRequest(paymentKey, amount);

        // [Stubbing] 가짜 행동 정의: "이 키와 금액으로 물어보면 무조건 true를 반환해!"
        given(paymentClient.validatePayment(paymentKey, amount))
                .willReturn(true);

        // when
        Long orderId = orderService.placeOrder(request);

        // then
        assertThat(orderId).isNotNull();

        // [Verify] 실제로 외부 API(Mock)가 호출되었는지 확인
        verify(paymentClient, times(1)).validatePayment(paymentKey, amount);
    }

    @Test
    void 외부_PG사_검증_실패_시_예외발생() {
        // given
        PaymentCreateRequest request = new PaymentCreateRequest("invalid_key", 15000L);

        // [Stubbing] 가짜 행동 정의: "false를 반환해!"
        given(paymentClient.validatePayment(any(), any()))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 검증 실패: 유효하지 않은 결제입니다.");
    }
}