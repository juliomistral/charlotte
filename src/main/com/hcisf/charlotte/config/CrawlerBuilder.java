package com.hcisf.charlotte.config;


import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.ExecutorMultiThreadedResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.MultiThreadedResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.ThreadSafeLoadedResourceRepository;

import com.hcisf.charlotte.loader.JsoupHttpLoader;
import com.hcisf.charlotte.loader.Loader;

import com.hcisf.charlotte.loader.filters.ExcludeMailToLinksFilter;
import com.hcisf.charlotte.loader.filters.Filter;
import com.hcisf.charlotte.loader.filters.ScanOnlyLinksInDomainFilter;
import com.hcisf.charlotte.report.Reporter;
import com.hcisf.charlotte.report.ResourceStatsGather;
import com.hcisf.charlotte.report.gathers.BrokenLinkStatsGatherer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CrawlerBuilder {
    List<ResourceStatsGather> gatherers;
    List<Filter> filters;
    boolean createMultiThreaded;


    private CrawlerBuilder(boolean createMultiThreaded) {
        this.createMultiThreaded = createMultiThreaded;

        gatherers = new LinkedList<ResourceStatsGather>();
        filters = new LinkedList<Filter>();
    }

    public static CrawlerBuilder aMultithreadedCrawler() {
        return new CrawlerBuilder(true);
    }

    public CrawlerBuilder withBrokenLinksReport() {
        gatherers.add(new BrokenLinkStatsGatherer());
        return this;
    }

    public CrawlerBuilder withNoMailToFilter() {
        filters.add(new ExcludeMailToLinksFilter());
        return this;
    }

    public CrawlerBuilder withLinksInDomainFilter(String domain) {
        filters.add(new ScanOnlyLinksInDomainFilter(domain));
        return this;
    }

    public ResourceCrawler build() {
        ResourceCrawler crawler;

        ExecutorService scannerPool = Executors.newFixedThreadPool(5);
        LoadedResourceRepository repo = new ThreadSafeLoadedResourceRepository();
        Reporter reporter = new Reporter(gatherers);

        Loader loader = new JsoupHttpLoader(5000);
        if (!filters.isEmpty()) {
            loader.addFilters(filters);
        }

        crawler = new ExecutorMultiThreadedResourceCrawler(scannerPool, repo, loader, reporter);

        return crawler;
    }
}
