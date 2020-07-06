package com.worksvn.student_service.modules.common.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.WorkingToolDto;
import com.worksvn.common.modules.common.services.BaseWorkingToolService;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WorkingToolService extends BaseWorkingToolService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Override
    public PageDto<WorkingToolDto> queryWorkingToolDtos(List<String> sortBy, List<String> sortType,
                                                        Integer pageIndex, Integer pageSize,
                                                        Set<Integer> ids) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_queryWorkingTools(ids, sortBy, sortType, pageIndex, pageSize));
    }

    public Set<Integer> getExistIDs(Set<Integer> ids) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getExistWorkingToolIDs(ids));
    }
}
