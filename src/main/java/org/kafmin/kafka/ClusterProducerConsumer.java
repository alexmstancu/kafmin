package org.kafmin.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ClusterProducerConsumer implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(ClusterProducerConsumer.class);
    private final Duration longDuration = Duration.ofSeconds(4);
    private final Duration pollTimeout = Duration.ofMillis(500);
    private final String clusterId;
    private final String bootstrapServers;
    private final KafkaProducer<String, String> producer;
    private final KafkaConsumer<String, String> consumer;

    public ClusterProducerConsumer(String clusterId, String bootstrapServers) {
        this.clusterId = clusterId;
        this.bootstrapServers = bootstrapServers;
        this.producer = new KafkaProducer<>(getProducerProperties());
        logger.debug("Producer for cluster {} was initialized.", clusterId);
        this.consumer = new KafkaConsumer<>(getConsumerProperties());
        logger.debug("Consumer for cluster {} was initialized.", clusterId);
    }

    public RecordMetadata produceMessage(String topic, String key, String message) throws ExecutionException, InterruptedException {
        RecordMetadata metadata = producer.send(new ProducerRecord<>(topic, key, message)).get();
        logger.debug("Produced message to topic {}: key {}, message {}", topic, key, message);
        return metadata;
    }

    public Iterable<ConsumerRecord<String, String>> consumerRecords(String topic) {
        List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);
        List<TopicPartition> topicPartitions = toTopicPartitionList(partitionInfoList);
        consumer.assign(topicPartitions);
        consumer.seekToBeginning(Collections.emptyList());
        Optional<ConsumerRecords<String, String>> optionalConsumerRecords = poll();
        if (!optionalConsumerRecords.isPresent()) {
            return Collections.emptyList();
        }
        return optionalConsumerRecords.get().records(topic);
    }

    private Optional<ConsumerRecords<String, String>> poll() {
        int maxRetries = 3;
        int retires = 0;
        while (retires <= maxRetries) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(pollTimeout);
            if (!consumerRecords.isEmpty()) {
                return Optional.of(consumerRecords);
            }
            retires++;
        }
        return Optional.empty();
    }

    private List<TopicPartition> toTopicPartitionList(List<PartitionInfo> partitionInfoList) {
        return partitionInfoList
            .stream()
            .map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
            .collect(Collectors.toList());
    }

    private Properties getProducerProperties() {
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return producerProperties;
    }

    private Properties getConsumerProperties() {
        String consumerId = "kafmin-" + clusterId + "-";
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerId);
        return consumerProperties;
    }

    @Override
    public void close() {
        try {
            producer.close(longDuration);
            consumer.close(longDuration);
        } catch (Exception e) {
            logger.error("Could not close the ClusterProducerConsumer for cluster {}", clusterId, e);
        }
    }
}
