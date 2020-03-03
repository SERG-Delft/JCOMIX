package nl.tudelft.io.writers;

import org.junit.jupiter.api.Test;

abstract class TestWriterTest {

    private TestWriter testWriter;


    @Test
    abstract void writeTest();

    @Test
    abstract void getProperties();

    TestWriter getTestWriter () {
        return testWriter;
    }

    void setTestWriter(TestWriter testWriter) {
        this.testWriter = testWriter;
    }
}