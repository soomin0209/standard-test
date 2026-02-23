package com.example.sparta.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCreateRequest {
    @Min(1)
    private Long totalPrice;
    private List<OrderLineRequest> orderLines;
}
