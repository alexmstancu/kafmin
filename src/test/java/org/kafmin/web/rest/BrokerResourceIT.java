package org.kafmin.web.rest;

import org.kafmin.KafminApp;
import org.kafmin.domain.Broker;
import org.kafmin.repository.BrokerRepository;

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
 * Integration tests for the {@link BrokerResource} REST controller.
 */
@SpringBootTest(classes = KafminApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BrokerResourceIT {

    private static final String DEFAULT_BROKER_ID = "AAAAAAAAAA";
    private static final String UPDATED_BROKER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;

    private static final Boolean DEFAULT_IS_CONTROLLER = false;
    private static final Boolean UPDATED_IS_CONTROLLER = true;

    @Autowired
    private BrokerRepository brokerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBrokerMockMvc;

    private Broker broker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Broker createEntity(EntityManager em) {
        Broker broker = new Broker()
            .brokerId(DEFAULT_BROKER_ID)
            .host(DEFAULT_HOST)
            .port(DEFAULT_PORT)
            .isController(DEFAULT_IS_CONTROLLER);
        return broker;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Broker createUpdatedEntity(EntityManager em) {
        Broker broker = new Broker()
            .brokerId(UPDATED_BROKER_ID)
            .host(UPDATED_HOST)
            .port(UPDATED_PORT)
            .isController(UPDATED_IS_CONTROLLER);
        return broker;
    }

    @BeforeEach
    public void initTest() {
        broker = createEntity(em);
    }

    @Test
    @Transactional
    public void createBroker() throws Exception {
        int databaseSizeBeforeCreate = brokerRepository.findAll().size();
        // Create the Broker
        restBrokerMockMvc.perform(post("/api/brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(broker)))
            .andExpect(status().isCreated());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeCreate + 1);
        Broker testBroker = brokerList.get(brokerList.size() - 1);
        assertThat(testBroker.getBrokerId()).isEqualTo(DEFAULT_BROKER_ID);
        assertThat(testBroker.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testBroker.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testBroker.isIsController()).isEqualTo(DEFAULT_IS_CONTROLLER);
    }

    @Test
    @Transactional
    public void createBrokerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerRepository.findAll().size();

        // Create the Broker with an existing ID
        broker.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerMockMvc.perform(post("/api/brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(broker)))
            .andExpect(status().isBadRequest());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBrokers() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get all the brokerList
        restBrokerMockMvc.perform(get("/api/brokers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(broker.getId().intValue())))
            .andExpect(jsonPath("$.[*].brokerId").value(hasItem(DEFAULT_BROKER_ID)))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].isController").value(hasItem(DEFAULT_IS_CONTROLLER.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", broker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(broker.getId().intValue()))
            .andExpect(jsonPath("$.brokerId").value(DEFAULT_BROKER_ID))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.isController").value(DEFAULT_IS_CONTROLLER.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingBroker() throws Exception {
        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        int databaseSizeBeforeUpdate = brokerRepository.findAll().size();

        // Update the broker
        Broker updatedBroker = brokerRepository.findById(broker.getId()).get();
        // Disconnect from session so that the updates on updatedBroker are not directly saved in db
        em.detach(updatedBroker);
        updatedBroker
            .brokerId(UPDATED_BROKER_ID)
            .host(UPDATED_HOST)
            .port(UPDATED_PORT)
            .isController(UPDATED_IS_CONTROLLER);

        restBrokerMockMvc.perform(put("/api/brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBroker)))
            .andExpect(status().isOk());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeUpdate);
        Broker testBroker = brokerList.get(brokerList.size() - 1);
        assertThat(testBroker.getBrokerId()).isEqualTo(UPDATED_BROKER_ID);
        assertThat(testBroker.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testBroker.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testBroker.isIsController()).isEqualTo(UPDATED_IS_CONTROLLER);
    }

    @Test
    @Transactional
    public void updateNonExistingBroker() throws Exception {
        int databaseSizeBeforeUpdate = brokerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerMockMvc.perform(put("/api/brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(broker)))
            .andExpect(status().isBadRequest());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        int databaseSizeBeforeDelete = brokerRepository.findAll().size();

        // Delete the broker
        restBrokerMockMvc.perform(delete("/api/brokers/{id}", broker.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
