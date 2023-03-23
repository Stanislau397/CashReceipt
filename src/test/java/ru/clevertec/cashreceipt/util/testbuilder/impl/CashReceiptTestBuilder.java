package ru.clevertec.cashreceipt.util.testbuilder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.CashReceipt;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.Supermarket;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.util.testbuilder.TestBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCashReceipt")
@With
@EqualsAndHashCode
@ToString
public class CashReceiptTestBuilder implements TestBuilder<CashReceipt> {

    private Long cashReceiptId = 1L;
    private Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
    private List<CashReceiptProduct> cashReceiptProducts = List.of(
            CashReceiptProductTestBuilder.aCashReceiptProduct().build(),
            CashReceiptProductTestBuilder.aCashReceiptProduct().withQuantity(100).build()
    );
    private TotalPrice totalPriceForProducts = TotalPriceTestBuilder.aTotalPrice().build();
    private LocalDate printDate = LocalDate.now();
    private LocalTime printTime = LocalTime.now();

    @Override
    public CashReceipt build() {
        return CashReceipt.builder()
                .cashReceiptId(cashReceiptId)
                .cashReceiptProducts(cashReceiptProducts)
                .totalPriceForProducts(totalPriceForProducts)
                .printDate(printDate)
                .printTime(printTime)
                .supermarket(supermarket)
                .build();
    }
}
