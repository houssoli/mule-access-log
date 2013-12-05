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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class ReceiverBasedAccessLoggerFunctionalTest extends AccessLoggerFunctionalTestBase {
    @Override
    protected String getConfigResources() {
        return "mule/mule-access-log-receiver-test.xml";
    }

    @Test
    public void httpRequest_resourceNotFound_nothingIsLogged() {
        String path = "/unknown";
        String actualContent = httpRequest(path, NOT_FOUND);
        assertThat(actualContent, containsString("Cannot bind"));
        assertThat(testLogAppender.getLoggingEvents(), hasSize(0));
    }

    @Test
    public void httpsRequest_resourceNotFound_nothingIsLogged() {
        String path = "/unknown";
        String actualContent = httpsRequest(path, NOT_FOUND);
        assertThat(actualContent, containsString("Cannot bind"));
        assertThat(testLogAppender.getLoggingEvents(), hasSize(0));
    }
}
