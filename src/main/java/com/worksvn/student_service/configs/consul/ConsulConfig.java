package com.worksvn.student_service.configs.consul;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsulConfig {
    @Bean
    public ConsulServiceRegistry consulServiceRegistry(ConsulClient consulClient,
                                                       ConsulDiscoveryProperties consulDiscoveryProperties,
                                                       HeartbeatProperties heartbeatProperties) {
        return new ConsulServiceRegistry(consulClient, consulDiscoveryProperties, null, heartbeatProperties);
    }
}
