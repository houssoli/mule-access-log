package com.greenbird.mule.http.log.converter;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;
import org.mule.api.MuleMessage;

import java.util.Date;

public abstract class AbstractAccessLogConverter extends PatternConverter {
    private static final String QUOTE_OPTION = ";qt";
    private String defaultValue = "";
    private boolean quote;

    public AbstractAccessLogConverter(FormattingInfo formattingInfo, String option) {
        super(formattingInfo);
        if (option != null) {
            if (option.equals(QUOTE_OPTION)) {
                quote = true;
            } else if (option.endsWith(QUOTE_OPTION)) {
                quote = true;
                defaultValue = option.replaceFirst("(.*)" + QUOTE_OPTION, "$1");
            } else {
                defaultValue = option;
            }
        }
    }

    @Override
    protected String convert(LoggingEvent event) {
        String result;
        if (event.getMessage() instanceof MuleMessage) {
            result = formatResult(doConvert((MuleMessage) event.getMessage()));
        } else {
            notifyError(String.format("Got log message '%s', but expected a message of type MuleMessage.", event.getMessage()));
            result = formatResult(null);
        }
        return result;
    }

    /**
     * Notify about an error situation without using the logger API. Using the logger API might lead to eternal recursive
     * logging if the http log category is not properly configured to be logged separately from the other log categories.
     *
     * @param message the error message to log.
     */
    protected void notifyError(String message) {
        System.out.println(String.format("ERROR %s %s %s", new Date(), getClass().getName(), message));
    }

    protected String formatResult(String value) {
        String result;
        if (value != null && quote) {
            result = String.format("\"%s\"", value);
        } else {
            result = value;
        }
        return result != null ? result : defaultValue;
    }

    protected abstract String doConvert(MuleMessage message);
}
