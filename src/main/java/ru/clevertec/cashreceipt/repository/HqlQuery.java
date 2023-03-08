package ru.clevertec.cashreceipt.repository;

public class HqlQuery {

    private HqlQuery() {

    }

    //product queries
    public static final String SELECT_PRODUCT_BY_ID =
            "SELECT p FROM Product p " +
                    "WHERE p.productId = ?1";
    public static final String SELECT_PRODUCT_BY_NAME =
            "SELECT p FROM Product p " +
                    "WHERE p.name = ?1";
    public static final String SELECT_PRODUCT =
            "SELECT p FROM Product p" +
                    "WHERE p = ?1";
    public static final String DELETE_PRODUCT_BY_ID =
            "DELETE FROM Product p " +
                    "WHERE p.productId = ?1";

    //discountCard queries
    public static final String SELECT_DISCOUNT_CARD_BY_ID =
            "SELECT dc FROM DiscountCard dc " +
                    "WHERE dc.discountCardId = ?1";
    public static final String DELETE_DISCOUNT_CARD_BY_ID =
            "DELETE FROM DiscountCard dc " +
                    "WHERE dc.discountCardId = ?1";
}
