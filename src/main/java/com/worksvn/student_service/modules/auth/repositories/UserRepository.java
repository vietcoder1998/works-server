package com.worksvn.student_service.modules.auth.repositories;

import com.worksvn.student_service.modules.auth.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u " +
            "where u.username = ?1")
    User getUserFetchRole(String username);

    boolean existsByUsername(String username);

    @Query("select u.isBanned from User u where u.id = ?1")
    boolean isUserBanned(String userID);
}
