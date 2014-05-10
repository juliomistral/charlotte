package com.hcisf.charlotte.config;


import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.ExecutorMultiThreadedResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.ThreadSafeLoadedResourceRepository;

import com.hcisf.charlotte.loader.JsoupHttpLoader;
import com.hcisf.charlotte.loader.Loader;

import com.hcisf.charlotte.report.Reporter;
import com.hcisf.charlotte.report.ResourceStatsGather;
import com.hcisf.charlotte.report.gathers.BrokenLinkStatsGatherer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CrawlerBuilder {
    ExecutorService scannerPool = Executors.newFixedThreadPool(5);
    LoadedResourceRepository repo = new ThreadSafeLoadedResourceRepository();
    Loader loader = new JsoupHttpLoader(5000);
    List<ResourceStatsGather> gatherers = new ArrayList<ResourceStatsGather>(5);


    public static CrawlerBuilder aMultithreadedCrawler() {
        return new CrawlerBuilder();
    }


    public CrawlerBuilder withBrokenLinksReport() {
        gatherers.add(new BrokenLinkStatsGatherer());
        return this;
    }

    public ResourceCrawler build() {
        Reporter reporter = new Reporter(gatherers);
        ResourceCrawler crawler = new ExecutorMultiThreadedResourceCrawler(
            scannerPool, repo, loader, reporter
        );

        return crawler;
    }
}
