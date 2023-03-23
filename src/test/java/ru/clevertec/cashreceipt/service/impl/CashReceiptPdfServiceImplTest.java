package ru.clevertec.cashreceipt.service.impl;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;
import ru.clevertec.cashreceipt.entity.CashReceipt;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.Supermarket;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.util.testbuilder.impl.CashReceiptProductTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.CashReceiptTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.SuperMarketTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.TotalPriceTestBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


class CashReceiptPdfServiceImplTest {

    private CashReceiptPdfServiceImpl cashReceiptPdfService;

    @BeforeEach
    void setUp() {
        cashReceiptPdfService = Mockito.spy(new CashReceiptPdfServiceImpl());
    }

    @AfterEach
    void tearDown() {
        cashReceiptPdfService = null;
    }

    @Test
    void checkWriteCashReceiptIntoResponseAsPdfShouldBeCalledOneTime() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        CashReceipt cashReceipt = CashReceiptTestBuilder.aCashReceipt().build();

        cashReceiptPdfService.writeCashReceiptIntoResponseAsPdf(cashReceipt, httpServletResponse);

        verify(cashReceiptPdfService).writeCashReceiptIntoResponseAsPdf(cashReceipt, httpServletResponse);
    }

    @Nested
    class AddSupermarketInfoToHeaderTableTest {

        @Test
        void checkAddSupermarketInfoToHeaderTableShouldCaptureSupermarket() {
            Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
            PdfPTable givenTable = new PdfPTable(1);
            ArgumentCaptor<Supermarket> supermarketArgumentCaptor = ArgumentCaptor.forClass(Supermarket.class);

            doNothing().when(cashReceiptPdfService)
                    .addSupermarketInfoToHeaderTable(supermarketArgumentCaptor.capture(), any(PdfPTable.class));

            cashReceiptPdfService.addSupermarketInfoToHeaderTable(supermarket, givenTable);

            assertThat(supermarketArgumentCaptor.getValue())
                    .isEqualTo(supermarket);
        }

        @Test
        void checkAddSupermarketInfoToHeaderTableShouldCapturePdfTable() {
            Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
            PdfPTable givenTable = new PdfPTable(1);
            ArgumentCaptor<PdfPTable> pdfPTableArgumentCaptor = ArgumentCaptor.forClass(PdfPTable.class);

            doNothing().when(cashReceiptPdfService)
                    .addSupermarketInfoToHeaderTable(any(Supermarket.class), pdfPTableArgumentCaptor.capture());

            cashReceiptPdfService.addSupermarketInfoToHeaderTable(supermarket, givenTable);

            assertThat(pdfPTableArgumentCaptor.getValue())
                    .isEqualTo(givenTable);
        }

        @Test
        void checkAddSupermarketInfoToHeaderTableShouldBeCalledOneTime() {
            Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
            PdfPTable givenTable = new PdfPTable(1);

            cashReceiptPdfService.addSupermarketInfoToHeaderTable(supermarket, givenTable);

            verify(cashReceiptPdfService).addSupermarketInfoToHeaderTable(supermarket, givenTable);
        }
    }

    @Nested
    class AddTimeAndDateToHeaderTableTest {

        @Test
        void checkAddTimeAndDateShouldCaptureTime() {
            LocalTime givenTime = LocalTime.now();
            LocalDate givenDate = LocalDate.now();
            PdfPTable givenTable = new PdfPTable(1);
            ArgumentCaptor<LocalTime> localTimeArgumentCaptor = ArgumentCaptor.forClass(LocalTime.class);

            doNothing().when(cashReceiptPdfService)
                    .addTimeAndDateToHeaderTable(
                            localTimeArgumentCaptor.capture(),
                            any(LocalDate.class),
                            any(PdfPTable.class));

            cashReceiptPdfService.addTimeAndDateToHeaderTable(givenTime, givenDate, givenTable);

            assertThat(localTimeArgumentCaptor.getValue())
                    .isEqualTo(givenTime);
        }

        @Test
        void checkAddTimeAndDateShouldCaptureDate() {
            LocalTime givenTime = LocalTime.now();
            LocalDate givenDate = LocalDate.now();
            PdfPTable givenTable = new PdfPTable(1);
            ArgumentCaptor<LocalDate> localDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);

            doNothing().when(cashReceiptPdfService)
                    .addTimeAndDateToHeaderTable(
                            any(LocalTime.class),
                            localDateArgumentCaptor.capture(),
                            any(PdfPTable.class));

            cashReceiptPdfService.addTimeAndDateToHeaderTable(givenTime, givenDate, givenTable);

            assertThat(localDateArgumentCaptor.getValue())
                    .isEqualTo(givenDate);
        }

        @Test
        void checkAddTimeAndDateShouldCapturePdfTable() {
            LocalTime givenTime = LocalTime.now();
            LocalDate givenDate = LocalDate.now();
            PdfPTable givenTable = new PdfPTable(1);
            ArgumentCaptor<PdfPTable> pdfPTableArgumentCaptor = ArgumentCaptor.forClass(PdfPTable.class);

            doNothing().when(cashReceiptPdfService)
                    .addTimeAndDateToHeaderTable(
                            any(LocalTime.class),
                            any(LocalDate.class),
                            pdfPTableArgumentCaptor.capture());

            cashReceiptPdfService.addTimeAndDateToHeaderTable(givenTime, givenDate, givenTable);

            assertThat(pdfPTableArgumentCaptor.getValue())
                    .isEqualTo(givenTable);
        }

        @Test
        void checkAddTimeAndDateShouldBeCalledOneTime() {
            LocalTime givenTime = LocalTime.now();
            LocalDate givenDate = LocalDate.now();
            PdfPTable givenTable = new PdfPTable(1);

            cashReceiptPdfService.addTimeAndDateToHeaderTable(givenTime, givenDate, givenTable);

            verify(cashReceiptPdfService)
                    .addTimeAndDateToHeaderTable(givenTime, givenDate, givenTable);
        }
    }

    @Test
    void checkShouldCreateHeaderTable() {
        PdfPTable givenTable = new PdfPTable(1);
        Supermarket supermarket = SuperMarketTestBuilder.aSupermarket().build();
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();

        doNothing().when(cashReceiptPdfService)
                .addSupermarketInfoToHeaderTable(supermarket, givenTable);

        doNothing().when(cashReceiptPdfService)
                .addTimeAndDateToHeaderTable(localTime, localDate, givenTable);

        PdfPTable actualTable = cashReceiptPdfService.createHeaderTable(supermarket, localTime, localDate);

        assertThat(actualTable).isNotNull();
    }

    @Test
    @SneakyThrows(DocumentException.class)
    void checkShouldCreateTableForCashReceiptProducts() {
        CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder.aCashReceiptProduct().build();
        List<CashReceiptProduct> cashReceiptProductList = List.of(cashReceiptProduct, cashReceiptProduct);
        PdfPTable cashReceiptTable = new PdfPTable(4);

        doNothing().when(cashReceiptPdfService)
                .addHeaderCellForCashReceiptProductsTable(cashReceiptTable);

        doNothing().when(cashReceiptPdfService)
                .addCashReceiptProductsIntoTable(cashReceiptProductList, cashReceiptTable);

        PdfPTable actualTable = cashReceiptPdfService.createTableForCashReceiptProducts(cashReceiptProductList);

        assertThat(actualTable).isNotNull();
    }

    @Nested
    class AddHeaderCellForCashReceiptProductsTableTest {

        @Test
        void checkAddHeaderCellForCashReceiptProductsTableShouldCapturePdfTable() {
            PdfPTable givenTable = new PdfPTable(4);
            ArgumentCaptor<PdfPTable> argumentCaptor = ArgumentCaptor.forClass(PdfPTable.class);

            doNothing().when(cashReceiptPdfService)
                    .addHeaderCellForCashReceiptProductsTable(argumentCaptor.capture());

            cashReceiptPdfService.addHeaderCellForCashReceiptProductsTable(givenTable);

            assertThat(argumentCaptor.getValue())
                    .isEqualTo(givenTable);
        }

        @Test
        void checkAddHeaderCellForCashReceiptProductsTableShouldBeCalledOneTime() {
            PdfPTable givenTable = new PdfPTable(4);

            cashReceiptPdfService.addHeaderCellForCashReceiptProductsTable(givenTable);

            verify(cashReceiptPdfService).addHeaderCellForCashReceiptProductsTable(givenTable);
        }
    }

    @Nested
    class AddCashReceiptProductsIntoTableTest {

        @Test
        void checkAddCashReceiptProductsIntoTableAShouldCaptureCashReceiptProducts() {
            PdfPTable givenTable = new PdfPTable(4);
            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder.aCashReceiptProduct().build();
            List<CashReceiptProduct> givenCashReceiptProducts = List.of(cashReceiptProduct, cashReceiptProduct);
            ArgumentCaptor<List<CashReceiptProduct>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

            doNothing().when(cashReceiptPdfService)
                    .addCashReceiptProductsIntoTable(listArgumentCaptor.capture(), any(PdfPTable.class));

            cashReceiptPdfService.addCashReceiptProductsIntoTable(givenCashReceiptProducts, givenTable);

            assertThat(listArgumentCaptor.getValue())
                    .isEqualTo(givenCashReceiptProducts);
        }

        @Test
        void checkAddCashReceiptProductsIntoTableAShouldCapturePdfTable() {
            PdfPTable givenTable = new PdfPTable(4);
            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder.aCashReceiptProduct().build();
            List<CashReceiptProduct> givenCashReceiptProducts = List.of(cashReceiptProduct, cashReceiptProduct);
            ArgumentCaptor<PdfPTable> pdfPTableArgumentCaptor = ArgumentCaptor.forClass(PdfPTable.class);

            doNothing().when(cashReceiptPdfService)
                    .addCashReceiptProductsIntoTable(any(List.class), pdfPTableArgumentCaptor.capture());

            cashReceiptPdfService.addCashReceiptProductsIntoTable(givenCashReceiptProducts, givenTable);

            assertThat(pdfPTableArgumentCaptor.getValue())
                    .isEqualTo(givenTable);
        }

        @Test
        void checkAddCashReceiptProductsIntoTableAShouldBeCalledOneTime() {
            PdfPTable givenTable = new PdfPTable(4);
            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder.aCashReceiptProduct().build();
            List<CashReceiptProduct> givenCashReceiptProducts = List.of(cashReceiptProduct, cashReceiptProduct);

            cashReceiptPdfService.addCashReceiptProductsIntoTable(givenCashReceiptProducts, givenTable);

            verify(cashReceiptPdfService).addCashReceiptProductsIntoTable(givenCashReceiptProducts, givenTable);
        }
    }

    @Nested
    class CreateTotalPriceTableTest {

        @Test
        @SneakyThrows(DocumentException.class)
        void checkCreateTotalPriceTableShouldHave3Rows() {
            PdfPTable givenTable = new PdfPTable(2);
            TotalPrice givenTotalPrice = TotalPriceTestBuilder.aTotalPrice().build();

            doNothing().when(cashReceiptPdfService)
                    .addTotalPriceIntoTableWithDiscount(givenTotalPrice, givenTable);

            PdfPTable actualTable = cashReceiptPdfService.createTotalPriceTable(givenTotalPrice);

            assertThat(actualTable.size()).isEqualTo(3);
        }

        @Test
        @SneakyThrows(DocumentException.class)
        void checkCreateTotalPriceTableShouldHave1Row() {
            PdfPTable givenTable = new PdfPTable(2);
            TotalPrice givenTotalPrice = TotalPriceTestBuilder
                    .aTotalPrice()
                    .withDiscount(BigDecimal.ZERO)
                    .build();

            doNothing().when(cashReceiptPdfService)
                    .addTotalPriceIntoTableWithDiscount(givenTotalPrice, givenTable);

            PdfPTable actualTable = cashReceiptPdfService.createTotalPriceTable(givenTotalPrice);

            assertThat(actualTable.size()).isEqualTo(1);
        }
    }

    @Nested
    class AddTotalPriceIntoTableWithDiscountTest {

        @Test
        void checkAddTotalPriceIntoTableWithDiscountShouldCaptureTotalPrice() {
            TotalPrice givenTotalPrice = TotalPriceTestBuilder.aTotalPrice().build();
            PdfPTable givenTable = new PdfPTable(2);
            ArgumentCaptor<TotalPrice> totalPriceArgumentCaptor = ArgumentCaptor.forClass(TotalPrice.class);

            doNothing().when(cashReceiptPdfService)
                    .addTotalPriceIntoTableWithDiscount(totalPriceArgumentCaptor.capture(), any(PdfPTable.class));

            cashReceiptPdfService.addTotalPriceIntoTableWithDiscount(givenTotalPrice, givenTable);

            assertThat(totalPriceArgumentCaptor.getValue())
                    .isEqualTo(givenTotalPrice);
        }

        @Test
        void checkAddTotalPriceIntoTableWithDiscountShouldCapturePdfTable() {
            TotalPrice givenTotalPrice = TotalPriceTestBuilder.aTotalPrice().build();
            PdfPTable givenTable = new PdfPTable(2);
            ArgumentCaptor<PdfPTable> pdfPTableArgumentCaptor = ArgumentCaptor.forClass(PdfPTable.class);

            doNothing().when(cashReceiptPdfService)
                    .addTotalPriceIntoTableWithDiscount(any(TotalPrice.class), pdfPTableArgumentCaptor.capture());

            cashReceiptPdfService.addTotalPriceIntoTableWithDiscount(givenTotalPrice, givenTable);

            assertThat(pdfPTableArgumentCaptor.getValue())
                    .isEqualTo(givenTable);
        }

        @Test
        void checkAddTotalPriceIntoTableWithDiscountShouldBeCalledOneTime() {
            TotalPrice givenTotalPrice = TotalPriceTestBuilder.aTotalPrice().build();
            PdfPTable givenTable = new PdfPTable(2);

            cashReceiptPdfService.addTotalPriceIntoTableWithDiscount(givenTotalPrice, givenTable);

            verify(cashReceiptPdfService).addTotalPriceIntoTableWithDiscount(givenTotalPrice, givenTable);
        }
    }

    @Nested
    class AddTotalPriceIntoTableWithoutDiscountTest {

        @Test
        void checkAddTotalPriceIntoTableWithoutDiscountShouldCaptureTotalPrice() {
            TotalPrice givenTotalPrice = TotalPriceTestBuilder.aTotalPrice().build();
            PdfPTable givenTable = new PdfPTable(2);
            ArgumentCaptor<TotalPrice> totalPriceArgumentCaptor = ArgumentCaptor.forClass(TotalPrice.class);

            doNothing().when(cashReceiptPdfService)
                    .addTotalPriceIntoTableWithoutDiscount(totalPriceArgumentCaptor.capture(), any(PdfPTable.class));

            cashReceiptPdfService.addTotalPriceIntoTableWithoutDiscount(givenTotalPrice, givenTable);

            assertThat(totalPriceArgumentCaptor.getValue())
                    .isEqualTo(givenTotalPrice);
        }

        @Test
        void checkAddTotalPriceIntoTableWithoutDiscountShouldCapturePdfTable() {
            TotalPrice givenTotalPrice = TotalPriceTestBuilder.aTotalPrice().build();
            PdfPTable givenTable = new PdfPTable(2);
            ArgumentCaptor<PdfPTable> pdfPTableArgumentCaptor = ArgumentCaptor.forClass(PdfPTable.class);

            doNothing().when(cashReceiptPdfService)
                    .addTotalPriceIntoTableWithoutDiscount(any(TotalPrice.class), pdfPTableArgumentCaptor.capture());

            cashReceiptPdfService.addTotalPriceIntoTableWithoutDiscount(givenTotalPrice, givenTable);

            assertThat(pdfPTableArgumentCaptor.getValue())
                    .isEqualTo(givenTable);
        }

        @Test
        void checkAddTotalPriceIntoTableWithoutDiscountShouldBeCalledOneTime() {
            TotalPrice givenTotalPrice = TotalPriceTestBuilder.aTotalPrice().build();
            PdfPTable givenTable = new PdfPTable(2);

            cashReceiptPdfService.addTotalPriceIntoTableWithoutDiscount(givenTotalPrice, givenTable);

            verify(cashReceiptPdfService).addTotalPriceIntoTableWithoutDiscount(givenTotalPrice, givenTable);
        }
    }
}