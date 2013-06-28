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

public class ProtocolVersionConverterTest extends AccessLogConverterTestBase {
    private static final String VERSION = "HTTP/1.1";

    @Test
    public void doConvert_versionAvailable_versionUsed() {
        String logOutput = logWithInboundProperty(ProtocolVersionConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_VERSION_PROPERTY, VERSION);
        assertThat(logOutput, is(VERSION));
    }

    @Test
    public void doConvert_versionNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(ProtocolVersionConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_VERSION_PROPERTY, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
