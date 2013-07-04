package com.greenbird.mule.http.log;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.transport.http.HttpResponse;

class AccessLoggingHelper {
    private AccessLogger accessLogger = new AccessLogger();
    private RequestPropertiesRetainer requestPropertiesRetainer = new RequestPropertiesRetainer();

    MessageProcessor getListener(final MessageProcessor originalListener) {
        return new MessageProcessor() {
            @Override
            public MuleEvent process(MuleEvent event) throws MuleException {
                requestPropertiesRetainer.retainRequestProperties(event.getMessage());
                return originalListener.process(event);
            }
        };
    }

    // This is the normal way we capture the response message.
    // The receiver apply its default transformers after the execution flow has returned.
    void logAfterApply(MuleEvent event) {
        accessLogger.log(event.getMessage());
    }

    // This is where we log the response message if the normal execution flow 
    // is interrupted or can not be started (e.g. if the requested resource can not be found).
    HttpResponse logTransformedReply(HttpResponse httpResponse, MuleEvent event) {
        event.getMessage().setPayload(httpResponse);
        accessLogger.log(event.getMessage());
        return httpResponse;
    }
}
