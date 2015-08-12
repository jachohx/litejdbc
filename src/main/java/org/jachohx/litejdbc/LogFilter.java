/*
Copyright 2009-2014 Igor Polevoy

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/


package org.jachohx.litejdbc;

import org.apache.commons.lang.StringUtils;
import org.jachohx.litejdbc.util.StringUtil;
import org.slf4j.Logger;

public class LogFilter {
    
    public static void logQuery(Logger logger, String query, Object[] params, long queryStartTime){
        long time = System.currentTimeMillis() - queryStartTime;

        if (logger.isInfoEnabled()) {
            StringBuilder log =  new StringBuilder().append("Query: \"").append(query).append('"');
            if (!StringUtil.empty(params)) {
                log.append(", with parameters: ").append('<');
                log.append(StringUtils.join(params, ">, <"));
                log.append('>');
            }
            log(logger, log.append(", took: ").append(time).append(" milliseconds").toString());
        }
    }

    public static void log(Logger logger, String log){
        if (logger.isInfoEnabled()) {
           logger.info(log);
        }
    }

    public static void log(Logger logger, String log, Object param) {
        if (logger.isInfoEnabled()) {
           logger.info(log, param);
        }
    }

    public static void log(Logger logger, String log, Object param1, Object param2) {
        if (logger.isInfoEnabled()) {
           logger.info(log, param1, param2);
        }
    }

    public static void log(Logger logger, String log, Object... params) {
        if (logger.isInfoEnabled()) {
           logger.info(log, params);
        }
    }
    
    public static void error(Logger logger, String msg, Throwable t) {
    	if (logger.isErrorEnabled()) {
    		logger.error(msg, t);
    	}
    }

}
