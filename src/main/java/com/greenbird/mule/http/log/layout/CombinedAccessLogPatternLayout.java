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

/**
 * Pattern layout with a hardcoded pattern corresponding to the <a href="http://httpd.apache.org/docs/1.3/logs.html#combined"
 * target="_blank">Apache combined access log format</a>.
 * <p/>
 * This format is understood by most web log analysis tools.
 */
public class CombinedAccessLogPatternLayout extends AccessLoggerPatternLayout {
    public static final String PATTERN = "%h{-} - %u{-} [%d{dd/MMM/yyyy:HH:mm:ss Z}] \"%e{-} %U%q %H\" %s{-} %b{-} %f{-;qt} %a{-;qt}%n";

    public CombinedAccessLogPatternLayout() {
        super(PATTERN);
    }

    public CombinedAccessLogPatternLayout(String pattern) {
        throw new UnsupportedOperationException("This layout uses a hardcoded pattern. Use the default constructor.");
    }

    @Override
    public void setConversionPattern(String conversionPattern) {
        throw new UnsupportedOperationException("This layout uses a hardcoded pattern.");
    }
}
