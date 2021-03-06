package com.worksvn.student_service.modules.auth.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.CommonAPIs;
import com.worksvn.common.components.communication.ISApi;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.modules.auth.requests.UserFilter;
import com.worksvn.common.modules.auth.responses.UserDto;
import com.worksvn.common.modules.auth.responses.UserIDDto;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    public String registerNewUserByEmail(String email, String password, boolean isActivated) throws Exception {
        return registerUser(CommonAPIs.AUTH_registerNewUserByEmail(email, password, isActivated));
    }

    public String registerNewUserByFacebook(String fbAccessToken) throws Exception {
        return registerUser(CommonAPIs.AUTH_registerNewUserByFacebook(fbAccessToken));
    }

    public String registerNewUserByUsername(String username, String password, String email,
                                            boolean activated) throws Exception {
        return registerUser(CommonAPIs.AUTH_registerNewUserByUsername(username, password, email, activated));
    }

    private String registerUser(ISApi<?, UserIDDto> api) throws Exception {
        String userID;
        ISRestCommunicator.ExchangeResult<UserIDDto> result = restCommunicator.exchange(api);
        if (result.isSuccess()) {
            userID = result.getConvertedBody().getData().getUserID();
        } else if (result.getOriginBody().getCode() == ResponseValue.USERNAME_EXISTS.specialCode()) {
            userID = result.getOriginBodyData(UserIDDto.class).getUserID();
        } else {
            throw new ISResponseException(result);
        }
        return userID;
    }

    public PageDto<UserDto> queryUsers(UserFilter filter,
                                       List<String> sortBy, List<String> sortType,
                                       Integer pageIndex, Integer pageSize) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.AUTH_queryUsers(filter, sortBy, sortType, pageIndex, pageSize));
    }
}
