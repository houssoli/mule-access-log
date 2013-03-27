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
