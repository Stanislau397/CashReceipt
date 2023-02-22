package ru.clevertec.cashreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TotalPrice {

    private BigDecimal itemTotal;
    private BigDecimal discount;
    private BigDecimal subtotal;
}
