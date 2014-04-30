package com.hcisf.charlotte.crawler.multithreaded;


import com.hcisf.charlotte.crawler.ResourceCrawler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ActiveExecutorMonitor implements Runnable {
    private ResourceCrawler crawler;
    private Set<ResourceCrawlerExecutor> activeExecutors;
    private Thread monitoringThread;
    private Boolean isStarted;
    private int threshold;
    private AtomicInteger noActiveExecutorMissesCount;


    public ActiveExecutorMonitor(ResourceCrawler crawler, int threshold) {
        this.crawler = crawler;
        this.threshold = threshold;
        this.noActiveExecutorMissesCount = new AtomicInteger(0);
        this.activeExecutors = Collections.synchronizedSet(new HashSet<ResourceCrawlerExecutor>());
        this.isStarted = Boolean.FALSE;
    }

    @Override
    public void run() {
        while (true) {
            for (ResourceCrawlerExecutor exc : activeExecutors) {
                if (exc.isCompleted()) {
                    activeExecutors.remove(exc);
                }
            }

            if (activeExecutors.size() == 0) {
                noActiveExecutorMissesCount.incrementAndGet();
            } else {
                noActiveExecutorMissesCount.set(0);
            }

            if (noActiveExecutorMissesCount.get() == threshold) {
                crawler.shutdown();
                return;
            }
        }
    }

    public void registerActiveExecutor(ResourceCrawlerExecutor executor) {
        activeExecutors.add(executor);
        if (!isStarted) {
            startMonitoringThread();
        }
    }

    private void startMonitoringThread() {
        synchronized (isStarted) {
            if (!isStarted) {
                monitoringThread = new Thread(this);
                monitoringThread.start();
                isStarted = Boolean.TRUE;
            }
        }
    }

    public boolean isExecutorActive(ResourceCrawlerExecutor executor) {
        return activeExecutors.contains(executor);
    }

    public Boolean isStarted() {
        return isStarted;
    }
}
