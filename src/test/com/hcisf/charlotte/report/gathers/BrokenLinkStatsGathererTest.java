package com.hcisf.charlotte.report.gathers;


import com.hcisf.charlotte.MockBasedTest;
import com.hcisf.charlotte.domain.Resource;
import com.hcisf.charlotte.domain.ResourceStatus;
import com.hcisf.charlotte.report.Report;
import junitx.framework.ListAssert;
import junitx.framework.ObjectAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;


public class BrokenLinkStatsGathererTest extends MockBasedTest {
    BrokenLinkStatsGatherer gatherer;

    Resource resource;
    Report report;


    @Before
    public void setup() {
        resource = new Resource("someurl");
        report = new Report();
        gatherer = new BrokenLinkStatsGatherer();
    }

    @Test
    public void shouldAddAListOfBrokenLinksToTheReportIfItDoesntExistAlready() {
        // when the gatherer compiles stats for a resource and an empty report
        gatherer.collectStatsForReport(resource, report);

        // then the report should contain a value bound to the broken links property
        assertBrokenLinksListInReport();
    }

    @Test
    public void shouldUseTheExistingBrokenLinkListAttachedToTheReport() {
        // given a report that already contains a broken link
        List<String> brokenLinks = new ArrayList<String>(2);
        brokenLinks.add("broken link");
        report.put(BrokenLinkStatsGatherer.BROKEN_LINKS, brokenLinks);

        // when the gatherer compiles stats for a resource and an empty report
        gatherer.collectStatsForReport(resource, report);

        // then the broken links list in the report should still contain the previous broken link
        List<String> brokenLinksValue = assertBrokenLinksListInReport();
        ListAssert.assertContains(brokenLinksValue, "broken link");
    }

    @Test
    public void shouldAddTheURLOfAnInvalidResourceToTheBrokenLinksList() {
        // given an invalid resource
        resource.location = "invalid URL";
        resource.status = ResourceStatus.INVALID;

        // when the gatherer compiles stats for a resource and an empty report
        gatherer.collectStatsForReport(resource, report);

        // then the broken links list in the report should still contain the previous broken link
        List<String> brokenLinksValue = assertBrokenLinksListInReport();
        ListAssert.assertContains(brokenLinksValue, "invalid URL");
    }

    @Test
    public void shouldAddTheURLOfANotFoundResourceToTheBrokenLinksList() {
        // given an invalid resource
        resource.location = "not found URL";
        resource.status = ResourceStatus.NOT_FOUND;

        // when the gatherer compiles stats for a resource and an empty report
        gatherer.collectStatsForReport(resource, report);

        // then the broken links list in the report should still contain the previous broken link
        List<String> brokenLinksValue = assertBrokenLinksListInReport();
        ListAssert.assertContains(brokenLinksValue, "not found URL");
    }

    private List<String> assertBrokenLinksListInReport() {
        Object brokenLinksValue = report.get(BrokenLinkStatsGatherer.BROKEN_LINKS);
        Assert.assertNotNull(brokenLinksValue);

        // and the value is a list of broken link URL's
        ObjectAssert.assertInstanceOf(List.class, brokenLinksValue);

        return (List<String>)brokenLinksValue;
    }
}
