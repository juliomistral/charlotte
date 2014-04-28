package com.hcisf.charlotte.crawler;



import com.hcisf.charlotte.MockBasedTest;

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
}
