package com.hcisf.charlotte.crawler;


import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.Loader;

import java.util.concurrent.Executor;


public class ExecutorMultiThreadedResourceCrawler implements ResourceCrawler{
    public static final int DEFAULT_NUMBER_OF_THREADS = 5;

    private Executor scannerPool;
    private ResourceScanner scanner;


    public ExecutorMultiThreadedResourceCrawler(Executor scannerPool, LoadedResourceRepository repo, Loader loader) {
        this.scanner = new ResourceScanner(repo, loader, this);
        this.scannerPool = scannerPool;
    }

    @Override
    public void registerForScanning(Resource resource) {
        ResourceCrawlerExecutor executor = new ResourceCrawlerExecutor(resource, scanner);
        scannerPool.execute(executor);
    }
}
