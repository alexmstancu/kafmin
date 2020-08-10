package org.kafmin.service.mapper;

import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.TopicPartitionInfo;
import org.kafmin.domain.Broker;
import org.kafmin.domain.Topic;
import org.kafmin.domain.TopicDetails;
import org.kafmin.domain.TopicPartition;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TopicMapper {
    public static List<TopicDetails> from(DescribeTopicsResult describeTopicsResult) throws ExecutionException, InterruptedException {
        return describeTopicsResult
            .all()
            .get()
            .values()
            .stream()
            .map(description -> new TopicDetails(description.name(), description.isInternal(), description.partitions().size()))
            .collect(Collectors.toList());
    }

    public static Topic from(DescribeTopicsResult describeTopicsResult, String topicName) throws ExecutionException, InterruptedException {
        return describeTopicsResult
            .all()
            .get()
            .values()
            .stream()
            .map(TopicMapper::from)
            .findFirst()
            .get();
    }

    public static Topic from(TopicDescription topicDescription) {
        Topic topic = new Topic();
        topic.setName(topicDescription.name());
        topic.setIsInternal(topicDescription.isInternal());
        topic.setPartitions(from(topicDescription.partitions()));
        return topic;
    }

    public static List<TopicPartition> from(List<TopicPartitionInfo> topicPartitionInfoList) {
        return topicPartitionInfoList
            .stream()
            .map(TopicMapper::from)
            .collect(Collectors.toList());
    }

    public static TopicPartition from(TopicPartitionInfo topicPartitionInfo) {
        Broker leader = BrokerMapper.fromNode(topicPartitionInfo.leader());
        List<Broker> replicas = topicPartitionInfo.replicas().stream().map(BrokerMapper::fromNode).collect(Collectors.toList());
        return new TopicPartition(topicPartitionInfo.partition(), leader, replicas);
    }
}
