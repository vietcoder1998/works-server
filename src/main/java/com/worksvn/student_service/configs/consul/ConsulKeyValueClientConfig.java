package com.worksvn.student_service.configs.consul;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsulKeyValueClientConfig {
    @Value("${spring.cloud.consul.host}")
    private String consulHost;
    @Value("${spring.cloud.consul.port}")
    private int consulPort;

    @Bean
    public KeyValueClient consulKVClient() {
        return Consul.builder()
                .withHostAndPort(HostAndPort.fromParts(consulHost, consulPort))
                .build()
                .keyValueClient();
    }
}
