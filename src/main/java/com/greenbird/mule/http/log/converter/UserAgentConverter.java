package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.transport.http.HttpConstants;

import java.util.Map;

public class UserAgentConverter extends AbstractHttpHeaderConverter {
    public final static char CONVERSION_CHARACTER = 'a';

    public UserAgentConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }


    @Override
    protected String convertFromHeaders(Map<String, String> headers) {
        return headers.get(HttpConstants.HEADER_USER_AGENT);
    }
}
