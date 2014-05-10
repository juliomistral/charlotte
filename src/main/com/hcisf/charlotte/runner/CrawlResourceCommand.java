package com.hcisf.charlotte.runner;


import com.hcisf.charlotte.config.CrawlerBuilder;
import com.hcisf.charlotte.crawler.multithreaded.ExecutorMultiThreadedResourceCrawler;
import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.ThreadSafeLoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.JsoupHttpLoader;
import com.hcisf.charlotte.loader.Loader;
import com.hcisf.charlotte.report.Reporter;
import com.hcisf.charlotte.report.ResourceStatsGather;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlResourceCommand {
    private final static Logger log = LoggerFactory.getLogger(CrawlResourceCommand.class);

    private void execute(String location) {
        Thread currentThread = Thread.currentThread();
        currentThread.setName("main");

        ResourceCrawler crawler =
            CrawlerBuilder
                .aMultithreadedCrawler()
                .withBrokenLinksReport()
                .build();

        Resource toBeScanned = new Resource(location);
        crawler.crawlResource(toBeScanned);
    }

    public static void main(String[] args) {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();

        CrawlResourceCommand cmd = new CrawlResourceCommand();
        cmd.execute("http://www.simpleadsfasdasdsite.com");
    }
}
