package org.kafmin.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cluster.
 */
@Entity
@Table(name = "cluster")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cluster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cluster_id")
    private String clusterId;

    @Column(name = "name")
    private String name;

    @Column(name = "bootstrap_servers")
    private String bootstrapServers;

    @Transient
    @JsonSerialize()
    private Integer topicsCount;

    @Transient
    @JsonSerialize
    private Integer partitionsCount;

    @OneToMany(mappedBy = "cluster")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Broker> brokers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public Cluster clusterId(String clusterId) {
        this.clusterId = clusterId;
        return this;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getName() {
        return name;
    }

    public Cluster name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public Cluster bootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
        return this;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Set<Broker> getBrokers() {
        return brokers;
    }

    public Cluster brokers(Set<Broker> brokers) {
        this.brokers = brokers;
        return this;
    }

    public Cluster addBrokers(Broker broker) {
        this.brokers.add(broker);
        broker.setCluster(this);
        return this;
    }

    public Cluster removeBrokers(Broker broker) {
        this.brokers.remove(broker);
        broker.setCluster(null);
        return this;
    }

    public void setBrokers(Set<Broker> brokers) {
        this.brokers = brokers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cluster)) {
            return false;
        }
        return id != null && id.equals(((Cluster) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cluster{" +
            "id=" + getId() +
            ", clusterId='" + getClusterId() + "'" +
            ", name='" + getName() + "'" +
            ", bootstrapServers='" + getBootstrapServers() + "'" +
            "}";
    }

    public Integer getPartitionsCount() {
        return partitionsCount;
    }

    public void setPartitionsCount(Integer partitionsCount) {
        this.partitionsCount = partitionsCount;
    }

    public Integer getTopicsCount() {
        return topicsCount;
    }

    public void setTopicsCount(Integer topicsCount) {
        this.topicsCount = topicsCount;
    }
}
