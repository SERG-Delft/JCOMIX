package nl.tudelft.io.writers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class JestWriterTest extends TestWriterTest{

    private Map<String, String> properties;

    @BeforeEach
    void setUp() {
        properties = new HashMap<>();
        properties.put("test-save-path", "test/save/path");
        properties.put("chrome-driver-path", "chrome/driver/path");
        properties.put("html-url", "http://html-url.com/");
        setTestWriter(new JUnitWriter(properties));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getProperties() {
        Assertions.assertSame(properties, getTestWriter().getProperties());
    }

    @Test
    void writeTest() {
    }
}