package ru.clevertec.cashreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class CashReceiptProduct {

    private Integer quantity;
    private Product product;
    private TotalPrice totalPrice;
}
