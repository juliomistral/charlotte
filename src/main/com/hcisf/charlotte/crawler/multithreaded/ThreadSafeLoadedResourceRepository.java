package com.hcisf.charlotte.crawler.multithreaded;

import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class ThreadSafeLoadedResourceRepository implements LoadedResourceRepository {
    private static final Logger log = LoggerFactory.getLogger(ThreadSafeLoadedResourceRepository.class);
    Set<Resource> registry;


    public ThreadSafeLoadedResourceRepository() {
        this.registry = new HashSet<Resource>();
    }

    @Override
    synchronized public void registerVisitedResource(Resource resource) {
        log.debug("Thread '{}' is registering resource as being visited: {}",
                  Thread.currentThread().getName(),
                  resource.location);
        registry.add(resource);
    }

    @Override
    synchronized public boolean wasResourceVisited(Resource resource) {
        boolean isResourceInRegistry = registry.contains(resource);
        log.debug("Thread '{}' is checking if resource was visited: {} ({})",
                Thread.currentThread().getName(),
                resource.location,
                isResourceInRegistry);
        return isResourceInRegistry;
    }
}
