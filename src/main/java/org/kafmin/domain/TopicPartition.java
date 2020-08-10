package org.kafmin.domain;

import java.util.List;

public class TopicPartition {
    private int id;
    private Broker leader;
    private List<Broker> replicas;

    public TopicPartition(int id, Broker leader, List<Broker> replicas) {
        this.id = id;
        this.leader = leader;
        this.replicas = replicas;
    }

    public int getId() {
        return id;
    }

    public Broker getLeader() {
        return leader;
    }

    public List<Broker> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<Broker> replicas) {
        this.replicas = replicas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLeader(Broker leader) {
        this.leader = leader;
    }
}
