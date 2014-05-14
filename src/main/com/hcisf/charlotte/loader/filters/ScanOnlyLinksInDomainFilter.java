package com.hcisf.charlotte.loader.filters;


import com.google.common.net.InternetDomainName;

public class ScanOnlyLinksInDomainFilter implements Filter {
    private String domain;
    private final static String HTTP_PROTOCOL = "http://";
    private final static String FTP_PROTOCOL = "ftp://";


    public ScanOnlyLinksInDomainFilter(String domain) {
        domain = stripOffProtocols(domain);
        InternetDomainName domainName = InternetDomainName.from(domain).topPrivateDomain();
        this.domain = "." + domainName.topPrivateDomain().toString();
    }

    private String stripOffProtocols(String domain) {
        if (domain.startsWith(HTTP_PROTOCOL)) {
            return domain.substring(HTTP_PROTOCOL.length());
        }
        if (domain.startsWith(FTP_PROTOCOL)) {
            return domain.substring(FTP_PROTOCOL.length());
        }
        return domain;
    }

    @Override
    public boolean include(String location) {
        return location.contains(domain);
    }
}
