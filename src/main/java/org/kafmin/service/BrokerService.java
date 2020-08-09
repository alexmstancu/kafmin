package org.kafmin.service;

import org.kafmin.domain.Broker;
import org.kafmin.kafka.KafkaAdministrationCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrokerService {
    @Autowired
    private KafkaAdministrationCenter adminCenter;

    // TODO
    public Optional<Broker> get(String clusterId, String brokerId) {
        return Optional.empty();
    }
}
