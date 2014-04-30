package com.hcisf.charlotte.crawler.multithreaded;


import com.hcisf.charlotte.MockBasedTest;
import static org.mockito.Mockito.*;

import com.hcisf.charlotte.crawler.ResourceCrawler;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.concurrent.atomic.AtomicInteger;


@PrepareForTest({ActiveExecutorMonitor.class})
public class ActiveExecutorMonitorTest extends MockBasedTest {
    private static final int NUMBER_OF_TRIES = 3;
    ActiveExecutorMonitor monitor;

    @Mock ResourceCrawler crawler;
    @Mock ResourceCrawlerExecutor executor;
    @Mock ResourceCrawlerExecutor anotherExecutor;
    @Mock Thread thread;


    @Before
    public void setup() throws Exception {
        PowerMockito.whenNew(Thread.class).withAnyArguments().thenReturn(thread);
        monitor = new ActiveExecutorMonitor(crawler, NUMBER_OF_TRIES);
    }

    @Test
    public void shouldBeSetToNotStartedIfNoExecutorsHaveBeenRegistered() {
        assert !monitor.isStarted();
    }

    @Test
    public void shouldBeSetStartedOnceAnExecutorIsRegistered() {
        // when an exeuctor is registered
        monitor.registerActiveExecutor(executor);

        // then the monitor is set to started
        assert monitor.isStarted();
    }

    @Test
    public void shouldFireOffAThreadToCheckOnTheSetOfActiveExecutorsWhenAnActiveExecutorIsRegistered() throws Exception {
        // when an exeuctor is registered
        monitor.registerActiveExecutor(executor);

        // then a thread is created using the monitor as the runnable tasks
        PowerMockito.verifyNew(Thread.class, times(1)).withArguments(monitor);

        // and the thread is started
        verify(thread, times(1)).start();
    }

    @Test
    public void shouldDeregisterCompletedExecutorsWhenTheMonitoringThreadChecksForCompletedExecutors() {
        // given an executor is registered
        monitor.registerActiveExecutor(executor);

        // and the executor completes
        when(executor.isCompleted()).thenReturn(true);

        // when the monitoring thread executes the completed exeutor check
        monitor.run();

        // then executor is removed from the set of active executors
        assert !monitor.isExecutorActive(executor);
    }

    @Test
    public void shouldStopCheckingForActiveExecutorsAfterFindingNoneInSucessiveTries() {
        // given an executor is registered
        monitor.registerActiveExecutor(executor);

        // and the executor completes
        when(executor.isCompleted()).thenReturn(true);

        // when the monitoring thread executes the completed executor check
        monitor.run();

        // then the check should stop after finding no active executors 3 times in a row
        AtomicInteger missedCount = (AtomicInteger)Whitebox.getInternalState(monitor, "noActiveExecutorMissesCount");
        assert missedCount.intValue() == NUMBER_OF_TRIES;
    }

    @Test
    public void shouldShutDownTheCrawlerWhenTheMonitorStopsCheckingForActiveThreads() {
        // given an executor is registered
        monitor.registerActiveExecutor(executor);

        // and the executor completes
        when(executor.isCompleted()).thenReturn(true);

        // when the monitoring thread executes the completed executor check
        monitor.run();

        // then the monitor should shut down the crawler
        verify(crawler, times(1)).shutdown();
    }
}
