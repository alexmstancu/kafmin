package org.kafmin.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaAdministrationCenter {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAdministrationCenter.class);
    private static final DescribeClusterOptions describeOptions = new DescribeClusterOptions().timeoutMs(3000);
    public static final String HTTP_LOCALHOST_9093 = "http://localhost:9092";

    private Map<String, Admin> kafkaAdminByClusterId = new HashMap<>();

//    public KafkaAdministrationCenter() {
//        logger.debug("Starting the KafkaAdministrationCenter.");
//        testConnection();
//    }

    // TODO this should be removed later on
    private void testConnection() {
        try {
            createCluster(HTTP_LOCALHOST_9093);
        } catch (Exception e) {
            logger.error("Something went wrong describing cluster with bootstrapServers" + HTTP_LOCALHOST_9093, e);
        }
    }

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
            addClusterAdmin(clusterResult.clusterId().get(), admin);
        }
        return clusterResult;
    }

    private void addClusterAdmin(String clusterId, Admin admin) {
        kafkaAdminByClusterId.put(clusterId, admin);
    }

    private Admin getClusterAdmin(String clusterId) {
        return kafkaAdminByClusterId.get(clusterId);
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
