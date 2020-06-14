package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestStopWatch extends Stopwatch {
    private static final String STARTING_STRING = "\nTest runtime list:" +
            " \n-------------------------------------\n";
    private static final Logger log = LoggerFactory.getLogger(TestStopWatch.class);
    private static final StringBuilder ALL_TEST_LOG = new StringBuilder(STARTING_STRING);


    private void logInfo(Description description, long nanos) {
        String logText = String.format("Test %s : spent %d milliseconds",
                description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        ALL_TEST_LOG.append(logText).append("\n");
        log.info(logText);
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, nanos);
    }

    public static void getAllTestInfo() {
        log.info(ALL_TEST_LOG.toString());
        ALL_TEST_LOG.setLength(0);
        ALL_TEST_LOG.append(STARTING_STRING);
    }
}

