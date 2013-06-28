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

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.api.MuleMessage;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpConstants;
import org.mule.transport.http.HttpResponse;

import java.net.MalformedURLException;
import java.net.URL;

public class RequestPathConverter extends AbstractAccessLogConverter {
    public final static char CONVERSION_CHARACTER = 'U';

    public RequestPathConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected String doConvert(MuleMessage message) {
        String path = getProperty(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, message);
        if (path == null) {
            path = getPathFromRequestProperty(message);
        }
        if (path == null && message.getPayload() instanceof HttpResponse) {
            path = getPathFromResponse((HttpResponse) message.getPayload());
        }
        return path;
    }

    private String getPathFromRequestProperty(MuleMessage message) {
        String path = null;
        String request = getProperty(HttpConnector.HTTP_REQUEST_PROPERTY, message);
        if (request != null) {
            URL requestUrl;
            try {
                requestUrl = new URL("http://dummy" + request);
                path = requestUrl.getPath();
            } catch (MalformedURLException e) {
                notifyError(String.format("Error while extracting path from request '%s': %s", request, e));
            }
        }
        return path;
    }

    private String getPathFromResponse(HttpResponse response) {
        String path = null;
        try {
            if (response.getStatusCode() == HttpConstants.SC_NOT_FOUND &&
                    response.getBodyAsString().startsWith("Cannot bind to address \"")) {
                path = new URL(response.getBodyAsString().replaceAll(".*?\"(.*?)\".*", "$1")).getPath();
            }
        } catch (Exception e) {
            notifyError(String.format("Error while extracting path from not found response: %s", e));
        }
        return path;
    }
}
