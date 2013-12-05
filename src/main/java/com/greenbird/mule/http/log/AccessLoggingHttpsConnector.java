package com.greenbird.mule.http.log;

import org.mule.api.MuleContext;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.api.transformer.Transformer;
import org.mule.api.transport.NoReceiverForEndpointException;
import org.mule.transport.http.HttpMessageReceiver;
import org.mule.transport.http.HttpsConnector;
import org.mule.transport.http.RequestLine;

import java.net.Socket;
import java.util.List;

public class AccessLoggingHttpsConnector extends HttpsConnector {
    private AccessLoggingHelper accessLoggingHelper = new AccessLoggingHelper();

    public AccessLoggingHttpsConnector(MuleContext context) {
        super(context);
    }

    @Override
    public HttpMessageReceiver lookupReceiver(Socket socket, RequestLine requestLine) throws NoReceiverForEndpointException {
        try {
            return super.lookupReceiver(socket, requestLine);
        } catch (NoReceiverForEndpointException e) {
            accessLoggingHelper.logNotFound(requestLine, getMuleContext());
            throw e;
        }
    }

    @Override
    public List<Transformer> getDefaultInboundTransformers(ImmutableEndpoint endpoint) {
        return accessLoggingHelper.addInboundTransformer(super.getDefaultInboundTransformers(endpoint));
    }

    @Override
    public List<Transformer> getDefaultResponseTransformers(ImmutableEndpoint endpoint) {
        return accessLoggingHelper.addResponseTransformer(super.getDefaultResponseTransformers(endpoint));
    }


}
