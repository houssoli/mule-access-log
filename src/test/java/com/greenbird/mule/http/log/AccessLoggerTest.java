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

package com.greenbird.mule.http.log;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AccessLoggerTest extends AccessLogConverterTestBase {
    private AccessLogger logger = new AccessLogger();

    @Test
    public void logMessage_success_messageContainingTheResponseLoggedToTheAccessLogCategory() throws TransformerException {
        MuleMessage expectedMessage = testMessage();
        logger.log(expectedMessage);

        assertThat(logAppender().getLoggingEvents().size(), is(1));
        LoggingEvent logEvent = logAppender().getLoggingEvents().get(0);
        assertThat(logEvent.getLevel(), is(Level.INFO));
        MuleMessage actualMessage = (MuleMessage) logEvent.getMessage();
        assertThat(actualMessage, is(expectedMessage));
    }
}
