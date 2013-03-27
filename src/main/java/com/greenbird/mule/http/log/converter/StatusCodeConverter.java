package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.transport.http.HttpResponse;

public class StatusCodeConverter extends AbstractResponseValueConverter {
    public final static char CONVERSION_CHARACTER = 's';

    public StatusCodeConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected String convertFromResponse(HttpResponse response) {
        return String.valueOf(response.getStatusCode());
    }
}
