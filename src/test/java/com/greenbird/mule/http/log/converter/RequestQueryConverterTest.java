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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestQueryConverterTest extends AccessLogConverterTestBase {
    private static final String QUERY = "query";

    @Test
    public void doConvert_queryAvailable_queryUsed() {
        String logOutput = logWithInboundProperty(RequestQueryConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_QUERY_STRING, QUERY);
        assertThat(logOutput, is("?" + QUERY));
    }

    @Test
    public void doConvert_queryNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(RequestQueryConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_QUERY_STRING, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
