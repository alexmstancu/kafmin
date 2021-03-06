package org.kafmin.web.rest;

import org.kafmin.domain.Topic;
import org.kafmin.service.TopicService;
import org.kafmin.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing {@link org.kafmin.domain.Topic}.
 */
@RestController
@RequestMapping("/api")
public class TopicResource {

    private final Logger log = LoggerFactory.getLogger(TopicResource.class);

    private static final String ENTITY_NAME = "topic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicService topicService;

    public TopicResource(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * {@code POST  /topics} : Create a new topic.
     *
     * @param topic the topic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topic, or with status {@code 400 (Bad Request)} if the topic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topics/{clusterDbId}")
    public ResponseEntity<Topic> createTopic(@PathVariable Long clusterDbId,  @RequestBody Topic topic) throws URISyntaxException, ExecutionException, InterruptedException {
        log.debug("REST request to save Topic : {}", topic);
        if (topic.getId() != null) {
            throw new BadRequestAlertException("A new topic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Topic result = topicService.create(clusterDbId, topic);
        return ResponseEntity.created(new URI("/api/topics/" + clusterDbId + "/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code PUT  /topics} : Updates an existing topic.
     *
     * @param topic the topic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topic,
     * or with status {@code 400 (Bad Request)} if the topic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topic couldn't be updated.
     */
    @PutMapping("/topics/{clusterDbId}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long clusterDbId, @RequestBody Topic topic) throws ExecutionException, InterruptedException {
        log.debug("REST request to update Topic : {} for cluster {}", topic, clusterDbId);
        if (topic.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Topic result = topicService.update(clusterDbId, topic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topic.getName()))
            .body(result);
    }

    /**
     * {@code GET  /topics} : get all the topics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topics in body.
     */
//    @GetMapping("/topics")
    public List<Topic> getAllTopics() {
        log.debug("REST request to get all Topics");
        return topicService.findAll();
    }

    /**
     * {@code GET  /topics/:id} : get the "id" topic.
     *
     * @param name the id of the topic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topics/{clusterDbId}/{name}")
    public ResponseEntity<Topic> getTopic(@PathVariable Long clusterDbId, @PathVariable String name) throws ExecutionException, InterruptedException {
        log.debug("REST request to get Topic : {} for clusterDbId: {}", name, clusterDbId);
        Optional<Topic> topic = topicService.findOne(clusterDbId, name);
        return ResponseUtil.wrapOrNotFound(topic);
    }

    /**
     * {@code DELETE  /topics/:id} : delete the "id" topic.
     *
     * @param topicName the id of the topic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topics/{clusterDbId}/{topicName}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long clusterDbId, @PathVariable String topicName) throws ExecutionException, InterruptedException {
        log.debug("Request to delete Topic : {} from cluster {}", topicName, clusterDbId);
        topicService.delete(clusterDbId, topicName);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, topicName)).build();
    }
}
