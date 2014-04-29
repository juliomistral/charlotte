package com.hcisf.charlotte.crawler.multithreaded;

import com.hcisf.charlotte.crawler.ResourceScanner;
import com.hcisf.charlotte.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceCrawlerExecutor implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(ResourceCrawlerExecutor.class);
    private Resource resource;
    private ResourceScanner scanner;
    private boolean isCompleted;


    public ResourceCrawlerExecutor(Resource resource, ResourceScanner scanner) {
        this.resource = resource;
        this.scanner = scanner;
        this.isCompleted = false;
    }

    @Override
    public void run() {
        log.info("Scanning resource: {}", resource.location);
        scanner.scan(resource);
        setCompleted(true);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
