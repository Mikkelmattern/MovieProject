package mikkelmattern.utils;

import mikkelmattern.exceptions.ApiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void shouldReadAndTrimProperty() {
        String value = Utils.getPropertyValue("TEST_VALUE", "test-config.properties");

        assertEquals("hello", value);
    }

    @Test
    void shouldThrowWhenResourceDoesNotExist() {
        ApiException exception = assertThrows(
                ApiException.class,
                () -> Utils.getPropertyValue("TEST_VALUE", "missing.properties")
        );

        assertEquals(500, exception.getCode());
        assertTrue(exception.getMessage().contains("Resource not found"));
    }

    @Test
    void shouldThrowWhenPropertyDoesNotExistOrIsBlank() {
        ApiException missing = assertThrows(
                ApiException.class,
                () -> Utils.getPropertyValue("UNKNOWN", "test-config.properties")
        );
        ApiException blank = assertThrows(
                ApiException.class,
                () -> Utils.getPropertyValue("BLANK_VALUE", "test-config.properties")
        );

        assertEquals(500, missing.getCode());
        assertEquals(500, blank.getCode());
    }
}
