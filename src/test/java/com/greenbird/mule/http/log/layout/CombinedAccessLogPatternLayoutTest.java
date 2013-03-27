package com.greenbird.mule.http.log.layout;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CombinedAccessLogPatternLayoutTest {
    @Rule
    public ExpectedException exceptionAssertion = ExpectedException.none();

    @Test
    public void instantiate_defaultConstructor_commonLogPatternUsed() {
        assertThat(new CombinedAccessLogPatternLayout().getConversionPattern(), is(CombinedAccessLogPatternLayout.PATTERN));
    }

    @Test
    public void instantiate_patternConstructor_unsupportedOperationExceptionIsThrown() {
        exceptionAssertion.expect(UnsupportedOperationException.class);
        exceptionAssertion.expectMessage("hardcoded");
        new CombinedAccessLogPatternLayout(null);
    }

    @Test
    public void setConversionPattern_normal_unsupportedOperationExceptionIsThrown() {
        exceptionAssertion.expect(UnsupportedOperationException.class);
        exceptionAssertion.expectMessage("hardcoded");
        new CombinedAccessLogPatternLayout().setConversionPattern(null);
    }
}
