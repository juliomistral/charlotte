package com.hcisf.charlotte.loader.filters;


public class ExcludeMailToLinksFilter implements Filter {
    public static final String MAILTO_PROTOCOL_PREFIX = "mailto:";

    @Override
    public boolean include(String location) {
        return !location.toLowerCase().startsWith(MAILTO_PROTOCOL_PREFIX);
    }
}
