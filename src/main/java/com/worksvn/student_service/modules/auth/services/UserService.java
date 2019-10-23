package com.worksvn.student_service.modules.auth.services;

import com.worksvn.common.components.communication.CommonAPIs;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.modules.auth.responses.UserIDDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private ISRestCommunicator isRestCommunicator;

    public String registerNewUserByUsername(String username, String password) throws Exception {
        String userID;
        ISRestCommunicator.ExchangeResult<UserIDDto> result = isRestCommunicator
                .exchange(CommonAPIs.AUTH_registerNewUserByUsername(username, password));
        if (result.isSuccess()) {
            userID = result.getConvertedBody().getData().getUserID();
        } else if (result.getOriginBody().getCode() == ResponseValue.USERNAME_EXISTS.specialCode()) {
            userID = result.getOriginBodyData(UserIDDto.class).getUserID();
        } else {
            throw new ISResponseException(result);
        }
        return userID;
    }
}
