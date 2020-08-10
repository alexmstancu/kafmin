package org.kafmin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

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

    // prettier-ignore
    @Override
    public String toString() {
        return "Topic{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isInternal='" + isIsInternal() + "'" +
            "}";
    }
}
