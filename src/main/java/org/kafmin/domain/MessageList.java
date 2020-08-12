package org.kafmin.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Transient;
import java.util.List;

public class MessageList {
    @Transient
    @JsonSerialize
    private Cluster cluster;

    @Transient
    @JsonSerialize
    private String topic;

    @Transient
    @JsonSerialize
    private Integer partitionsCount;

    @Transient
    @JsonSerialize
    private List<Message> messages;

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "MessageList{" +
            "cluster=" + cluster +
            ", topic='" + topic + '\'' +
            ", messages=" + messages +
            '}';
    }

    public Integer getPartitionsCount() {
        return partitionsCount;
    }

    public void setPartitionsCount(Integer partitionsCount) {
        this.partitionsCount = partitionsCount;
    }
}
