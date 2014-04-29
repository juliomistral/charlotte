package com.hcisf.charlotte.crawler.multithreaded;


import com.hcisf.charlotte.MockBasedTest;
import com.hcisf.charlotte.crawler.LoadedResourceRepository;
import com.hcisf.charlotte.crawler.ResourceScanner;
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
    @Mock ExecutorService scannerPool;

    @Mock ResourceCrawlerExecutor executor;
    @Mock ActiveExecutorMonitor monitor;
    @Mock ResourceScanner scanner;


    @Before
    public void setup() throws Exception{
        PowerMockito.whenNew(ResourceScanner.class).withAnyArguments().thenReturn(scanner);
        PowerMockito.whenNew(ResourceCrawlerExecutor.class).withAnyArguments().thenReturn(executor);
        PowerMockito.whenNew(ActiveExecutorMonitor.class).withAnyArguments().thenReturn(monitor);

        crawler = new ExecutorMultiThreadedResourceCrawler(scannerPool, repo, loader);
    }

    @Test
    public void shouldCreateAResourceScannerForScanningAllRegisteredResources() throws Exception {
        // when a resource is registered to be scanned
        crawler.registerForScanning(resource);

        // and another resource is registered to be scanned
        crawler.registerForScanning(anotherResource);

        // then only one resource scanner is instantiated
        PowerMockito.verifyNew(
            ResourceScanner.class, times(1)
        ).withArguments(repo, loader, crawler);
    }

    @Test
    public void shouldCreateAResourceCrawlerExecutorForEachRegisteredResource() throws Exception {
        // when a resource is registered to be scanned
        crawler.registerForScanning(resource);

        // and another resource is registered to be scanned
        crawler.registerForScanning(anotherResource);

        // then a resource scanner is used to scan all registered resources
        PowerMockito.verifyNew(ResourceCrawlerExecutor.class).withArguments(resource, scanner);
        PowerMockito.verifyNew(ResourceCrawlerExecutor.class).withArguments(anotherResource, scanner);
    }

    @Test
    public void shouldUseTheScannerPoolToExecuteTheCreatedResourceCrawlerExecutor() {
        // when a resource is registered to be scanned
        crawler.registerForScanning(resource);

        // and another resource is registered to be scanned
        crawler.registerForScanning(anotherResource);

        // then the pool executes each created resource scanner exeucutor
        verify(scannerPool, times(2)).execute(executor);
    }

    @Test
    public void shouldAddTheCreatedExecutorToTheActiveExecutorMonity() {
        // when a resource is registered to be scanned
        crawler.registerForScanning(resource);

        // then the crawler registers the created executor to the active executor monitor
        verify(monitor, times(1)).registerActiveExecutor(executor);
    }
}
