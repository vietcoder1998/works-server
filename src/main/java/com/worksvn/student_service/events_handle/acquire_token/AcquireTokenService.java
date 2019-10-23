package com.worksvn.student_service.events_handle.acquire_token;

import com.worksvn.common.annotations.event.EventHandler;
import com.worksvn.common.components.communication.AcquireNewTokenService;
import com.worksvn.student_service.events_handle.ApplicationEvent;
import com.worksvn.student_service.events_handle.ApplicationEventHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP)
public class AcquireTokenService implements ApplicationEventHandle {

    @Autowired
    private AcquireNewTokenService acquireNewTokenService;

    @Override
    public String startMessage() {
        return "Start acquiring access token...";
    }

    @Override
    public String successMessage() {
        return "Acquiring access token...OK";
    }

    @Override
    public void handleEvent() throws Exception {
        acquireNewTokenService.acquireNewToken();
    }
}
