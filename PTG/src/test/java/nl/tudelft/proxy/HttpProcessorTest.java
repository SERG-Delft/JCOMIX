package nl.tudelft.proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpProcessorTest {

    @Test
    void TestGetters() {
        String url = "https://fake.url.com/";
        String connectionClient = "HttpClient";
        HttpProcessor httpProcessor = new HttpProcessor(url, connectionClient);

        Assertions.assertEquals(url, httpProcessor.getUrl());
        Assertions.assertEquals(connectionClient, httpProcessor.getConnectionType().getValue());
    }
}
