package com.hcisf.charlotte.crawler;

import com.hcisf.charlotte.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceCrawlerExecutor implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(ResourceCrawlerExecutor.class);
    private Resource resource;
    private ResourceScanner scanner;


    public ResourceCrawlerExecutor(Resource resource, ResourceScanner scanner) {
        this.resource = resource;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        log.info("Scanning resource: {}", resource.location);
        scanner.scan(resource);
    }
}
