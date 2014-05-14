package com.hcisf.charlotte.crawler.multithreaded;



import com.hcisf.charlotte.MockBasedTest;

import com.hcisf.charlotte.crawler.multithreaded.ThreadSafeLoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;
import org.junit.Before;
import org.junit.Test;


public class ThreadSafeLoadedResourceRepositoryTest extends MockBasedTest {
    ThreadSafeLoadedResourceRepository repo;
    Resource resource;


    @Before
    public void setup() {
        resource = new Resource("location");
        repo = new ThreadSafeLoadedResourceRepository();
    }

    @Test
    public void shouldReturnAResourceAsScannedIfItWasPreviouslyRegistered() {
        // When a resource is registered with the repo
        repo.registerVisitedResource(resource);

        // Then it's marked as being scanned by the repo
        assert repo.wasResourceVisited(resource);
    }

    @Test
    public void shouldNotReturnAResourceAsScannedIfItWasNeverVisited() {
        // When a resource is registered with the repo
        repo.registerVisitedResource(resource);

        // Then another resource being scanned will come up as not visited
        Resource anotherResource = new Resource("other url");
        assert !repo.wasResourceVisited(anotherResource);
    }
}
