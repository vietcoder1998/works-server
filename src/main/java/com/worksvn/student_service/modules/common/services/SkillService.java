package com.worksvn.student_service.modules.common.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.modules.common.responses.SkillDto;
import com.worksvn.common.modules.common.services.BaseSkillService;
import com.worksvn.student_service.components.communication.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SkillService extends BaseSkillService {
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Override
    public PageDto<SkillDto> querySkillDtos(List<String> sortBy, List<String> sortType,
                                            Integer pageIndex, Integer pageSize,
                                            Set<Integer> ids) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_querySkills(ids, sortBy, sortType, pageIndex, pageSize));
    }

    public Set<Integer> getExistIDs(Set<Integer> ids) throws Exception {
        return restCommunicator.exchangeForSuccess(APIs.PUBLIC_getExistSkillIDs(ids));
    }
}
