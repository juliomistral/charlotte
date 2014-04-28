package com.hcisf.charlotte.crawler;

import com.hcisf.charlotte.MockBasedTest;
import com.hcisf.charlotte.domain.Resource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;


public class ResourceCrawlerExecutorTest extends MockBasedTest {
    ResourceCrawlerExecutor executor;

    @Mock Resource resource;
    @Mock ResourceScanner scanner;


    @Before
    public void setup() {
        executor = new ResourceCrawlerExecutor(resource, scanner);
    }

    @Test
    public void shouldUseTheScannerToScanTheResourceDuringARun() {
        // when the executor is run
        executor.run();

        // then the scanner scans the provided resource
        verify(scanner, times(1)).scan(resource);
    }


}
