package com.hcisf.charlotte.crawler

import com.hcisf.charlotte.domain.Resource
import com.hcisf.charlotte.report.Report


interface ResourceCrawler {
    public void crawlResource(Resource resource);

    public Report shutdown();


}
