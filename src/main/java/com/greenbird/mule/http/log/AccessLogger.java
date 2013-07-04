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
import org.mule.api.MuleMessage;

/**
 * Logger that logs the transformed message containing the http response to the access log category.
 */
public class AccessLogger {
    public static final String ACCESS_LOG_CATEGORY = "http.accesslog";
    private Logger logger = Logger.getLogger(ACCESS_LOG_CATEGORY);

    public void log(MuleMessage message) {
        logger.info(message);
    }
}
