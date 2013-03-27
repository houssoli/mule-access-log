/*
 * Copyright 2013 greenbird Integration Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greenbird.mule.http.log.transformer;

import com.greenbird.mule.http.log.converter.AccessLogConverterTestBase;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transport.http.HttpResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AccessLoggingMessageToHttpResponseTest extends AccessLogConverterTestBase {
    private AccessLoggingMessageToHttpResponse transformer = new AccessLoggingMessageToHttpResponse();

    @Test
    public void transformMessage_success_messageContainingTheResponseLoggedToTheAccessLogCategory() throws TransformerException {
        MuleMessage expectedMessage = testMessage();
        HttpResponse expectedResponse = (HttpResponse) transformer.transformMessage(expectedMessage, null);
        assertThat(expectedResponse, is(notNullValue()));
        assertThat(logAppender().getLoggingEvents().size(), is(1));
        LoggingEvent logEvent = logAppender().getLoggingEvents().get(0);
        assertThat(logEvent.getLevel(), is(Level.INFO));
        MuleMessage actualMessage = (MuleMessage) logEvent.getMessage();
        assertThat(actualMessage, is(expectedMessage));
        HttpResponse actualResponse = (HttpResponse) actualMessage.getPayload();
        assertThat(actualResponse, is(expectedResponse));
    }
}
