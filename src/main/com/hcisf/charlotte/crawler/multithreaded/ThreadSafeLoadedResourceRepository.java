package com.hcisf.charlotte.crawler.multithreaded;

import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class ThreadSafeLoadedResourceRepository implements LoadedResourceRepository {
    Set<Resource> registry;


    public ThreadSafeLoadedResourceRepository() {
        this.registry = new HashSet<Resource>();
    }

    @Override
    synchronized public void registerVisitedResource(Resource resource) {
        registry.add(resource);
    }

    @Override
    synchronized public boolean wasResourceVisited(Resource resource) {
        return registry.contains(resource);
    }
}
