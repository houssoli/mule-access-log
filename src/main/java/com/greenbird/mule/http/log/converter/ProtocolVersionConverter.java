package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.api.MuleMessage;
import org.mule.transport.http.HttpConnector;

public class ProtocolVersionConverter extends AbstractAccessLogConverter {
    public final static char CONVERSION_CHARACTER = 'H';

    public ProtocolVersionConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected String doConvert(MuleMessage message) {
        return message.getInboundProperty(HttpConnector.HTTP_VERSION_PROPERTY);
    }
}
