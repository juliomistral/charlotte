package com.hcisf.charlotte.crawler.multithreaded;


import com.hcisf.charlotte.MockBasedTest;
import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceScanner;
import com.hcisf.charlotte.report.Reporter;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.Loader;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;


@PrepareForTest({ExecutorMultiThreadedResourceCrawler.class})
public class ExecutorMultiThreadedResourceCrawlerTest extends MockBasedTest {
    ExecutorMultiThreadedResourceCrawler crawler;

    @Mock Resource resource;
    @Mock Resource anotherResource;

    @Mock Loader loader;
    @Mock LoadedResourceRepository repo;
    @Mock Reporter reporter;

    @Mock ExecutorService scannerPool;
    @Mock ResourceCrawlerExecutor executor;
    @Mock ActiveExecutorMonitor monitor;
    @Mock ResourceScanner scanner;


    @Before
    public void setup() throws Exception{
        PowerMockito.whenNew(ResourceScanner.class).withAnyArguments().thenReturn(scanner);
        PowerMockito.whenNew(ResourceCrawlerExecutor.class).withAnyArguments().thenReturn(executor);
        PowerMockito.whenNew(ActiveExecutorMonitor.class).withAnyArguments().thenReturn(monitor);

        crawler = new ExecutorMultiThreadedResourceCrawler(scannerPool, repo, loader, reporter);
    }

    @Test
    public void shouldCreateAResourceScannerForScanningAllRegisteredResources() throws Exception {
        // when a resource is registered to be scanned
        crawler.crawlResource(resource);

        // and another resource is registered to be scanned
        crawler.crawlResource(anotherResource);

        // then only one resource scanner is instantiated
        PowerMockito.verifyNew(
            ResourceScanner.class, times(1)
        ).withArguments(repo, loader, crawler, reporter);
    }

    @Test
    public void shouldCreateAResourceCrawlerExecutorForEachRegisteredResource() throws Exception {
        // when a resource is registered to be scanned
        crawler.crawlResource(resource);

        // and another resource is registered to be scanned
        crawler.crawlResource(anotherResource);

        // then a resource scanner is used to scan all registered resources
        PowerMockito.verifyNew(ResourceCrawlerExecutor.class).withArguments(resource, scanner);
        PowerMockito.verifyNew(ResourceCrawlerExecutor.class).withArguments(anotherResource, scanner);
    }

    @Test
    public void shouldUseTheScannerPoolToExecuteTheCreatedResourceCrawlerExecutor() {
        // when a resource is registered to be scanned
        crawler.crawlResource(resource);

        // and another resource is registered to be scanned
        crawler.crawlResource(anotherResource);

        // then the pool executes each created resource scanner exeucutor
        verify(scannerPool, times(2)).execute(executor);
    }

    @Test
    public void shouldAddTheCreatedExecutorToTheActiveExecutorMonity() throws Exception {
        // when a resource is registered to be scanned
        crawler.crawlResource(resource);

        // and another resource is registered to be scanned
        crawler.crawlResource(anotherResource);

        // then the crawler registers the created executor to the active executor monitor
        PowerMockito.verifyNew(ActiveExecutorMonitor.class, times(1)).withArguments(crawler, 3, 1000);
        verify(monitor, times(2)).registerActiveExecutor(executor);
    }

    @Test
    public void shouldShutdownTheExecutorPoolDuringAShutdown() {
        // when the executor is told to shut down
        crawler.shutdown();

        // then the crawler notifies the executor pool to shutdown immediately
        verify(scannerPool, times(1)).shutdownNow();
    }
}
