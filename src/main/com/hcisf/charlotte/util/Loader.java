package com.hcisf.charlotte.util;


import com.hcisf.charlotte.domain.Resource;

import java.net.URL;


public interface Loader {
    public Resource loadResource(URL resource);
}
