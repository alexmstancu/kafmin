package org.kafmin.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class KafkaAdministrationCenter {
    public static final String HTTP_LOCALHOST_9093 = "http://localhost:9092";
    private final Logger logger = LoggerFactory.getLogger(KafkaAdministrationCenter.class);

    private Map<String, Admin> kafkaAdminByCluster = new HashMap<>();

    public KafkaAdministrationCenter() {
        logger.debug("Starting the KafkaAdministrationCenter.");
        testConnection();
    }

    // TODO this should be removed later on
    private void testConnection() {
        try {
            describeCluster(HTTP_LOCALHOST_9093);
        } catch (Exception e) {
            logger.error("Something went wrong describing cluster " + HTTP_LOCALHOST_9093, e);
        }
    }

    public DescribeClusterResult describeCluster(String clusterId) {
        DescribeClusterResult clusterResult = getClusterAdmin(clusterId).describeCluster();

        try {
            logger.debug("Describing ClusterId: {}, Controller: {}, Nodes: {}.",
                clusterResult.clusterId().get(),
                clusterResult.controller().get(),
                clusterResult.nodes().get());
        } catch (Exception e) {
            logger.error("Could not get the cluster description");
        }

        return null;
    }

    private Admin getClusterAdmin(String clusterId) {
        return kafkaAdminByCluster.computeIfAbsent(clusterId, this::createAdmin);
    }


    private Admin createAdmin(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return Admin.create(properties);
    }
}
