package nl.tudelft.io.readers;

import nl.tudelft.io.LogUtil;
import nl.tudelft.program.Flag;
import nl.tudelft.util.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

import static nl.tudelft.io.DirectoryUtil.getCurrentWorkingDirectory;

/**
 * This class represents the config reader.
 *
 * @author Dimitri Stallenberg
 */
public final class ConfigReader {

    private static File file;

    static {
        file = new File(getCurrentWorkingDirectory() + System.getProperty("file.separator") + "config.json");

        if (!file.exists()) {
            LogUtil.getInstance().warning("Config file is missing! "
                    + "Run the program with argument --init to generate one, or create one yourself.");
            System.exit(1);
        }
    }

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private ConfigReader() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    /**
     * This method reads the config file and returns a mapping between properties and values.
     *
     * @param flagMap the flag map to find out which properties we need in the config
     * @return the map of properties and values.
     */
    public static Map<String, String> readConfig(Map<String, Flag> flagMap) {
        Map<String, String> config = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(FileUtil.readFile(file)));
            ConfigReader.validateConfig(flagMap, jsonObject);
            Iterator<String> iterator = jsonObject.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                config.put(key, jsonObject.getString(key));
            }

        } catch (JSONException e) {
            // TODO
            e.printStackTrace();
        }

        return config;
    }

    /**
     * This method validates the config file.
     * It checks whether the required fields exist and if the values conform to their requirements.
     *
     * @param flagMap    the mapping between flag strings and flag objects
     * @param jsonObject the content of the file
     */
    private static void validateConfig(Map<String, Flag> flagMap, JSONObject jsonObject) {
        try {
            for (String arg : flagMap.keySet()) {
                Flag flag = flagMap.get(arg);
                if (!flag.isSetting()) {
                    continue;
                }

                String s = jsonObject.getString(flag.getProperty());
                if (flag.hasFormat() && !s.matches(flag.getFormat())) {
                    LogUtil.getInstance().warning("Config file "
                            + flag.getProperty()
                            + " property has wrong format!");
                    System.exit(1);
                }

                if (flag.hasOptions() && !flag.getOptions().contains(s)) {
                    LogUtil.getInstance().warning("Config file "
                            + flag.getProperty()
                            + " property should be one of the following: "
                            + flag.getOptions().toString());
                    System.exit(1);
                }

                if (!flag.hasArgument() && !s.matches("true|false")) {
                    LogUtil.getInstance().warning("Config file "
                            + flag.getProperty()
                            + " property should be either true or false");
                    System.exit(1);
                }
            }

        } catch (JSONException e) {
            LogUtil.getInstance().log(Level.SEVERE, "Config file is missing the "
                    + e.getMessage().split("\"")[1]
                    + " property!", e);
            System.exit(1);
        }
    }

    /**
     * This method converts a time string to integer minutes.
     *
     * @param time the time string
     * @return the time in minutes
     */
    public static int readTimeToMinutes(String time) {
        final int secondsPerMinute = 60;
        final int minutesPerHour = 60;
        final int hoursPerDay = 24;

        int minutes = 0;

        if (time.contains("s")) {
            minutes = Integer.parseInt(time.substring(0, time.length() - 1)) / secondsPerMinute;
        } else if (time.contains("m")) {
            minutes = Integer.parseInt(time.substring(0, time.length() - 1));
        } else if (time.contains("h")) {
            minutes = Integer.parseInt(time.substring(0, time.length() - 1)) * minutesPerHour;
        } else if (time.contains("d")) {
            minutes = Integer.parseInt(time.substring(0, time.length() - 1)) * minutesPerHour * hoursPerDay;
        }

        return minutes;
    }
}
