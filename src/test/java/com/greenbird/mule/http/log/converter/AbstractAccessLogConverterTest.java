package com.greenbird.mule.http.log.converter;

import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.transport.http.HttpConnector;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test of the generic handling of options etc. in the converter super class. We use a random
 * converter implementation for testing. The behaviour is the same for all converters.
 */
public class AbstractAccessLogConverterTest extends AccessLogConverterTestBase {
    private static final String PATH = "/path";

    private DefaultMuleMessage message;

    @Before
    public void setUp() {
        message = testMessage();
    }

    @Test
    public void doConvert_noOptionAndValueIsAvailable_valueIsUsed() {
        message.setInboundProperty(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
        String logOutput = log("%U", message);
        assertThat(logOutput, is(PATH));
    }

    @Test
    public void doConvert_defaultOptionAndValueAvailable_valueIsUsed() {
        message.setInboundProperty(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
        String logOutput = log("%U{default}", message);
        assertThat(logOutput, is(PATH));
    }

    @Test
    public void doConvert_defaultOptionAndValueNotAvailable_defaultIsUsed() {
        String logOutput = log("%U{default}", message);
        assertThat(logOutput, is("default"));
    }

    @Test
    public void doConvert_quoteOptionOnlyAndValueAvailable_valueIsUsedAndQuoted() {
        message.setInboundProperty(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
        String logOutput = log("%U{;qt}", message);
        assertThat(logOutput, is(format("\"%s\"", PATH)));
    }

    @Test
    public void doConvert_quoteAndDefaultOptionAndValueAvailable_valueIsUsedAndQuoted() {
        message.setInboundProperty(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
        String logOutput = log("%U{default;qt}", message);
        assertThat(logOutput, is(format("\"%s\"", PATH)));
    }

    @Test
    public void doConvert_quoteOptionOnlyAndValueNotAvailable_emptyStringIsUsed() {
        String logOutput = log("%U{;qt}", message);
        assertThat(logOutput, is(""));
    }

    @Test
    public void doConvert_quoteAndDefaultOptionAndValueNotAvailable_unquotedDefaultIsUsed() {
        String logOutput = log("%U{default;qt}", message);
        assertThat(logOutput, is("default"));
    }

    @Test
    public void doConvert_quoteAndQuotedDefaultOptionAndValueNotAvailable_quotedDefaultIsUsed() {
        String logOutput = log("%U{\"default\";qt}", message);
        assertThat(logOutput, is("\"default\""));
    }
}
