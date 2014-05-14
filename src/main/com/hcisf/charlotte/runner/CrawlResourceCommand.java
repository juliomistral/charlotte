package com.hcisf.charlotte.runner;


import com.hcisf.charlotte.config.CrawlerBuilder;
import com.hcisf.charlotte.crawler.multithreaded.MultiThreadedResourceCrawler;
import com.hcisf.charlotte.domain.Resource;

import org.apache.log4j.BasicConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CrawlResourceCommand {
    private final static Logger log = LoggerFactory.getLogger(CrawlResourceCommand.class);
    private MultiThreadedResourceCrawler crawler;


    private void execute(String location) {
        Thread currentThread = Thread.currentThread();
        currentThread.setName("main");

        crawler = (MultiThreadedResourceCrawler)
            CrawlerBuilder
                .aMultithreadedCrawler()
                .withBrokenLinksReport()
                .withNoMailToFilter()
                .withLinksInDomainFilter(location)
                .build();

        setupForcedShutdownHook();
        executeResourceCrawl(location);

        if (crawler.isCompelte()) {
            log.info("Crawling COMPLETE:  {}", crawler.getReport());
        }
    }

    private void setupForcedShutdownHook() {
        VMShutdownHook hook = new VMShutdownHook(crawler);
        Runtime.getRuntime().addShutdownHook(new Thread(hook));
    }

    private void executeResourceCrawl(String location) {
        Resource toBeScanned = new Resource(location);
        crawler.crawlResource(toBeScanned);
    }

    class VMShutdownHook implements Runnable {
        MultiThreadedResourceCrawler crawler;

        VMShutdownHook(MultiThreadedResourceCrawler crawler) {
            log.info("VM shutdown hook created for crawler:  {}", crawler);
            this.crawler = crawler;
        }

        @Override
        public void run() {
            if (!crawler.isCompelte()) {
                log.info("Recieved FORCED shutdown, closing crawler and publishing report...");
                crawler.forcedShutdown();
                log.info("Crawling INCOMPLETE: " + crawler.getReport());
            }
        }
    }

    public static void main(String[] args) {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();

        CrawlResourceCommand cmd = new CrawlResourceCommand();
        cmd.execute("http://www.simplesite.com");
    }
}
