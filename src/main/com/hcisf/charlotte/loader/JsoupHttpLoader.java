package com.hcisf.charlotte.loader;


import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.domain.ResourceStatus;
import com.hcisf.charlotte.domain.ResourceType;
import com.hcisf.charlotte.loader.filters.Filter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class JsoupHttpLoader implements Loader {
    private static final Logger log = LoggerFactory.getLogger(JsoupHttpLoader.class);
    int timeout;
    List<Filter> filters;

    public JsoupHttpLoader(int timeout) {
        this.timeout = timeout;
        this.filters = new LinkedList<Filter>();
    }

    @Override
    public void addFilters(List<Filter> filters) {
        this.filters.addAll(filters);
    }

    @Override
    public void populateResource(Resource resource) {
        log.info("Loading resource from it's location:  {}", resource.location);
        try {
            URL contentURL = new URL(resource.location);
            Document doc = Jsoup.parse(contentURL, this.timeout);

            resource.type = ResourceType.HTML;  // For now, the only type we support
            resource.children = loadChildLinksIntoResources(doc);
            resource.status = ResourceStatus.PARSED;

        } catch (MalformedURLException mue) {
            log.error("Resource location was malformed....resource NOT POPULATED", mue);
            resource.status = ResourceStatus.INVALID;
        } catch(IOException ioe) {
            log.error("Resource location was unavailable....resource NOT POPULATED", ioe);
            resource.status = ResourceStatus.UNAVAILABLE;
        }
    }

    private List<Resource> loadChildLinksIntoResources(Document doc) {
        Elements links = doc.select("a[href]");

        List<Resource> linkResources = new ArrayList<Resource>(links.size());
        for (Element link : links) {
            String location = link.attr("abs:href");
            if (isLocationValid(location)) {
                linkResources.add(new Resource(location));
            }
        }

        return linkResources;
    }

    private boolean isLocationValid(String location) {
        for (Filter filter : filters) {
            if (!filter.include(location)) {
                return false;
            }
        }
        return true;
    }

}
