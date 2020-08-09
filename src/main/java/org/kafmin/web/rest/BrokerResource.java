package org.kafmin.web.rest;

import org.kafmin.domain.Broker;
import org.kafmin.repository.BrokerRepository;
import org.kafmin.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.kafmin.domain.Broker}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BrokerResource {

    private final Logger log = LoggerFactory.getLogger(BrokerResource.class);

    private static final String ENTITY_NAME = "broker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BrokerRepository brokerRepository;

    public BrokerResource(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;
    }

    /**
     * {@code GET  /brokers/:id} : get the "id" broker.
     *
     * @param id the id of the broker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the broker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/brokers/{clusterId}/{id}")
    public ResponseEntity<Broker> getBroker(@PathVariable String clusterId, @PathVariable Long id) {
        log.debug("REST request to get Broker: {} for cluster: {}", id, clusterId);
        Optional<Broker> broker = brokerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(broker);
    }

    /**
     * {@code PUT  /brokers} : Updates an existing broker.
     *
     * @param broker the broker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated broker,
     * or with status {@code 400 (Bad Request)} if the broker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the broker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/brokers")
    public ResponseEntity<Broker> updateBroker(@RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to update Broker : {}", broker);
        if (broker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Broker result = brokerRepository.save(broker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, broker.getId().toString()))
            .body(result);
    }

    /*
     * Below are unused APIs !
     */

    /**
     * TODO this endpoint cannot be used and should be deleted/hidden
     * @deprecated
     */
    @PostMapping("/brokers")
    public ResponseEntity<Broker> createBroker(@RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to save Broker : {}", broker);
        if (broker.getId() != null) {
            throw new BadRequestAlertException("A new broker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Broker result = brokerRepository.save(broker);
        return ResponseEntity.created(new URI("/api/brokers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * TODO this will not be used
     */
    @GetMapping("/brokers")
    public List<Broker> getAllBrokers() {
        log.debug("REST request to get all Brokers");
        return brokerRepository.findAll();
    }

    /**
     * TODO this endpoint cannot be used and should be deleted/hidden
     * @deprecated
     */
    @DeleteMapping("/brokers/{id}")
    public ResponseEntity<Void> deleteBroker(@PathVariable Long id) {
        log.debug("REST request to delete Broker : {}", id);
        brokerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
