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

import com.greenbird.mule.http.log.factory.RequestPropertiesRetainingHttpMuleMessageFactory;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;
import org.mule.api.MuleMessage;

import java.util.Date;
import java.util.Map;

public abstract class AbstractAccessLogConverter extends PatternConverter {
    private static final String QUOTE_OPTION = ";qt";
    private String defaultValue = "";
    private boolean quote;

    public AbstractAccessLogConverter(FormattingInfo formattingInfo, String option) {
        super(formattingInfo);
        if (option != null) {
            if (option.equals(QUOTE_OPTION)) {
                quote = true;
            } else if (option.endsWith(QUOTE_OPTION)) {
                quote = true;
                defaultValue = option.replaceFirst("(.*)" + QUOTE_OPTION, "$1");
            } else {
                defaultValue = option;
            }
        }
    }

    @Override
    protected String convert(LoggingEvent event) {
        String result;
        if (event.getMessage() instanceof MuleMessage) {
            result = formatResult(doConvert((MuleMessage) event.getMessage()));
        } else {
            notifyError(String.format("Got log message '%s', but expected a message of type MuleMessage.", event.getMessage()));
            result = formatResult(null);
        }
        return result;
    }

    /**
     * Notify about an error situation without using the logger API. Using the logger API might lead to eternal recursive
     * logging if the http log category is not properly configured to be logged separately from the other log categories.
     *
     * @param message the error message to log.
     */
    protected void notifyError(String message) {
        System.out.println(String.format("ERROR %s %s %s", new Date(), getClass().getName(), message));
    }

    protected String formatResult(String value) {
        String result;
        if (value != null && quote) {
            result = String.format("\"%s\"", value);
        } else {
            result = value;
        }
        return result != null ? result : defaultValue;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getProperty(String propertyName, MuleMessage message) {
        Object value = null;
        Map<String, Object> retainedProperties =
                message.getInvocationProperty(RequestPropertiesRetainingHttpMuleMessageFactory.INITIAL_REQUEST_PROPERTY);
        if (retainedProperties != null) {
            value = retainedProperties.get(propertyName);
        }
        if (value == null) {
            value = message.getInboundProperty(propertyName);
        }
        return (T) value;
    }

    protected abstract String doConvert(MuleMessage message);
}
