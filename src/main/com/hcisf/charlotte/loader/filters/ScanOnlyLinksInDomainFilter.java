package com.hcisf.charlotte.loader.filters;


import com.google.common.net.InternetDomainName;

public class ScanOnlyLinksInDomainFilter implements Filter {
    private String domain;


    public ScanOnlyLinksInDomainFilter(String domain) {
        InternetDomainName domainName = InternetDomainName.from(domain).topPrivateDomain();
        this.domain = domainName.topPrivateDomain().toString();
    }

    @Override
    public boolean include(String location) {
        InternetDomainName locationDomain = InternetDomainName.from(location).topPrivateDomain();
        return locationDomain.toString().equals(domain);
    }
}
