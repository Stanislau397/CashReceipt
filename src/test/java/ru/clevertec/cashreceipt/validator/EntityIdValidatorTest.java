package ru.clevertec.cashreceipt.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EntityIdValidatorTest {

    static Stream<String> validIdArgumentsProvider() {
        return Stream.of("1", "55", "100");
    }

    static Stream<String> invalidIdArgumentsProvider() {
        return Stream.of("1.25", "1d", "1-2");
    }

    @ParameterizedTest
    @MethodSource("validIdArgumentsProvider")
    void checkIsEntityIdValidShouldReturnTrue(String validId) {
        //when
        boolean condition = EntityIdValidator.isEntityIdValid(validId);
        //then
        assertThat(condition).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidIdArgumentsProvider")
    void checkIsEntityIdValidShouldReturnFalse(String invalidId) {
        //when
        boolean condition = EntityIdValidator.isEntityIdValid(invalidId);
        //then
        assertThat(condition).isFalse();
    }
}