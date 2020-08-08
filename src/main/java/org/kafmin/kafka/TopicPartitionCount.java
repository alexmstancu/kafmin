package org.kafmin.kafka;

import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TopicPartitionCount {
    private final int topics;
    private final int partitions;

    public TopicPartitionCount(int topics, int partitions) {
        this.topics = topics;
        this.partitions = partitions;
    }

    public int getPartitions() {
        return partitions;
    }

    public int getTopics() {
        return topics;
    }

    public static TopicPartitionCount extract(DescribeTopicsResult describeTopicResult) throws ExecutionException, InterruptedException {
        Map<String, TopicDescription> topicDescriptionMap = describeTopicResult.all().get();
        int topicsCount = topicDescriptionMap.size();
        int partitionsCount = topicDescriptionMap.values().stream()
            .map(description -> description.partitions().size())
            .reduce(0, Integer::sum);
        return new TopicPartitionCount(topicsCount, partitionsCount);
    }
}
