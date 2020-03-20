package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.common.modules.common.responses.IdentityCardImageDto;
import com.worksvn.common.modules.common.responses.LatLon;
import com.worksvn.common.modules.employer.responses.UserSimpleInfo;
import com.worksvn.common.modules.student.responses.*;
import com.worksvn.student_service.modules.student.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, String> {

    Student findFirstById(String id);

    Student findFirstByIdAndSchoolID(String id, String schoolID);

    boolean existsByIdAndSchoolID(String id, String schoolID);

    @Query("select s.schoolID from Student s where s.id = ?1")
    String getSchoolID(String id);

    @Query("select new com.worksvn.common.modules.student.responses.StudentInfoDto" +
            "(s.id, s.firstName, s.lastName, " +
            "s.gender, s.birthday, s.identityCard, " +
            "s.email, s.phone, " +
            "s.address, s.regionID, s.lat, s.lon, " +
            "s.studentCode, " +
            "s.avatarUrl, s.coverUrl, s.description, " +
            "sar.attitudeRating, sar.skillRating, sar.jobAccomplishmentRating, sar.ratingCount, " +
            "s.identityCardFrontImageUrl, s.identityCardBackImageUrl, " +
            "s.majorID) " +
            "from Student s " +
            "left join StudentAverageRating sar on sar.studentID = s.id " +
            "where s.id = ?1")
    StudentInfoDto getStudentInfo(String studentID);

    @Query("select new com.worksvn.common.modules.student.responses.StudentNavigationDto" +
            "(s.id, s.avatarUrl, s.coverUrl, " +
            "s.regionID, s.firstName, s.lastName) " +
            "from Student s " +
            "where s.id = ?1")
    StudentNavigationDto getStudentNavigationDto(String studentID);

    @Query("select new com.worksvn.common.modules.employer.responses.UserSimpleInfo" +
            "(s.id, s.firstName, s.lastName,s.avatarUrl, s.gender) " +
            "from Student s " +
            "where s.id = ?1")
    UserSimpleInfo getSimpleInfo(String id);

    @Query("select new com.worksvn.common.modules.common.responses.LatLon" +
            "(s.lat, s.lon)" +
            "from Student s " +
            "where s.id = ?1")
    LatLon getLatLon(String id);

    @Query("select new com.worksvn.common.modules.student.responses.StudentQueryActiveJobInfo" +
            "(s.schoolID, s.majorID, s.lat, s.lon) " +
            "from Student s where s.id = ?1")
    StudentQueryActiveJobInfo getQueryActiveJobInfo(String id);

    @Query("select s.avatarUrl from Student s where s.id = ?1")
    String getStudentAvatarUrl(String studentID);

    @Modifying
    @Transactional
    @Query("update Student s set s.avatarUrl = ?2 where s.id = ?1")
    void updateStudentAvatar(String studentID, String avatarUrl);

    @Query("select s.coverUrl from Student s where s.id = ?1")
    String getStudentCoverUrl(String studentID);

    @Modifying
    @Transactional
    @Query("update Student s set s.coverUrl = ?2 where s.id = ?1")
    void updateStudentCover(String studentID, String coverUrl);

    @Query("select new  com.worksvn.common.modules.common.responses.IdentityCardImageDto" +
            "(s.identityCardFrontImageUrl, s.identityCardBackImageUrl) " +
            "from Student s " +
            "where s.id = ?1")
    IdentityCardImageDto getStudentIdentityCardUrls(String studentID);

    @Modifying
    @Transactional
    @Query("update Student s " +
            "set s.identityCardFrontImageUrl = ?2, s.identityCardBackImageUrl = ?3 " +
            "where s.id = ?1")
    void updateStudentIdentityCardUrls(String studentID, String frontUrl, String backUrl);

    @Modifying
    @Transactional
    @Query("update Student s set s.lookingForJob = ?2 where s.id = ?1")
    void updateStudentLookingForJob(String studentID, boolean lookingForJob);

    @Modifying
    @Transactional
    @Query("update Student s set s.description = ?2 where s.id = ?1")
    void updateStudentDescription(String studentID, String description);

    @Modifying
    @Transactional
    @Query("delete from Student s where s.id in ?1")
    void deleteList(Set<String> userIDs);

    @Query("select new com.worksvn.common.modules.student.responses.StudentJobApplyInfo" +
            "(s.firstName, s.lastName, s.gender, s.avatarUrl) " +
            "from Student s " +
            "where s.id = ?1")
    StudentJobApplyInfo getStudentJobApplyInfo(String studentID);

    @Query("select new com.worksvn.common.modules.student.responses.StudentJobOfferInfo" +
            "(s.firstName, s.lastName, s.avatarUrl) " +
            "from Student s " +
            "where s.id = ?1")
    StudentJobOfferInfo getStudentJobOfferInfo(String studentID);

    @Modifying
    @Transactional
    @Query("update Student s set s.profileVerified = ?2 " +
            "where s.id = ?1")
    void updateStudentProfileVerified(String studentID, boolean verified);
}
