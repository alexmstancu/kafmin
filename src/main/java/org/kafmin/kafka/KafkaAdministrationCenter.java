package org.kafmin.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.KafkaException;
import org.kafmin.domain.Cluster;
import org.kafmin.repository.ClusterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaAdministrationCenter {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAdministrationCenter.class);
    private static final DescribeClusterOptions describeOptions = new DescribeClusterOptions().timeoutMs(3000);
    public static final String HTTP_LOCALHOST_9093 = "http://localhost:9092";

    private final Map<String, Admin> kafkaAdminByClusterId = new HashMap<>();

    @Autowired
    private ClusterRepository clusterRepository;

    @PostConstruct
    public void init() {
        List<Cluster> existingClusters = clusterRepository.findAll();
        logger.debug("Initializing the KafkaAdminClients for the existing clusters in the DB: {}", existingClusters);
        existingClusters.forEach(cluster -> {
            try {
                createCluster(cluster.getBootstrapServers());
            } catch (Exception e) {
                logger.error("Could not initialize KafkaAdminClient at startup for cluster {}", cluster, e);
            }
        });
    }

    // CLUSTER ADMINISTRATION

    /**
     * Gets the cluster description for that cluster.
     *
     * @return the already 'described' cluster or null if could not 'get' the description
     */
    public DescribeClusterResult describeCluster(String clusterId) {
        return describeClusterResultGet(getClusterAdmin(clusterId).describeCluster(describeOptions));
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

        DescribeClusterResult clusterResult = describeClusterResultGet(admin.describeCluster(describeOptions));
        if (clusterResult != null) {
            String clusterId = clusterResult.clusterId().get();
            if (kafkaAdminByClusterId.containsKey(clusterId)) {
                return null;
            }

            addClusterAdmin(clusterId, admin);
        }
        return clusterResult;
    }

    public void deleteCluster(String clusterId) {
        kafkaAdminByClusterId.get(clusterId).close(Duration.ofSeconds(5));
        kafkaAdminByClusterId.remove(clusterId);
    }

    private DescribeClusterResult describeClusterResultGet(DescribeClusterResult clusterResult) {
        try {
            logger.debug("Describing ClusterId: {}, Controller: {}, Nodes: {}.",
                clusterResult.clusterId().get(),
                clusterResult.controller().get(),
                clusterResult.nodes().get());
        } catch (Exception e) {
            logger.error("Could not 'get' the cluster description.");
            return null;
        }
        return clusterResult;
    }

    // KAFKA ADMIN

    private Admin getClusterAdmin(String clusterId) {
        return kafkaAdminByClusterId.get(clusterId);
    }

    private void addClusterAdmin(String clusterId, Admin admin) {
        kafkaAdminByClusterId.put(clusterId, admin);
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
