package org.kafmin.service;

import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.kafmin.domain.Cluster;
import org.kafmin.domain.Topic;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service Implementation for managing {@link Topic}.
 */
@Service
public class TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    @Autowired
    private KafkaAdministrationCenter adminCenter;

    @Autowired
    private ClusterService clusterService;

    public Topic save(Topic topic) {
        log.debug("Request to save Topic : {}", topic);
        return null;
    }

    public Optional<Topic> findOne(Long clusterDbId, String topicName) throws ExecutionException, InterruptedException {
        log.debug("Request to get Topic : {} for cluster {}", topicName, clusterDbId);

        Cluster cluster = retrieveCluster(clusterDbId);

        DescribeTopicsResult describeTopicsResult = adminCenter.describeTopics(cluster.getClusterId(), Collections.singletonList(topicName));

        return Optional.empty();
    }

    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
    }

    public List<Topic> findAll() {
        log.debug("Request to get all Topics");
        return Collections.emptyList();
    }

    private Cluster retrieveCluster(Long clusterDbId) throws ExecutionException, InterruptedException {
        Optional<Cluster> clusterOptional = clusterService.get(clusterDbId);
        return clusterOptional.get();
    }

}
