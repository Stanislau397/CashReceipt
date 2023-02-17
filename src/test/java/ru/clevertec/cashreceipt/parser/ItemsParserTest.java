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
        //given
        String[] items = {"3-5", "1-2", "2-4"};
        int expectedSize = 3;

        //when
        Map<Long, Integer> actualItems = itemsParser.parse(items);

        //then
        assertThat(actualItems.size()).isEqualTo(expectedSize);
    }

    @Test
    void checkParseShouldThrowParserException() {
        //given
        String[] items = {"3", "1-1", "2-4"};
        //then
        assertThatThrownBy(() -> itemsParser.parse(items))
                .isInstanceOf(ParserException.class)
                .hasMessage(COULD_NOT_PARSE_GIVEN_PARAMETER);
    }
}