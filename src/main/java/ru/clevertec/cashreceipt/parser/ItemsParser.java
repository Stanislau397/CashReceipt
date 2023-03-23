package ru.clevertec.cashreceipt.parser;

import org.springframework.stereotype.Component;
import ru.clevertec.cashreceipt.exception.ParserException;
import ru.clevertec.cashreceipt.validator.ParameterValidator;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.clevertec.cashreceipt.exception.ExceptionMessage.COULD_NOT_PARSE_GIVEN_PARAMETER;

@Component
public class ItemsParser {

    private static final String DASH_DELIMITER = "-";

    public Map<Long, Integer> parse(String[] items) {
        return Arrays.stream(items)
                .peek(parameter -> {
                    if (!ParameterValidator.isParameterValid(parameter)) {
                        throw new ParserException(COULD_NOT_PARSE_GIVEN_PARAMETER);
                    }
                })
                .map(parameter -> parameter.split(DASH_DELIMITER))
                .collect(Collectors.toMap(k -> Long.parseLong(k[0]), v -> Integer.parseInt(v[1])));
    }
}
