package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpConstants;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReferrerConverterTest extends AccessLogConverterTestBase {
    private static final String REFERRER = "http://referrer";

    @Test
    public void doConvert_referrerAvailable_referrerUsed() {
        Map<String, String> headerMap = Collections.singletonMap(HttpConstants.HEADER_REFERER, REFERRER);
        String logOutput = logWithInboundProperty(ReferrerConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_HEADERS, headerMap);
        assertThat(logOutput, is(REFERRER));
    }

    @Test
    public void doConvert_referrerNotAvailable_defaultUsed() {
        Map<String, String> headerMap = Collections.emptyMap();
        String logOutput = logWithInboundProperty(ReferrerConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_HEADERS, headerMap);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    @Test
    public void doConvert_headersNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(ReferrerConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_HEADERS, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
