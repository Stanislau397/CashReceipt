package ru.clevertec.cashreceipt.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EntityIdValidatorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1", "55", "100"
    })
    void checkIsEntityIdValidShouldReturnTrue(String id) {
        //when
        boolean condition = EntityIdValidator.isEntityIdValid(id);
        //then
        assertThat(condition).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1.25", "1d", "1-2"
    })
    void checkIsEntityIdValidShouldReturnFalse(String id) {
        //when
        boolean condition = EntityIdValidator.isEntityIdValid(id);
        //then
        assertThat(condition).isFalse();
    }
}