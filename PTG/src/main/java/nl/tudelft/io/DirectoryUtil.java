package nl.tudelft.io;

import java.io.File;

/**
 * This class handles the checking and finding of the directory.
 *
 * @author Dimitri Stallenberg
 */
public final class DirectoryUtil {

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws an instantiation exception
     */
    private DirectoryUtil() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    /**
     * Checks whether a certain file exists.
     * @param filePath the file path of the file to check for existence within the current directory.
     * @return boolean depending on the file existence
     */
    public static boolean fileExists(String filePath) {
        File file = new File(getCurrentWorkingDirectory() + System.getProperty("file.separator") + filePath);

        return file.exists();
    }

    /**
     * Gets the directory path this application runs in.
     * @return the path of the current directory
     */
    public static String getCurrentWorkingDirectory() {
        File jarPath = new File(DirectoryUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return jarPath.getParentFile().getAbsolutePath();
    }
}
