package ru.clevertec.cashier.validator;

import java.util.regex.Pattern;

public class ParameterValidator {

    private ParameterValidator() {

    }

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("^[0-9]+(-[0-9]+)");

    public static boolean isParameterValid(String parameter) {
        return parameter != null && PARAMETER_PATTERN.matcher(parameter).matches();
    }
}
