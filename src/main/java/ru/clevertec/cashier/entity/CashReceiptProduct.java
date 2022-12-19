package ru.clevertec.cashier.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class CashReceiptProduct {

    private Integer quantity;
    private Product product;
    private TotalPrice totalPrice;
}
