package nl.tudelft.proxy;

import nl.tudelft.io.LogUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.logging.Level;

/**
 * This class represents the HttpProcessor.
 * It manages the connections to the web server.
 *
 * @author Dimitri Stallenberg
 */
public class HttpProcessor implements Proxy {

    private CloseableHttpClient httpclient;
    private String url;
    private NameValuePair connectionType;

    /**
     * Constructor.
     *
     * @param url            the url to connect to
     * @param connectionType the type of connection to make
     */
    public HttpProcessor(String url, String connectionType) {
        this.httpclient = HttpClients.createDefault();
        this.url = url;

        this.connectionType = new BasicNameValuePair("ConnectionType", connectionType);
    }

    @Override
    public String submit(List<NameValuePair> pairs) {
        HttpPost httpPost = new HttpPost(url);

        pairs.add(connectionType);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String contents = EntityUtils.toString(entity);

            EntityUtils.consume(entity);
            return contents;
        } catch (Exception e) {
            LogUtil.getInstance().log(Level.SEVERE, "Cannot make request to server!", e);
            System.exit(1);
        }

        return "";
    }
}
