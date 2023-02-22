package ru.clevertec.cashreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TotalPrice {

    private BigDecimal itemTotal;
    private BigDecimal discount;
    private BigDecimal subtotal;
}
