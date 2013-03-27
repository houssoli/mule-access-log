package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.mule.transport.http.HttpResponse;

public class ContentLengthConverter extends AbstractResponseValueConverter {
    public final static char CONVERSION_CHARACTER = 'b';

    public ContentLengthConverter(FormattingInfo formattingInfo, String defaultValue) {
        super(formattingInfo, defaultValue);
    }

    @Override
    protected String convertFromResponse(HttpResponse response) {
        long contentLength = response.getContentLength();
        return contentLength > 0 ? String.valueOf(contentLength) : null;
    }
}
