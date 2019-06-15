package nl.tudelft.io;

import nl.tudelft.util.FileUtil;

import java.io.File;

import static nl.tudelft.io.DirectoryUtil.getCurrentWorkingDirectory;

/**
 * This class will initialize the proxy and config files.
 *
 * @author Dimitri Stallenberg
 */
public final class Initialize {

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private Initialize() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    /**
     * This method creates an example config or proxy file if it does not exist.
     */
    public static void init() {
        File fileConfig = new File(getCurrentWorkingDirectory() + System.getProperty("file.separator") + "config.json");
        File fileProxy = new File(getCurrentWorkingDirectory() + System.getProperty("file.separator") + "proxy.json");

        if (!fileConfig.exists()) {
            FileUtil.copyFile("config.json", fileConfig.getAbsolutePath());
            LogUtil.getInstance().info("Example config file created under: " + getCurrentWorkingDirectory());
        }
        if (!fileProxy.exists()) {
            FileUtil.copyFile("proxy.json", fileProxy.getAbsolutePath());
            LogUtil.getInstance().info("Example proxy file created under: " + getCurrentWorkingDirectory());
        }

    }
}
