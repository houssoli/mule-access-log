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

package com.greenbird.mule.http.log.layout;

import com.greenbird.mule.http.log.AccessLogConverterTestBase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.transport.http.HttpConnector;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AccessLoggerPatternLayoutTest extends AccessLogConverterTestBase {
    private static final String PATH = "/path";

    private AccessLoggerPatternLayout layout = new AccessLoggerPatternLayout();
    private DefaultMuleMessage message;


    @Before
    public void setUp() {
        message = testMessage();
        message.setInboundProperty(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
    }

    @Test
    public void instantiate_patternGivenInSetter_patternParsingOk() {
        layout.setConversionPattern("%U");
        String formattedResult = layout.format(new LoggingEvent(null, Logger.getLogger(getClass()), Level.INFO, message, null));
        assertThat(formattedResult, is(PATH));
    }

    @Test
    public void instantiate_patternGivenInConstructor_patternParsingOk() {
        layout = new AccessLoggerPatternLayout("%U");
        String formattedResult = layout.format(new LoggingEvent(null, Logger.getLogger(getClass()), Level.INFO, message, null));
        assertThat(formattedResult, is(PATH));
    }

    @Test
    public void format_unknownConversionCharacter_formattingDelegatedToSuperclass() {
        layout = new AccessLoggerPatternLayout("%c");
        String formattedResult = layout.format(new LoggingEvent(null, Logger.getLogger(getClass()), Level.INFO, message, null));
        assertThat(formattedResult, is(getClass().getName()));
    }

    @Test
    public void format_formatModifierSpecified_formatModifierDelegatedToSuperclass() {
        layout = new AccessLoggerPatternLayout("%-10U");
        String formattedResult = layout.format(new LoggingEvent(null, Logger.getLogger(getClass()), Level.INFO, message, null));
        assertThat(formattedResult, is(format("%s     ", PATH)));
    }
}
