package org.kafmin.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaAdministrationCenter {
    private final Logger logger = LoggerFactory.getLogger(KafkaAdministrationCenter.class);

    private Map<String, Admin> kafkaAdminClientsByCluster = new HashMap<>();

    public KafkaAdministrationCenter() {
        Admin kafkaAdminClient = createAdmin();
        DescribeClusterResult describeClusterResult = kafkaAdminClient.describeCluster();
        try {
            logger.debug("ClusterId {}", describeClusterResult.clusterId().get());
            logger.debug("Nodes {}", describeClusterResult.nodes().get());
            logger.debug("Controller {}", describeClusterResult.controller().get());
        } catch (Exception e) {
            logger.error("Something went wrong describing ");
        }
    }

    public DescribeClusterResult describeCluster(String clusterId) throws ExecutionException, InterruptedException {
        Admin kafkaAdminClient = kafkaAdminClientsByCluster.get(clusterId);
        DescribeClusterResult clusterResult = kafkaAdminClient.describeCluster();
        clusterResult.clusterId().get();
        clusterResult.nodes().get();
        clusterResult.controller().get();
        return clusterResult;
    }

    public Admin createAdmin() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        return Admin.create(properties);
    }
}
