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
