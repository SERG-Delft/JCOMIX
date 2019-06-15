package nl.tudelft.program.to_builder;

import nl.tudelft.tobuilder.Pair;
import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.program.Program;
import nl.tudelft.tobuilder.Builder;
import nl.tudelft.util.FileUtil;

import java.io.File;
import java.util.*;

/**
 * This class will run the TO builder program.
 *
 * @author Dimitri Stallenberg
 */
public class ToBuilder extends Program {

    /**
     * Constructor.
     *
     * @param argumentProcessor the argumentProcessor Object containing all the properties and arguments
     */
    public ToBuilder(ArgumentProcessor argumentProcessor) {
        super(argumentProcessor);
    }

    @Override
    public void start() {
        Builder builder = new Builder(ProxyReader.getLanguage());

        List<Pair<String, Integer>> values = new ArrayList<>(ProxyReader.readProxyEntries().values());
        values.sort(Comparator.comparingInt(Pair::getSecond));

        String text = FileUtil.readFile(new File(ProxyReader.getExpectedOutputFilePath()), true);

        Map<String, String> map = builder.build(text, values);

        for (String key : map.keySet()) {
            File file = new File("generated" + System.getProperty("file.separator") + key);
            file.getParentFile().mkdirs();
            FileUtil.writeFile(file, map.get(key), false);
        }
    }

}
