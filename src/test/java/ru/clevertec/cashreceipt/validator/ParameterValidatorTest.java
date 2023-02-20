package ru.clevertec.cashreceipt.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParameterValidatorTest {

    static Stream<String> validParameterArgumentsProvider() {
        return Stream.of("3-1", "100-5", "1-100");
    }

    static Stream<String> invalidParameterArgumentsProvider() {
        return Stream.of("3", "-5", "1:2");
    }

    @ParameterizedTest
    @MethodSource("validParameterArgumentsProvider")
    void checkIsParameterValidShouldReturnTrue(String validParameter) {
        //when
        boolean condition = ParameterValidator.isParameterValid(validParameter);
        //then
        assertThat(condition).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidParameterArgumentsProvider")
    void checkIsParameterValidShouldReturnFalse(String invalidParameter) {
        //when
        boolean condition = ParameterValidator.isParameterValid(invalidParameter);
        //then
        assertThat(condition).isFalse();
    }
}