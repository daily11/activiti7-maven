package com.swust.activiti7.model;

public class DeploymentVO {
    private String id;
    private String key;
    private String name;
    private String resourceName;
    private int resourceVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(int resourceVersion) {
        this.resourceVersion = resourceVersion;
    }
}
