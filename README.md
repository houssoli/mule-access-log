# mule-access-log
Library for [Mule ESB] that enables web access logging from inbound [HTTP endpoints].

It extends the log4j 1.2 [PatternLayout] with support for conversion characters specific to HTTP request 
and response messages.

You can specify the log pattern yourself or use predefined patterns such as the [Apache combined log format] which is
understood by most web log analysis tools.

## License
[Apache 2.0]

## Obtain
The project is based on [Maven]. Project snapshots are available from the Sonatype OSS snapshot repository:
https://oss.sonatype.org/content/repositories/snapshots/.

Example dependency config:

```xml
<dependency>
    <groupId>com.greenbird.mule</groupId>
    <artifactId>greenbird-mule-access-log</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

When the project is released the artifacts will be deployed to the central Maven repository.

You can also download the jars directly if you need too:

Snapshots: https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/mule/greenbird-mule-access-log/

Include the jar as a runtime dependency for your project and you're ready to go.

## Usage

### 1. Replace the HTTP response transformer
Replace the the regular MuleMessageToHttpResponse transformer with our subclass AccessLoggingMessageToHttpResponse.

This is done on the http connector level like this:
```xml
<http:connector name="loggingHttpConnector">
    <service-overrides responseTransformer="com.greenbird.mule.http.log.transformer.AccessLoggingMessageToHttpResponse"/>
</http:connector>
``` 

This will ensure that the HTTP message is logged to the http.accesslog category on the INFO level.

### 2. Configure the http.accesslog log category
Update your [Mule log configuration] with a http.accesslog category that is written to a separate file.

Example log4j.properties fragment:

```
log4j.logger.http.accesslog=INFO, accessLog
log4j.additivity.http.accesslog=false

log4j.appender.accessLog=org.apache.log4j.RollingFileAppender
log4j.appender.accessLog.File=${mule.home}/logs/http-access.log
log4j.appender.accessLog.MaxFileSize=10MB
log4j.appender.accessLog.MaxBackupIndex=10
log4j.appender.accessLog.append=true
```

### 3. Configure the log appender layout
We currently provide three layouts:
* <b>com.greenbird.mule.http.log.layout.CombinedAccessLogPatternLayout</b>: Produces log lines on the [Apache combined log format].
* <b>com.greenbird.mule.http.log.layout.CommonAccessLogPatternLayout</b>: Produces log lines on the [Apache common log format].
* <b>com.greenbird.mule.http.log.layout.AccessLoggerPatternLayout</b>: [PatternLayout] with support for conversion characters specific to HTTP request and response messages. See step 4. below for details.

Example log4j.properties fragment:
```
log4j.logger.http.accesslog=INFO, accessLog
log4j.additivity.http.accesslog=false

log4j.appender.accessLog=org.apache.log4j.RollingFileAppender
log4j.appender.accessLog.File=${mule.home}/logs/http-access.log
# set layout
log4j.appender.accessLog.layout=com.greenbird.mule.http.log.layout.CommonAccessLogPatternLayout
log4j.appender.accessLog.MaxFileSize=10MB
log4j.appender.accessLog.MaxBackupIndex=10
log4j.appender.accessLog.append=true
```

### (4. Configure the AccessLoggerPatternLayout)
If you are using one of the predefined layouts you are done. If you are using the AccessLoggerPatternLayout you now
need to configure the conversion pattern for your log lines.

AccessLoggerPatternLayout is a [PatternLayout] extension that adds the HTTP request/response 
specific conversion characters described below.

<table border="1" CELLPADDING="8">
    <tr><th>Conversion Character</th> <th>Effect</th></tr>
    <tr>
        <td align=center><b>h</b></td>
        <td>The ip address of the requesting client (remote-<b>h</b>ost).</td>
    </tr>
    <tr>
        <td align=center><b>u</b></td>
        <td>The <b>u</b>ser id of the person requesting the document as determined by HTTP authentication.</td>
    </tr>
    <tr>
        <td align=center><b>e</b></td>
        <td>The HTTP m<b>e</b>thod of the request.</td>
    </tr>
    <tr>
        <td align=center><b>U</b></td>
        <td>The path of the requested resource.</td>
    </tr>
    <tr>
        <td align=center><b>q</b></td>
        <td>The request <b>q</b>uery string prefixed with '?'.</td>
    </tr>
    <tr>
        <td align=center><b>H</b></td>
        <td>The <b>H</b>TTP protocol version of the request.</td>
    </tr>
    <tr>
        <td align=center><b>s</b></td>
        <td>The HTTP <b>s</b>tatus code of the response.</td>
    </tr>
    <tr>
        <td align=center><b>b</b></td>
        <td>The size of the returned content in <b>b</b>ytes (content-length).</td>
    </tr>
    <tr>
        <td align=center><b>f</b></td>
        <td>Re<b>f</b>errer. The page the current request was linked from.</td>
    </tr>
    <tr>
        <td align=center><b>a</b></td>
        <td>The user <b>a</b>gent that created the request.
    </tr>
</table>

All specifiers may optionally be followed by an option specifier enclosed between braces. The option format
is <b>{[default value][;qt]}</b>.

* <b>default value</b>: any string you want to be displayed if the conversion produces a
null result. E.g: <b>%a{-}</b> will print a dash if the user agent is not available. The empty string is used if no default value is specified.
* <b>;qt</b>: Specifies that you want the conversion result to be surrounded by "quotes". Note that the default value is not quoted. If you want the default value to be quoted you need to add quotes to the default value specification: <b>%a{"Agent unknown";qt}</b>.


Example log4j.properties fragment configuring the AccessLoggerPatternLayout to produce the [Apache combined log format]:

```
log4j.logger.http.accesslog=INFO, accessLog
log4j.additivity.http.accesslog=false

log4j.appender.accessLog=org.apache.log4j.RollingFileAppender
log4j.appender.accessLog.File=${mule.home}/logs/http-access.log
# set layout and pattern
log4j.appender.accessLog.layout=com.greenbird.mule.http.log.layout.AccessLoggerPatternLayout
log4j.appender.accessLog.layout.ConversionPattern=%h{-} - %u{-} [%d{dd/MMM/yyyy:HH:mm:ss Z}] "%e %U%q %H" %s{-} %b{-} %f{-;qt} %a{-;qt}%n
log4j.appender.accessLog.MaxFileSize=10MB
log4j.appender.accessLog.MaxBackupIndex=10
log4j.appender.accessLog.append=true
```
 
[Apache 2.0]:                 http://www.apache.org/licenses/LICENSE-2.0.html
[Apache combined log format]: http://httpd.apache.org/docs/1.3/logs.html#combined
[Apache common log format]:   http://httpd.apache.org/docs/1.3/logs.html#common
[issues]:                     https://github.com/greenbird/mule-access-log/issues
[HTTP endpoints]:             www.mulesoft.org/documentation/display/current/HTTP+Transport+Reference
[Maven]:                      http://maven.apache.org/
[Mule ESB]:                   http://www.mulesoft.org/
[Mule log configuration]:     http://www.mulesoft.org/documentation/display/current/Logging+With+Mule+ESB+3.x
[PatternLayout]:              http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html


