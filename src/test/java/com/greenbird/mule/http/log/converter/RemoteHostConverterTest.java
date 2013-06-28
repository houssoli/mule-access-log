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
import org.mule.api.config.MuleProperties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RemoteHostConverterTest extends AccessLogConverterTestBase {
    private static final String REGULAR_IP = "1.2.3.4";
    private static final String PATH_AND_PORT_STYLE_IP = "/1.2.3.4:567";

    @Test
    public void doConvert_regularIpAddress_addressReturnedAsIs() {
        String logOutput = logWithInboundProperty(RemoteHostConverter.CONVERSION_CHARACTER,
                MuleProperties.MULE_REMOTE_CLIENT_ADDRESS, REGULAR_IP);
        assertThat(logOutput, is(REGULAR_IP));
    }

    @Test
    public void doConvert_pathAndPortStyleIpAddress_ipAddressExtracted() {
        String logOutput = logWithInboundProperty(RemoteHostConverter.CONVERSION_CHARACTER,
                MuleProperties.MULE_REMOTE_CLIENT_ADDRESS, PATH_AND_PORT_STYLE_IP);
        assertThat(logOutput, is(REGULAR_IP));
    }

    @Test
    public void doConvert_ipAddressNotAvailableDefaultDefined_defaultUsed() {
        String logOutput = logWithInboundProperty(RemoteHostConverter.CONVERSION_CHARACTER,
                MuleProperties.MULE_REMOTE_CLIENT_ADDRESS, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
