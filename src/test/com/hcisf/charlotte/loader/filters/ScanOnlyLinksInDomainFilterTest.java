package com.hcisf.charlotte.loader.filters;


import com.hcisf.charlotte.MockBasedTest;
import static org.fest.assertions.Assertions.*;
import org.junit.Before;
import org.junit.Test;


public class ScanOnlyLinksInDomainFilterTest extends MockBasedTest {
    ScanOnlyLinksInDomainFilter filter;


    @Before
    public void setup() {
    }

    @Test
    public void shouldIncludeAnyLinkThatBelongsToTheConfiguredDomain() {
        // given a filter configured for a domain
        filter = new ScanOnlyLinksInDomainFilter("domain.com");

        // then the filter should include a link in that domain
        boolean output = filter.include("domain.com");
        assertThat(output).isTrue();
    }

    @Test
    public void shouldIncludeAnyLinkThatIsASubDomainOfTheConfiguredDomain() {
        // given a filter configured for a domain
        filter = new ScanOnlyLinksInDomainFilter("domain.com");

        // then the filter should include a link in sub-domain
        boolean output = filter.include("sub1.domain.com");
        assertThat(output).isTrue();
    }

    @Test
    public void shouldNotIncludeALinkThatIsNotPartOfTheConfiguredDomain() {
        // given a filter configured for a domain
        filter = new ScanOnlyLinksInDomainFilter("domain.com");

        // then the filter should exclude a link from another domain
        boolean output = filter.include("zzzzzz.com");
        assertThat(output).isFalse();
    }

    @Test
    public void shouldNotIncludeALinkEvenIfItsAPartialMatchToTheConfiguredDomain() {
        // given a filter configured for a domain
        filter = new ScanOnlyLinksInDomainFilter("domain.com");

        // then the filter should exclude a link from another domain that partial matches
        boolean output = filter.include("otherdomain.com");
        assertThat(output).isFalse();
    }
}
