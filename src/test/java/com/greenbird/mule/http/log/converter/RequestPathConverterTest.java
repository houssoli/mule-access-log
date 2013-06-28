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
import org.apache.commons.httpclient.HttpVersion;
import org.junit.Test;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpConstants;
import org.mule.transport.http.HttpResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestPathConverterTest extends AccessLogConverterTestBase {
    private static final String REQUEST = "/path?query";
    private static final String PATH = "/path";

    @Test
    public void doConvert_pathAvailableInRequestPathProperty_pathFromPropertyUsed() {
        String logOutput =
                logWithInboundProperty(RequestPathConverter.CONVERSION_CHARACTER, HttpConnector.HTTP_REQUEST_PATH_PROPERTY, PATH);
        assertThat(logOutput, is(PATH));
    }

    @Test
    public void doConvert_pathAvailableInRequestProperty_pathFromPropertyUsed() {
        String logOutput =
                logWithInboundProperty(RequestPathConverter.CONVERSION_CHARACTER, HttpConnector.HTTP_REQUEST_PROPERTY, REQUEST);
        assertThat(logOutput, is(PATH));
    }

    @Test
    public void doConvert_pathNotAvailable_defaultUsed() {
        String logOutput = logWithInboundProperty(RequestPathConverter.CONVERSION_CHARACTER,
                HttpConnector.HTTP_REQUEST_PATH_PROPERTY, null);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    @Test
    public void doConvert_pathAvailableInNotFoundReply_pathFromNotFoundReplyUsed() {
        HttpResponse response = new HttpResponse() {{
            setStatusLine(HttpVersion.HTTP_1_1, HttpConstants.SC_NOT_FOUND);
            setBody("Cannot bind to address \"http://localhost:61000/path\" No component registered on that endpoint");
        }};
        String logOutput = logWithPayload(RequestPathConverter.CONVERSION_CHARACTER, response);
        assertThat(logOutput, is(PATH));
    }

    @Test
    public void doConvert_replyStatusIsNotFoundButResponseDoesNotContainPath_defaultUsed() {
        HttpResponse response = new HttpResponse() {{
            setStatusLine(HttpVersion.HTTP_1_1, HttpConstants.SC_NOT_FOUND);
        }};
        String logOutput = logWithPayload(RequestPathConverter.CONVERSION_CHARACTER, response);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    @Test
    public void doConvert_replyHasStatusOtherThanNotFound_defaultUsed() {
        HttpResponse response = new HttpResponse() {{
            setStatusLine(HttpVersion.HTTP_1_1, HttpConstants.SC_NOT_FOUND);
            setBody("Cannot bind to address \"illegalPath\" No component registered on that endpoint");
        }};
        String logOutput = logWithPayload(RequestPathConverter.CONVERSION_CHARACTER, response);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }

    @Test
    public void doConvert_replyContainsIllegalPath_exceptionNotPropagatedAndDefaultUsed() {
        HttpResponse response = new HttpResponse() {{
            setStatusLine(HttpVersion.HTTP_1_1, HttpConstants.SC_BAD_REQUEST);
            setBody("Cannot bind to address \"http://localhost:61000/path\" No component registered on that endpoint");
        }};
        String logOutput = logWithPayload(RequestPathConverter.CONVERSION_CHARACTER, response);
        assertThat(logOutput, is(DEFAULT_VALUE));
    }
}
