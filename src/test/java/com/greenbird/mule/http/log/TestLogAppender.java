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

package com.greenbird.mule.http.log;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.NullAppender;

import java.util.ArrayList;
import java.util.List;

/**
 * Appender that can be used to capture logging events from Log4J loggers.
 */
public class TestLogAppender extends NullAppender {
    private List<LoggingEvent> loggingEvents = new ArrayList<LoggingEvent>();
    private List<String> logLines = new ArrayList<String>();

    public TestLogAppender(Class<?> loggerClass) {
        this(loggerClass.getName());
    }

    public TestLogAppender(String loggerName) {
        Logger.getLogger(loggerName).addAppender(this);
    }

    @Override
    public void doAppend(LoggingEvent event) {
        loggingEvents.add(event);
        if (layout != null) {
            logLines.add(layout.format(event));
        } else {
            logLines.add(event.getRenderedMessage());
        }
    }

    public List<LoggingEvent> getLoggingEvents() {
        return loggingEvents;
    }

    public void close() {
        loggingEvents.clear();
        logLines.clear();
        setLayout(null);
        Logger.getRootLogger().removeAppender(this);
    }

    public List<String> getLogLines() {
        return logLines;
    }
}