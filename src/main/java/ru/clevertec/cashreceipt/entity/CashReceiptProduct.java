package ru.clevertec.cashreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CashReceiptProduct {

    private Integer quantity;
    private Product product;
    private TotalPrice totalPrice;
}
