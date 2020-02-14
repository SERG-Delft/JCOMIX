package nl.tudelft.program.to_builder;

import nl.tudelft.proxy.HttpProcessor;
import nl.tudelft.tobuilder.Pair;
import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.program.Program;
import nl.tudelft.tobuilder.Builder;
import nl.tudelft.util.FileUtil;

import java.io.File;
import java.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
        Map<String, Pair<String, Integer>> fieldDefaults = ProxyReader.readProxyEntries();
        Map<String, List<String>> injections = ProxyReader.readInjections();

        List<Pair<String, Integer>> values = new ArrayList<>(fieldDefaults.values());
        values.sort(Comparator.comparingInt(Pair::getSecond));

        String text = getExampleMessage(fieldDefaults);

        Builder builder = new Builder(ProxyReader.getLanguage());
        Map<String, String> map = builder.build(text, ProxyReader.getLanguage(), values, injections);

        String path = getArgumentProcessor().getPropertyValue("to-read-path");

        for (String key : map.keySet()) {
            File file = new File(path + System.getProperty("file.separator") + key);
            file.getParentFile().mkdirs();
            FileUtil.writeFile(file, map.get(key), false);
        }
    }

    /**
     * This method makes a request to the SUT with just the default inputs to generate a valid example message.
     *
     * @param fieldDefaults the fields with their default values.
     * @return the valid example message
     */
    private String getExampleMessage(Map<String, Pair<String, Integer>> fieldDefaults) {
        HttpProcessor processor = new HttpProcessor(getArgumentProcessor().getPropertyValue("proxy-url"), getArgumentProcessor().getPropertyValue("connection"));
        List<NameValuePair> pairs = new ArrayList<>();

        for (String field : fieldDefaults.keySet()) {
            pairs.add(new BasicNameValuePair(field, fieldDefaults.get(field).getFirst()));
        }

        return processor.submit(pairs);
    }
}
