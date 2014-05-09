package com.hcisf.charlotte.report;


import com.hcisf.charlotte.domain.Resource;

public interface ResourceStatsGather {
    void collectStatsForReport(Resource resource, Report report);
}
