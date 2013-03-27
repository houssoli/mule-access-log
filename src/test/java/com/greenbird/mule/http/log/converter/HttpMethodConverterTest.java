package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.transport.http.HttpConnector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpMethodConverterTest extends AccessLogConverterTestBase {
    private static final String HTTP_METHOD = "METHOD";

    @Test
    public void doConvert_methodAvailable_methodUsed() {
        String logOutput = logWithInboundProperty(HttpMethodConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_METHOD_PROPERTY, HTTP_METHOD);
        assertThat(logOutput, is(HTTP_METHOD));
    }

    @Test
    public void doConvert_httpMethodNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(HttpMethodConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_METHOD_PROPERTY, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
