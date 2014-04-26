package com.hcisf.charlotte.crawler;

import com.hcisf.PowerMockWithSpecTestNameRunner;
import com.hcisf.charlotte.domain.LoadedResourceRepository;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.Loader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;


@RunWith(PowerMockWithSpecTestNameRunner.class)
public class ResourceScannerTest {
    public static final String RESOURCE_URL = "http://some.url";

    ResourceScanner scanner;

    LoadedResourceRepository repository;
    Loader loader;
    ResourceCrawler crawler;

    Resource resource;


    @Before
    public void setup() {
        resource = new Resource(RESOURCE_URL);

        repository = mock(LoadedResourceRepository.class);
        loader = mock(Loader.class);
        crawler = mock(ResourceCrawler.class);

        scanner = new ResourceScanner(repository, loader, crawler);
    }

    @Test
    public void shouldUseTheLoaderToPopulateTheProvidedResource() {
        // When the scanner scans a resource
        scanner.scan(resource);

        // Then the loader is used to populate the resource
        verify(loader, times(1)).populateResource(resource);

    }

    @Test
    public void shouldMarkTheLoadedResourceAsBeingVisited() {
        // When the scanner scans a resource"
        scanner.scan(resource);

        // Then the resource is registered with the repository
        verify(repository, times(1)).registerVisitedResource(resource);
    }

    @Test
    public void shouldSkipScanningAnyResourceThatsAlreadyBeenScanned() {
        // given: "the provided resource URL was already visited"
        doReturn(true).when(repository).wasResourceVisited(any(Resource.class));

        // when: "the scanner scans a resource"
        scanner.scan(resource);

        // then: "the scanner immediately exists without trying to load the resource"
        verify(loader, never()).populateResource(any(Resource.class));
    }

    @Test
    public void shouldRegisterEachChildResourceWithTheResourceCrawlerToBeScanned() {
        // given: "the resource to be scanned has child resources"
        Resource child1 = new Resource("child1");
        Resource child2 = new Resource("child2");
        resource.addChildren(child1, child2);

        // when: "the scanner scans a resource"
        scanner.scan(resource);

        // then: "the children are registered with the resource crawler"
        verify(crawler, times(1)).registerForScanning(child1);
        verify(crawler, times(1)).registerForScanning(child2);
    }
}