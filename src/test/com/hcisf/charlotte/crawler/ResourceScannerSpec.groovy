package com.hcisf.charlotte.crawler

import com.hcisf.charlotte.domain.LoadedResourceRepository
import com.hcisf.charlotte.domain.Resource
import com.hcisf.charlotte.domain.ResourceStatus
import com.hcisf.charlotte.loader.Loader
import spock.lang.*


class ResourceScannerSpec extends Specification {
    public static final String RESOURCE_URL = "http://some.url";

    ResourceScanner scanner

    LoadedResourceRepository repository
    Loader loader
    ResourceCrawler crawler;

    Resource resource


    void setup() {
        resource = new Resource()

        repository = Mock(LoadedResourceRepository)
        loader = Mock(Loader)
        crawler = Mock(ResourceCrawler)

        loader.loadResource(*_) >> resource
        scanner = new ResourceScanner(repository, loader, crawler)
    }

    def "should use the loader to populate the provided resource"() {
        when: "the scanner scans a resource"
            scanner.scan(resource)

        then: "the loader is used to load the resource via it's URL"
            1 * loader.populateResource(resource)
    }

    def "should mark the loaded resource as being visited"() {
        when: "the scanner scans a resource"
            scanner.scan(resource)

        then: "the resource is registered with the repository"
            1 * repository.registerVisitedResource(resource)
    }

    def "should skip scanning any resource that's already been scanned"() {
        given: "the provided resource URL was already visited"
            repository.wasResourceVisited(RESOURCE_URL) >> true

        when: "the scanner scans a resource"
            scanner.scan(resource)

        then: "the scanner immediately exists without trying to load the resource"
            0 * loader.loadResource(RESOURCE_URL)
    }

    def "should register each child resource with the resource crawler to be scanned"() {
        given: "the resource to be scanned has child resources"
            Resource child1 = new Resource()
            Resource child2 = new Resource()
            resource.addChildren(child1, child2)

        when: "the scanner scans a resource"
            scanner.scan(resource)

        then: "the children are registered with the resource crawler"
            1 * crawler.registerForScanning(child1)
            1 * crawler.registerForScanning(child2)
    }
}