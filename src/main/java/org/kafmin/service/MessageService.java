package org.kafmin.service;

import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.kafmin.domain.Cluster;
import org.kafmin.domain.Message;
import org.kafmin.domain.MessageList;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.kafka.TopicPartitionCount;
import org.kafmin.repository.MessageRepository;
import org.kafmin.service.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service Implementation for managing {@link Message}.
 */
@Service
public class MessageService {
    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private KafkaAdministrationCenter adminCenter;

    /**
     * Save a message.
     *
     * @param message the entity to save.
     * @return the persisted entity.
     */
    public void produce(Long clusterDbId, Message message) throws ExecutionException, InterruptedException {
        log.debug("Request to produce Message : {}", message);
        Cluster cluster = retrieveCluster(clusterDbId);
        adminCenter.produceMessage(cluster.getClusterId(), message);
    }

    /**
     * Get all the messages.
     *
     * @return the list of entities.
     */
    public MessageList consume(Long clusterDbId, String topic) throws ExecutionException, InterruptedException {
        log.debug("Request to get all Messages for topic {} in cluster {}", topic, clusterDbId);
        Cluster cluster = retrieveCluster(clusterDbId);
        Iterable<ConsumerRecord<String, String>> consumerRecords = adminCenter.consumeMessages(cluster.getClusterId(), topic);
        List<Message> messages = MessageMapper.from(consumerRecords);
        removeUnusedFields(cluster);
        return MessageMapper.toMessageList(topic, cluster, messages, getPartitionsCount(cluster.getClusterId(), topic));
    }


    /**
     * Get one message by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Message> findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        return messageRepository.findById(id);
    }

    /**
     * Delete the message by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.deleteById(id);
    }

    private Cluster retrieveCluster(Long clusterDbId) throws ExecutionException, InterruptedException {
        return clusterService.get(clusterDbId).get();
    }

    private void removeUnusedFields(Cluster cluster) {
        cluster.setTopics(Collections.emptyList());
        cluster.setBrokers(Collections.emptySet());
    }

    private int getPartitionsCount(String clusterId, String topic) throws ExecutionException, InterruptedException {
        DescribeTopicsResult describeTopicsResult = adminCenter.describeTopics(clusterId, Collections.singletonList(topic));
        TopicPartitionCount topicPartitionCount = TopicPartitionCount.extract(describeTopicsResult);
        return topicPartitionCount.getPartitions();
    }
}
