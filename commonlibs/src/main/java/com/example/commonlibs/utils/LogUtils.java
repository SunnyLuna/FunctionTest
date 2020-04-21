package com.example.commonlibs.utils;

import android.os.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Created by hizha on 2018/3/29.
 */

public class LogUtils {

    private static final String LOG_DIR = Environment.getExternalStorageDirectory().getPath() + "/dc_Log/campus";


    public static Logger logger = LoggerFactory.getLogger("trace");


    public static void config(boolean printOnLogCat, boolean printOnFile) {
        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.stop();
        context.reset();
        if (!printOnLogCat && !printOnFile) {

            return;

        }

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        if (printOnLogCat) {
            LogcatAppender logcatAppender = configLogCat(context);
            root.addAppender(logcatAppender);
        }

        if (printOnFile) {

            RollingFileAppender rollingFileAppender = configLogFile(context);
            root.addAppender(rollingFileAppender);

        }
        root.setLevel(Level.ALL);
        // print any status messages (warnings, etc) encountered in logback config
        StatusPrinter.print(context);
    }


    private static LogcatAppender configLogCat(LoggerContext context) {

        // setup LogcatAppender
        PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
        encoder2.setContext(context);
        encoder2.setCharset(Charset.forName("UTF-8"));
//        encoder2.setPattern("[%file:%M:%line] - %msg %n");
        encoder2.setPattern("[%M:%line] - %msg %n");
        encoder2.start();

        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(context);
        logcatAppender.setEncoder(encoder2);
        logcatAppender.start();
        return logcatAppender;
    }


    private static RollingFileAppender configLogFile(LoggerContext context) {

        // setup FileAppender
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);//是否以追加方式输出。默认为true。
        rollingFileAppender.setContext(context);//是否以追加方式输出。默认为true。

        String prefix = "";
        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setFileNamePattern(LOG_DIR + File.separator + prefix + "%d{yyyy-MM-dd}.log");
        rollingPolicy.setMaxHistory(7);//声明归档日志最大保留时间
        rollingPolicy.setCleanHistoryOnStart(true);//如果设置为true，则当appender启动时，会删除所有归档日志文件。
        rollingPolicy.setTotalSizeCap(FileSize.valueOf("512MB"));//如果设置为true，则当appender启动时，会删除所有归档日志文件。
        rollingPolicy.setParent(rollingFileAppender);  // parent and context required!
        rollingPolicy.setContext(context);
        rollingPolicy.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);


        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setCharset(Charset.forName("UTF-8"));
        encoder1.setContext(context);
//        encoder1.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
//        encoder1.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%file:%M:%line] - %msg %n");
        encoder1.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%file:%M:%line] - %msg %n");
        encoder1.start();


        rollingFileAppender.setEncoder(encoder1);
        rollingFileAppender.start();

        return rollingFileAppender;


    }


}
