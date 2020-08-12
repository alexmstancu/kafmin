package org.kafmin.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.config.ConfigResource;
import org.kafmin.domain.Cluster;
import org.kafmin.domain.GenericConfig;
import org.kafmin.domain.Topic;
import org.kafmin.repository.ClusterRepository;
import org.kafmin.service.mapper.ConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaAdministrationCenter {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAdministrationCenter.class);
    private static final int TIMEOUT_MS = 3000;
    private static final DescribeClusterOptions DESCRIBE_CLUSTER_OPTIONS = new DescribeClusterOptions().timeoutMs(TIMEOUT_MS);
    private static final DescribeTopicsOptions DESCRIBE_TOPICS_OPTIONS = new DescribeTopicsOptions().timeoutMs(TIMEOUT_MS);
    private static final DescribeConfigsOptions DESCRIBE_CONFIGS_OPTIONS = new DescribeConfigsOptions().timeoutMs(TIMEOUT_MS);
    private static final CreateTopicsOptions CREATE_TOPICS_OPTIONS = new CreateTopicsOptions().timeoutMs(TIMEOUT_MS);
    private static final DeleteTopicsOptions DELETE_TOPICS_RESULT = new DeleteTopicsOptions().timeoutMs(TIMEOUT_MS);
    private static final AlterConfigsOptions ALTER_CONFIGS_OPTIONS = new AlterConfigsOptions().timeoutMs(TIMEOUT_MS);
    private static final ListTopicsOptions LIST_TOPICS_OPTIONS = new ListTopicsOptions().timeoutMs(3000);

    private final Map<String, Admin> kafkaAdminByClusterId = new HashMap<>();
    private final Map<String, ClusterProducerConsumer> clusterProducerConsumerByClusterId = new HashMap<>();

    @Autowired
    private ClusterRepository clusterRepository;

    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {
        List<Cluster> existingClusters = clusterRepository.findAll();
        logger.debug("Initializing the KafkaAdminClients for the existing clusters in the DB: {}", existingClusters);
        existingClusters.forEach(cluster -> {
            try {
                createCluster(cluster.getBootstrapServers());
            } catch (Exception e) {
                logger.error("Could not initialize KafkaAdminClient at startup for cluster {}", cluster, e);
            }
        });
        tmpDELETE_THIS_METHOD();
    }

    private void tmpDELETE_THIS_METHOD() throws ExecutionException, InterruptedException {
        ClusterProducerConsumer producerConsumer = clusterProducerConsumerByClusterId.get("wrd2tnF3T6efxMnMO40yDQ");
        RecordMetadata recordMetadata = producerConsumer.produceMessage("topic1", "alex", "salut" + new Date());
        logger.debug("RecordMetadata: {}", recordMetadata);
        Iterable<ConsumerRecord<String, String>> records = producerConsumer.consumerRecords("topic1");
        records.forEach(record -> {
            logger.debug("Key: {}, Record: {}", record.key(), record.value());
        });

        recordMetadata = producerConsumer.produceMessage("topic2", "alex2", "salut2" + new Date());
        logger.debug("RecordMetadata: {}", recordMetadata);
        records = producerConsumer.consumerRecords("topic2");
        records.forEach(record -> {
            logger.debug("Key: {}, Record: {}", record.key(), record.value());
        });
    }

    // CLUSTER ADMINISTRATION

    /**
     * Gets the cluster description for that cluster.
     *
     * @return the already 'described' cluster or null if could not 'get' the description
     */
    public DescribeClusterResult describeCluster(String clusterId) {
        return describeClusterResultGet(getClusterAdmin(clusterId).describeCluster(DESCRIBE_CLUSTER_OPTIONS));
    }

    /**
     * Connects to the bootstrapServer and gets the cluster description for that cluster.
     *
     * @return the already 'described' cluster or null if could not connect or could not 'get' the description
     */
    public DescribeClusterResult createCluster(String bootstrapServers) throws ExecutionException, InterruptedException {
        Admin admin = createAdmin(bootstrapServers);
        if (admin == null) {
            return null;
        }

        DescribeClusterResult clusterResult = describeClusterResultGet(admin.describeCluster(DESCRIBE_CLUSTER_OPTIONS));
        if (clusterResult != null) {
            String clusterId = clusterResult.clusterId().get();
            if (kafkaAdminByClusterId.containsKey(clusterId)) {
                return null;
            }

            addClusterAdmin(clusterId, admin);
            addClusterProducerConsumer(clusterId, bootstrapServers);
        }
        return clusterResult;
    }

    public void deleteCluster(String clusterId) {
        kafkaAdminByClusterId.get(clusterId).close(Duration.ofSeconds(4));
        kafkaAdminByClusterId.remove(clusterId);

        clusterProducerConsumerByClusterId.get(clusterId).close();;
        clusterProducerConsumerByClusterId.remove(clusterId);
    }

    private DescribeClusterResult describeClusterResultGet(DescribeClusterResult clusterResult) {
        try {
            logger.debug("Describing ClusterId: {}, Controller: {}, Nodes: {}.",
                clusterResult.clusterId().get(),
                clusterResult.controller().get(),
                clusterResult.nodes().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the cluster description", e);
            return null;
        }
        return clusterResult;
    }

    // TOPICS & PARTITIONS MANAGEMENT

    private CreateTopicsResult createTopicsResultGet(CreateTopicsResult createTopicResultGet, String clusterId) {
        try {
            logger.debug("Topics listing for cluster: {}, listing: {}", clusterId, createTopicResultGet.all().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the CreateTopicsResult fpr cluster {}.", clusterId, e);
            return null;
        }
        return createTopicResultGet;
    }

    public CreateTopicsResult createTopic(String clusterId, Topic topic) {
        Admin clusterAdmin = getClusterAdmin(clusterId);
        NewTopic newTopic = new NewTopic(topic.getName(), topic.getNumPartitions(), topic.getReplicationFactor());
        CreateTopicsResult createTopicsResult = clusterAdmin.createTopics(Collections.singletonList(newTopic), CREATE_TOPICS_OPTIONS);
        return createTopicsResultGet(createTopicsResult, clusterId);
    }

    private DeleteTopicsResult deleteTopicsResultGet(DeleteTopicsResult deleteTopicsResult, String clusterId, String topicName) {
        try {
            logger.debug("DeleteTopicResult for cluster{} is {}", clusterId, deleteTopicsResult.all().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the DeleteTopicsResult for cluster {} and topic {}", clusterId, topicName);
            return null;
        }
        return deleteTopicsResult;
    }

    public DeleteTopicsResult deleteTopic(String clusterId, String topicName) {
        Admin clusterAdmin = getClusterAdmin(clusterId);
        DeleteTopicsResult deleteTopicsResult = clusterAdmin.deleteTopics(Collections.singletonList(topicName), DELETE_TOPICS_RESULT);
        return deleteTopicsResultGet(deleteTopicsResult, clusterId, topicName);
    }

    private AlterConfigsResult alterConfigsResultGet(AlterConfigsResult alterConfigsResult, String clusterId, String topicName) {
        try {
            logger.debug("AlterConfigsResult for topic {} in cluster: {}, listing: {}", topicName, clusterId, alterConfigsResult.all().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the AlterConfigsResult for topic {} in cluster {}.", topicName, clusterId, e);
            return null;
        }
        return alterConfigsResult;
    }

    public void updateTopicConfig(String clusterId, String topicName, List<GenericConfig> configsToUpdate) {
        Admin clusterAdmin = getClusterAdmin(clusterId);
        Map<ConfigResource, Collection<AlterConfigOp>> configsToUpdateMap = new HashMap<>();
        ConfigResource topicResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        configsToUpdateMap.put(topicResource, ConfigMapper.toAlterOpList(configsToUpdate));
        alterConfigsResultGet(clusterAdmin.incrementalAlterConfigs(configsToUpdateMap, ALTER_CONFIGS_OPTIONS), clusterId, topicName);
    }

    public ListTopicsResult listTopics(String clusterId) {
        return listTopicsResultGet(getClusterAdmin(clusterId).listTopics(LIST_TOPICS_OPTIONS), clusterId);
    }

    private ListTopicsResult listTopicsResultGet(ListTopicsResult listTopicsResult, String clusterId) {
        try {
            logger.debug("Topics listing for cluster: {}, listing: {}", clusterId, listTopicsResult.namesToListings().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the topics listing for cluster {}.", clusterId, e);
            return null;
        }
        return listTopicsResult;
    }

    /**
     * Describe all topics & associated partitions for the given cluster
     */
    public DescribeTopicsResult describeTopics(String clusterId) throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = listTopics(clusterId);
        return describeTopics(clusterId, listTopicsResult.names().get());
    }

    /**
     * Describe only the given topics for the given cluster
     */
    public DescribeTopicsResult describeTopics(String clusterId, Collection<String> topics) {
        return describeTopicsResultGet(getClusterAdmin(clusterId).describeTopics(topics, DESCRIBE_TOPICS_OPTIONS), clusterId);
    }

    private DescribeTopicsResult describeTopicsResultGet(DescribeTopicsResult describeTopicsResult, String clusterId) {
        try {
            logger.debug("Topics description for cluster: {}, description: {}", clusterId, describeTopicsResult.all().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the topic description for cluster {}", clusterId, e);
            return null;
        }
        return describeTopicsResult;
    }

    public DescribeConfigsResult describeTopicConfig(String clusterId, String topicName) {
        Admin clusterAdmin = getClusterAdmin(clusterId);
        ConfigResource topicResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        DescribeConfigsResult describeConfigsResult = clusterAdmin.describeConfigs(Collections.singletonList(topicResource), DESCRIBE_CONFIGS_OPTIONS);
        return describeConfigResourceGet(describeConfigsResult, clusterId);
    }

    // BROKER MANAGEMENT

    private DescribeConfigsResult describeConfigResourceGet(DescribeConfigsResult describeConfigsResult, String clusterId) {
        try {
            logger.debug("Configs description for cluster: {}, description: {}", clusterId, describeConfigsResult.all().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the configs description for cluster {}", clusterId, e);
            return null;
        }
        return describeConfigsResult;
    }

    public DescribeConfigsResult describeBrokerConfig(String clusterId, String brokerId) {
        Admin clusterAdmin = getClusterAdmin(clusterId);
        ConfigResource brokerResource = new ConfigResource(ConfigResource.Type.BROKER, brokerId);
        DescribeConfigsResult describeConfigsResult = clusterAdmin.describeConfigs(Collections.singletonList(brokerResource), DESCRIBE_CONFIGS_OPTIONS);
        return describeConfigResourceGet(describeConfigsResult, clusterId);
    }

    // KAFKA ADMIN MANAGEMENT

    private Admin getClusterAdmin(String clusterId) {
        return kafkaAdminByClusterId.get(clusterId);
    }

    private void addClusterAdmin(String clusterId, Admin admin) {
        kafkaAdminByClusterId.put(clusterId, admin);
    }

    private void addClusterProducerConsumer(String clusterId, String bootstrapServers) {
        ClusterProducerConsumer clusterProducerConsumer = new ClusterProducerConsumer(clusterId, bootstrapServers);
        clusterProducerConsumerByClusterId.put(clusterId, clusterProducerConsumer);
    }

    private Admin createAdmin(String bootstrapServers) {
        try {
            logger.debug("Creating KafkaAdminClient for bootstrapServers: {}", bootstrapServers);
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            return Admin.create(properties);
        } catch (KafkaException e) {
            logger.error("Failed to create KafkaAdminClient for bootstrapServers: {}", bootstrapServers, e);
            return null;
        }
    }
}
