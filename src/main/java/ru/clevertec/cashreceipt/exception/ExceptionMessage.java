package ru.clevertec.cashreceipt.exception;

public class ExceptionMessage {

    private ExceptionMessage() {

    }

    //Products
    public static final String PRODUCT_BY_GIVEN_ID_NOT_FOUND = "Product with id %d not found!";

    //DiscountCard
    public static final String DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND = "Discount card with id %d not found!";

    //Parser
    public static final String COULD_NOT_PARSE_GIVEN_PARAMETER = "Could not parse parameter!";

    public static final String GIVEN_ID_IS_NOT_VALID = "Given id '%s' is not valid!";
}
