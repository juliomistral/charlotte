package com.hcisf.charlotte.crawler.multithreaded;

import com.hcisf.charlotte.MockBasedTest;
import com.hcisf.charlotte.crawler.ResourceScanner;
import com.hcisf.charlotte.domain.Resource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;


public class ResourceCrawlerExecutorTest extends MockBasedTest {
    ResourceCrawlerExecutor executor;

    Resource resource;
    @Mock ResourceScanner scanner;
    @Mock ResourceCrawlerExecutorListener listener;


    @Before
    public void setup() {
        resource = new Resource("some url");
        executor = new ResourceCrawlerExecutor(resource, scanner);
    }

    @Test
    public void shouldUseTheScannerToScanTheResourceDuringARun() {
        // when the executor is run
        executor.run();

        // then the scanner scans the provided resource
        verify(scanner, times(1)).scan(resource);
    }

    @Test
    public void shouldNotifyItsListenerWhenItsCompletedCrawlingAResourceDuringARun() {
        // given a listener is registered with the executor
        executor.addListener(listener);

        // when the executor is run
        executor.run();

        // then the scanner scans the provided resource
        verify(listener, times(1)).handleExecutorCompleted(executor);
    }
}
