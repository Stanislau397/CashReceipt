package ru.clevertec.cashier.repository;

public class HqlQuery {

    private HqlQuery() {

    }

    //product queries
    public static final String SELECT_PRODUCT_BY_ID =
            "SELECT p FROM Product p " +
                    "WHERE p.productId = :productId";

    //discountCard queries
    public static final String SELECT_DISCOUNT_CARD_BY_ID =
            "SELECT dc FROM DiscountCard dc " +
                    "WHERE dc.discountCardId = :discountCardId";
}
