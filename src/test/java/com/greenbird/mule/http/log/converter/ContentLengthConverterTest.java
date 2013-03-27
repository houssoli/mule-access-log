package com.greenbird.mule.http.log.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.transport.NullPayload;
import org.mule.transport.http.HttpResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentLengthConverterTest extends AccessLogConverterTestBase {
    private static final long LENGTH = 365L;

    @Mock
    private HttpResponse mockResponse;

    @Test
    public void doConvert_responseAvailable_responseContentLengthUsed() {
        when(mockResponse.getContentLength()).thenReturn(LENGTH);
        String logOutput = logWithPayload(ContentLengthConverter.CONVERSION_CHARACTER, mockResponse);
        assertThat(logOutput, is(String.valueOf(LENGTH)));
    }

    @Test
    public void doConvert_contentLengthIsZero_defaultUsed() {
        String logOutput = logWithPayload(ContentLengthConverter.CONVERSION_CHARACTER, mockResponse);
        assertThat(logOutput, is(String.valueOf(DEFAULT_VALUE)));
    }

    @Test
    public void doConvert_responseNotAvailable_defaultUsed() {
        String logOutput = logWithPayload(ContentLengthConverter.CONVERSION_CHARACTER, NullPayload.getInstance());
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
