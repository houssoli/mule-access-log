package com.greenbird.mule.http.log.layout;

/**
 * Pattern layout with a hardcoded pattern corresponding to the <a href="http://httpd.apache.org/docs/1.3/logs.html#combined"
 * target="_blank">Apache combined access log format</a>.
 * <p/>
 * This format is understood by most web log analysis tools.
 */
public class CombinedAccessLogPatternLayout extends AccessLoggerPatternLayout {
    public static final String PATTERN = "%h{-} - %u{-} [%d{dd/MMM/yyyy:HH:mm:ss Z}] \"%e %U%q %H\" %s{-} %b{-} %f{-;qt} %a{-;qt}%n";

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
