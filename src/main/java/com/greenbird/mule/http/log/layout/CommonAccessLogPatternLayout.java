package com.greenbird.mule.http.log.layout;

/**
 * Pattern layout with a hardcoded pattern corresponding to the <a href="http://httpd.apache.org/docs/1.3/logs.html#common"
 * target="_blank">Apache common access log format</a>.
 * <p/>
 * This format is understood by most web log analysis tools.
 */
public class CommonAccessLogPatternLayout extends AccessLoggerPatternLayout {
    public static final String PATTERN = "%h{-} - %u{-} [%d{dd/MMM/yyyy:HH:mm:ss Z}] \"%e %U%q %H\" %s{-} %b{-}%n";

    public CommonAccessLogPatternLayout() {
        super(PATTERN);
    }

    public CommonAccessLogPatternLayout(String pattern) {
        throw new UnsupportedOperationException("This layout uses a hardcoded pattern. Use the default constructor.");
    }

    @Override
    public void setConversionPattern(String conversionPattern) {
        throw new UnsupportedOperationException("This layout uses a hardcoded pattern.");
    }
}
