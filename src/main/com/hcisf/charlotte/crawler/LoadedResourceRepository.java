package com.hcisf.charlotte.crawler;


import com.hcisf.charlotte.domain.Resource;

import java.net.URL;

public interface LoadedResourceRepository {
    public void registerVisitedResource(Resource resource);

    public boolean wasResourceVisited(Resource resource);
}
