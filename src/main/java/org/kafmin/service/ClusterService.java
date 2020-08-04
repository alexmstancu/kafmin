package org.kafmin.service;

import org.kafmin.domain.Cluster;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.repository.ClusterRepository;
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

    public Cluster create(Cluster cluster) throws ExecutionException, InterruptedException {
        Optional<Cluster> existing = clusterRepository.findByName(cluster.getName());
        if (existing.isPresent()) {
            return null;
        }

        Cluster incomingCluster = clusterRepository.save(cluster);
        Cluster actualCluster = ClusterMapper.fromDescription(adminCenter.describeCluster(incomingCluster.getClusterId()));
        actualCluster.setName(incomingCluster.getName());
        actualCluster.setId(incomingCluster.getId());
        return actualCluster;
    }

    public Cluster update(Cluster cluster) throws ExecutionException, InterruptedException {
        Cluster incomingCluster = clusterRepository.save(cluster);
        Cluster actualCluster = ClusterMapper.fromDescription(adminCenter.describeCluster(incomingCluster.getClusterId()));
        actualCluster.setName(incomingCluster.getName());
        actualCluster.setId(incomingCluster.getId());
        return actualCluster;
    }

}
