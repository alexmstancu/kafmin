package org.kafmin.service.mapper;

import org.apache.kafka.common.Node;
import org.kafmin.domain.Broker;

import java.util.Collection;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BrokerMapper {
    public static Broker fromNode(Node node) {
        return new Broker()
            .brokerId(node.idString())
            .host(node.host())
            .port(node.port());
    }

    public static Broker fromNode(Node node, Node controller) {
        Broker broker = fromNode(node);
        boolean isController = controller != null && node.id() == controller.id();
        broker.isController(isController);
        return broker;
    }

    public static Set<Broker> fromNodes(Collection<Node> nodes, Node controller) {
        return nodes.stream().map(n -> fromNode(n, controller)).collect(Collectors.toSet());
    }

    public static String toBootstrapServersStringList(Set<Broker> brokers) {
        StringJoiner joiner = new StringJoiner(",");
        brokers.forEach(broker -> joiner.add(createIpAddress(broker)));
        return joiner.toString();
    }

    private static String createIpAddress(Broker broker) {
        return broker.getHost() + ":" + broker.getPort();
    }
}
