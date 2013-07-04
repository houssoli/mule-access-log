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
import org.mule.transport.http.HttpResponse;

public class ProtocolVersionConverter extends AbstractAccessLogConverter {
    public final static char CONVERSION_CHARACTER = 'H';

    public ProtocolVersionConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected String doConvert(MuleMessage message) {
        String version = getProperty(HttpConnector.HTTP_VERSION_PROPERTY, message);
        if(version == null && message.getPayload() instanceof HttpResponse) {
            version = ((HttpResponse)message.getPayload()).getHttpVersion().toString();
        }
        return version;
    }
}
