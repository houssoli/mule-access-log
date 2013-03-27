package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.api.MuleMessage;
import org.mule.transport.http.HttpConnector;

import static org.springframework.util.StringUtils.hasText;

public class RequestQueryConverter extends AbstractAccessLogConverter {
    public final static char CONVERSION_CHARACTER = 'q';

    public RequestQueryConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected String doConvert(MuleMessage message) {
        String query = message.getInboundProperty(HttpConnector.HTTP_QUERY_STRING);
        if (hasText(query)) {
            query = String.format("?%s", query);
        }
        return query;
    }
}
