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

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpMuleMessageFactory;
import org.mule.transport.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestPropertiesRetainingHttpMuleMessageFactory extends HttpMuleMessageFactory {
    public static final String INITIAL_REQUEST_PROPERTY = RequestPropertiesRetainingHttpMuleMessageFactory.class.getName() + ".initialRequestProperties";
    private static final String[] PROPERTIES_TO_RETAIN = new String[]{
            HttpConnector.HTTP_HEADERS,
            HttpConnector.HTTP_METHOD_PROPERTY,
            HttpConnector.HTTP_VERSION_PROPERTY,
            HttpConnector.HTTP_REQUEST_PROPERTY,
            HttpConnector.HTTP_REQUEST_PATH_PROPERTY,
            HttpConnector.HTTP_QUERY_STRING,
            MuleProperties.MULE_REMOTE_CLIENT_ADDRESS
    };

    public RequestPropertiesRetainingHttpMuleMessageFactory(MuleContext context) {
        super(context);
    }

    @Override
    protected void addProperties(DefaultMuleMessage message, Object transportMessage) throws Exception {
        super.addProperties(message, transportMessage);
        if (transportMessage instanceof HttpRequest && message.getInvocationProperty(INITIAL_REQUEST_PROPERTY) == null) {
            Map<String, Object> retainedProperties = new HashMap<String, Object>(PROPERTIES_TO_RETAIN.length);
            for (String property : PROPERTIES_TO_RETAIN) {
                retainedProperties.put(property, message.getInboundProperty(property));
            }
            message.setInvocationProperty(INITIAL_REQUEST_PROPERTY, retainedProperties);
        }
    }
}
