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

package com.greenbird.mule.http.log.factory;

import com.greenbird.mule.http.log.AccessLogConverterTestBase;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpRequest;
import org.mule.transport.http.RequestLine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.greenbird.mule.http.log.factory.RequestPropertiesRetainingHttpMuleMessageFactory.INITIAL_REQUEST_PROPERTY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestPropertiesRetainingHttpMuleMessageFactoryTest extends AccessLogConverterTestBase {
    private static final String ENCODING = "UTF-8";
    private static final String REQUEST = "/path?query=value";
    private RequestPropertiesRetainingHttpMuleMessageFactory transformer;

    @Before
    public void setUp() {
        transformer = new RequestPropertiesRetainingHttpMuleMessageFactory(context());
    }

    @Test
    public void transformMessage_normal_relevantPropertiesRetained() throws Exception {
        MuleMessage message = transformer.create(httpRequest(), ENCODING);
        Map<String, Object> retainedProperties =
                message.getInvocationProperty(RequestPropertiesRetainingHttpMuleMessageFactory.INITIAL_REQUEST_PROPERTY);
        assertThat(retainedProperties.size(), is(7));
        @SuppressWarnings("unchecked")
        Map<String, String> headers = (Map<String, String>) retainedProperties.get(HttpConnector.HTTP_HEADERS);
        assertThat(headers.get("testHeader"), is("testValue"));
        assertThat(retainedProperties.get(HttpConnector.HTTP_METHOD_PROPERTY).toString(), is("GET"));
        assertThat(retainedProperties.get(HttpConnector.HTTP_VERSION_PROPERTY).toString(), is("HTTP/1.1"));
        assertThat(retainedProperties.get(HttpConnector.HTTP_REQUEST_PROPERTY).toString(), is(REQUEST));
        assertThat(retainedProperties.get(HttpConnector.HTTP_QUERY_STRING).toString(), is("query=value"));
    }

    @Test
    public void transformMessage_payloadNotHttpRequest_retainedPropertiesNotAvailable() throws Exception {
        GetMethod getMethod = mock(GetMethod.class);
        when(getMethod.getStatusLine()).thenReturn(new StatusLine("HTTP/1.1 200 Ok"));
        when(getMethod.getURI()).thenReturn(new URI("http://test", false));
        when(getMethod.getResponseHeaders()).thenReturn(new Header[0]);
        MuleMessage message = transformer.create(getMethod, ENCODING);
        Map<String, Object> retainedProperties =
                message.getInvocationProperty(INITIAL_REQUEST_PROPERTY);
        assertThat(retainedProperties, is(nullValue()));
    }

    @Test
    public void addProperties_retainedPropertiesAlreadySet_propertiesLeftAsIs() throws Exception {
        DefaultMuleMessage message = testMessage();
        HashMap<String, Object> initialRetainedProperties = new HashMap<String, Object>();
        message.setInvocationProperty(INITIAL_REQUEST_PROPERTY,
                initialRetainedProperties);
        transformer.addProperties(message, httpRequest());
        assertThat(message.getInvocationProperty(INITIAL_REQUEST_PROPERTY), is((Object) initialRetainedProperties));
    }

    private HttpRequest httpRequest() throws IOException {
        return new HttpRequest(new RequestLine("GET", REQUEST, HttpVersion.HTTP_1_1),
                new Header[]{new Header("testHeader", "testValue")}, ENCODING);
    }
}
