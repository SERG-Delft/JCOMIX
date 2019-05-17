package nl.tudelft.proxy;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * This interface serves as the proxy.
 * Extend from it to create your own type of proxy.
 *
 * @author Dimitri Stallenberg
 */
public interface Proxy {

    /**
     * This method submits a list of NameValuePairs to the web server.
     *
     * @param pairs the list of pairs
     * @return the result string of the web server
     */
    String submit(List<NameValuePair> pairs);
}
