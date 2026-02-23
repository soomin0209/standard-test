package com.example.sparta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCreateRequest {

    private String paymentKey;
    private Long totalAmount;
}
