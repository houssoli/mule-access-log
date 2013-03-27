package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.transport.http.HttpConnector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtocolVersionConverterTest extends AccessLogConverterTestBase {
    private static final String VERSION = "HTTP/1.1";

    @Test
    public void doConvert_versionAvailable_versionUsed() {
        String logOutput = logWithInboundProperty(ProtocolVersionConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_VERSION_PROPERTY, VERSION);
        assertThat(logOutput, is(VERSION));
    }

    @Test
    public void doConvert_versionNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(ProtocolVersionConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_VERSION_PROPERTY, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
