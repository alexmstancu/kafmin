package org.kafmin.service;

import org.kafmin.domain.Topic;
import org.kafmin.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Topic}.
 */
@Service
public class TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic save(Topic topic) {
        log.debug("Request to save Topic : {}", topic);
        return topicRepository.save(topic);
    }

    public Optional<Topic> findOne(Long clusterDbId, String name) {
        log.debug("Request to get Topic : {} for cluster {}", name, clusterDbId);


        return topicRepository.findById(0L);
    }

    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
    }

    public List<Topic> findAll() {
        log.debug("Request to get all Topics");
        return topicRepository.findAll();
    }
}
