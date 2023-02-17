package ru.clevertec.cashreceipt.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParameterValidatorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "3-1", "100-5", "1-100"
    })
    void checkIsParameterValidShouldReturnTrue(String parameter) {
        //when
        boolean condition = ParameterValidator.isParameterValid(parameter);
        //then
        assertThat(condition).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "3", "-5", "1:2"
    })
    void checkIsParameterValidShouldReturnFalse(String parameter) {
        //when
        boolean condition = ParameterValidator.isParameterValid(parameter);
        //then
        assertThat(condition).isFalse();
    }
}