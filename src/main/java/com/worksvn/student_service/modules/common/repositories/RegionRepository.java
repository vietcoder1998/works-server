package com.worksvn.student_service.modules.common.repositories;

import com.worksvn.student_service.modules.common.models.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Region findFirstByName(String name);
}
