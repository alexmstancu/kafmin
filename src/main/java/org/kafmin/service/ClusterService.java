package org.kafmin.service;

import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.kafmin.domain.Cluster;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.repository.ClusterRepository;
import org.kafmin.service.mapper.BrokerMapper;
import org.kafmin.service.mapper.ClusterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private KafkaAdministrationCenter adminCenter;

    public Cluster create(Cluster incomingCluster) throws ExecutionException, InterruptedException {
        Optional<Cluster> existing = clusterRepository.findByName(incomingCluster.getName());
        if (existing.isPresent()) {
            return null;
        }

        // it comes with just name & at least one broker
        DescribeClusterResult createdClusterResult = adminCenter.createCluster(BrokerMapper.toBootstrapServersStringList(incomingCluster.getBrokers()));
        if (createdClusterResult == null) {
            return null;
        }

        Cluster createdCluster = ClusterMapper.fromDescription(createdClusterResult);

        incomingCluster.setClusterId(createdCluster.getClusterId());
        Cluster savedCluster = clusterRepository.save(incomingCluster);

        createdCluster.setName(savedCluster.getName());
        createdCluster.setId(savedCluster.getId());
        return createdCluster;
    }

    public Optional<Cluster> get(Long id) {
        return null;

    }

    public Cluster update(Cluster cluster) throws ExecutionException, InterruptedException {
        Cluster incomingCluster = clusterRepository.save(cluster);
        Cluster actualCluster = ClusterMapper.fromDescription(adminCenter.describeCluster(incomingCluster.getClusterId()));
        actualCluster.setName(incomingCluster.getName());
        actualCluster.setId(incomingCluster.getId());
        return actualCluster;
    }

}
