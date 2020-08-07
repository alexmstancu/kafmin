package org.kafmin.web.rest;

import org.kafmin.KafminApp;
import org.kafmin.domain.Cluster;
import org.kafmin.repository.ClusterRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ClusterResource} REST controller.
 */
@SpringBootTest(classes = KafminApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ClusterResourceIT {

    private static final String DEFAULT_CLUSTER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLUSTER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BOOTSTRAP_SERVERS = "AAAAAAAAAA";
    private static final String UPDATED_BOOTSTRAP_SERVERS = "BBBBBBBBBB";

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClusterMockMvc;

    private Cluster cluster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cluster createEntity(EntityManager em) {
        Cluster cluster = new Cluster()
            .clusterId(DEFAULT_CLUSTER_ID)
            .name(DEFAULT_NAME)
            .bootstrapServers(DEFAULT_BOOTSTRAP_SERVERS);
        return cluster;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cluster createUpdatedEntity(EntityManager em) {
        Cluster cluster = new Cluster()
            .clusterId(UPDATED_CLUSTER_ID)
            .name(UPDATED_NAME)
            .bootstrapServers(UPDATED_BOOTSTRAP_SERVERS);
        return cluster;
    }

    @BeforeEach
    public void initTest() {
        cluster = createEntity(em);
    }

//    @Test
    @Transactional
    public void createCluster() throws Exception {
        int databaseSizeBeforeCreate = clusterRepository.findAll().size();
        // Create the Cluster
        restClusterMockMvc.perform(post("/api/clusters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isCreated());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate + 1);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getClusterId()).isEqualTo(DEFAULT_CLUSTER_ID);
        assertThat(testCluster.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCluster.getBootstrapServers()).isEqualTo(DEFAULT_BOOTSTRAP_SERVERS);
    }

    @Test
    @Transactional
    public void createClusterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clusterRepository.findAll().size();

        // Create the Cluster with an existing ID
        cluster.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClusterMockMvc.perform(post("/api/clusters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate);
    }


//    @Test
    @Transactional
    public void getAllClusters() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList
        restClusterMockMvc.perform(get("/api/clusters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].clusterId").value(hasItem(DEFAULT_CLUSTER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bootstrapServers").value(hasItem(DEFAULT_BOOTSTRAP_SERVERS)));
    }

//    @Test
    @Transactional
    public void getCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get the cluster
        restClusterMockMvc.perform(get("/api/clusters/{id}", cluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cluster.getId().intValue()))
            .andExpect(jsonPath("$.clusterId").value(DEFAULT_CLUSTER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bootstrapServers").value(DEFAULT_BOOTSTRAP_SERVERS));
    }
    @Test
    @Transactional
    public void getNonExistingCluster() throws Exception {
        // Get the cluster
        restClusterMockMvc.perform(get("/api/clusters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

//    @Test
    @Transactional
    public void updateCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster
        Cluster updatedCluster = clusterRepository.findById(cluster.getId()).get();
        // Disconnect from session so that the updates on updatedCluster are not directly saved in db
        em.detach(updatedCluster);
        updatedCluster
            .clusterId(UPDATED_CLUSTER_ID)
            .name(UPDATED_NAME)
            .bootstrapServers(UPDATED_BOOTSTRAP_SERVERS);

        restClusterMockMvc.perform(put("/api/clusters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCluster)))
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getClusterId()).isEqualTo(UPDATED_CLUSTER_ID);
        assertThat(testCluster.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCluster.getBootstrapServers()).isEqualTo(UPDATED_BOOTSTRAP_SERVERS);
    }

    @Test
    @Transactional
    public void updateNonExistingCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClusterMockMvc.perform(put("/api/clusters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

//    @Test
    @Transactional
    public void deleteCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        int databaseSizeBeforeDelete = clusterRepository.findAll().size();

        // Delete the cluster
        restClusterMockMvc.perform(delete("/api/clusters/{id}", cluster.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
