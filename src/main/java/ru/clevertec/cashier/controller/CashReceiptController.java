package ru.clevertec.cashier.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.cashier.entity.CashReceipt;
import ru.clevertec.cashier.entity.CashReceiptProduct;
import ru.clevertec.cashier.entity.DiscountCard;
import ru.clevertec.cashier.entity.Supermarket;
import ru.clevertec.cashier.entity.TotalPrice;
import ru.clevertec.cashier.parser.ItemsParser;
import ru.clevertec.cashier.service.DiscountCardService;
import ru.clevertec.cashier.service.ProductService;
import ru.clevertec.cashier.util.PdfGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class CashReceiptController {

    private static final String ITEM = "item";
    private static final String CARD = "card";
    private static final String CHECK_URN = "/check";

    private ItemsParser parametersParser;
    private PdfGenerator pdfGenerator;
    private ProductService productService;
    private DiscountCardService discountCardService;

    @GetMapping(CHECK_URN)
    public void generatePdfFile(HttpServletRequest request, HttpServletResponse response) {
        String[] itemArray = request.getParameterValues(ITEM);
        String cardId = request.getParameter(CARD);

        DiscountCard discountCard = DiscountCard.builder().build();
        if (cardId != null) {
            discountCard = discountCardService.findDiscountCardById(cardId);
        }

        Map<Long, Integer> productIdAndQuantityMap = parametersParser.parse(itemArray);
        List<CashReceiptProduct> cashReceiptProducts = productService
                .buildCashReceiptProductsByProductIdAndQuantity(productIdAndQuantityMap);
        TotalPrice totalPrice = productService
                .buildTotalPriceForAllCashReceiptProducts(cashReceiptProducts, discountCard);

        Supermarket supermarket = Supermarket.builder()
                .name("SUPERMARKET 123")
                .phoneNumber("123-456-7890")
                .address("12 MILKYWAY Galaxy/Earth")
                .build();

        CashReceipt cashReceipt = CashReceipt.builder()
                .cashReceiptId(12345L)
                .supermarket(supermarket)
                .cashReceiptProducts(cashReceiptProducts)
                .totalPriceForProducts(totalPrice)
                .printDate(LocalDate.now())
                .printTime(LocalTime.now())
                .build();

        pdfGenerator.generate(cashReceipt, response);
    }
}
