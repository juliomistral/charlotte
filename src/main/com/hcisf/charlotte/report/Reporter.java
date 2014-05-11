package com.hcisf.charlotte.report;

import com.hcisf.charlotte.domain.Resource;

import java.util.Collection;


public class Reporter {
    private Collection<ResourceStatsGather> gatherers;
    private Report report;


    public Reporter(Collection<ResourceStatsGather> gatherers) {
        this.gatherers = gatherers;
        this.report = new Report();
    }

    public synchronized void gatherStatistics(Resource resource) {
        for (ResourceStatsGather gatherer : gatherers) {
            gatherer.collectStatsForReport(resource, report);
        }
    }

    public Report compileReport() {
        return this.report;
    }
}
