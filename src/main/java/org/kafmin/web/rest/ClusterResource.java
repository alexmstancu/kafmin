package org.kafmin.web.rest;

import org.kafmin.domain.Cluster;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.repository.ClusterRepository;
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
 * REST controller for managing {@link org.kafmin.domain.Cluster}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClusterResource {

    private final Logger log = LoggerFactory.getLogger(ClusterResource.class);

    private static final String ENTITY_NAME = "cluster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClusterRepository clusterRepository;

    public ClusterResource(ClusterRepository clusterRepository) {
        KafkaAdministrationCenter administrationCenter = new KafkaAdministrationCenter();
        this.clusterRepository = clusterRepository;
    }

    /**
     * {@code POST  /clusters} : Create a new cluster.
     *
     * @param cluster the cluster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cluster, or with status {@code 400 (Bad Request)} if the cluster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clusters")
    public ResponseEntity<Cluster> createCluster(@RequestBody Cluster cluster) throws URISyntaxException {
        log.debug("REST request to save Cluster : {}", cluster);
        if (cluster.getId() != null) {
            throw new BadRequestAlertException("A new cluster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cluster result = clusterRepository.save(cluster);
        return ResponseEntity.created(new URI("/api/clusters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clusters} : Updates an existing cluster.
     *
     * @param cluster the cluster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cluster,
     * or with status {@code 400 (Bad Request)} if the cluster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cluster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clusters")
    public ResponseEntity<Cluster> updateCluster(@RequestBody Cluster cluster) throws URISyntaxException {
        log.debug("REST request to update Cluster : {}", cluster);
        if (cluster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cluster result = clusterRepository.save(cluster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cluster.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /clusters} : get all the clusters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clusters in body.
     */
    @GetMapping("/clusters")
    public List<Cluster> getAllClusters() {
        log.debug("REST request to get all Clusters");
        return clusterRepository.findAll();
    }

    /**
     * {@code GET  /clusters/:id} : get the "id" cluster.
     *
     * @param id the id of the cluster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cluster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clusters/{id}")
    public ResponseEntity<Cluster> getCluster(@PathVariable Long id) {
        log.debug("REST request to get Cluster : {}", id);
        Optional<Cluster> cluster = clusterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cluster);
    }

    /**
     * {@code DELETE  /clusters/:id} : delete the "id" cluster.
     *
     * @param id the id of the cluster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clusters/{id}")
    public ResponseEntity<Void> deleteCluster(@PathVariable Long id) {
        log.debug("REST request to delete Cluster : {}", id);
        clusterRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
