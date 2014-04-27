package com.hcisf.charlotte.crawler;

import com.hcisf.charlotte.domain.Resource;


public class ResourceCrawlerExecutor implements Runnable {
    private Resource resource;
    private ResourceScanner scanner;


    public ResourceCrawlerExecutor(Resource resource, ResourceScanner scanner) {
        this.resource = resource;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        scanner.scan(resource);
    }
}
