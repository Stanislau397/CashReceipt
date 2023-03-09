package ru.clevertec.cashreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class CashReceipt {

    private Long cashReceiptId;
    private Supermarket supermarket;
    private List<CashReceiptProduct> cashReceiptProducts;
    private TotalPrice totalPriceForProducts;
    private LocalDate printDate;
    private LocalTime printTime;
}
