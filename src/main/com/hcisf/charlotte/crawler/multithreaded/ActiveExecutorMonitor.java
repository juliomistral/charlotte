package com.hcisf.charlotte.crawler.multithreaded;


import com.hcisf.charlotte.crawler.ResourceCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class ActiveExecutorMonitor implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(ActiveExecutorMonitor.class);
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
        }
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

    @Override
    public void run() {
        while (true) {
            synchronized (activeExecutors) {
                pruneCompletedExecutors();
                updateMissCount();

                if (isMissCountAtThreshold()) {
                    crawler.shutdown();
                    return;
                }

                try {
                    activeExecutors.wait(1000);
                } catch (InterruptedException e) {}
            }

        }
    }

    private void pruneCompletedExecutors() {
        Iterator<ResourceCrawlerExecutor> iterator = activeExecutors.iterator();
        while (iterator.hasNext()) {
            ResourceCrawlerExecutor exc = iterator.next();
            if (exc.isCompleted()) {
                iterator.remove();
            }
        }
    }

    private void updateMissCount() {
        if (activeExecutors.size() == 0) {
            noActiveExecutorMissesCount.incrementAndGet();
        } else {
            noActiveExecutorMissesCount.set(0);
        }
    }

    private boolean isMissCountAtThreshold() {
        return noActiveExecutorMissesCount.get() == threshold;
    }
}
