package com.hcisf.charlotte.loader.filters;


import com.hcisf.charlotte.MockBasedTest;
import static org.fest.assertions.Assertions.*;
import org.junit.Before;
import org.junit.Test;


public class ExcludeMailToLinksFilterTest extends MockBasedTest {
    ExcludeMailToLinksFilter filter;


    @Before
    public void setup() {
        filter = new ExcludeMailToLinksFilter();
    }

    @Test
    public void shouldReturnFalseForAnyLinkThatBeginsWithTheMailToProtocol() {
        boolean output = filter.include("mailto:someone@somedomain.com");
        assertThat(output).isFalse();
    }

    @Test
    public void shouldReturnTrueForAnyLinkThatDoesNotBeginWithTheMailToProtocol() {
        boolean output = filter.include("http://google.com");
        assertThat(output).isTrue();
    }

    @Test
    public void shouldExcludeMailtoLinksRegardlessOfCapitalization() {
        assertThat(filter.include("MAILTO:someone@somedomain.com")).isFalse();
        assertThat(filter.include("MaIlTo:someone@somedomain.com")).isFalse();
    }
}
