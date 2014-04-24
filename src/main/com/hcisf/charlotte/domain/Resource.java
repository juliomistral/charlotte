package com.hcisf.charlotte.domain;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;


public class Resource {
    public ResourceType type;
    public ResourceStatus status;
    public String rawResponse;
    public URL url;
    public Collection<Resource> children;

    public Resource() {
        this.status = ResourceStatus.EMPTY;
    }

    public void addChild(Resource resource) {
        this.children = this.children == null ? new LinkedList<Resource>() : this.children;
        this.children.add(resource);
    }

    public void addChildren(Resource... resources) {
        for (Resource resource : resources) {
            this.addChild(resource);
        }
    }
}
