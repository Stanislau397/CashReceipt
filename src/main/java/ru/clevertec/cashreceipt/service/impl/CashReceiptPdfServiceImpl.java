package ru.clevertec.cashreceipt.service.impl;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.entity.CashReceipt;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.entity.Supermarket;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.exception.PdfServiceException;
import ru.clevertec.cashreceipt.service.CashReceiptPdfService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CashReceiptPdfServiceImpl implements CashReceiptPdfService {

    private static final String SUBSTRATE_PATH = "src/main/resources/static/pdf/Clevertec_Template.pdf";
    private static final String DOLLAR_SIGN = "$";
    private static final String DATE = "DATE: ";
    private static final String TIME = "TIME: ";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";
    private static final String DASH = "-";

    private static final float TABLE_WIDTH = 100f;
    private static final float SPACING_BEFORE_TABLE = 20f;
    private static final int TOTAL_PRICE_TABLE_COLUMNS = 2;
    private static final int CASH_RECEIPT_PRODUCTS_TABLE_COLUMNS = 4;

    private static final int CELL_ONE_WIDTH = 10;
    private static final int CELL_TWO_WIDTH = 16;
    private static final int CELL_THREE_WIDTH = 12;
    private static final int CELL_FOUR_WIDTH = 7;

    private static final Paragraph EMPTY_PARAGRAPH = new Paragraph(100, "\u00a0");
    private static final Chunk EMPTY_CHUNK = new Chunk("");

    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.TIMES, 15);
    private static final Font TABLE_HEAD_FONT = FontFactory.getFont(FontFactory.TIMES, 14);
    private static final Font TABLE_CELLS_FONT = FontFactory.getFont(FontFactory.TIMES, 14);
    private static final Font FOOTER_FONT = FontFactory.getFont(FontFactory.TIMES, 14);

    private static final Phrase CASH_RECEIPT_PHRASE = new Phrase("CASH RECEIPT", HEADER_FONT);
    private static final Phrase QUANTITY_PHRASE = new Phrase("QTY", TABLE_HEAD_FONT);
    private static final Phrase PRICE_PHRASE = new Phrase("PRICE", TABLE_HEAD_FONT);
    private static final Phrase TOTAL_PHRASE = new Phrase("TOTAL", TABLE_HEAD_FONT);
    private static final Phrase DESCRIPTION_PHRASE = new Phrase("DESCRIPTION", TABLE_HEAD_FONT);
    private static final Phrase SUBTOTAL_PHRASE = new Phrase("SUBTOTAL", FOOTER_FONT);
    private static final Phrase DISCOUNT_FROM_CARD_PHRASE = new Phrase("DISCOUNT FROM CARD", FOOTER_FONT);
    private static final Phrase TOTAL_PRICE_PHRASE = new Phrase("TOTAL", FOOTER_FONT);

    @Override
    @SneakyThrows(PdfServiceException.class)
    public void writeCashReceiptIntoResponseAsPdf(CashReceipt cashReceipt, HttpServletResponse response) {
        Document document = new Document(PageSize.A4);
        Supermarket supermarket = cashReceipt.getSupermarket();
        LocalTime printTime = cashReceipt.getPrintTime();
        LocalDate printDate = cashReceipt.getPrintDate();
        List<CashReceiptProduct> cashReceiptProducts = cashReceipt.getCashReceiptProducts();
        TotalPrice totalPrice = cashReceipt.getTotalPriceForProducts();
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            document.add(EMPTY_CHUNK);

            PdfPTable headerTable = createHeaderTable(supermarket, printTime, printDate);
            PdfPTable cashReceiptProductsTable = createTableForCashReceiptProducts(cashReceiptProducts);
            PdfPTable totalPriceTable = createTotalPriceTable(totalPrice);

            addTemplateToPdf(pdfWriter);
            document.add(EMPTY_PARAGRAPH);
            document.add(headerTable);
            document.add(cashReceiptProductsTable);
            document.add(totalPriceTable);
        } catch (DocumentException | IOException e) {
            throw new PdfServiceException(e.getMessage());
        } finally {
            document.close();
        }
    }

    public void addTemplateToPdf(PdfWriter pdfWriter) throws IOException {
        PdfContentByte contentByte = pdfWriter.getDirectContent();
        PdfReader reader = new PdfReader(SUBSTRATE_PATH);
        PdfImportedPage page = pdfWriter.getImportedPage(reader, 1);
        contentByte.addTemplate(page, 0, 0);
    }

    public PdfPTable createHeaderTable(Supermarket supermarket, LocalTime time, LocalDate date) {
        PdfPTable pdfPTable = new PdfPTable(1);
        pdfPTable.setWidthPercentage(TABLE_WIDTH);

        addSupermarketInfoToHeaderTable(supermarket, pdfPTable);
        addTimeAndDateToHeaderTable(time, date, pdfPTable);
        return pdfPTable;
    }

    public void addSupermarketInfoToHeaderTable(Supermarket supermarket, PdfPTable headerTable) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        pdfPCell.setPhrase(CASH_RECEIPT_PHRASE);
        headerTable.addCell(pdfPCell);

        pdfPCell.setPhrase(new Phrase(supermarket.getName(), HEADER_FONT));
        headerTable.addCell(pdfPCell);

        pdfPCell.setPhrase(new Phrase(supermarket.getAddress(), HEADER_FONT));
        headerTable.addCell(pdfPCell);

        pdfPCell.setPhrase(new Phrase(supermarket.getPhoneNumber(), HEADER_FONT));
        headerTable.addCell(pdfPCell);
    }

    public void addTimeAndDateToHeaderTable(LocalTime time, LocalDate date, PdfPTable headerTable) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        String formattedTime = time.format(DateTimeFormatter.ofPattern(TIME_PATTERN));

        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        pdfPCell.setPhrase(new Phrase(DATE.concat(formattedDate), HEADER_FONT));
        headerTable.addCell(pdfPCell);

        pdfPCell.setPhrase(new Phrase(TIME.concat(formattedTime), HEADER_FONT));
        headerTable.addCell(pdfPCell);
    }

    public PdfPTable createTableForCashReceiptProducts(List<CashReceiptProduct> cashReceiptProducts) throws DocumentException {
        PdfPTable cashReceiptProductsTable = new PdfPTable(CASH_RECEIPT_PRODUCTS_TABLE_COLUMNS);
        cashReceiptProductsTable.setSpacingBefore(SPACING_BEFORE_TABLE);
        cashReceiptProductsTable.setWidthPercentage(TABLE_WIDTH);
        cashReceiptProductsTable.setWidths(new int[]{CELL_ONE_WIDTH, CELL_TWO_WIDTH, CELL_THREE_WIDTH, CELL_FOUR_WIDTH});
        cashReceiptProductsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        cashReceiptProductsTable.setHorizontalAlignment(Element.ALIGN_CENTER);

        addHeaderCellForCashReceiptProductsTable(cashReceiptProductsTable);
        addCashReceiptProductsIntoTable(cashReceiptProducts, cashReceiptProductsTable);

        return cashReceiptProductsTable;
    }

    public void addHeaderCellForCashReceiptProductsTable(PdfPTable cashReceiptProductsTable) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        pdfPCell.setPhrase(QUANTITY_PHRASE);
        cashReceiptProductsTable.addCell(pdfPCell);

        pdfPCell.setPhrase(DESCRIPTION_PHRASE);
        cashReceiptProductsTable.addCell(pdfPCell);

        pdfPCell.setPhrase(PRICE_PHRASE);
        cashReceiptProductsTable.addCell(pdfPCell);

        pdfPCell.setPhrase(TOTAL_PHRASE);
        cashReceiptProductsTable.addCell(pdfPCell);
    }

    public void addCashReceiptProductsIntoTable(List<CashReceiptProduct> cashReceiptProducts, PdfPTable cashReceiptProductsTable) {
        cashReceiptProducts.forEach(cashReceiptProduct -> {

            Product product = cashReceiptProduct.getProduct();
            TotalPrice totalPrice = cashReceiptProduct.getTotalPrice();
            String quantity = cashReceiptProduct.getQuantity().toString();
            String productName = product.getName();
            String productPrice = DOLLAR_SIGN.concat(product.getPrice().toString());
            String itemTotalPrice = SPACE.concat(DOLLAR_SIGN).concat(totalPrice.getItemTotal().toString());
            String discount = DASH.concat(DOLLAR_SIGN).concat(totalPrice.getDiscount().toString());

            cashReceiptProductsTable.addCell(new Phrase(quantity, TABLE_CELLS_FONT));
            cashReceiptProductsTable.addCell(new Phrase(productName, TABLE_CELLS_FONT));
            cashReceiptProductsTable.addCell(new Phrase(productPrice, TABLE_CELLS_FONT));
            cashReceiptProductsTable.addCell(new Phrase(itemTotalPrice, TABLE_CELLS_FONT));

            if (!BigDecimal.ZERO.equals(totalPrice.getDiscount())) {
                cashReceiptProductsTable.addCell(EMPTY_STRING);
                cashReceiptProductsTable.addCell(EMPTY_STRING);
                cashReceiptProductsTable.addCell(EMPTY_STRING);
                cashReceiptProductsTable.addCell(new Phrase(discount, TABLE_CELLS_FONT));
            }
        });
    }

    public PdfPTable createTotalPriceTable(TotalPrice totalPrice) throws DocumentException {
        PdfPTable totalPriceTable = new PdfPTable(TOTAL_PRICE_TABLE_COLUMNS);
        totalPriceTable.setWidthPercentage(TABLE_WIDTH);
        totalPriceTable.setSpacingBefore(SPACING_BEFORE_TABLE);
        if (!BigDecimal.ZERO.equals(totalPrice.getDiscount())) {
            addTotalPriceIntoTableWithDiscount(totalPrice, totalPriceTable);
        } else {
            addTotalPriceIntoTableWithoutDiscount(totalPrice, totalPriceTable);
        }
        return totalPriceTable;
    }

    public void addTotalPriceIntoTableWithDiscount(TotalPrice totalPrice, PdfPTable totalPriceTable) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        String itemTotal = DOLLAR_SIGN.concat(totalPrice.getItemTotal().toString());
        String discount = DOLLAR_SIGN.concat(totalPrice.getDiscount().toString());
        String subtotal = DOLLAR_SIGN.concat(totalPrice.getSubtotal().toString());

        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setPhrase(SUBTOTAL_PHRASE);
        totalPriceTable.addCell(pdfPCell);

        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setPhrase(new Phrase(itemTotal, FOOTER_FONT));
        totalPriceTable.addCell(pdfPCell);

        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setPhrase(DISCOUNT_FROM_CARD_PHRASE);
        totalPriceTable.addCell(pdfPCell);

        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setPhrase(new Phrase(discount, FOOTER_FONT));
        totalPriceTable.addCell(pdfPCell);

        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setPhrase(TOTAL_PRICE_PHRASE);
        totalPriceTable.addCell(pdfPCell);

        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setPhrase(new Phrase(subtotal, FOOTER_FONT));
        totalPriceTable.addCell(pdfPCell);
    }

    public void addTotalPriceIntoTableWithoutDiscount(TotalPrice totalPrice, PdfPTable totalPriceTable) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        String itemTotal = DOLLAR_SIGN.concat(totalPrice.getItemTotal().toString());

        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setPhrase(TOTAL_PRICE_PHRASE);
        totalPriceTable.addCell(pdfPCell);

        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setPhrase(new Phrase(itemTotal, FOOTER_FONT));
        totalPriceTable.addCell(pdfPCell);
    }
}
