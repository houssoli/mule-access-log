package com.greenbird.mule.http.log;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.Transformer;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.HttpConstants;
import org.mule.transport.http.HttpResponse;
import org.mule.transport.http.RequestLine;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

class AccessLoggingHelper {
    private AccessLogger accessLogger = new AccessLogger();
    private RequestPropertiesRetainer requestPropertiesRetainer = new RequestPropertiesRetainer();

    List<Transformer> addInboundTransformer(List<Transformer> originalTransformers) {
        return addTransformer(originalTransformers, new AbstractMessageTransformer() {
            @Override
            public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
                retainRequestProperties(message);
                return message.getPayload();
            }
        });
    }

    List<Transformer> addResponseTransformer(List<Transformer> originalTransformers) {
        return addTransformer(originalTransformers, new AbstractMessageTransformer() {
            @Override
            public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
                accessLogger.log(message);
                return message.getPayload();
            }
        });
    }

    void logNotFound(RequestLine requestLine, MuleContext muleContext) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusLine(requestLine.getHttpVersion(), HttpConstants.SC_NOT_FOUND);

        DefaultMuleMessage message = new DefaultMuleMessage(httpResponse, muleContext);
        message.setInboundProperty(HttpConnector.HTTP_METHOD_PROPERTY, requestLine.getMethod());

        try {
            URI uri = new URI(requestLine.getUri());
            message.setInboundProperty(HttpConnector.HTTP_REQUEST_PATH_PROPERTY, uri.getPath());
            if (hasText(uri.getQuery())) {
                message.setInboundProperty(HttpConnector.HTTP_QUERY_STRING, uri.getQuery());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace(); // we swallow this that should never happen
        }
        accessLogger.log(message);
    }

    void retainRequestProperties(MuleMessage message) {
        requestPropertiesRetainer.retainRequestProperties(message);
    }

    private List<Transformer> addTransformer(List<Transformer> originalTransformers, Transformer transformerToAppend) {
        List<Transformer> appendedList = new LinkedList<Transformer>(originalTransformers);
        appendedList.add(transformerToAppend);
        return appendedList;
    }

}
