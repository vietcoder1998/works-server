package com.worksvn.student_service.modules.auth.services;

import com.worksvn.common.components.communication.CommonAPIs;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.modules.auth.requests.ResetPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountSecurityService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public void resetUserPassword(String userID, ResetPasswordDto resetPasswordDto) throws Exception {
        ISRestCommunicator.ExchangeResult result = restCommunicator
                .exchange(CommonAPIs.AUTH_resetUserPassword(userID, resetPasswordDto));
        if (!result.isSuccess()) {
            throw new ISResponseException(result);
        }
    }
}
