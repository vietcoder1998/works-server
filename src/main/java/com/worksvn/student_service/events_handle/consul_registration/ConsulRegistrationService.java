package com.worksvn.student_service.events_handle.consul_registration;

import com.ecwid.consul.v1.agent.model.NewService;
import com.worksvn.common.annotations.event.EventHandler;
import com.worksvn.student_service.events_handle.ApplicationEvent;
import com.worksvn.student_service.events_handle.ApplicationEventHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP)
public class ConsulRegistrationService implements ApplicationEventHandle {
    @Autowired
    private ConsulServiceRegistry consulServiceRegistry;
    @Autowired
    private ConsulDiscoveryProperties consulDiscoveryProperties;

    @Value("${spring.cloud.service-registry.auto-registration.enabled:true}")
    private boolean isAutoConfigurationEnabled;
    @Value("${spring.cloud.consul.name}")
    private String serviceName;
    @Value("${server.port:8080}")
    private int serverPort;
    @Value("${spring.cloud.consul.discovery.health-check-path:/actuator/health}")
    private String consulHealthCheckPath;
    @Value("${spring.cloud.consul.discovery.health-check-interval:10s}")
    private String consulHealthCheckInterval;
    @Value("${spring.cloud.consul.discovery.health-check-timeout:5s}")
    private String consulHealthCheckTimeout;

    @Override
    public String startMessage() {
        return "Start registering service to consul...";
    }

    @Override
    public String successMessage() {
        return "Consul registration...OK";
    }

    @Override
    public void handleEvent() throws Exception {
        if (!isAutoConfigurationEnabled) {
            consulServiceRegistry.register(consulRegistration());
        }
    }

    private ConsulRegistration consulRegistration() {
        NewService newService = new NewService();
        newService.setId(consulDiscoveryProperties.getHostname() + ":" + serverPort);
        newService.setName(serviceName);
        List<String> serviceTags = new ArrayList<>();
        serviceTags.add("secure=false");
        newService.setTags(serviceTags);
        newService.setAddress(consulDiscoveryProperties.getHostname());
        newService.setPort(serverPort);
        NewService.Check check = new NewService.Check();
        check.setInterval(consulHealthCheckInterval);
        check.setHttp("http://" + consulDiscoveryProperties.getHostname() + ":" + serverPort + consulHealthCheckPath);
        check.setTimeout(consulHealthCheckTimeout);
        newService.setCheck(check);
        consulDiscoveryProperties.setInstanceId(newService.getId());
        return new ConsulRegistration(newService, consulDiscoveryProperties);
    }
}
