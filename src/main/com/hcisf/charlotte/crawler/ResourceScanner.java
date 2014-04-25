package com.hcisf.charlotte.crawler;

import com.hcisf.charlotte.domain.LoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.Loader;

import java.net.URL;

public class ResourceScanner {
    LoadedResourceRepository loadedResourceRepository;
    Loader loader;
    ResourceCrawler crawler;



    public ResourceScanner(LoadedResourceRepository loadedResourceRepository, Loader loader, ResourceCrawler crawler) {
        this.loadedResourceRepository = loadedResourceRepository;
        this.loader = loader;
        this.crawler = crawler;
    }

    public void scan(String resourceUrl) {
        if (loadedResourceRepository.wasResourceVisited(resourceUrl)) {
            return;
        }

        Resource resource = loader.loadResource(resourceUrl);
        loadedResourceRepository.registerVisitedResource(resource);

        for (Resource child : resource.children) {
            crawler.registerForScanning(child);
        }
    }
}
