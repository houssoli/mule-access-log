package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.api.MuleMessage;
import org.mule.transport.http.HttpConnector;

import java.util.Map;

public abstract class AbstractHttpHeaderConverter extends AbstractAccessLogConverter {

    public AbstractHttpHeaderConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected final String doConvert(MuleMessage message) {
        Map<String, String> headers = message.getInboundProperty(HttpConnector.HTTP_HEADERS);

        String result = null;
        if (headers != null) {
            result = convertFromHeaders(headers);
        } else {
            notifyError(String.format("Expected HTTP headers on inbound property %s, but found none.", 
                    HttpConnector.HTTP_HEADERS));
        }
        return result;
    }

    protected abstract String convertFromHeaders(Map<String, String> headers);
}
