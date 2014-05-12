package com.hcisf.charlotte.crawler.multithreaded;

import com.hcisf.charlotte.crawler.ResourceCrawler;

/**
 * Created by juliomistral on 5/12/14.
 */
public interface MultiThreadedResourceCrawler extends ResourceCrawler{
    public boolean isCompelte();

    void shutdown();

    void forcedShutdown();
}
