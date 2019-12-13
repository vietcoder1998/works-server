package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.modules.common.enums.Gender;
import com.worksvn.common.modules.common.responses.RegionAddress;
import com.worksvn.common.modules.student.requests.NewStudentInfoDto;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Student {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "school_id")
    private String schoolID;
    @Column(name = "major_id")
    private int majorID;
    @Column(name = "region_id")
    private Integer regionID;
    @Column(name = "student_code")
    private String studentCode;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "identity_card")
    private String identityCard;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "cover_url")
    private String coverUrl;
    @Column(name = "description")
    private String description;
    @Column(name = "identity_card_front_image_url")
    private String identityCardFrontImageUrl;
    @Column(name = "identity_card_back_image_url")
    private String identityCardBackImageUrl;
    @Column(name = "is_profile_verified")
    private Boolean profileVerified = false;
    @Column(name = "is_looking_for_job")
    private Boolean lookingForJob = true;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;
    @Column(name = "complete_percent")
    private int completePercent;
    @Column(name = "created_date")
    private Date createdDate = new Date();

    public Student(String id, String schoolID, NewStudentRegistrationDto registrationDto) {
        this.id = id;
        this.schoolID = schoolID;
        this.firstName = registrationDto.getFirstName();
        this.lastName = registrationDto.getLastName();
        this.email = registrationDto.getEmail();
        this.phone = registrationDto.getPhone();
        calculateCompletePercent();
    }

    public void update(NewStudentInfoDto updateInfo,
                       RegionAddress regionAddress) {
        this.firstName = updateInfo.getFirstName();
        this.lastName = updateInfo.getLastName();
        this.gender = Gender.valueOf(updateInfo.getGender());
        this.email = updateInfo.getEmail();
        this.phone = updateInfo.getPhone();
        if (updateInfo.getBirthday() > 0) {
            this.birthday = new Date(updateInfo.getBirthday());
        }
        this.identityCard = updateInfo.getIdentityCard();
        if (regionAddress != null) {
            this.lat = regionAddress.getLat();
            this.lon = regionAddress.getLon();
            this.address = regionAddress.getAddress();
            if (regionAddress.getRegion() != null) {
                this.regionID = regionAddress.getRegion().getId();
            }
        }
        this.studentCode = updateInfo.getStudentCode();
        this.majorID = updateInfo.getMajorID();
        calculateCompletePercent();
    }

    public void calculateCompletePercent() {
        int totalCompletedField = (firstName == null ? 0 : 1) +
                (lastName == null ? 0 : 1) +
                (email == null ? 0 : 1) +
                (gender == null ? 0 : 1) +
                (identityCard == null ? 0 : 1) +
                (phone == null ? 0 : 1) +
                ((lat == null || lon == null) ? 0 : 1) +
                (birthday == null ? 0 : 1) +
                (avatarUrl == null ? 0 : 1) +
                (description == null ? 0 : 1);
        this.completePercent = (int) Math.round(totalCompletedField / 10.0 * 100.0);
    }
}
