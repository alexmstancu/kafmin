package org.kafmin.domain;

public class TopicDetails {
    private String name;
    private boolean isInternal;
    private int partitions;

    public TopicDetails(String name, boolean isInternal, int partitions) {
        this.name = name;
        this.isInternal = isInternal;
        this.partitions = partitions;
    }

    public String getName() {
        return name;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public int getPartitions() {
        return partitions;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }
}
