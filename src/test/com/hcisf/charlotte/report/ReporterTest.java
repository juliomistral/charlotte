package com.hcisf.charlotte.report;


import com.hcisf.charlotte.domain.Resource;

import com.hcisf.charlotte.MockBasedTest;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.Collection;


@PrepareForTest({Reporter.class})
public class ReporterTest extends MockBasedTest {
    private Reporter reporter;

    @Mock Report report;
    @Mock Resource resource;
    @Mock ResourceStatsGather statsGatherer;
    @Mock ResourceStatsGather anotherStatsGatherer;


    @Before
    public void setUp() throws Exception {
        Collection<ResourceStatsGather> gatherers = new ArrayList<ResourceStatsGather>(2);
        gatherers.add(statsGatherer);
        gatherers.add(anotherStatsGatherer);

        PowerMockito.whenNew(Report.class).withAnyArguments().thenReturn(report);

        reporter = new Reporter(gatherers);
    }

    @Test
    public void shouldCreateABlankReportWhenInstantiated() throws Exception {
        PowerMockito.verifyNew(Report.class).withNoArguments();
    }

    @Test
    public void shouldCallEachRegisteredStatsGathererWithTheProvidedResource() {
        // when the reporter gathers the stats for a resource
        reporter.gatherStatistics(resource);

        // then each of the gatherers registered when the reporter was instantiated are called to collect stats
        verify(statsGatherer, times(1)).collectStatsForReport(resource, report);
    }

    @Test
    public void shouldReturnTheInstantiatedReportWhenCompilingTheFinalReport() {
        // when the reporter compiles the final report
        Report output = reporter.compileReport();

        // then the compiled report is the report created when the reporter was instantiated
        assert output == report;
    }
}
