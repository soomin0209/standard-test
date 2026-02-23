package com.example.sparta.payment;

import org.springframework.stereotype.Component;

@Component
public class NaverPaymentClient implements PaymentClient {

    @Override
    public boolean validatePayment(String paymentKey, Long amount) {
        return false;
    }
}
