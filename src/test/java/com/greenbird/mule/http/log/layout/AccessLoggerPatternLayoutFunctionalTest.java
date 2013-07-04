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

import com.greenbird.mule.http.log.AccessLogger;
import com.greenbird.mule.http.log.TestLogAppender;
import com.greenbird.mule.http.log.layout.util.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.tomcat.util.buf.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.http.HttpConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AccessLoggerPatternLayoutFunctionalTest extends FunctionalTestCase {
    private static final String BASE_URL = "http://localhost:6428";
    private static final String HTTPS_BASE_URL = "https://localhost:6429";
    private static final String CLIENT_IP = "127.0.0.1";
    private static final String REFERRER = "http://test.com";
    private static final String AGENT = "Commons-HttpClient";
    private static final String GET = "GET";
    private static final int OK = 200;
    private static final int UNAUTHORIZED = 401;
    private static final String QUERY = "?parameter=value";
    private static final int NOT_FOUND = 404;
    private static final int SYSTEM_ERROR = 500;

    private TestLogAppender testLogAppender;
    private HttpClient httpClient;

    @Override
    protected String getConfigResources() {
        return "mule/mule-access-log-test.xml";
    }

    @Before
    public void setUpAppender() {
        testLogAppender = new TestLogAppender(AccessLogger.ACCESS_LOG_CATEGORY);
        testLogAppender.setLayout(new CombinedAccessLogPatternLayout());
    }

    @Before
    public void setHttpClient() {
        Protocol.registerProtocol("https",
                new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 6429));
        httpClient = new HttpClient();
    }

    @After
    public void tearDownAppender() {
        testLogAppender.close();
    }

    @Test
    public void httpRequest_normalRequest_allValuesLoggedExceptUser() {
        String path = "/testPath";
        String expectedContent = "Test body";
        String actualContent = httpRequest(path, OK);
        assertThat(actualContent, is(expectedContent));
        assertOnLogEntry(path, actualContent.length());
    }

    @Test
    public void httpRequest_responseTransformedOnFlow_allValuesLoggedExceptUser() {
        String path = "/responseTransformedTestPath";
        String expectedContent = "Test body with transformed response";
        String actualContent = httpRequest(path, OK);
        assertThat(actualContent, is(expectedContent));
        assertOnLogEntry(path, actualContent.length());
    }

    @Test
    public void httpRequest_flowViaOutboundEndpoint_allValuesLoggedExceptUser() {
        String path = "/testPathWithOutboundEndpoint";
        String expectedContent = "Test body via endpoint";
        String actualContent = httpRequest(path, OK);
        assertThat(actualContent, is(expectedContent));
        assertOnLogEntry(path, actualContent.length());
    }

    @Test
    public void httpRequest_securedResourceAccessGranted_allValues() {
        String path = "/securedTestPath";
        String expectedContent = "Authenticated test body";
        String actualContent = httpRequest(BASE_URL + path + QUERY, OK, "testUser", "testPw");
        assertThat(actualContent, is(expectedContent));
        assertOnLogEntry(CLIENT_IP, "testUser", GET, path, QUERY, OK, String.valueOf(actualContent.length()), REFERRER, AGENT);
    }

    @Test
    public void httpRequest_securedResourceAccessNotGranted_allValuesLoggedExceptUser() {
        String path = "/securedTestPath";
        String actualContent = httpRequest(path, UNAUTHORIZED);
        assertThat(actualContent, containsString("Authentication denied"));
        assertOnLogEntry(CLIENT_IP, "-", GET, path, QUERY, UNAUTHORIZED, String.valueOf(actualContent.length()), REFERRER, AGENT);
    }

    @Test
    public void httpRequest_resourceNotFound_onlyPathStatusAndContentLengthAreLogged() {
        String path = "/unknown";
        String actualContent = httpRequest(path, NOT_FOUND);
        assertThat(actualContent, containsString("Cannot bind"));
        assertOnLogEntry("-", "-", "-", path, "", NOT_FOUND, String.valueOf(actualContent.length()), "-", "-");
    }

    @Test
    public void httpRequest_exceptionInFLow_allValuesLoggedExceptUser() {
        String path = "/exceptionPath";
        String actualContent = httpRequest(path, SYSTEM_ERROR);
        assertThat(actualContent, containsString("ExpressionRuntimeException"));
        assertOnLogEntry(CLIENT_IP, "-", GET, path, QUERY, SYSTEM_ERROR, String.valueOf(actualContent.length()), REFERRER, AGENT);
    }

    @Test
    public void httpsRequest_normalRequest_allValuesLoggedExceptUser() {
        String path = "/httpsPath";
        String expectedContent = "Test body via HTTPS";
        String actualContent = httpsRequest(path, OK);
        assertThat(actualContent, is(expectedContent));
        assertOnLogEntry(path, actualContent.length());
    }

    @Test
    public void httpsRequest_resourceNotFound_onlyPathStatusAndContentLengthAreLogged() {
        String path = "/unknown";
        String actualContent = httpsRequest(path, NOT_FOUND);
        assertThat(actualContent, containsString("Cannot bind"));
        assertOnLogEntry("-", "-", "-", path, "", NOT_FOUND, String.valueOf(actualContent.length()), "-", "-");
    }

    private void assertOnLogEntry(String path, int contentLength) {
        assertOnLogEntry(CLIENT_IP, "-", GET, path, QUERY, OK, String.valueOf(contentLength), REFERRER, AGENT);
    }

    private void assertOnLogEntry(String clientIp, String userId, final String method, final String path, String query, int status,
                                  String contentLength, String referrer, String agent) {
        LogEntry entry = parseLogEntry();

        assertThat(entry.getUserId(), is(userId));
        assertThat(entry.getClientIp(), is(clientIp));

        String time = entry.getTime();
        try {
            //will throw exception if date is malformed
            new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z").parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertThat(entry.getRequest(), is(method + " " + path + query + " HTTP/1.1"));
        assertThat(entry.getStatus(), is(String.valueOf(status)));
        assertThat(entry.getContentLength(), is(contentLength));
        assertThat(entry.getReferrer(), is(referrer));
        assertThat(entry.getUserAgent(), containsString(agent));
    }

    private LogEntry parseLogEntry() {
        List<LoggingEvent> loggingEvents = testLogAppender.getLoggingEvents();
        assertThat(loggingEvents.size(), is(1));
        assertThat(loggingEvents.get(0).getLevel(), is(Level.INFO));
        String accessLine = testLogAppender.getLogLines().get(0);
        return new LogEntry(accessLine);
    }

    private String httpRequest(String path, int expectedStatus) {
        return httpRequest(BASE_URL + path + QUERY, expectedStatus, null, null);
    }

    private String httpsRequest(String path, int expectedStatus) {
        return httpRequest(HTTPS_BASE_URL + path + QUERY, expectedStatus, null, null);
    }

    private String httpRequest(String url, int expectedStatus, String username, String password) {
        GetMethod method = new GetMethod(url);
        method.setDoAuthentication(true);
        method.addRequestHeader(HttpConstants.HEADER_REFERER, REFERRER);
        if (username != null) {
            String credentials;
            try {
                credentials = new String(Base64.encode((username + ":" + password).getBytes()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            method.addRequestHeader(HttpConstants.HEADER_AUTHORIZATION, "Basic " + credentials);
        }

        String content;
        try {
            httpClient.executeMethod(method);
            assertThat(method.getStatusCode(), is(expectedStatus));
            content = method.getResponseBodyAsString();
            method.releaseConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    private static class LogEntry {
        private static final Pattern LOG_PATTERN = Pattern.compile("^(.*?) - (.*?) \\[(.*?)\\] \"(.*?)\" (\\d{3}) (\\d+|-) (\".*?\"|-) (\".*?\"|-)\n$");
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
