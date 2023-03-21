package ru.clevertec.cashreceipt.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.Supermarket;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.util.testbuilder.impl.CashReceiptProductTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.SuperMarketTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.TotalPriceTestBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PdfGeneratorTest {

    private PdfGenerator pdfGenerator;
    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document();
        document.open();
        pdfGenerator = Mockito.spy(new PdfGenerator());
    }

    @AfterEach
    void tearDown() {
        document.close();
        pdfGenerator = null;
    }


//    @Nested
//    class BuildHeaderTest {
//
//        private static Stream<Supermarket> supermarketArgumentsProvider() {
//            return Stream.of(
//                    SuperMarketTestBuilder.aSupermarket().build(),
//                    SuperMarketTestBuilder.aSupermarket()
//                            .withPhoneNumber("+111111 111-11-11")
//                            .build()
//            );
//        }
//
//        private static Stream<String> currentDateArgumentsProvider() {
//            return Stream.of("12/11/2022", "01/01/2023");
//        }
//
//        private static Stream<String> currentTimeArgumentsProvider() {
//            return Stream.of("11:25:54", "12:16:32");
//        }
//
//        @ParameterizedTest
//        @MethodSource("supermarketArgumentsProvider")
//        @SneakyThrows(DocumentException.class)
//        void checkBuildHeaderShouldCaptureSupermarket(Supermarket supermarket) {
//            ArgumentCaptor<Supermarket> supermarketCaptor = ArgumentCaptor.forClass(Supermarket.class);
//            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//
//            doNothing().when(pdfGenerator).buildHeader(
//                    any(Document.class),
//                    supermarketCaptor.capture(),
//                    any(String.class),
//                    any(String.class)
//            );
//            pdfGenerator.buildHeader(document, supermarket, currentDate, currentTime);
//
//            assertThat(supermarket).isEqualTo(supermarketCaptor.getValue());
//        }
//
//        @ParameterizedTest
//        @MethodSource("currentDateArgumentsProvider")
//        @SneakyThrows(DocumentException.class)
//        void checkBuildHeaderShouldCaptureCurrentDate(String currentDate) {
//            Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
//            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//            ArgumentCaptor<String> currentDateCaptor = ArgumentCaptor.forClass(String.class);
//
//            doNothing().when(pdfGenerator).buildHeader(
//                    any(Document.class),
//                    any(Supermarket.class),
//                    currentDateCaptor.capture(),
//                    any(String.class)
//            );
//            pdfGenerator.buildHeader(document, supermarket, currentDate, currentTime);
//
//            assertThat(currentDate).isEqualTo(currentDateCaptor.getValue());
//        }
//
//        @ParameterizedTest
//        @MethodSource("currentTimeArgumentsProvider")
//        @SneakyThrows(DocumentException.class)
//        void checkBuildHeaderShouldCaptureCurrentTime(String currentTime) {
//            Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
//            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//            ArgumentCaptor<String> currentTimeCaptor = ArgumentCaptor.forClass(String.class);
//
//            doNothing().when(pdfGenerator).buildHeader(
//                    any(Document.class),
//                    any(Supermarket.class),
//                    any(String.class),
//                    currentTimeCaptor.capture()
//            );
//            pdfGenerator.buildHeader(document, supermarket, currentDate, currentTime);
//
//            assertThat(currentTime).isEqualTo(currentTimeCaptor.getValue());
//        }
//
//        @Test
//        @SneakyThrows(DocumentException.class)
//        void checkBuildHeaderShouldBeCalledOneTime() {
//            Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
//            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//
//            pdfGenerator.buildHeader(document, supermarket, currentDate, currentTime);
//
//            verify(pdfGenerator, times(1))
//                    .buildHeader(document, supermarket, currentDate, currentTime);
//        }
//    }
//
//    @Nested
//    class BuildTableTest {
//
//        private static Stream<List<CashReceiptProduct>> cashReceiptProductsArgumentsProvider() {
//            return Stream.of(
//                    List.of(
//                            CashReceiptProductTestBuilder.aCashReceiptProduct()
//                                    .build(),
//                            CashReceiptProductTestBuilder.aCashReceiptProduct()
//                                    .withQuantity(10)
//                                    .build()),
//                    List.of(CashReceiptProductTestBuilder.aCashReceiptProduct()
//                                    .build(),
//                            CashReceiptProductTestBuilder.aCashReceiptProduct()
//                                    .withQuantity(5)
//                                    .build())
//            );
//        }
//
//        @ParameterizedTest
//        @MethodSource("cashReceiptProductsArgumentsProvider")
//        @SneakyThrows(DocumentException.class)
//        void checkBuildTableShouldCaptureCashReceiptProducts(List<CashReceiptProduct> cashReceiptProducts) {
//            ArgumentCaptor<List<CashReceiptProduct>> cashReceiptProductsCaptor = ArgumentCaptor.forClass(List.class);
//
//            doNothing().when(pdfGenerator).buildTable(
//                    any(Document.class),
//                    cashReceiptProductsCaptor.capture()
//            );
//            pdfGenerator.buildTable(document, cashReceiptProducts);
//
//            assertThat(cashReceiptProducts).isEqualTo(cashReceiptProductsCaptor.getValue());
//        }
//
//        @ParameterizedTest
//        @MethodSource("cashReceiptProductsArgumentsProvider")
//        @SneakyThrows(DocumentException.class)
//        void checkBuildTableShouldBeCalledOneTime(List<CashReceiptProduct> cashReceiptProducts) {
//            pdfGenerator.buildTable(document, cashReceiptProducts);
//
//            verify(pdfGenerator, times(1))
//                    .buildTable(document, cashReceiptProducts);
//        }
//    }
//
//    @Nested
//    class BuildFooterTest {
//
//        private static Stream<TotalPrice> totalPriceForAllProductsArgumentsProvider() {
//            return Stream.of(
//                    TotalPriceTestBuilder.aTotalPrice().build(),
//                    TotalPriceTestBuilder.aTotalPrice()
//                            .withDiscount(BigDecimal.ZERO)
//                            .build()
//            );
//        }
//
//        @ParameterizedTest
//        @MethodSource("totalPriceForAllProductsArgumentsProvider")
//        @SneakyThrows(DocumentException.class)
//        void checkBuildFooterShouldCaptureTotalPriceForAllProducts(TotalPrice totalPriceForAlProducts) {
//            ArgumentCaptor<TotalPrice> totalPriceCaptor = ArgumentCaptor.forClass(TotalPrice.class);
//
//            doNothing().when(pdfGenerator)
//                    .buildFooter(any(Document.class), totalPriceCaptor.capture());
//            pdfGenerator.buildFooter(document, totalPriceForAlProducts);
//
//            assertThat(totalPriceForAlProducts).isEqualTo(totalPriceCaptor.getValue());
//        }
//
//        @ParameterizedTest
//        @MethodSource("totalPriceForAllProductsArgumentsProvider")
//        @SneakyThrows(DocumentException.class)
//        void checkBuildFooterShouldBeCalledOneTime(TotalPrice totalPriceForAlProducts) {
//            pdfGenerator.buildFooter(document, totalPriceForAlProducts);
//
//            verify(pdfGenerator, times(1))
//                    .buildFooter(document, totalPriceForAlProducts);
//        }
//    }
}