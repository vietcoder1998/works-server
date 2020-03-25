package com.worksvn.student_service.modules.employer.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployerService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public void checkEmployerExist(String employerID) throws Exception {
        restCommunicator.exchangeForSuccess(APIs.EMPLOYER_checkEmployerExists(employerID));
    }
}
