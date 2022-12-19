package ru.clevertec.cashier.validator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EntityIdValidatorTest {

    @Test
    void isEntityIdValidWillReturnTrue() {
        //given
        String validId = "1";
        //when
        boolean condition = EntityIdValidator.isEntityIdValid(validId);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void isEntityIdValidWillReturnFalse() {
        //given
        String invalidId = "1.25";
        //when
        boolean condition = EntityIdValidator.isEntityIdValid(invalidId);
        //then
        assertThat(condition).isFalse();
    }

    @Test
    void isEntityIdValidWillReturnFalseBecauseOfNullString() {
        //given
        String nullString = null;
        //when
        boolean condition = EntityIdValidator.isEntityIdValid(nullString);
        //then
        assertThat(condition).isFalse();
    }
}