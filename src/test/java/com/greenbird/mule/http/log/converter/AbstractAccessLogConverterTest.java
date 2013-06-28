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

package com.greenbird.mule.http.log.converter;

import com.greenbird.mule.http.log.AccessLogConverterTestBase;
import com.greenbird.mule.http.log.factory.RequestPropertiesRetainingHttpMuleMessageFactory;
import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.transport.http.HttpConnector;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void getProperty_valueOnlyStoredInRetainedProperties_valueIsUsed() {
        Map<String, Object> retainedProperties = new HashMap<String, Object>();
        retainedProperties.put(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
        message.setInvocationProperty(RequestPropertiesRetainingHttpMuleMessageFactory.INITIAL_REQUEST_PROPERTY, 
                retainedProperties);
        String logOutput = log("%U{default}", message);
        assertThat(logOutput, is(PATH));
    }
}
