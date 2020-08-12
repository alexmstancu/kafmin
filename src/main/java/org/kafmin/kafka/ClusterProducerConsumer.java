package org.kafmin.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ClusterProducerConsumer implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(ClusterProducerConsumer.class);
    private final Duration longDuration = Duration.ofSeconds(4);
    private final Duration shortDuration = Duration.ofMillis(500);
    private final String clusterId;
    private final String bootstrapServers;
    private final Map<String, KafkaConsumer<String, String>> consumersByTopic = new HashMap<>();
    private final KafkaProducer<String, String> producer;

    public ClusterProducerConsumer(String clusterId, String bootstrapServers, List<String> topics) {
        this.clusterId = clusterId;
        this.bootstrapServers = bootstrapServers;
        this.producer = new KafkaProducer<>(getProducerProperties());
        logger.debug("Producer for cluster {} was initialized.", clusterId);
        initConsumers(topics);
    }

    public RecordMetadata produceMessage(String topic, String key, String message) throws ExecutionException, InterruptedException {
        RecordMetadata metadata = producer.send(new ProducerRecord<>(topic, key, message)).get();
        logger.debug("Produced message to topic {}: key {}, message {}", topic, key, message);
        return metadata;
    }

    public Iterable<ConsumerRecord<String, String>> consumeAllRecords(String topic) {
        KafkaConsumer<String, String> consumer = consumersByTopic.get(topic);
        try {
            consumer.seekToBeginning(Collections.emptyList());
            ConsumerRecords<String, String> consumerRecords = consumer.poll(shortDuration);
            logger.debug("Consumed {} messages from topic {} cluster {}", consumerRecords.count(), topic, clusterId);
            return consumerRecords.records(topic);
        } catch (Exception e) {
            logger.error("Could not consume from topic {} cluster {}", topic, clusterId, e);
        }
        return Collections.emptyList();
    }

    public void addConsumer(String topic) {
        initConsumers(Collections.singletonList(topic));
    }

    public void removeConsumer(String topic) {
        KafkaConsumer<String, String> consumer = consumersByTopic.get(topic);
        try {
            consumer.close(longDuration);
            consumersByTopic.remove(topic);
        } catch (Exception e) {
            logger.error("Could not close the consumer for cluster {} topic {}", clusterId, topic, e);
        }
    }

    private void initConsumers(List<String> topics) {
        topics.forEach(topic -> {
            KafkaConsumer<String, String> consumer = createConsumer(topic);
            consumersByTopic.put(topic, consumer);
        });
        logger.debug("All consumers for cluster {}, topics {} were initialized.", clusterId, topics);
    }

    private KafkaConsumer<String, String> createConsumer(String topic) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getConsumerProperties(topic));
        consumer.subscribe(Collections.singletonList(topic));
        consumer.poll(longDuration);
        logger.debug("Consumer for cluster {} topic {} was initialized.", clusterId, topic);
        return consumer;
    }

    private Properties getProducerProperties() {
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return producerProperties;
    }

    private Properties getConsumerProperties(String topic) {
        String consumerId = "kafmin-" + clusterId + "-" + topic;
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
            consumersByTopic.values().forEach(c -> c.close(longDuration));
        } catch (Exception e) {
            logger.error("Could not close the ClusterProducerConsumer for cluster {}", clusterId, e);
        }
    }
}
