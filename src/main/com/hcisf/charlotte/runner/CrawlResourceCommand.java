package com.hcisf.charlotte.runner;


import com.hcisf.charlotte.crawler.multithreaded.ExecutorMultiThreadedResourceCrawler;
import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceCrawler;
import com.hcisf.charlotte.crawler.multithreaded.ThreadSafeLoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.JsoupHttpLoader;
import com.hcisf.charlotte.loader.Loader;
import org.apache.log4j.BasicConfigurator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlResourceCommand {

    private void execute(String location) {
        ExecutorService scannerPool = Executors.newFixedThreadPool(5);
        LoadedResourceRepository repo = new ThreadSafeLoadedResourceRepository();
        Loader loader = new JsoupHttpLoader(5000);

        ResourceCrawler crawler = new ExecutorMultiThreadedResourceCrawler(scannerPool, repo, loader);

        Resource toBeScanned = new Resource(location);
        crawler.registerForScanning(toBeScanned);
    }

    public static void main(String[] args) {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();

        CrawlResourceCommand cmd = new CrawlResourceCommand();
        cmd.execute("http://www.simpleadsfasdasdsite.com");
    }
}
