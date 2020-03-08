package nl.tudelft.io.readers;

import nl.tudelft.io.LogUtil;
import nl.tudelft.tobuilder.Pair;
import nl.tudelft.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

/**
 * This class will read the proxy.json file.
 *
 * @author Dimitri Stallenberg
 */
public final class ProxyReader {

    private static File file;

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private ProxyReader() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    // TODO
    public static void setFilePath(String path) {
        file = new File(path);
    }

    /**
     * This method reads the proxy entries template file.
     *
     * @return a ordered mapping between fields and standard values
     */
    public static Map<String, Pair<String, Integer>> readProxyEntries() {
        if (file == null) {
            LogUtil.getInstance().warning("Proxy file path is not given please use setFilePath function!");
            System.exit(1);
        }

        if (!file.exists()) {
            LogUtil.getInstance().warning("Proxy file is missing! "
                    + "Run the program with argument --init to generate one, or create one yourself.");
            System.exit(1);
        }

        Map<String, Pair<String, Integer>> proxyEntries = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readFile(file, false));
            JSONArray jsonArray = jsonObject.getJSONArray("target-entries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String key = object.getString("proxy-key");
                String value = object.getString("value");

                proxyEntries.put(key, new Pair<>(value, i));
            }
        } catch (JSONException e) {
            // TODO
            e.printStackTrace();
        }

        return proxyEntries;
    }

    /**
     * This method reads the language from the file.
     *
     * @return the language used
     */
    public static String getLanguage() {
        if (file == null) {
            LogUtil.getInstance().warning("Proxy file path is not given please use setFilePath function!");
            System.exit(1);
        }

        String expectedOutput = "";

        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readFile(file, false));

            expectedOutput = jsonObject.getString("output-language");
        } catch (JSONException e) {
            // TODO
            e.printStackTrace();
        }

        return expectedOutput;
    }

    /**
     * This methods reads the injections from the file.
     *
     * @return the injections
     */
    public static Map<String, List<String>> readInjections() {
        if (file == null) {
            LogUtil.getInstance().warning("Proxy file path is not given please use setFilePath function!");
            System.exit(1);
        }

        Map<String, List<String>> injections = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readFile(file, false));
            jsonObject = jsonObject.getJSONObject("injections");

            for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                JSONArray injectionArray = jsonObject.getJSONArray(key);
                List<String> subInjections = new ArrayList<>();

                for (int i = 0; i < injectionArray.length(); i++) {
                    String injection = injectionArray.getString(i);
                    subInjections.add(injection);
                }

                injections.put(key, subInjections);
            }
        } catch (JSONException e) {
            // TODO
            e.printStackTrace();
        }

        return injections;
    }
}
