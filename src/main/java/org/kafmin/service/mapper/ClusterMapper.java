package org.kafmin.service.mapper;

import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.kafmin.domain.Cluster;

import java.util.concurrent.ExecutionException;

public class ClusterMapper {
    public static Cluster fromDescription(DescribeClusterResult describeClusterResult) throws ExecutionException, InterruptedException {
        return new Cluster()
            .clusterId(describeClusterResult.clusterId().get())
            .brokers(BrokerMapper.fromNodes(describeClusterResult.nodes().get(), describeClusterResult.controller().get()));
    }
}
