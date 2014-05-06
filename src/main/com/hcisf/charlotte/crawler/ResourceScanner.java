package com.hcisf.charlotte.crawler;

import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.Loader;
import com.hcisf.charlotte.report.Reporter;

public class ResourceScanner {
    LoadedResourceRepository loadedResourceRepository;
    Loader loader;
    ResourceCrawler crawler;
    Reporter reporter;


    public ResourceScanner(LoadedResourceRepository loadedResourceRepository,
                           Loader loader,
                           ResourceCrawler crawler,
                           Reporter reporter) {
        this.loadedResourceRepository = loadedResourceRepository;
        this.loader = loader;
        this.crawler = crawler;
        this.reporter = reporter;
    }

    public void scan(Resource resource) {
        if (loadedResourceRepository.wasResourceVisited(resource)) {
            return;
        }

        loadedResourceRepository.registerVisitedResource(resource);
        loader.populateResource(resource);

        for (Resource child : resource.children) {
            crawler.crawlResource(child);
        }
    }
}
