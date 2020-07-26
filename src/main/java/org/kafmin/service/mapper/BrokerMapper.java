package org.kafmin.service.mapper;

import org.apache.kafka.common.Node;
import org.kafmin.domain.Broker;

import java.util.Collection;
import java.util.Set;
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
        broker.isController(node.id() == controller.id());
        return broker;
    }

    public static Set<Broker> fromNodes(Collection<Node> nodes, Node controller) {
        return nodes.stream().map(n -> fromNode(n, controller)).collect(Collectors.toSet());
    }
}
