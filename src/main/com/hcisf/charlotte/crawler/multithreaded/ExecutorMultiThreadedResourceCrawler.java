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


public class ExecutorMultiThreadedResourceCrawler implements MultiThreadedResourceCrawler {
    private static final Logger log = LoggerFactory.getLogger(ExecutorMultiThreadedResourceCrawler.class);

    private Boolean isShutdown;
    private Boolean isComplete;
    private ExecutorService scannerPool;
    private ResourceScanner scanner;
    private Reporter reporter;
    private ActiveExecutorMonitor monitor;


    public ExecutorMultiThreadedResourceCrawler(ExecutorService scannerPool,
                                                LoadedResourceRepository repo,
                                                Loader loader,
                                                Reporter reporter) {
        this.isShutdown = true;
        this.isComplete = false;
        this.scannerPool = scannerPool;
        this.reporter = reporter;

        this.scanner = new ResourceScanner(repo, loader, this, reporter);
        this.monitor = new ActiveExecutorMonitor(this, 3, 1000);
    }

    @Override
    public void crawlResource(Resource resource) {
        isShutdown = false;
        ResourceCrawlerExecutor executor = new ResourceCrawlerExecutor(resource, scanner);
        monitor.registerActiveExecutor(executor);
        scannerPool.execute(executor);
    }

    @Override
    public Report getReport() {
        synchronized (isShutdown) {
            while (!isShutdown) {
                try {
                    isShutdown.wait();
                } catch (InterruptedException e) {}
            }
        }

        return reporter.compileReport();
    }

    @Override
    public boolean isCompelte() {
        return isComplete;
    }

    @Override
    public void shutdown() {
        shutdown(false);
    }

    @Override
    public void forcedShutdown() {
        shutdown(true);
    }

    private void shutdown(boolean isForcedShutdown) {
        log.info("Shutting down crawler...");
        scannerPool.shutdownNow();

        synchronized (isShutdown) {
            if (!isShutdown) {
                isShutdown.notifyAll();
                this.isShutdown = true;
                this.isComplete = !isForcedShutdown;
            }
        }
    }


}
