/*
 *    Copyright (c) 2020, VRAI Labs and/or its affiliates. All rights reserved.
 *
 *    This software is licensed under the Apache License, Version 2.0 (the
 *    "License") as published by the Apache Software Foundation.
 *
 *    You may not use this file except in compliance with the License. You may
 *    obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
 *
 */

package io.supertokens.storage.mysql.output;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import io.supertokens.storage.mysql.ResourceDistributor;
import io.supertokens.storage.mysql.Start;
import io.supertokens.storage.mysql.utils.Utils;
import org.slf4j.LoggerFactory;

public class Logging extends ResourceDistributor.SingletonResource {

    private static final String RESOURCE_ID = "io.supertokens.storage.mysql.output.Logging";
    private final Logger infoLogger;
    private final Logger errorLogger;

    private Logging(Start start, String infoLogPath, String errorLogPath) {
        this.infoLogger = infoLogPath.equals("null")
                ? createLoggerForConsole(start, "io.supertokens.storage.mysql.Info." + start.getProcessId())
                : createLoggerForFile(start, infoLogPath, "io.supertokens.storage.mysql.Info." + start.getProcessId());
        this.errorLogger = errorLogPath.equals("null")
                ? createLoggerForConsole(start, "io.supertokens.storage.mysql.Error." + start.getProcessId())
                : createLoggerForFile(start, errorLogPath,
                        "io.supertokens.storage.mysql.Error." + start.getProcessId());
    }

    private static Logging getInstance(Start start) {
        return (Logging) start.getResourceDistributor().getResource(RESOURCE_ID);
    }

    public static void initFileLogging(Start start, String infoLogPath, String errorLogPath) {
        if (getInstance(start) == null) {
            start.getResourceDistributor().setResource(RESOURCE_ID, new Logging(start, infoLogPath, errorLogPath));

        }
    }

    public static void debug(Start start, String msg) {
        try {
            msg = msg.trim();
            if (getInstance(start) != null) {
                getInstance(start).infoLogger.debug(msg);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void info(Start start, String msg) {
        try {
            msg = msg.trim();
            systemOut(msg);
            if (getInstance(start) != null) {
                getInstance(start).infoLogger.info(msg);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void warn(Start start, String msg) {
        try {
            msg = msg.trim();
            if (getInstance(start) != null) {
                getInstance(start).errorLogger.warn(msg);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void error(Start start, String err, boolean toConsoleAsWell) {
        try {
            err = err.trim();
            if (getInstance(start) != null) {
                getInstance(start).errorLogger.error(err);
            }
            if (toConsoleAsWell || getInstance(start) == null) {
                systemErr(err);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void error(Start start, String message, boolean toConsoleAsWell, Exception e) {
        try {
            String err = Utils.exceptionStacktraceToString(e).trim();
            if (getInstance(start) != null) {
                getInstance(start).errorLogger.error(err);
            } else {
                systemErr(err);
            }
            if (message != null) {
                message = message.trim();
                if (getInstance(start) != null) {
                    getInstance(start).errorLogger.error(message);
                }
                if (toConsoleAsWell || getInstance(start) == null) {
                    systemErr(message);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    private static void systemOut(String msg) {
        if (!Start.silent) {
            System.out.println(msg);
        }
    }

    private static void systemErr(String err) {
        System.err.println(err);
    }

    public static void stopLogging(Start start) {
        if (getInstance(start) == null) {
            return;
        }
        getInstance(start).infoLogger.detachAndStopAllAppenders();
        getInstance(start).errorLogger.detachAndStopAllAppenders();
    }

    private Logger createLoggerForFile(Start start, String file, String name) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        LayoutWrappingEncoder ple = new LayoutWrappingEncoder(start);
        ple.setContext(lc);
        ple.start();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(file);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(name);
        logger.addAppender(fileAppender);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }

    private Logger createLoggerForConsole(Start start, String name) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        LayoutWrappingEncoder ple = new LayoutWrappingEncoder(start);
        ple.setContext(lc);
        ple.start();
        ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
        logConsoleAppender.setEncoder(ple);
        logConsoleAppender.setContext(lc);
        logConsoleAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(name);
        logger.addAppender(logConsoleAppender);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }

}
