package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.transport.http.HttpConnector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestPathConverterTest extends AccessLogConverterTestBase {
    private static final String PATH = "/path";

    @Test
    public void doConvert_pathAvailable_pathUsed() {
        String logOutput = logWithInboundProperty(RequestPathConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
        assertThat(logOutput, is(PATH));
    }

    @Test
    public void doConvert_pathNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(RequestPathConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_REQUEST_PATH_PROPERTY, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
