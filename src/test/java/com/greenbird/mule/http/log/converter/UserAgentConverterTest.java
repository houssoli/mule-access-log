package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpConstants;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserAgentConverterTest extends AccessLogConverterTestBase {
    private static final String USER_AGENT = "UserAgent";

    @Test
    public void doConvert_userAgentAvailable_userAgentUsed() {
        Map<String, String> headerMap = Collections.singletonMap(HttpConstants.HEADER_USER_AGENT, USER_AGENT);
        String logOutput = logWithInboundProperty(UserAgentConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_HEADERS, headerMap);
        assertThat(logOutput, is(USER_AGENT));
    }

    @Test
    public void doConvert_userAgentNotAvailable_defaultUsed() {
        Map<String, String> headerMap = Collections.emptyMap();
        String logOutput = logWithInboundProperty(UserAgentConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_HEADERS, headerMap);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    @Test
    public void doConvert_headersNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(UserAgentConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_HEADERS, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
