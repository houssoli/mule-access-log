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

import org.mule.RequestContext;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.api.security.SecurityContext;
import org.mule.api.transformer.TransformerException;
import org.mule.transport.http.HttpConstants;
import org.mule.transport.http.HttpResponse;
import org.mule.transport.http.transformers.MuleMessageToHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.format;

/**
 * Transformer that extends the regular {@code MuleMessageToHttpResponse} class by adding access logging on the
 * <a href="http://httpd.apache.org/docs/1.3/logs.html#combined" target="_blank">Apache combined log format</a>.
 * This format is understood by most log analysis tools.
 * 
 * The log statements are logged on the {@code INFO} level to the {@code http.accesslog} log category. You should 
 * configure this category to be written to a separate file with a log format containing only the log message.
 */
public class CombinedLoggingMessageToHttpResponse extends MuleMessageToHttpResponse {
    public static final String DATE_FORMAT = "dd/MMM/yyyy:HH:mm:ss Z";
    private Logger logger = LoggerFactory.getLogger("http.accesslog");

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        HttpResponse response = (HttpResponse) super.transformMessage(message, outputEncoding);
        String clientIp = formatEntry(getClientIp(message));
        String userId = formatEntry(getUserId());
        String time = formatEntry(formatDate());
        String request = formatEntry(quote(buildRequestEntry(message)));
        String status = formatEntry(response.getStatusCode());
        String contentLength = formatEntry(response.getContentLength());
        String referrer = formatEntry(quote(httpHeader(message, HttpConstants.HEADER_REFERER)));
        String userAgent = formatEntry(quote(httpHeader(message, HttpConstants.HEADER_USER_AGENT)));

        String logEntry = format("%s - %s %s %s %s %s %s %s", clientIp, userId, time, request, status, contentLength, referrer, userAgent);
        logger.info(logEntry);
        return response;
    }

    private String getClientIp(MuleMessage message) {
        String address = message.getInboundProperty(MuleProperties.MULE_REMOTE_CLIENT_ADDRESS);
        return address != null ? address.replace("/", "").replaceAll(":.*", "") : null;
    }

    private String getUserId() {
        String user = null;
        SecurityContext securityContext = RequestContext.getEvent().getSession().getSecurityContext();
        if (securityContext != null) {
            Object principal = securityContext.getAuthentication().getPrincipal();
            if (principal instanceof User) {
                user = ((User) principal).getUsername();
            }
        }
        return user;
    }

    private String formatDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        String dateString = dateFormat.format(new Date());
        return format("[%s]", dateString);
    }

    private String buildRequestEntry(MuleMessage message) {
        return format("%s %s %s",
                message.getInboundProperty("http.method"),
                message.getInboundProperty("http.request"),
                message.getInboundProperty("http.version")
        );
    }

    private String quote(String value) {
        return value != null ? format("\"%s\"", value) : null;
    }

    private String httpHeader(MuleMessage message, String header) {
        Map<String, String> headers = message.getInboundProperty("http.headers");
        return headers != null ? headers.get(header) : null;
    }

    private String formatEntry(Object value) {
        String stringValue = value != null ? value.toString() : "-";
        return "0".equals(value) ? "-" : stringValue;
    }

}
