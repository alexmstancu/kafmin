package org.kafmin.repository;

import org.kafmin.domain.Broker;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Broker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerRepository extends JpaRepository<Broker, Long> {
}
