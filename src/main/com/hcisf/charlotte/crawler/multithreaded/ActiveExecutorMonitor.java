package com.hcisf.charlotte.crawler.multithreaded;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class ActiveExecutorMonitor implements Runnable, ResourceCrawlerExecutorListener {
    public final static String MONITOR_THREAD_NAME = "ActiveExecutorMonitor";
    private final static Logger log = LoggerFactory.getLogger(ActiveExecutorMonitor.class);

    private MultiThreadedResourceCrawler crawler;
    private Set<ResourceCrawlerExecutor> activeExecutors;
    private Thread monitoringThread;
    private Boolean isStarted;
    private int threshold;
    private int pollingInterval;
    private AtomicInteger noActiveExecutorMissesCount;


    public ActiveExecutorMonitor(MultiThreadedResourceCrawler crawler, int threshold, int pollingInterval) {
        this.crawler = crawler;
        this.threshold = threshold;
        this.pollingInterval = pollingInterval;

        this.noActiveExecutorMissesCount = new AtomicInteger(0);
        this.activeExecutors = new HashSet<ResourceCrawlerExecutor>();
        this.isStarted = Boolean.FALSE;
    }

    public boolean isExecutorActive(ResourceCrawlerExecutor executor) {
        return activeExecutors.contains(executor);
    }

    public Boolean isStarted() {
        return isStarted;
    }

    public void registerActiveExecutor(ResourceCrawlerExecutor executor) {
        synchronized (activeExecutors) {
            activeExecutors.add(executor);
            executor.addListener(this);
        }
        if (!isStarted) {
            startMonitoringThread();
        }
    }

    private void startMonitoringThread() {
        synchronized (isStarted) {
            if (!isStarted) {
                monitoringThread = new Thread(this, MONITOR_THREAD_NAME);
                monitoringThread.start();
                isStarted = Boolean.TRUE;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (activeExecutors) {
                updateMissCount();

                if (isMissCountAtThreshold()) {
                    crawler.shutdown();
                    return;
                }

                try {
                    activeExecutors.wait(pollingInterval);
                } catch (InterruptedException e) {}
            }

        }
    }

    @Override
    public void handleExecutorCompleted(ResourceCrawlerExecutor executor) {
        synchronized (activeExecutors) {
            log.debug("...REMOVING completed executor for resource:  {}", executor.getResource());
            this.activeExecutors.remove(executor);
        }
    }

    private void updateMissCount() {
        if (activeExecutors.size() == 0) {
            noActiveExecutorMissesCount.incrementAndGet();
        } else {
            noActiveExecutorMissesCount.set(0);
        }
        log.info("Active executor count:  {}, Miss count: {}", activeExecutors.size(), noActiveExecutorMissesCount.get());
    }

    private boolean isMissCountAtThreshold() {
        return noActiveExecutorMissesCount.get() == threshold;
    }
}
