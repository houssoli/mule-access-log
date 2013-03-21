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

package org.greenbird.mule.http.log;

import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.http.HttpConstants;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class CombinedLoggingMessageToHttpResponseFunctionalTest extends FunctionalTestCase {
    private static final String REFERRER = "http://test.com";
    private static final String BASE_URL = "http://localhost:6428";
    private static final String CLIENT_IP = "127.0.0.1";

    private TestLogAppender testLogAppender;

    @Override
    protected String getConfigResources() {
        return "mule/mule-access-log-test.xml";
    }

    @Before
    public void setUp() {
        testLogAppender = new TestLogAppender("http.accesslog");
    }

    @After
    public void tearDown() {
        testLogAppender.close();
    }

    @Test
    public void getRequest_securedResourceAccessGranted_expectedValuesAreLogged() throws Exception {
        String credentials = new BASE64Encoder().encode("admin:adminPw".getBytes());
        String request = "/securedTestPath?testParam=testParamValue";
        int status = 200;

        HttpURLConnection connection = connection(request);
        connection.setRequestProperty(HttpConstants.HEADER_AUTHORIZATION, "Basic " + credentials);
        connection.setRequestProperty(HttpConstants.HEADER_REFERER, REFERRER);

        connection.connect();
        assertThat(connection.getResponseCode(), is(status));

        assertOnLogEntry("admin", request, status, String.valueOf("Authenticated test body".length()), "http://test.com");
    }

    @Test
    public void getRequest_securedResourceAccessNot_expectedValuesAreLogged() throws Exception {
        String request = "/securedTestPath?testParam=testParamValue";
        int status = 401;

        HttpURLConnection connection = connection(request);
        connection.setRequestProperty(HttpConstants.HEADER_REFERER, REFERRER);

        connection.connect();
        assertThat(connection.getResponseCode(), is(status));

        assertOnLogEntry("-", request, status, "268", "http://test.com");
    }

    @Test
    public void getRequest_minimalRequest_expectedValuesAreLogged() throws Exception {
        String request = "/testPath";
        int status = 200;

        HttpURLConnection connection = connection(request);

        connection.connect();
        assertThat(connection.getResponseCode(), is(status));

        assertOnLogEntry("-", request, status, String.valueOf("Test body".length()), "-");
    }

    private void assertOnLogEntry(String expectedUserId, final String expectedRequest, int expectedStatus, String expectedContentLength, String expectedReferrer) throws Exception {
        LogEntry entry = parseLogEntry();

        assertThat(entry.getUserId(), is(expectedUserId));
        assertThat(entry.getClientIp(), is(CLIENT_IP));

        String time = entry.getTime();
        //will throw exception if date is malformed
        new SimpleDateFormat(CombinedLoggingMessageToHttpResponse.DATE_FORMAT).parse(time);

        assertThat(entry.getRequest(), is("GET " + expectedRequest + " HTTP/1.1"));
        assertThat(entry.getStatus(), is(String.valueOf(expectedStatus)));
        assertThat(entry.getContentLength(), is(expectedContentLength));
        assertThat(entry.getReferrer(), is(expectedReferrer));
        assertThat(entry.getUserAgent(), containsString("Java"));
    }

    private LogEntry parseLogEntry() throws IOException {
        List<LoggingEvent> loggingEvents = testLogAppender.getLoggingEvents();
        assertThat(loggingEvents.size(), is(1));
        String accessLine = loggingEvents.get(0).getRenderedMessage();
        return new LogEntry(accessLine);
    }

    private HttpURLConnection connection(final String request) throws IOException {
        return (HttpURLConnection) new URL(BASE_URL + request).openConnection();
    }

    private static class LogEntry {
        private static final Pattern LOG_PATTERN = Pattern.compile("^(.*?) - (.*?) \\[(.*?)\\] \"(.*?)\" (\\d{3}) (\\d+|-) (\".*?\"|-) (\".*?\"|-)$");
        private String clientIp;
        private String userId;
        private String time;
        private String request;
        private String status;
        private String contentLength;
        private String referrer;
        private String userAgent;

        private LogEntry(String logLine) {
            Matcher logMatcher = LOG_PATTERN.matcher(logLine);
            assertThat(logMatcher.matches(), is(true));
            clientIp = logMatcher.group(1);
            userId = logMatcher.group(2);
            time = logMatcher.group(3);
            request = logMatcher.group(4);
            status = logMatcher.group(5);
            contentLength = logMatcher.group(6);
            referrer = logMatcher.group(7).replace("\"", "");
            userAgent = logMatcher.group(8).replace("\"", "");
        }

        public String getClientIp() {
            return clientIp;
        }

        public String getUserId() {
            return userId;
        }

        public String getTime() {
            return time;
        }

        public String getRequest() {
            return request;
        }

        public String getStatus() {
            return status;
        }

        public String getContentLength() {
            return contentLength;
        }

        public String getReferrer() {
            return referrer;
        }

        public String getUserAgent() {
            return userAgent;
        }
    }
}
