package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.transport.http.HttpConnector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestQueryConverterTest extends AccessLogConverterTestBase {
    private static final String QUERY = "query";

    @Test
    public void doConvert_queryAvailable_queryUsed() {
        String logOutput = logWithInboundProperty(RequestQueryConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_QUERY_STRING, QUERY);
        assertThat(logOutput, is("?" + QUERY));
    }

    @Test
    public void doConvert_queryNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(RequestQueryConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_QUERY_STRING, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
