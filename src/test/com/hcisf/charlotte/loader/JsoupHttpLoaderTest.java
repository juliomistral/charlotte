package com.hcisf.charlotte.loader;


import com.hcisf.PowerMockWithSpecTestNameRunner;

import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.domain.ResourceStatus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.powermock.api.mockito.PowerMockito;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;


@RunWith(PowerMockWithSpecTestNameRunner.class)
@PrepareForTest({Jsoup.class, JsoupHttpLoader.class})
public class JsoupHttpLoaderTest  {
    private static final int TIMEOUT = 1000;
    private static final String RESOURCE_URL = "http://google.com";

    JsoupHttpLoader loader;
    Resource resource;
    Document document;
    Elements elements;

    @Before
    public void setup() throws Exception {
        PowerMockito.mockStatic(Jsoup.class);

        resource = new Resource(RESOURCE_URL);
        document = mock(Document.class);
        elements = new Elements();
        doReturn(elements).when(document).select(anyString());

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
        // When the loader loads the resource URL
        Resource output;
        output = loader.loadResource(RESOURCE_URL);

        // Then the returned resource is set to parsed
        assertNotNull(output);
        assertEquals(ResourceStatus.PARSED, output.status);
    }

    @Test
    public void shouldReturnAResourceSetToInvalidIfLocationIsMalformed() throws Exception {
        // Given the provided resource location is malformed
        PowerMockito.whenNew(URL.class)
                    .withArguments(RESOURCE_URL)
                    .thenThrow(new MalformedURLException());

        // When the loader loads the resource URL
        Resource output;
        output = loader.loadResource(RESOURCE_URL);

        // Then the returned resource is set to invalid
        assertNotNull(output);
        assertEquals(ResourceStatus.INVALID, output.status);
    }

    @Test
    public void shouldReturnAResourceSetToUnavailableIfCannotConnectToLocation() throws Exception {
        // Given the provided resource location is unavailable
        PowerMockito.whenNew(URL.class)
                    .withArguments(RESOURCE_URL)
                    .thenThrow(new IOException());

        // When the loader loads the resource URL
        Resource output;
        output = loader.loadResource(RESOURCE_URL);

        // Then the returned resource is set to unavailable
        assertNotNull(output);
        assertEquals(ResourceStatus.UNAVAILABLE, output.status);
    }

    @Test
    public void shouldCreateAChildResourceForEveryLinkFoundInTheParsedDocument() throws Exception {
        // Given the parsed document contains a link in it
        Element element = mock(Element.class);
        when(
            element.attr(anyString())
        ).thenReturn(
            RESOURCE_URL
        );
        elements.add(element);

        // When the loader loads the resource URL
        Resource output;
        output = loader.loadResource(RESOURCE_URL);

        // Then the returned resource has 1 child
        assertNotNull(output.children);
        assert output.children.size() == 1;
        Resource child = output.children.get(0);

        // Add the child is an empty resource with it's location set
        assertEquals(ResourceStatus.EMPTY, child.status);
        assertEquals(RESOURCE_URL, child.location);
    }
}
