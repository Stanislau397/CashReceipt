package ru.clevertec.cashreceipt.validator;

import java.util.regex.Pattern;

public class EntityIdValidator {

    private EntityIdValidator() {

    }

    private static final Pattern ENTITY_ID_PATTERN = Pattern.compile("^\\+?(0|[1-9]\\d*)$");

    public static boolean isEntityIdValid(String entityId) {
        return entityId != null && ENTITY_ID_PATTERN.matcher(entityId).matches();
    }
}
