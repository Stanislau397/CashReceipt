package ru.clevertec.cashreceipt.parser;

import org.springframework.stereotype.Component;
import ru.clevertec.cashreceipt.exception.ParserException;
import ru.clevertec.cashreceipt.validator.ParameterValidator;

import java.util.HashMap;
import java.util.Map;

import static ru.clevertec.cashreceipt.exception.ExceptionMessage.COULD_NOT_PARSE_GIVEN_PARAMETER;

@Component
public class ItemsParser {

    private static final String DASH_DELIMITER = "-";

    public Map<Long, Integer> parse(String[] items) {
        Map<Long, Integer> productIdAndQuantityMap = new HashMap<>();
        for (String parameter : items) {
            if (!ParameterValidator.isParameterValid(parameter)) {
                throw new ParserException(COULD_NOT_PARSE_GIVEN_PARAMETER);
            }
            String[] values = parameter.split(DASH_DELIMITER);
            long productId = Long.parseLong(values[0]);
            int quantity = Integer.parseInt(values[1]);
            productIdAndQuantityMap.put(productId, quantity);
        }
        return productIdAndQuantityMap;
    }
}
