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
import org.mule.transport.http.HttpResponse;

public abstract class AbstractResponseValueConverter extends AbstractAccessLogConverter {

    public AbstractResponseValueConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected final String doConvert(MuleMessage message) {
        String result = null;
        if (message.getPayload() instanceof HttpResponse) {
            result = convertFromResponse((HttpResponse) message.getPayload());
        } else {
            notifyError(String.format("Expected payload of type %s but was %s.",
                    HttpResponse.class.getName(), message.getPayload().getClass().getName()));
        }
        return result;
    }

    protected abstract String convertFromResponse(HttpResponse response);
}
