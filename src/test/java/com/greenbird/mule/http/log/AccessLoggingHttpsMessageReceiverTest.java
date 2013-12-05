package com.greenbird.mule.http.log;

import com.greenbird.test.mule.GreenbirdMuleFunctionalTestCase;
import org.junit.Test;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transformer.Transformer;
import org.mule.api.transport.Connector;
import org.mule.transport.ConnectException;
import org.mule.transport.http.HttpsConnector;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class AccessLoggingHttpsMessageReceiverTest extends GreenbirdMuleFunctionalTestCase {
    @Override
    protected String getConfigResources() {
        return "mule/mule-access-log-receiver-test.xml";
    }

    @Test
    public void connect_multipleCalls_transformersOnlyAddedOnce() throws Exception {
        InboundEndpoint endpoint = bean("https-test-endpoint");
        new TestReceiver(bean(HttpsConnector.class), flow("test-flow"), endpoint).test();

    }

    private static class TestReceiver extends AccessLoggingHttpsMessageReceiver {
        private TestReceiver(Connector connector, FlowConstruct flowConstruct, InboundEndpoint endpoint) throws CreateException {
            super(connector, flowConstruct, endpoint);
        }

        void test() throws ConnectException {
            defaultInboundTransformers = new ArrayList<Transformer>();
            defaultResponseTransformers = new ArrayList<Transformer>();
            doConnect();
            doConnect();
            assertThat(defaultInboundTransformers, hasSize(1));
            assertThat(defaultResponseTransformers, hasSize(1));

        }
    }
}
