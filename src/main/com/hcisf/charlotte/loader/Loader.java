package com.hcisf.charlotte.loader;


import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.loader.filters.Filter;

import java.util.List;


public interface Loader {
    public void addFilters(List<Filter> filters);

    public void populateResource(Resource emptyResource);
}
