package org.kafmin.service.mapper;

import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.kafmin.domain.TopicDetails;

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
}
