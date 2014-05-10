package com.hcisf.charlotte.report.gathers;


import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.domain.ResourceStatus;
import com.hcisf.charlotte.report.Report;
import com.hcisf.charlotte.report.ResourceStatsGather;

import java.util.LinkedList;
import java.util.List;


public class BrokenLinkStatsGatherer implements ResourceStatsGather {
    public static final String BROKEN_LINKS = "brokenLinks";

    @Override
    public void collectStatsForReport(Resource resource, Report report) {
        if (!report.containsKey(BROKEN_LINKS)) {
            report.put(BROKEN_LINKS, new LinkedList<String>());
        }

        List<String> brokenLinks = (List<String>)report.get(BROKEN_LINKS);
        if (isResourceABrokenLink(resource)) {
            brokenLinks.add(resource.location);
        }
    }

    private boolean isResourceABrokenLink(Resource resource) {
        return resource.status == ResourceStatus.INVALID
                || resource.status == ResourceStatus.NOT_FOUND;
    }
}
