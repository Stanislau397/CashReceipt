package ru.clevertec.cashreceipt.util;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.clevertec.cashreceipt.entity.CashReceipt;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.entity.Supermarket;
import ru.clevertec.cashreceipt.entity.TotalPrice;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class PdfGenerator {

    private static final String CASH_RECEIPT = "CASH RECEIPT";
    private static final String QUANTITY = "QTY";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String PRICE = "PRICE";
    private static final String TOTAL = "TOTAL";
    private static final String DISCOUNT_FROM_CARD = "DISCOUNT FROM CARD";
    private static final String SUBTOTAL = "SUBTOTAL";
    private static final String DOLLAR_SIGN = "$";
    private static final String DATE = "DATE: ";
    private static final String TIME = "TIME: ";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String EMPTY_STRING = "";

    private static final float TABLE_WIDTH = 100f;
    private static final int DOCUMENT_WIDTH = 400;
    private static final int DOCUMENT_HEIGHT = 700;

    private static final int CELL_ONE_WIDTH = 6;
    private static final int CELL_TWO_WIDTH = 14;
    private static final int CELL_THREE_WIDTH = 10;
    private static final int CELL_FOUR_WIDTH = 7;

    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int TWELVE = 12;
    private static final int ELEVEN = 11;

    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.TIMES, TWELVE);
    private static final Font TABLE_HEAD_FONT = FontFactory.getFont(FontFactory.TIMES, ELEVEN);
    private static final Font TABLE_CELLS_FONT = FontFactory.getFont(FontFactory.TIMES, TWELVE);
    private static final Font FOOTER_FONT = FontFactory.getFont(FontFactory.TIMES, TWELVE);

    public void generate(CashReceipt cashReceipt, HttpServletResponse response) {
        Rectangle rect = new Rectangle(DOCUMENT_WIDTH, DOCUMENT_HEIGHT);
        Document document = new Document(rect);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            String currentDate = cashReceipt.getPrintDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
            String currentTime = cashReceipt.getPrintTime().format(DateTimeFormatter.ofPattern(TIME_PATTERN));
            List<CashReceiptProduct> cashReceiptProducts = cashReceipt.getCashReceiptProducts();
            TotalPrice totalPriceForAllProducts = cashReceipt.getTotalPriceForProducts();
            Supermarket supermarket = cashReceipt.getSupermarket();

            buildHeader(document, supermarket, currentDate, currentTime);
            buildTable(document, cashReceiptProducts);
            buildFooter(document, totalPriceForAllProducts);
        } catch (DocumentException | IOException e) {

        } finally {
            document.close();
        }
    }

    public void buildHeader(Document document, Supermarket supermarket, String currentDate, String currentTime) throws DocumentException {
        Paragraph titleParagraph = new Paragraph(CASH_RECEIPT, HEADER_FONT);
        Paragraph supermarketNameParagraph = new Paragraph(supermarket.getName(), HEADER_FONT);
        Paragraph addressParagraph = new Paragraph(supermarket.getAddress(), HEADER_FONT);
        Paragraph phoneNumberParagraph = new Paragraph(supermarket.getPhoneNumber(), HEADER_FONT);

        titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        supermarketNameParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        addressParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        phoneNumberParagraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(titleParagraph);
        document.add(supermarketNameParagraph);
        document.add(addressParagraph);
        document.add(phoneNumberParagraph);

        Font fontForDateAndTime = FontFactory.getFont(FontFactory.TIMES, TWELVE);
        Paragraph dateParagraph = new Paragraph(DATE + currentDate, fontForDateAndTime);
        Paragraph timeParagraph = new Paragraph(TIME + currentTime, fontForDateAndTime);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        timeParagraph.setAlignment(Element.ALIGN_RIGHT);
        timeParagraph.setSpacingAfter(TWELVE);
        document.add(dateParagraph);
        document.add(timeParagraph);
    }

    public void buildTable(Document document, List<CashReceiptProduct> cashReceiptProducts) throws DocumentException {
        Paragraph tableParagraph = new Paragraph();
        PdfPTable table = new PdfPTable(FOUR);

        table.setWidthPercentage(TABLE_WIDTH);
        table.setWidths(new int[]{CELL_ONE_WIDTH, CELL_TWO_WIDTH, CELL_THREE_WIDTH, CELL_FOUR_WIDTH});
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableParagraph.add(table);
        PdfPCell cell = new PdfPCell();

        cell.setBorder(Rectangle.NO_BORDER);

        cell.setPaddingBottom(FIVE);
        cell.setPaddingRight(THREE);
        cell.setPhrase(new Phrase(QUANTITY, TABLE_HEAD_FONT));
        table.addCell(cell);
        cell.setPhrase(new Phrase(DESCRIPTION, TABLE_HEAD_FONT));
        table.addCell(cell);
        cell.setPhrase(new Phrase(PRICE, TABLE_HEAD_FONT));
        table.addCell(cell);
        cell.setPhrase(new Phrase(TOTAL, TABLE_HEAD_FONT));
        table.addCell(cell);

        cashReceiptProducts.forEach(o -> {
            Product product = o.getProduct();
            TotalPrice total = o.getTotalPrice();
            table.addCell(new Phrase(String.valueOf(o.getQuantity()), PdfGenerator.TABLE_CELLS_FONT));
            table.addCell(new Phrase(product.getName(), TABLE_CELLS_FONT));
            table.addCell(new Phrase(DOLLAR_SIGN + product.getPrice(), TABLE_CELLS_FONT));
            table.addCell(new Phrase(" " + DOLLAR_SIGN + total.getItemTotal(), TABLE_CELLS_FONT));
            if (!Objects.equals(total.getDiscount(), BigDecimal.ZERO)) {
                table.addCell(EMPTY_STRING);
                table.addCell(EMPTY_STRING);
                table.addCell(EMPTY_STRING);
                table.addCell(new Phrase("-" + DOLLAR_SIGN + total.getDiscount(), TABLE_CELLS_FONT));
            }
        });
        document.add(tableParagraph);
    }

    public void buildFooter(Document document, TotalPrice totalPriceForAllProducts) throws DocumentException {
        if (!totalPriceForAllProducts.getDiscount().equals(BigDecimal.ZERO)) {
            Chunk spaceBetween = new Chunk(new VerticalPositionMark());
            Paragraph priceWithoutDiscount = new Paragraph();
            Paragraph discountForAllItems = new Paragraph();
            Paragraph subtotalForAllItems = new Paragraph();

            priceWithoutDiscount.add(new Phrase(SUBTOTAL, PdfGenerator.FOOTER_FONT));
            priceWithoutDiscount.add(spaceBetween);
            priceWithoutDiscount.add(new Phrase(DOLLAR_SIGN + totalPriceForAllProducts.getItemTotal(), FOOTER_FONT));

            discountForAllItems.add(new Phrase(DISCOUNT_FROM_CARD, PdfGenerator.FOOTER_FONT));
            discountForAllItems.add(spaceBetween);
            discountForAllItems.add(new Phrase(DOLLAR_SIGN + totalPriceForAllProducts.getDiscount(), FOOTER_FONT));

            subtotalForAllItems.add(new Phrase(TOTAL, PdfGenerator.FOOTER_FONT));
            subtotalForAllItems.add(spaceBetween);
            subtotalForAllItems.add(new Phrase(DOLLAR_SIGN + totalPriceForAllProducts.getSubtotal()));

            priceWithoutDiscount.setExtraParagraphSpace(TWO);
            document.add(priceWithoutDiscount);
            document.add(discountForAllItems);
            document.add(subtotalForAllItems);
        } else {
            Chunk spaceBetween = new Chunk(new VerticalPositionMark());
            Paragraph itemsTotalPrice = new Paragraph();
            itemsTotalPrice.add(new Phrase(TOTAL, PdfGenerator.FOOTER_FONT));
            itemsTotalPrice.add(spaceBetween);
            itemsTotalPrice.add(new Phrase(DOLLAR_SIGN + totalPriceForAllProducts.getItemTotal(), FOOTER_FONT));
            itemsTotalPrice.setExtraParagraphSpace(TWO);
            document.add(itemsTotalPrice);
        }
    }
}
