package ru.clevertec.cashreceipt.parser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.cashreceipt.exception.ParserException;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.COULD_NOT_PARSE_GIVEN_PARAMETER;

class ItemsParserTest {

    private ItemsParser itemsParser;

    @BeforeEach
    void setUp() {
        itemsParser = new ItemsParser();
    }

    @AfterEach
    void tearDown() {
        itemsParser = null;
    }

    @Test
    void checkParseShouldReturn3() {
        String[] items = {"3-5", "1-2", "2-4"};
        int expectedSize = 3;

        Map<Long, Integer> actualItems = itemsParser.parse(items);

        assertThat(actualItems.size()).isEqualTo(expectedSize);
    }

    @Test
    void checkParseShouldThrowParserException() {
        String[] items = {"3", "1-1", "2-4"};

        assertThatThrownBy(() -> itemsParser.parse(items))
                .isInstanceOf(ParserException.class)
                .hasMessage(COULD_NOT_PARSE_GIVEN_PARAMETER);
    }
}