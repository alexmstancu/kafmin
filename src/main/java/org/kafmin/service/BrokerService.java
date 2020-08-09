package org.kafmin.service;

import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.kafmin.domain.Broker;
import org.kafmin.domain.Cluster;
import org.kafmin.domain.GenericConfig;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.service.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class BrokerService {
    @Autowired
    private KafkaAdministrationCenter adminCenter;

    @Autowired
    private ClusterService clusterService;

    public Optional<Broker> get(Long clusterDbId, String brokerId) throws ExecutionException, InterruptedException {
        Cluster cluster = retrieveCluster(clusterDbId);
        Broker broker = findBroker(cluster, brokerId);
        removeUnusedFields(cluster);
        enhanceWithConfigs(broker);
        return Optional.of(broker);
    }

    private void enhanceWithConfigs(Broker broker) throws ExecutionException, InterruptedException {
        List<GenericConfig> brokerConfigs = retrieveConfigs(broker.getCluster().getClusterId(), broker.getBrokerId());
        broker.setConfigs(brokerConfigs);
    }

    private Cluster retrieveCluster(Long clusterDbId) throws ExecutionException, InterruptedException {
        Optional<Cluster> clusterOptional = clusterService.get(clusterDbId);
        return clusterOptional.get();
    }

    private void removeUnusedFields(Cluster cluster ) {
        cluster.setTopics(Collections.emptyList());
        cluster.setBrokers(Collections.emptySet());
    }

    private Broker findBroker(Cluster cluster, String brokerId) {
        for (Broker candidate : cluster.getBrokers()) {
            if (candidate.getBrokerId().equals(brokerId)) {
                candidate.setCluster(cluster);
                return candidate;
            }
        }
        return null;
    }

    private List<GenericConfig> retrieveConfigs(String clusterId, String brokerId) throws ExecutionException, InterruptedException {
        DescribeConfigsResult describeConfigsResult = adminCenter.describeBrokerConfig(clusterId, brokerId);
        return ConfigMapper.fromSingleResource(describeConfigsResult);
    }
}
