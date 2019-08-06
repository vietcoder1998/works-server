package com.worksvn.student_service.modules.oauth2.repositories;

import com.worksvn.student_service.modules.oauth2.models.entities.ResourceServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceServerRepository extends JpaRepository<ResourceServer, String> {
}
