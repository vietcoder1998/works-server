package com.worksvn.student_service.events_handle.resource_server_registration;

import com.worksvn.common.annotations.event.EventHandler;
import com.worksvn.common.components.communication.CommonAPIs;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.student_service.events_handle.ApplicationEvent;
import com.worksvn.student_service.events_handle.ApplicationEventHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP)
public class ResourceServerPersistenceService implements ApplicationEventHandle {
    private static Logger logger = LoggerFactory.getLogger(ResourceServerPersistenceService.class);

    @Value("${application.oauth2.resource-server.id}")
    private String thisResourceID;

    @Autowired
    private ISRestCommunicator isRestCommunicator;

    @Override
    public String startMessage() {
        return "Start registering this resource server...";
    }

    @Override
    public String successMessage() {
        return "registration this resource server...OK";
    }

    @Override
    public void handleEvent() throws Exception {
        ISRestCommunicator.ExchangeResult result = isRestCommunicator
                .exchange(CommonAPIs.AUTH_registerResourceServer(thisResourceID));
        if (result.isSuccess()) {
            logger.info("This resource server [" + thisResourceID + "] registered");
        } else {
            logger.info("This resource server [" + thisResourceID + "] exists");
        }
    }
}
