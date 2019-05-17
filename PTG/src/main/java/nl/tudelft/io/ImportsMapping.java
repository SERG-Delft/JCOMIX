package nl.tudelft.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class maps certain keywords to import strings.
 *
 * @author Dimitri Stallenberg
 */
public final class ImportsMapping {

    private static Map<String, String> importMap = buildImportMap();

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws an instantiation exception
     */
    private ImportsMapping() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    /**
     * This method builds the import map.
     *
     * @return the mapping between keywords and imports
     */
    private static Map<String, String> buildImportMap() {
        Map<String, String> map = new HashMap<>();

        // junit
        map.put("AfterEach", "org.junit.jupiter.api.AfterEach");
        map.put("BeforeEach", "org.junit.jupiter.api.BeforeEach");
        map.put("Test", "org.junit.jupiter.api.Test");
        map.put("TestCase", "junit.framework.TestCase");

        // selenium
        map.put("ChromeDriver", "org.openqa.selenium.chrome.ChromeDriver");
        map.put("By", "org.openqa.selenium.By");
        map.put("WebDriver", "org.openqa.selenium.WebDriver");

        // java util
        map.put("TimeUnit", "java.util.concurrent.TimeUnit");

        // asserts
        map.put("assert", "org.junit.jupiter.api.Assertions");


        return map;
    }

    /**
     * Gets the import string corresponding to the given keyword.
     *
     * @param keyword the keyword to find the import for
     * @return the import string
     */
    private static String getImport(String keyword) {
        return importMap.get(keyword);
    }

    /**
     * Creates a list of import strings based on which keywords are contained in the text.
     *
     * @param text the text string to check
     * @return the list of imports that where discovered
     */
    public static List<String> getImports(String text) {
        List<String> list = new ArrayList<>();

        for (String key : importMap.keySet()) {
            if (text.contains(key)) {
                list.add(getImport(key));
            }
        }

        return list;
    }
}
