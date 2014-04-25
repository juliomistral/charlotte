package com.hcisf.charlotte.domain;

import java.util.LinkedList;
import java.util.List;


public class Resource {
    public ResourceType type;
    public ResourceStatus status;
    public String rawResponse;
    public String location;
    public List<Resource> children;


    public Resource(String location) {
        this.location = location;
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
