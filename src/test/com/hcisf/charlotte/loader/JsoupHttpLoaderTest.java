package com.hcisf.charlotte.loader;


import com.hcisf.PowerMockWithSpecTestNameRunner;

import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.domain.ResourceStatus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import com.hcisf.charlotte.loader.filters.Filter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import static org.mockito.Mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(PowerMockWithSpecTestNameRunner.class)
@PrepareForTest({Jsoup.class, JsoupHttpLoader.class})
public class JsoupHttpLoaderTest  {
    private static final int TIMEOUT = 1000;
    private static final String RESOURCE_URL = "http://google.com";

    JsoupHttpLoader loader;

    Resource resource;
    @Mock Filter filter;
    @Mock Document document;
    Elements elements;
    List<Filter> filters;

    @Before
    public void setup() throws Exception {
        PowerMockito.mockStatic(Jsoup.class);

        resource = new Resource(RESOURCE_URL);
        elements = new Elements();
        doReturn(elements).when(document).select(anyString());

        filters = new ArrayList<Filter>(1);

        when(
            Jsoup.parse(any(URL.class), anyInt())
        ).thenReturn(
            document
        );

        loader = new JsoupHttpLoader(TIMEOUT);
    }

    @Test
    public void shouldAttemptToLoadTheContentAtTheProvidedResourceLocation() throws Exception {
        // When the loader populates the resource
        loader.populateResource(resource);

        // Then parsing library is asked to parse the content at the resource location
        PowerMockito.verifyStatic();
        Jsoup.parse(any(URL.class), eq(TIMEOUT));
    }

    @Test
    public void shouldReturnAResourceSetToParsedIfTheLoadedContentIsParsedSuccessfully() throws Exception {
        // When the loader populates the resource URL
        loader.populateResource(resource);

        // Then the returned resource is set to parsed
        assertEquals(ResourceStatus.PARSED, resource.status);
    }

    @Test
    public void shouldReturnAResourceSetToInvalidIfLocationIsMalformed() throws Exception {
        // Given the provided resource location is malformed
        PowerMockito.whenNew(URL.class)
                    .withArguments(RESOURCE_URL)
                    .thenThrow(new MalformedURLException());

        // When the loader populates the resource URL
        loader.populateResource(resource);

        // Then the returned resource is set to invalid
        assertEquals(ResourceStatus.INVALID, resource.status);
    }

    @Test
    public void shouldReturnAResourceSetToUnavailableIfCannotConnectToLocation() throws Exception {
        // Given the provided resource location is unavailable
        PowerMockito.whenNew(URL.class)
                    .withArguments(RESOURCE_URL)
                    .thenThrow(new IOException());

        // When the loader populates the resource URL
        loader.populateResource(resource);

        // Then the returned resource is set to unavailable
        assertEquals(ResourceStatus.UNAVAILABLE, resource.status);
    }

    @Test
    public void shouldCreateAChildResourceForEveryValidLinkFoundInTheParsedDocument() throws Exception {
        // Given the parsed document contains a link in it
        addMockElementToDocumentElements(RESOURCE_URL);

        // When the loader populates the resource URL
        loader.populateResource(resource);

        // Then the returned resource has 1 child
        assert resource.children.size() == 1;
        Resource child = resource.children.get(0);

        // Add the child is an empty resource with it's location set
        assertEquals(ResourceStatus.EMPTY, child.status);
        assertEquals(RESOURCE_URL, child.location);
    }

    @Test
    public void shouldNotCreateAChildResourceForLinksRemovedByTheRegisteredFilters() {
        // given the parsed document contains 2 links in it
        addMockElementToDocumentElements("excluded");
        addMockElementToDocumentElements("included");

        // and the filter will exclude one link and include link
        when(filter.include("excluded")).thenReturn(false);
        when(filter.include("included")).thenReturn(true);
        filters.add(filter);

        // and the loader is configured to use the filter
        loader.addFilters(filters);

        // when the loader populates the resource
        loader.populateResource(resource);

        // then the resource doesn't contain a child resource for the excluded link
        assert resource.children.size() == 1;
        Resource child = resource.children.get(0);
        assert !child.location.equals("excluded");

        // but the resource contains a child resource for the included link
        assert child.location.equals("included");
    }

    private void addMockElementToDocumentElements(String resourceUrl) {
        Element link = mock(Element.class);
        when(
                link.attr(anyString())
        ).thenReturn(
                resourceUrl
        );
        elements.add(link);
    }
}
