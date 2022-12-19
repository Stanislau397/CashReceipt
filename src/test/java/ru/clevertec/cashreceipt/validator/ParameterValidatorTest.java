package ru.clevertec.cashreceipt.validator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParameterValidatorTest {

    @Test
    void isParameterValidWillReturnTrue() {
        //given
        String validParameter = "3-1";
        //when
        boolean condition = ParameterValidator.isParameterValid(validParameter);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void isParameterValidWillReturnFalse() {
        //given
        String invalidParameter = "3";
        //when
        boolean condition = ParameterValidator.isParameterValid(invalidParameter);
        //then
        assertThat(condition).isFalse();
    }

    @Test
    void isParameterValidWillReturnFalseBecauseParameterIsNull() {
        //given
        String nullParameter = null;
        //when
        boolean condition = ParameterValidator.isParameterValid(nullParameter);
        //then
        assertThat(condition).isFalse();
    }
}