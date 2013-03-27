package com.greenbird.mule.http.log.layout;

import com.greenbird.mule.http.log.converter.AbstractAccessLogConverter;
import com.greenbird.mule.http.log.converter.ContentLengthConverter;
import com.greenbird.mule.http.log.converter.HttpMethodConverter;
import com.greenbird.mule.http.log.converter.ProtocolVersionConverter;
import com.greenbird.mule.http.log.converter.ReferrerConverter;
import com.greenbird.mule.http.log.converter.RemoteHostConverter;
import com.greenbird.mule.http.log.converter.RequestPathConverter;
import com.greenbird.mule.http.log.converter.RequestQueryConverter;
import com.greenbird.mule.http.log.converter.StatusCodeConverter;
import com.greenbird.mule.http.log.converter.UserAgentConverter;
import com.greenbird.mule.http.log.converter.UserNameConverter;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

/**
 * log4j 1.2 <a href="http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html"
 * target="_blank">PatternLayout</a> extension that adds the HTTP request/response specific conversion characters described below.
 * <p/>
 * All specifiers may optionally be followed by an option specifier enclosed between braces. The option format
 * is <code><b>{[default value][;qt]}</b></code>
 * <ul>
 * <li>
 * <code><b>default value</b></code>: any string you want to be displayed if the conversion produces a
 * <code>null</code> result. E.g: <b>%a{-}</b> will print a dash if the user agent is not available.
 * <p/>
 * The empty string is used if no default value is specified.
 * </li>
 * <li>
 * <code><b>;qt</b></code>: Specifies that you want the conversion result to be surrounded by "quotes".
 * Note that the default value is not quoted. If you want the default value to be quoted you need to add
 * quotes to the default value specification: <b>%a{"Agent unknown";qt}</b>.
 * </li>
 * </ul>
 * <p/>
 * <table border="1" CELLPADDING="8">
 * <tr><th>Conversion Character</th> <th>Effect</th></tr>
 * <tr>
 * <td align=center><b>h</b></td>
 * <td>The ip address of the requesting client (remote-<b>h</b>ost).</td>
 * </tr>
 * <tr>
 * <td align=center><b>u</b></td>
 * <td>The <b>u</b>ser id of the person requesting the document as determined by HTTP authentication.</td>
 * </tr>
 * <tr>
 * <td align=center><b>e</b></td>
 * <td>The HTTP m<b>e</b>thod of the request.</td>
 * </tr>
 * <tr>
 * <td align=center><b>U</b></td>
 * <td>The path of the requested resource.</td>
 * </tr>
 * <tr>
 * <td align=center><b>q</b></td>
 * <td>The request <b>q</b>uery string prefixed with '?'.</td>
 * </tr>
 * <tr>
 * <td align=center><b>H</b></td>
 * <td>The <b>H</b>TTP protocol version of the request.</td>
 * </tr>
 * <tr>
 * <td align=center><b>s</b></td>
 * <td>The HTTP <b>s</b>tatus code of the response.</td>
 * </tr>
 * <tr>
 * <td align=center><b>b</b></td>
 * <td>The size of the returned content in <b>b</b>ytes (content-length).</td>
 * </tr>
 * <tr>
 * <td align=center><b>f</b></td>
 * <td>Re<b>f</b>errer. The page the current request was linked from.</td>
 * </tr>
 * <tr>
 * <td align=center><b>a</b></td>
 * <td>The user <b>a</b>gent that created the request.
 * </tr>
 * </table>
 * <p/>
 * <p/>
 * Example pattern that corresponds to the <a href="http://httpd.apache.org/docs/1.3/logs.html#combined"
 * target="_blank">Apache combined access log format</a>:
 * <p/>
 * <code><b>%h{-} - %u{-} [%d{dd/MMM/yyyy:HH:mm:ss Z}] "%e %U%q %H" %s{-} %b{-} %f{-;qt} %a{-;qt}%n</b></code>
 */
public class AccessLoggerPatternLayout extends PatternLayout {
    public AccessLoggerPatternLayout() {
        super();
    }

    public AccessLoggerPatternLayout(String pattern) {
        super(pattern);
    }

    @Override
    protected PatternParser createPatternParser(String pattern) {
        return new PatternParser(pattern) {
            @Override
            protected void finalizeConverter(char conversionCharacter) {
                switch (conversionCharacter) {
                    case RemoteHostConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new RemoteHostConverter(formattingInfo, extractOption()));
                        break;
                    case UserNameConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new UserNameConverter(formattingInfo, extractOption()));
                        break;
                    case HttpMethodConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new HttpMethodConverter(formattingInfo, extractOption()));
                        break;
                    case RequestPathConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new RequestPathConverter(formattingInfo, extractOption()));
                        break;
                    case RequestQueryConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new RequestQueryConverter(formattingInfo, extractOption()));
                        break;
                    case ProtocolVersionConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new ProtocolVersionConverter(formattingInfo, extractOption()));
                        break;
                    case StatusCodeConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new StatusCodeConverter(formattingInfo, extractOption()));
                        break;
                    case ContentLengthConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new ContentLengthConverter(formattingInfo, extractOption()));
                        break;
                    case ReferrerConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new ReferrerConverter(formattingInfo, extractOption()));
                        break;
                    case UserAgentConverter.CONVERSION_CHARACTER:
                        addAccessLogConverter(new UserAgentConverter(formattingInfo, extractOption()));
                        break;
                    default:
                        super.finalizeConverter(conversionCharacter);
                }
            }

            private void addAccessLogConverter(AbstractAccessLogConverter converter) {
                addConverter(converter);
                currentLiteral.setLength(0);
            }
        };
    }
}
