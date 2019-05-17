package nl.tudelft.util;

import nl.tudelft.io.LogUtil;

import java.io.*;
import java.net.URL;

public final class FileUtil {

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private FileUtil() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    /**
     * Read a text file as-is.
     *
     * @param f the path
     * @return the text of the file
     */
    public static String readFile(File f) {
        try {
            FileReader fileReader = new FileReader(f);
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            LogUtil.getInstance().warning("Failed to read the file! " + f.toString());
            System.exit(1);
        }
        return "";
    }

    /**
     * Read a text file and remove all carriage-return characters.
     *
     * @param f     the file
     * @return      the normalized text of the file
     */
    public static String readAndNormalizeFile(File f) {
        return readFile(f).replaceAll("\\s+", " ");
    }

    /**
     * Write text to a file.
     *
     * @param f      the file to write to
     * @param text   the text to write
     * @param append whether to append or not
     */
    public static void writeFile(File f, String text, boolean append) {
        try {
            File parent = f.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }

            FileWriter fileWriter = new FileWriter(f, append);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(text);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            LogUtil.getInstance().warning("Failed to write the file! " + f.toString());
            System.exit(1);
        }
    }

    /**
     * Get the file from classpath to the resource folder.
     *
     * @param fileName  the filename
     * @return          the File
     */
    public static File getFileFromResources(String fileName) {
        ClassLoader classLoader = FileUtil.class.getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    public static void copyFile(String source, String target){
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(source);
        try {
            FileOutputStream outputStream = new FileOutputStream(target);
            int content;
            while ((content = inputStream.read()) != -1) {
                // convert to char and display it
                outputStream.write(content);
            }
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
