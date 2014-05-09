package com.hcisf.charlotte.crawler.multithreaded;


import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceCrawler;
import com.hcisf.charlotte.crawler.ResourceScanner;

import com.hcisf.charlotte.domain.Resource;

import com.hcisf.charlotte.loader.Loader;
import com.hcisf.charlotte.report.Report;
import com.hcisf.charlotte.report.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;


public class ExecutorMultiThreadedResourceCrawler implements ResourceCrawler {
    private static final Logger log = LoggerFactory.getLogger(ExecutorMultiThreadedResourceCrawler.class);

    private ExecutorService scannerPool;
    private ResourceScanner scanner;
    private LoadedResourceRepository repo;
    private Reporter reporter;
    private ActiveExecutorMonitor monitor;


    public ExecutorMultiThreadedResourceCrawler(ExecutorService scannerPool,
                                                LoadedResourceRepository repo,
                                                Loader loader,
                                                Reporter reporter) {
        this.repo = repo;
        this.scannerPool = scannerPool;
        this.reporter = reporter;

        this.scanner = new ResourceScanner(repo, loader, this, reporter);
        this.monitor = new ActiveExecutorMonitor(this, 3, 1000);
    }

    @Override
    public void crawlResource(Resource resource) {
        ResourceCrawlerExecutor executor = new ResourceCrawlerExecutor(resource, scanner);
        monitor.registerActiveExecutor(executor);
        scannerPool.execute(executor);
    }

    @Override
    public Report shutdown() {
        scannerPool.shutdownNow();
        return reporter.compileReport();
    }
}
