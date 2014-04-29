package com.hcisf.charlotte.crawler.multithreaded;


import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceCrawler;
import com.hcisf.charlotte.crawler.ResourceScanner;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;


public class ExecutorMultiThreadedResourceCrawler implements ResourceCrawler {
    private static final Logger log = LoggerFactory.getLogger(ExecutorMultiThreadedResourceCrawler.class);
    public static final int DEFAULT_NUMBER_OF_THREADS = 5;

    private ExecutorService scannerPool;
    private ResourceScanner scanner;
    private LoadedResourceRepository repo;

    private Thread progressChecker;
    private Set<ResourceCrawlerExecutor> runningExecutors;


    public ExecutorMultiThreadedResourceCrawler(ExecutorService scannerPool, LoadedResourceRepository repo, Loader loader) {
        this.repo = repo;
        this.scannerPool = scannerPool;

        this.scanner = new ResourceScanner(repo, loader, this);
        this.runningExecutors = Collections.synchronizedSet(new HashSet<ResourceCrawlerExecutor>());
    }

    @Override
    public void registerForScanning(Resource resource) {
        ResourceCrawlerExecutor executor = new ResourceCrawlerExecutor(resource, scanner);

        runningExecutors.add(executor);
        scannerPool.execute(executor);
    }

    private void startProgressCheckerThread() {
        this.progressChecker = new Thread(new Runnable() {
            @Override
            public void run() {
                for (ResourceCrawlerExecutor exc : runningExecutors) {
                }
            }

        });

        progressChecker.start();
    }
}
