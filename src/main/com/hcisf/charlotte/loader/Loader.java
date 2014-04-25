package com.hcisf.charlotte.loader;


import com.hcisf.charlotte.domain.Resource;

import java.net.URL;


public interface Loader {
    public Resource loadResource(String resourceURL);

    public void populateResource(Resource emptyResource);
}
