package org.kafmin.service;

import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.kafmin.domain.Cluster;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.kafmin.kafka.TopicPartitionCount;
import org.kafmin.repository.ClusterRepository;
import org.kafmin.service.mapper.BrokerMapper;
import org.kafmin.service.mapper.ClusterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

        Cluster kafkaCluster = ClusterMapper.fromDescription(createdClusterResult);

        incomingCluster.setClusterId(kafkaCluster.getClusterId());
        incomingCluster.setBootstrapServers(BrokerMapper.toBootstrapServersStringList(kafkaCluster.getBrokers()));
        Cluster dbCluster = clusterRepository.save(incomingCluster);

        enhanceFromDb(kafkaCluster, dbCluster);
        enhanceWithTopicPartitionsCount(kafkaCluster);
        return kafkaCluster;
    }

    public Cluster update(Cluster incomingCluster) throws ExecutionException, InterruptedException {
        String newName = incomingCluster.getName();

        Cluster dbCluster = clusterRepository.findById(incomingCluster.getId()).get();
        Cluster kafkaCluster = ClusterMapper.fromDescription(adminCenter.describeCluster(dbCluster.getClusterId()));

        dbCluster.setName(newName);
        dbCluster.setBootstrapServers(BrokerMapper.toBootstrapServersStringList(kafkaCluster.getBrokers()));
        clusterRepository.save(dbCluster);

        enhanceFromDb(kafkaCluster, dbCluster);
        return kafkaCluster;
    }

    public Optional<Cluster> get(Long id) throws ExecutionException, InterruptedException {
        Optional<Cluster> dbCluster = clusterRepository.findById(id);
        if (!dbCluster.isPresent()) {
            return dbCluster;
        }

        Cluster kafkaCluster = ClusterMapper.fromDescription(adminCenter.describeCluster(dbCluster.get().getClusterId()));
        enhanceFromDb(kafkaCluster, dbCluster.get());
        enhanceWithTopicPartitionsCount(kafkaCluster);

        return Optional.of(kafkaCluster);
    }

    public List<Cluster> getAll() throws ExecutionException, InterruptedException {
        // they have id and name
        List<Cluster> dbClusters = clusterRepository.findAll();
        if (dbClusters.isEmpty()) {
            return dbClusters;
        }

        List<Cluster> result = new ArrayList<>();

        for (Cluster dbCluster : dbClusters) {
            Cluster kafkaCluster = ClusterMapper.fromDescription(adminCenter.describeCluster(dbCluster.getClusterId()));
            enhanceFromDb(kafkaCluster, dbCluster);
            enhanceWithTopicPartitionsCount(kafkaCluster);
            result.add(kafkaCluster);
        }

        return result;
    }

    public void delete(Long id) {
        Optional<Cluster> optional = clusterRepository.findById(id);
        if (optional.isPresent()) {
            String clusterId = optional.get().getClusterId();
            adminCenter.deleteCluster(clusterId);
            clusterRepository.deleteById(id);
        }
    }

    private void enhanceFromDb(Cluster kafkaCluster, Cluster dbCluster) {
        kafkaCluster.setId(dbCluster.getId());
        kafkaCluster.setName(dbCluster.getName());
    }

    private void enhanceWithTopicPartitionsCount(Cluster kafkaCluster) throws ExecutionException, InterruptedException {
        DescribeTopicsResult describeTopicsResult = adminCenter.describeTopics(kafkaCluster.getClusterId());
        TopicPartitionCount topicPartitionCount = TopicPartitionCount.extract(describeTopicsResult);
        kafkaCluster.setTopicsCount(topicPartitionCount.getTopics());
        kafkaCluster.setPartitionsCount(topicPartitionCount.getPartitions());
    }
}
