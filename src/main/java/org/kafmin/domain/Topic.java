package org.kafmin.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * A Topic.
 */
@Entity
@Table(name = "topic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_internal")
    private Boolean isInternal;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private List<GenericConfig> configs;

    @Transient
    @JsonSerialize
    private List<TopicPartition> partitions;

    @Transient
    @JsonSerialize
    private Cluster cluster;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer numPartitions;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Short replicationFactor;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Topic name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIsInternal() {
        return isInternal;
    }

    public Topic isInternal(Boolean isInternal) {
        this.isInternal = isInternal;
        return this;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Topic)) {
            return false;
        }
        return id != null && id.equals(((Topic) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Topic{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", isInternal=" + isInternal +
            ", configs=" + configs +
            ", partitions=" + partitions +
            ", cluster=" + cluster +
            ", numPartitions=" + numPartitions +
            ", replicationFactor=" + replicationFactor +
            '}';
    }

    public List<GenericConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<GenericConfig> configs) {
        this.configs = configs;
    }

    public List<TopicPartition> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<TopicPartition> partitions) {
        this.partitions = partitions;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Integer getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(Integer numPartitions) {
        this.numPartitions = numPartitions;
    }

    public Short getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(Short replicationFactor) {
        this.replicationFactor = replicationFactor;
    }
}
