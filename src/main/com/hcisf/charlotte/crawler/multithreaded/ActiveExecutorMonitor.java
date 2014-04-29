package com.hcisf.charlotte.crawler.multithreaded;


public class ActiveExecutorMonitor implements Runnable {
    @Override
    public void run() {
//        this.progressChecker = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Iterator<ResourceCrawlerExecutor> runningExecutorsIter = runningExecutors.iterator();
//                while (runningExecutorsIter.hasNext()) {
//                    ResourceCrawlerExecutor runningExecutor = runningExecutorsIter.next();
//                    if (runningExecutor.isCompleted()) {
//                        runningExecutors.remove(runningExecutor);
//                    }
//                }
//
//
//            }
//
//        });
//
//        progressChecker.start();
    }

    public void registerActiveExecutor(ResourceCrawlerExecutor executor) {

    }
}
