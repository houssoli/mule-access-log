/*
 * Copyright 2013 greenbird Integration Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
