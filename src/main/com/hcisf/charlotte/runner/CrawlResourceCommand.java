package com.hcisf.charlotte.runner;


import com.hcisf.charlotte.crawler.multithreaded.ExecutorMultiThreadedResourceCrawler;
import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.ThreadSafeLoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.JsoupHttpLoader;
import com.hcisf.charlotte.loader.Loader;
import com.hcisf.charlotte.report.Reporter;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlResourceCommand {
    private final static Logger log = LoggerFactory.getLogger(CrawlResourceCommand.class);

    private void execute(String location) {
        Thread currentThread = Thread.currentThread();
        currentThread.setName("main");

        ExecutorService scannerPool = Executors.newFixedThreadPool(5);
        LoadedResourceRepository repo = new ThreadSafeLoadedResourceRepository();
        Loader loader = new JsoupHttpLoader(5000);
        Reporter reporter = new Reporter();

        ResourceCrawler crawler = new ExecutorMultiThreadedResourceCrawler(scannerPool, repo, loader, reporter);

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
