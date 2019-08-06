package com.worksvn.student_service.events_handle;

public interface ApplicationEventHandle {
    void handleEvent() throws Exception;
    String startMessage();
    String successMessage();
}
