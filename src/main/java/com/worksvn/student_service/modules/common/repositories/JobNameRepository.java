package com.worksvn.student_service.modules.common.repositories;

import com.worksvn.student_service.modules.common.models.entities.JobName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobNameRepository extends JpaRepository<JobName, Integer> {
    JobName findFirstById(Integer id);
}
