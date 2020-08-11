package org.kafmin.service;

import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.kafmin.domain.Cluster;
import org.kafmin.domain.GenericConfig;
import org.kafmin.domain.Topic;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.service.mapper.ConfigMapper;
import org.kafmin.service.mapper.TopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public Topic create(Long clusterDbId, Topic incomingTopic) throws ExecutionException, InterruptedException {
        log.debug("Request to get Topic : {} for cluster {}", incomingTopic.getName(), clusterDbId);
        Cluster cluster = retrieveCluster(clusterDbId);
        adminCenter.createTopic(cluster.getClusterId(), incomingTopic);
        return retrieveAndPopulateTopic(incomingTopic.getName(), cluster);
    }

    public Topic update(Long clusterDbId, Topic incomingUpdatedTopic) throws ExecutionException, InterruptedException {
        log.debug("Request to update Topic : {} for cluster {}", incomingUpdatedTopic, clusterDbId);
        Cluster cluster = retrieveCluster(clusterDbId);
        Topic originalTopic = retrieveAndPopulateTopic(incomingUpdatedTopic.getName(), cluster);
        List<GenericConfig> configsToUpdate = diff(originalTopic.getConfigs(), incomingUpdatedTopic.getConfigs());
        if (configsToUpdate.isEmpty()) {
            return originalTopic;
        }
        adminCenter.updateTopicConfig(cluster.getClusterId(), incomingUpdatedTopic.getName(), configsToUpdate);
        return retrieveAndPopulateTopic(incomingUpdatedTopic.getName(), cluster);
    }

    public Optional<Topic> findOne(Long clusterDbId, String topicName) throws ExecutionException, InterruptedException {
        log.debug("Request to get Topic : {} for cluster {}", topicName, clusterDbId);
        Cluster cluster = retrieveCluster(clusterDbId);
        Topic topic = retrieveAndPopulateTopic(topicName, cluster);
        return Optional.of(topic);
    }

    private Topic retrieveAndPopulateTopic(String topicName, Cluster cluster) throws ExecutionException, InterruptedException {
        Topic topic = retrieveTopic(cluster.getClusterId(), topicName);
        topic.setCluster(cluster);
        topic.setConfigs(retrieveConfigs(cluster.getClusterId(), topicName));
        removeUnusedFields(cluster);
        return topic;
    }

    public void delete(Long clusterDbId, String topicName) throws ExecutionException, InterruptedException {
        log.debug("Request to delete Topic : {} from cluster {}", topicName, clusterDbId);
        Cluster cluster = retrieveCluster(clusterDbId);
        adminCenter.deleteTopic(cluster.getClusterId(), topicName);
    }

    public List<Topic> findAll() {
        log.debug("Request to get all Topics");
        return Collections.emptyList();
    }

    private Cluster retrieveCluster(Long clusterDbId) throws ExecutionException, InterruptedException {
        Optional<Cluster> clusterOptional = clusterService.get(clusterDbId);
        return clusterOptional.get();
    }

    private Topic retrieveTopic(String clusterId, String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult describeTopicsResult = adminCenter.describeTopics(clusterId, Collections.singletonList(topicName));
        return TopicMapper.from(describeTopicsResult, topicName);
    }

    private List<GenericConfig> retrieveConfigs(String clusterId, String topicName) throws ExecutionException, InterruptedException {
        DescribeConfigsResult describeConfigsResult = adminCenter.describeTopicConfig(clusterId, topicName);
        return ConfigMapper.fromSingleResource(describeConfigsResult);
    }

    private void removeUnusedFields(Cluster cluster ) {
        cluster.setTopics(Collections.emptyList());
        cluster.setBrokers(Collections.emptySet());
    }

    private List<GenericConfig> diff(List<GenericConfig> original, List<GenericConfig> updated) {
        Map<String, GenericConfig> originalConfigMap = toMap(original);
        List<GenericConfig> diffResult = new ArrayList<>();
        updated.forEach(updatedConfig -> {
            String originalConfigValue = originalConfigMap.get(updatedConfig.getName()).getValue();
            if (!originalConfigValue.equals(updatedConfig.getValue())) {
                diffResult.add(updatedConfig);
            }
        });
        return diffResult;
    }

    private Map<String, GenericConfig> toMap(List<GenericConfig> configs) {
        return configs.stream().collect(Collectors.toMap(GenericConfig::getName, config -> config));
    }
}
