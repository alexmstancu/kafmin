package org.kafmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * A Broker.
 */
@Entity
@Table(name = "broker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Broker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "broker_id")
    private String brokerId;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private Integer port;

    @Column(name = "is_controller")
    private Boolean isController;

    @Transient
    @JsonSerialize
    private List<GenericConfig> configs;

    @ManyToOne
    @JsonIgnoreProperties(value = "brokers", allowSetters = true)
    private Cluster cluster;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public Broker brokerId(String brokerId) {
        this.brokerId = brokerId;
        return this;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getHost() {
        return host;
    }

    public Broker host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public Broker port(Integer port) {
        this.port = port;
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean isIsController() {
        return isController;
    }

    public Broker isController(Boolean isController) {
        this.isController = isController;
        return this;
    }

    public void setIsController(Boolean isController) {
        this.isController = isController;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public Broker cluster(Cluster cluster) {
        this.cluster = cluster;
        return this;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Broker)) {
            return false;
        }
        return id != null && id.equals(((Broker) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Broker{" +
            "id=" + getId() +
            ", brokerId='" + getBrokerId() + "'" +
            ", host='" + getHost() + "'" +
            ", port=" + getPort() +
            ", isController='" + isIsController() + "'" +
            "}";
    }

    public List<GenericConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<GenericConfig> configs) {
        this.configs = configs;
    }
}
