package com.example.sparta.payment;

public interface PaymentClient {

    boolean validatePayment(String paymentKey, Long amount);
}
