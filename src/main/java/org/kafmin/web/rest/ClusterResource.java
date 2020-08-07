package org.kafmin.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.kafmin.domain.Cluster;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.repository.ClusterRepository;
import org.kafmin.service.ClusterService;
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
import java.util.concurrent.ExecutionException;

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
    private final ClusterService clusterService;

    public ClusterResource(ClusterRepository clusterRepository, ClusterService clusterService) {
        this.clusterRepository = clusterRepository;
        this.clusterService = clusterService;
    }

    /**
     * {@code POST  /clusters} : Create a new cluster.
     *
     * @param cluster the cluster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cluster, or with status {@code 400 (Bad Request)} if the cluster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clusters")
    public ResponseEntity<Cluster> createCluster(@RequestBody Cluster cluster) throws URISyntaxException, ExecutionException, InterruptedException {
        log.debug("REST request to save Cluster : {}", cluster);

        validate(cluster);

        Cluster result = clusterService.create(cluster);

        if (result == null) {
            throw new BadRequestAlertException("The cluster already exists.", ENTITY_NAME, "idexists");
        }

        return ResponseEntity.created(new URI("/api/clusters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private void validate(Cluster cluster) {
        if (cluster.getId() != null || cluster.getClusterId() != null) {
            throw new BadRequestAlertException("A new cluster cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (cluster.getBrokers().isEmpty()) {
            throw new BadRequestAlertException("At least one bootstrap server must be populated", ENTITY_NAME, "nobootstrap");
        }

        if (StringUtils.isBlank(cluster.getName())) {
            throw new BadRequestAlertException("The cluster must have a unique name", ENTITY_NAME, "nobootstrap");
        }
    }

    /**
     * {@code PUT  /clusters} : Updates an existing cluster.
     *
     * @param cluster the cluster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cluster,
     * or with status {@code 400 (Bad Request)} if the cluster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cluster couldn't be updated.
     */
    @PutMapping("/clusters")
    public ResponseEntity<Cluster> updateCluster(@RequestBody Cluster cluster) {
        log.debug("REST request to update Cluster : {}", cluster);
        if (cluster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        // TODO
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
    public List<Cluster> getAllClusters() throws ExecutionException, InterruptedException {
        log.debug("REST request to get all Clusters");
        return clusterService.getAll();
    }

    /**
     * {@code GET  /clusters/:id} : get the "id" cluster.
     *
     * @param id the id of the cluster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 \(OK)} and with body the cluster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clusters/{id}")
    public ResponseEntity<Cluster> getCluster(@PathVariable Long id) throws ExecutionException, InterruptedException {
        log.debug("REST request to get Cluster : {}", id);
        Optional<Cluster> cluster = clusterService.get(id);
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
        clusterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
