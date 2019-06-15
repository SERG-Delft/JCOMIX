package nl.tudelft.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * This class handles the logging.
 * It is a singleton class.
 *
 * @author Dimitri Stallenberg
 */
public final class LogUtil {

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws an instantiation exception
     */
    private LogUtil() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    private static Logger instance;

    // TODO throw away the logs after a certain amount of space

    /**
     * This method creates the logger that we use for this app.
     * TODO this method can be shortened by using a config file where the logger settings are defined could be worth looking into.
     * @return the created logger
     */
    private static Logger makeLogger() {
        String currentDirectory = DirectoryUtil.getCurrentWorkingDirectory();
        String separator = System.getProperty("file.separator");

        Logger newLogger = Logger.getLogger("Global");

        File logsDir = new File(currentDirectory + separator + "logs");
        if (!logsDir.exists()) {
            if (!logsDir.mkdir()) {
                newLogger.log(Level.SEVERE, "Failed to create the log folder");
            }
        }

        try {
            Handler fileHandler = new FileHandler(
                    currentDirectory + separator + "logs" + separator + "all.log");
            Handler developerFileHandler = new FileHandler(
                    currentDirectory + separator + "logs" + separator + "dev.log");
            Handler infoFileHandler = new FileHandler(
                    currentDirectory + separator + "logs" + separator + "info.log");
            Handler errorFileHandler = new FileHandler(
                    currentDirectory + separator + "logs" + separator + "error.log");

            newLogger.addHandler(fileHandler);
            newLogger.addHandler(developerFileHandler);
            newLogger.addHandler(infoFileHandler);
            newLogger.addHandler(errorFileHandler);

            fileHandler.setLevel(Level.ALL);
            developerFileHandler.setLevel(Level.FINEST);
            infoFileHandler.setLevel(Level.INFO);
            errorFileHandler.setLevel(Level.WARNING);

        } catch (IOException e) {
            newLogger.log(Level.SEVERE, "Error occurred in FileHandler", e);
        }

        newLogger.config("Logger configuration done.");
        return newLogger;
    }

    /**
     * Gets the instance of the logger.
     * @return the instance of the logger
     */
    public static Logger getInstance() {
        if (null == instance) {
            instance = makeLogger();
        }
        return instance;
    }
}
