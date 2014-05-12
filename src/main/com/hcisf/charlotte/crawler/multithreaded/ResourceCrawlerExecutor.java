package com.hcisf.charlotte.crawler.multithreaded;

import com.hcisf.charlotte.crawler.ResourceScanner;
import com.hcisf.charlotte.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ResourceCrawlerExecutor implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(ResourceCrawlerExecutor.class);
    private Resource resource;
    private ResourceScanner scanner;
    private boolean isCompleted;
    private ResourceCrawlerExecutorListener listener;


    public ResourceCrawlerExecutor(Resource resource, ResourceScanner scanner) {
        this.resource = resource;
        this.scanner = scanner;
        this.isCompleted = false;
    }

    @Override
    public void run() {
        log.info("Scanning resource: {}", resource.location);
        scanner.scan(resource);
        setExecutorAsCompleted();
    }

    private void setExecutorAsCompleted() {
        this.isCompleted = true;
        if (listener != null) {
            listener.handleExecutorCompleted(this);
        }
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void addListener(ResourceCrawlerExecutorListener listener) {
        this.listener = listener;
    }

    public Resource getResource() {
        return resource;
    }
}
