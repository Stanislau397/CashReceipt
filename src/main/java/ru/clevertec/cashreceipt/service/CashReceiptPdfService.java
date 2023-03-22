package ru.clevertec.cashreceipt.service;

import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.cashreceipt.entity.CashReceipt;

public interface CashReceiptPdfService {

    void writeCashReceiptIntoResponseAsPdf(CashReceipt cashReceipt, HttpServletResponse response);
}
