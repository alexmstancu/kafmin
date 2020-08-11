package org.kafmin.service.mapper;

import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.common.config.ConfigResource;
import org.kafmin.domain.GenericConfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ConfigMapper {
    public static List<GenericConfig> fromSingleResource(DescribeConfigsResult describeConfigsResult) throws ExecutionException, InterruptedException {
        Map<ConfigResource, Config> configResourceConfigMap = describeConfigsResult.all().get();
        ConfigResource key = configResourceConfigMap.keySet().iterator().next();
        Config kafkaConfigList = configResourceConfigMap.get(key);
        return kafkaConfigList
            .entries()
            .stream()
            .map(configEntry -> new GenericConfig(configEntry.name(), configEntry.value(), configEntry.isReadOnly()))
            .collect(Collectors.toList());
    }

    public static Collection<AlterConfigOp> toAlterOpList(List<GenericConfig> configs) {
        return configs
            .stream()
            .map(ConfigMapper::toConfigEntry)
            .map(ConfigMapper::configEntryToAlterOpSet)
            .collect(Collectors.toList());
    }

    private static AlterConfigOp configEntryToAlterOpSet(ConfigEntry config) {
        return new AlterConfigOp(config, AlterConfigOp.OpType.SET);
    }

    public static ConfigEntry toConfigEntry(GenericConfig config) {
        return new ConfigEntry(config.getName(), config.getValue());
    }

}
