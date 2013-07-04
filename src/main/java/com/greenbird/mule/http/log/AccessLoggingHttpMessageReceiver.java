package com.greenbird.mule.http.log;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.transport.Connector;
import org.mule.transport.http.HttpMessageReceiver;
import org.mule.transport.http.HttpResponse;

public class AccessLoggingHttpMessageReceiver extends HttpMessageReceiver {
    private AccessLoggingHelper loggingHelper = new AccessLoggingHelper();

    public AccessLoggingHttpMessageReceiver(Connector connector, FlowConstruct flowConstruct, InboundEndpoint endpoint) throws CreateException {
        super(connector, flowConstruct, endpoint);
    }

    @Override
    public void setListener(final MessageProcessor processor) {
        super.setListener(loggingHelper.getListener(processor));
    }

    @Override
    protected void applyResponseTransformers(MuleEvent event) throws MuleException {
        super.applyResponseTransformers(event);
        loggingHelper.logAfterApply(event);
    }

    @Override
    protected HttpResponse transformResponse(Object response, MuleEvent event) throws MuleException {
        HttpResponse httpResponse = super.transformResponse(response, event);
        loggingHelper.logTransformedReply(httpResponse, event);
        return httpResponse;
    }
}
