package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.common.modules.candidate.responses.IdentityCardImageDto;
import com.worksvn.common.modules.student.responses.StudentInfoDto;
import com.worksvn.common.modules.student.responses.StudentNavigationDto;
import com.worksvn.student_service.modules.student.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StudentRepository extends JpaRepository<Student, String> {

    Student findFirstById(String id);

    Student findFirstByIdAndSchoolID(String id, String schoolID);

    boolean existsByIdAndSchoolID(String id, String schoolID);

    @Query("select new com.worksvn.common.modules.student.responses.StudentInfoDto" +
            "(s.id, s.firstName, s.lastName, s.gender, s.email, s.phone, s.address," +
            "s.birthday, s.avatarUrl, s.coverUrl, s.description, s.identityCard," +
            "sar.attitudeRating, sar.skillRating, sar.jobAccomplishmentRating, sar.ratingCount," +
            "s.regionID, s.lat, s.lon," +
            "s.identityCardFrontImageUrl, s.identityCardBackImageUrl," +
            "s.studentCode, s.majorID) " +
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

    @Query("select new com.worksvn.common.modules.candidate.responses.IdentityCardImageDto" +
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
    @Query("update Student s set s.isLookingForJob = ?2 where s.id = ?1")
    void updateStudentLookingForJob(String studentID, boolean isLookingForJob);

    @Modifying
    @Transactional
    @Query("update Student s set s.description = ?2 where s.id = ?1")
    void updateStudentDescription(String studentID, String description);
}
