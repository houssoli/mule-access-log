package com.greenbird.mule.http.log.converter;

import com.greenbird.mule.http.log.TestLogAppender;
import com.greenbird.mule.http.log.layout.AccessLoggerPatternLayout;
import com.greenbird.mule.http.log.transformer.AccessLoggingMessageToHttpResponse;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.mule.DefaultMuleMessage;
import org.mule.RequestContext;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.MuleSession;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.session.DefaultMuleSession;
import org.mule.tck.MuleTestUtils;
import org.mule.transport.NullPayload;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public abstract class AccessLogConverterTestBase {
    protected static final String DEFAULT_VALUE = "testDefault";
    private TestLogAppender testLogAppender;
    private Logger logger;

    private MuleContext muleContext;

    @Before
    public void setUpLogger() {
        testLogAppender = new TestLogAppender(AccessLoggingMessageToHttpResponse.ACCESS_LOG_CATEGORY);
        logger = Logger.getLogger(AccessLoggingMessageToHttpResponse.ACCESS_LOG_CATEGORY);
    }

    @Before
    public void setUpContext() throws Exception {
        muleContext = new DefaultMuleContextFactory().createMuleContext();
    }

    @After
    public void tearDownLogger() {
        testLogAppender.close();
    }

    protected String logWithInboundProperty(char conversionCharacter, String name, Object value) {
        DefaultMuleMessage message = testMessage();
        if (value != null) {
            message.setInboundProperty(name, value);
        }
        return log(conversionCharacter, message);
    }

    protected String logWithSession(char conversionCharacter, MuleSession session) throws Exception {
        MuleMessage message = testMessage();
        MuleEvent testEvent = MuleTestUtils.getTestEvent(NullPayload.getInstance(), session, context());
        RequestContext.setEvent(testEvent);
        return log(conversionCharacter, message);
    }

    protected String logWithPayload(char conversionCharacter, Object payload) {
        MuleMessage message = testMessage();
        message.setPayload(payload);
        return log(conversionCharacter, message);
    }

    protected MuleContext context() {
        return muleContext;
    }

    private String log(char conversionCharacter, MuleMessage message) {
        return log(specifierWithDefault(conversionCharacter), message);
    }

    protected String log(String pattern, MuleMessage message) {
        logger.info(message);
        assertThat(testLogAppender.getLoggingEvents().size(), is(1));
        return new AccessLoggerPatternLayout(pattern).format(testLogAppender.getLoggingEvents().get(0));
    }

    private String specifierWithDefault(char conversionCharacter) {
        return format("%%%s{%s}", conversionCharacter, DEFAULT_VALUE);
    }

    protected DefaultMuleMessage testMessage() {
        MuleEvent testEvent;
        try {
            testEvent = MuleTestUtils.getTestEvent(NullPayload.getInstance(), new DefaultMuleSession(), context());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestContext.setEvent(testEvent);
        return new DefaultMuleMessage(NullPayload.getInstance(), muleContext);
    }

    protected TestLogAppender logAppender() {
        return testLogAppender;
    }
}
