package com.hcisf.charlotte.loader;


import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.domain.ResourceStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class JsoupHttpLoader implements Loader {
    int timeout;

    public JsoupHttpLoader(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public Resource loadResource(String resourceLocation) {
        Resource resource = new Resource(resourceLocation);
        populateResource(resource);
        return resource;
    }

    public void populateResource(Resource resource) {
        try {
            URL contentURL = new URL(resource.location);
            Document doc = Jsoup.parse(contentURL, this.timeout);

            resource.children = loadChildLinksIntoResources(doc);
            resource.status = ResourceStatus.PARSED;

        } catch (MalformedURLException mue) {
            resource.status = ResourceStatus.INVALID;
        } catch(IOException ioe) {
            resource.status = ResourceStatus.UNAVAILABLE;
        }
    }

    private List<Resource> loadChildLinksIntoResources(Document doc) {
        Elements links = doc.select("a[href]");

        List<Resource> linkResources = new ArrayList<Resource>(links.size());
        for (Element link : links) {
            String location = link.attr("abs:href");
            linkResources.add(new Resource(location));
        }

        return linkResources;
    }

}
