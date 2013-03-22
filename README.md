mule-access-log
===============
Library for [Mule ESB] that enables access logging on standard [Apache combined log format] from inbound HTTP endpoints.
This log format is understood by most web log analysis tools.

License
-------
[Apache 2.0]


Obtain
------
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

Usage
-----
You need to replace the the regular `MuleMessageToHttpResponse` transformer with our subclass `CombinedLoggingMessageToHttpResponse`.

This is done on the http connector level like this:
```xml
<http:connector name="loggingHttpConnector">
    <service-overrides responseTransformer="org.greenbird.mule.http.log.CombinedLoggingMessageToHttpResponse"/>
</http:connector>
```

The log statements are logged on the `INFO` level to the `http.accesslog` log category. 
You should configure this category to be written to a separate file with a log format containing only the log message.

Example log4j configuration fragment:

```
log4j.logger.http.accesslog=INFO, accessLog
log4j.additivity.http.accesslog=false

log4j.appender.accessLog=org.apache.log4j.RollingFileAppender
log4j.appender.accessLog.File=${mule.home}/logs/http-access.log
log4j.appender.accessLog.MaxFileSize=10MB
log4j.appender.accessLog.MaxBackupIndex=10
log4j.appender.accessLog.append=true
log4j.appender.accessLog.layout=org.apache.log4j.PatternLayout
log4j.appender.accessLog.layout.ConversionPattern=%m%n
```

Roadmap
-------
The project is in its infancy. See the project [issues] for information on planned features.

[Apache 2.0]: http://www.apache.org/licenses/LICENSE-2.0.html
[Apache combined log format]: http://httpd.apache.org/docs/1.3/logs.html#combined
[issues]: https://github.com/greenbird/mule-access-log/issues
[Maven]: http://maven.apache.org/
[Mule ESB]: http://www.mulesoft.org/


