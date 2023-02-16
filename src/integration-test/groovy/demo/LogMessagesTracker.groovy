package demo

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

/**
 * Makes possible to assert logging statements for SLF4J
 */
@Slf4j
@CompileStatic
class LogMessagesTracker extends AppenderBase<ILoggingEvent> {

    private final List<ILoggingEvent> events = []

    private LogMessagesTracker() {

    }

    static LogMessagesTracker trackLogMessagesFor(Class loggerClass, Level onLevel = Level.INFO) {
        Logger logbackLogger = getLoggerContext().getLogger(loggerClass)
        return internalTrackLogMessagesFor(logbackLogger, onLevel)
    }

    static LogMessagesTracker trackLogMessagesFor(String loggerName, Level onLevel = Level.INFO) {
        Logger logbackLogger = getLoggerContext().getLogger(loggerName)
        return internalTrackLogMessagesFor(logbackLogger, onLevel)
    }

    void cleanup() {
        events.clear()
    }


    private static LogMessagesTracker internalTrackLogMessagesFor(Logger logbackLogger, Level onLevel) {
        LogMessagesTracker appender = new LogMessagesTracker()

        logbackLogger.addAppender(appender)
        logbackLogger.setLevel(onLevel)

        appender.setContext(getLoggerContext())
        appender.start()

        return appender
    }

    private static LoggerContext getLoggerContext() {
        return LoggerFactory.getILoggerFactory() as LoggerContext
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        events.add(eventObject)
    }

    @Override
    String toString() {
        // used also by Spock diff, when there is no match
        return "received logMessages:\n${events?.collect { it.level.levelStr + ': "' + it.message + '"' }?.join('\n')}"
    }

    List<ILoggingEvent> getEvents() {
        return events
    }
}
