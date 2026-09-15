package mikkelmattern.utils;

import mikkelmattern.exceptions.ApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Utils {

    private Utils() {}

    public static String getPropertyValue(String propertyName, String resourceName) {
        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(resourceName)) {

            if (inputStream == null) {
                throw new ApiException(500, "Resource not found: " + resourceName);
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            String value = properties.getProperty(propertyName);

            if (value == null || value.isBlank()) {
                throw new ApiException(500, "Property " + propertyName + " not found in " + resourceName);
            }

            return value.trim();

        } catch (IOException exception) {
            throw new ApiException(500, "Could not read " + resourceName);
        }
    }
}