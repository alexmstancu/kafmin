package org.kafmin.web.rest;

import org.kafmin.KafminApp;
import org.kafmin.domain.Topic;
import org.kafmin.repository.TopicRepository;
import org.kafmin.service.TopicService;

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
 * Integration tests for the {@link TopicResource} REST controller.
 */
@SpringBootTest(classes = KafminApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TopicResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_INTERNAL = false;
    private static final Boolean UPDATED_IS_INTERNAL = true;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicService topicService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopicMockMvc;

    private Topic topic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topic createEntity(EntityManager em) {
        Topic topic = new Topic()
            .name(DEFAULT_NAME)
            .isInternal(DEFAULT_IS_INTERNAL);
        return topic;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topic createUpdatedEntity(EntityManager em) {
        Topic topic = new Topic()
            .name(UPDATED_NAME)
            .isInternal(UPDATED_IS_INTERNAL);
        return topic;
    }

    @BeforeEach
    public void initTest() {
        topic = createEntity(em);
    }

    @Test
    @Transactional
    public void createTopic() throws Exception {
        int databaseSizeBeforeCreate = topicRepository.findAll().size();
        // Create the Topic
        restTopicMockMvc.perform(post("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topic)))
            .andExpect(status().isCreated());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeCreate + 1);
        Topic testTopic = topicList.get(topicList.size() - 1);
        assertThat(testTopic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTopic.isIsInternal()).isEqualTo(DEFAULT_IS_INTERNAL);
    }

    @Test
    @Transactional
    public void createTopicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = topicRepository.findAll().size();

        // Create the Topic with an existing ID
        topic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicMockMvc.perform(post("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topic)))
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTopics() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList
        restTopicMockMvc.perform(get("/api/topics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isInternal").value(hasItem(DEFAULT_IS_INTERNAL.booleanValue())));
    }

    @Test
    @Transactional
    public void getTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", topic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isInternal").value(DEFAULT_IS_INTERNAL.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTopic() throws Exception {
        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTopic() throws Exception {
        // Initialize the database
        topicService.update(1L, topic);

        int databaseSizeBeforeUpdate = topicRepository.findAll().size();

        // Update the topic
        Topic updatedTopic = topicRepository.findById(topic.getId()).get();
        // Disconnect from session so that the updates on updatedTopic are not directly saved in db
        em.detach(updatedTopic);
        updatedTopic
            .name(UPDATED_NAME)
            .isInternal(UPDATED_IS_INTERNAL);

        restTopicMockMvc.perform(put("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTopic)))
            .andExpect(status().isOk());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate);
        Topic testTopic = topicList.get(topicList.size() - 1);
        assertThat(testTopic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTopic.isIsInternal()).isEqualTo(UPDATED_IS_INTERNAL);
    }

    @Test
    @Transactional
    public void updateNonExistingTopic() throws Exception {
        int databaseSizeBeforeUpdate = topicRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicMockMvc.perform(put("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topic)))
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTopic() throws Exception {
        // Initialize the database
        topicService.update(1L, topic);

        int databaseSizeBeforeDelete = topicRepository.findAll().size();

        // Delete the topic
        restTopicMockMvc.perform(delete("/api/topics/{id}", topic.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
