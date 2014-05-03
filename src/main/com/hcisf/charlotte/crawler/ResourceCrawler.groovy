package com.hcisf.charlotte.crawler

import com.hcisf.charlotte.domain.Resource


interface ResourceCrawler {
    public void crawlResource(Resource resource);

    public void shutdown();
}
