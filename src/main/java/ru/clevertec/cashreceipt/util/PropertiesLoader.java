package ru.clevertec.cashreceipt.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesLoader {

    private static final String APPLICATION_YML = "application.yml";
    private static final String ERROR_MESSAGE = "Could not load %s file";

    public static Properties loadProperties() {
        Properties configuration = new Properties();
        try (InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream(APPLICATION_YML)) {
            configuration.load(inputStream);
        } catch (IOException e) {
            log.error(ERROR_MESSAGE);
        }
        return configuration;
    }
}
