package nl.tudelft.io.readers;

import nl.tudelft.io.LogUtil;
import nl.tudelft.tobuilder.Pair;
import nl.tudelft.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will read the proxy.json file.
 *
 * @author Dimitri Stallenberg
 */
public final class ProxyReader {

    private static File file;

    static {
        file = new File(getCurrentWorkingDirectory() + System.getProperty("file.separator") + "proxy.json");

        if (!file.exists()) {
            LogUtil.getInstance().warning("Proxy file is missing! "
                    + "Run the program with argument --init to generate one, or create one yourself.");
            System.exit(1);
        }
    }

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private ProxyReader() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }


    private static String getCurrentWorkingDirectory() {
        File jarPath = new File(ProxyReader.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return jarPath.getParentFile().getAbsolutePath();
    }


    /**
     * This method reads the servlet entries template file.
     *
     * @return a ordered mapping between fields and standard values
     */
    public static Map<String, Pair<String, Integer>> readServletEntries() {
        Map<String, Pair<String, Integer>> servletEntries = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readAndNormalizeFile(file));
            JSONArray jsonArray = jsonObject.getJSONArray("target-entries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String key = object.getString("proxy-key");
                String value = object.getString("value");

                servletEntries.put(key, new Pair<>(value, i));
            }
        } catch (JSONException e) {
            // TODO
            e.printStackTrace();
        }

        return servletEntries;
    }

    /**
     * This method reads the expected output path from the file.
     *
     * @return the path to the file
     */
    public static String getExpectedOutputFilePath() {
        String expectedOutput = "";

        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readAndNormalizeFile(file));

            expectedOutput = jsonObject.getString("expected-output-path");
        } catch (JSONException e) {
            // TODO
            e.printStackTrace();
        }

        return expectedOutput;
    }

    /**
     * This method reads the language from the file.
     *
     * @return the language used
     */
    public static String getLanguage() {
        String expectedOutput = "";

        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readAndNormalizeFile(file));

            expectedOutput = jsonObject.getString("output-language");
        } catch (JSONException e) {
            // TODO
            e.printStackTrace();
        }

        return expectedOutput;
    }
}
