package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.mule.api.config.MuleProperties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RemoteHostConverterTest extends AccessLogConverterTestBase {
    private static final String REGULAR_IP = "1.2.3.4";
    private static final String PATH_AND_PORT_STYLE_IP = "/1.2.3.4:567";

    @Test
    public void doConvert_regularIpAddress_addressReturnedAsIs() {
        String logOutput = logWithInboundProperty(RemoteHostConverter.CONVERSION_CHARACTER,
                MuleProperties.MULE_REMOTE_CLIENT_ADDRESS, REGULAR_IP);
        assertThat(logOutput, is(REGULAR_IP));
    }

    @Test
    public void doConvert_pathAndPortStyleIpAddress_ipAddressExtracted() {
        String logOutput = logWithInboundProperty(RemoteHostConverter.CONVERSION_CHARACTER,
                MuleProperties.MULE_REMOTE_CLIENT_ADDRESS, PATH_AND_PORT_STYLE_IP);
        assertThat(logOutput, is(REGULAR_IP));
    }

    @Test
    public void doConvert_ipAddressNotAvailableDefaultDefined_defaultUsed() {
        String logOutput = logWithInboundProperty(RemoteHostConverter.CONVERSION_CHARACTER,
                MuleProperties.MULE_REMOTE_CLIENT_ADDRESS, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
