package org.kafmin.service.mapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.kafmin.domain.Cluster;
import org.kafmin.domain.Message;
import org.kafmin.domain.MessageList;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageMapper {

    public static Message from(ConsumerRecord<String, String> consumerRecord) {
        Message message = new Message();
        message.setKey(consumerRecord.key());
        message.setMessage(consumerRecord.value());
        message.setPartition(consumerRecord.partition());
        message.setTopic(consumerRecord.topic());
        message.setDate(new Date(consumerRecord.timestamp()));
        message.setOffset(consumerRecord.offset());
        return message;
    }

    public static List<Message> from(Iterable<ConsumerRecord<String, String>> consumerRecords) {
        return StreamSupport
            .stream(consumerRecords.spliterator(), false)
            .map(MessageMapper::from)
            .collect(Collectors.toList());
    }

    public static MessageList toMessageList(String topic, Cluster cluster, List<Message> messages) {
        MessageList messageList = new MessageList();
        messageList.setCluster(cluster);
        messageList.setTopic(topic);
        messageList.setMessages(messages);
        return messageList;
    }
}
