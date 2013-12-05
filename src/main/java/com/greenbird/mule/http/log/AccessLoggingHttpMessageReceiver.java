package com.greenbird.mule.http.log;

import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.ConnectException;
import org.mule.transport.http.HttpMessageReceiver;

public class AccessLoggingHttpMessageReceiver extends HttpMessageReceiver {
    private AccessLoggingHelper loggingHelper = new AccessLoggingHelper();
    private boolean initialized;

    public AccessLoggingHttpMessageReceiver(Connector connector, FlowConstruct flowConstruct, InboundEndpoint endpoint) throws CreateException {
        super(connector, flowConstruct, endpoint);
    }

    @Override
    protected void doConnect() throws ConnectException {
        if (!initialized) {
            defaultInboundTransformers = loggingHelper.addInboundTransformer(defaultInboundTransformers);
            defaultResponseTransformers = loggingHelper.addResponseTransformer(defaultResponseTransformers);
            initialized = true;
        }
        super.doConnect();
    }
}
