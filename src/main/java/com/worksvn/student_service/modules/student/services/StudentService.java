package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.constants.StringConstants;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.auth.requests.UserFilter;
import com.worksvn.common.modules.auth.responses.UserDto;
import com.worksvn.common.modules.common.responses.*;
import com.worksvn.common.modules.employer.responses.UserSimpleInfo;
import com.worksvn.common.modules.student.requests.UpdateStudentInfoDto;
import com.worksvn.common.modules.student.requests.StudentFilterDto;
import com.worksvn.common.modules.student.responses.*;
import com.worksvn.common.services.file_storage.FileStorageService;
import com.worksvn.common.services.internal_service.DistributedDataService;
import com.worksvn.common.services.notification.NotificationFactory;
import com.worksvn.common.services.notification.NotificationService;
import com.worksvn.common.services.notification.models.NotificationGroup;
import com.worksvn.common.utils.core.DateTimeUtils;
import com.worksvn.common.utils.core.FileChecker;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.constants.NumberConstants;
import com.worksvn.student_service.modules.auth.services.UserService;
import com.worksvn.student_service.modules.common.services.MajorService;
import com.worksvn.student_service.modules.services.geocoding.LocationService;
import com.worksvn.student_service.modules.student.models.entities.*;
import com.worksvn.student_service.modules.student.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    public static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private JPAQueryExecutor queryExecutor;

    @Autowired
    private UserService userService;
    @Autowired
    private DistributedDataService distributedDataService;
    @Autowired
    private StudentAverageRatingService studentAverageRatingService;
    @Autowired
    private StudentSkillService studentSkillService;
    @Autowired
    private StudentExperienceService studentExperienceService;
    @Autowired
    private StudentLanguageSkillService studentLanguageSkillService;
    @Autowired
    private StudentUnlockService studentUnlockService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private FileStorageService fileStorageService;
//    @Autowired
//    private MajorService majorService;
    @Autowired
    private NotificationService notificationService;

    @Value("${application.firebase.file-storage.student-dir.name:students/}")
    private String studentStorageDirectory;

    public PageDto<StudentPreview> getStudentPreviews(List<String> sortType, List<String> sortBy,
                                                      int pageIndex, int pageSize,
                                                      StudentFilterDto filter) throws Exception {
        if (filter == null) {
            filter = new StudentFilterDto();
        }

        if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
            UserFilter userFilter = new UserFilter();
            userFilter.setUsername(filter.getUsername());
            PageDto<UserDto> pageUsers = userService.queryUsers(userFilter, null, null, pageIndex, pageSize);
            if (pageUsers != null && pageUsers.getItems() != null && !pageUsers.getItems().isEmpty()) {
                Set<String> userIDs = pageUsers.getItems().stream()
                        .map(UserDto::getId)
                        .collect(Collectors.toSet());
                filter.setIds(userIDs);
            } else {
                return new PageDto<>();
            }
        }

        JPAQueryBuilder<StudentPreview> queryBuilder = new JPAQueryBuilder<>();

        JPAQueryBuilder<StudentPreview>.Condition whereCondition = queryBuilder.newCondition();

        String unlockedID = "0";
        if (filter.getEmployerID() != null) {
            queryBuilder.joinOn(JPAQueryBuilder.JoinType.LEFT_JOIN, StudentUnlocked.class, "su",
                    queryBuilder.newCondition()
                            .condition("su.student.id", "=", "s.id")
                            .and()
                            .paramCondition("su.employerID", "=", filter.getEmployerID()));
            unlockedID = "su.id";

            if (filter.getUnlocked() != null) {
                whereCondition.and().nullCondition("su.id", !filter.getUnlocked());
            }
        }

        queryBuilder.selectAsObject(StudentPreview.class,
                "s.id", "s.firstName", "s.lastName",
                "s.birthday", "s.avatarUrl",
                "s.email", "s.phone", "s.gender",
                "s.regionID", "s.address", "s.lat", "s.lon",
                "s.profileVerified", "s.lookingForJob", "s.completePercent",
                "sar.attitudeRating", "sar.skillRating",
                "sar.jobAccomplishmentRating", "sar.ratingCount",
                unlockedID, "s.schoolID", "s.majorID",
                "s.schoolYearStart", "s.schoolYearEnd",
                "s.studentCode", "s.createdDate")
                .from(Student.class, "s")
                .joinOn(JPAQueryBuilder.JoinType.LEFT_JOIN, StudentAverageRating.class, "sar",
                        queryBuilder.newCondition().condition("sar.studentID", "=", "s.id"));

        boolean groupByID = false;
        if (filter.getSchoolID() != null && !filter.getSchoolID().isEmpty()) {
            whereCondition.and().paramCondition("s.schoolID", "=", filter.getSchoolID());
        }
        if (filter.getRegionID() != null) {
            whereCondition.and().paramCondition("s.regionID", "=", filter.getRegionID());
        }
        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            whereCondition.and().paramCondition("s.email", "=", filter.getEmail());
        }
        if (filter.getPhone() != null && !filter.getPhone().isEmpty()) {
            whereCondition.and().paramCondition("s.phone", "=", filter.getPhone());
        }
        if (filter.getGender() != null) {
            whereCondition.and().paramCondition("s.gender", "=", filter.getGender());
        }
        if (filter.getLookingForJob() != null) {
            whereCondition.and().paramCondition("s.lookingForJob", "=", filter.getLookingForJob());
        }
        if (filter.getProfileVerified() != null) {
            whereCondition.and().paramCondition("s.profileVerified", "=", filter.getProfileVerified());
        }
        Integer startYear = filter.getBirthYearStart();
        Integer endYear = filter.getBirthYearEnd();
        if (startYear != null || endYear != null) {
            startYear = (startYear == null ? 0 : startYear) - 1900;
            if (startYear > 0) {
                whereCondition.and().paramCondition("s.birthday", ">=", new Date(startYear, 0, 1));
            }
            endYear = (endYear == null ? 0 : endYear) - 1900;
            if (endYear > 0) {
                whereCondition.and().paramCondition("s.birthday", "<", new Date(endYear, 0, 1));
            }
        }
        if (filter.getSchoolYearStart() != null && filter.getSchoolYearStart() > 0) {
            whereCondition.and().paramCondition("s.schoolYearStart", "=", filter.getSchoolYearStart());
        }
        if (filter.getSchoolYearEnd() != null && filter.getSchoolYearEnd() > 0) {
            whereCondition.and().paramCondition("s.schoolYearEnd", "=", filter.getSchoolYearEnd());
        }
        if (filter.getStudentCode() != null && !filter.getStudentCode().isEmpty()) {
            whereCondition.and().paramCondition("s.studentCode", "=", filter.getStudentCode());
        }
        if (filter.getSkillIDs() != null && !filter.getSkillIDs().isEmpty()) {
            queryBuilder.join(JPAQueryBuilder.JoinType.LEFT_JOIN, "s.skills", "sk");
            whereCondition.and().paramCondition("sk.SkillID", "IN", filter.getSkillIDs());
            groupByID = true;
        }
        if (filter.getMajorIDs() != null && !filter.getMajorIDs().isEmpty()) {
            whereCondition.and().paramCondition("s.majorID", "IN", filter.getMajorIDs());
        }
        if (filter.getIds() != null && !filter.getIds().isEmpty() ) {
            whereCondition.and().paramCondition("s.id", "IN", filter.getIds());
        }
        if (filter.getCreatedDate() != null && filter.getCreatedDate() > 0) {
            Date startDate = DateTimeUtils.extractDateOnly(filter.getCreatedDate());
            Date endDate = DateTimeUtils.addDayToDate(startDate, 1);
            whereCondition.and().paramCondition("s.createdDate", ">=", startDate)
                    .and()
                    .paramCondition("s.createdDate", "<", endDate);
        }

        if (groupByID) {
            queryBuilder.groupBy("s.id");
        }
        queryBuilder.where(whereCondition)
                .setCountQuerySelect(JPAQueryBuilder.distinct("s.id"))
                .orderBy(sortBy, sortType)
                .setPagination(pageIndex, pageSize, NumberConstants.MAX_PAGE_SIZE);

        PageDto<StudentPreview> result = queryExecutor.executePaginationQuery(queryBuilder);
        distributedDataService.completeCollection(result.getItems(), null);
        return result;
    }

    public StudentProfileDto getStudentProfile(String id, String schoolID, String employerID) throws Exception {
        Student s;
        if (schoolID == null) {
            s = getStudent(id);
        } else {
            s = getSchoolStudent(id, schoolID);
        }

        StudentAverageRatingDto car = studentAverageRatingService.getStudentAverageRatingDto(id);

        List<SkillDto> sks = studentSkillService.getStudentSkillDtos(id, null, null)
                .getItems();

        List<StudentLanguageSkillDto> lks = studentLanguageSkillService
                .getStudentLanguageSkillDtos(id).getItems();

        List<StudentExperienceDto> exps = studentExperienceService
                .getStudentExperienceDtos(id,
                        Arrays.asList("se." + StudentExperience.STARTED_DATE, "se." + StudentExperience.FINISHED_DATE),
                        Arrays.asList("desc", "asc"))
                .getItems();

        Boolean unlocked = null;
        if (employerID != null) {
            unlocked = studentUnlockService.checkStudentUnlockedByEmployer(id, employerID);
        }

        StudentProfileDto profileDto = new StudentProfileDto(s.getId(), s.getFirstName(), s.getLastName(),
                s.getBirthday(), s.getAvatarUrl(),
                s.getEmail(), s.getPhone(), s.getGender(),
                s.getRegionID(), s.getAddress(), s.getLat(), s.getLon(),
                s.getProfileVerified(), s.getLookingForJob(), s.getCompletePercent(),
                car, unlocked, s.getSchoolID(), s.getMajorID(), s.getSchoolYearStart(), s.getSchoolYearEnd(),
                s.getStudentCode(), s.getCreatedDate(),
                s.getCoverUrl(), s.getDescription(), s.getIdentityCard(),
                s.getIdentityCardFrontImageUrl(), s.getIdentityCardBackImageUrl(),
                sks, lks, exps);

        distributedDataService.complete(profileDto, null);

        return profileDto;
    }

    public StudentInfoDto getStudentInfo(String studentID) throws Exception {
        StudentInfoDto studentInfoDto = studentRepository.getStudentInfo(studentID);
        if (studentInfoDto == null) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
        distributedDataService.complete(studentInfoDto, null);
        return studentInfoDto;
    }

    public StudentNavigationDto getStudentNavigation(String studentID) throws Exception {
        StudentNavigationDto studentNavigationDto = studentRepository.getStudentNavigationDto(studentID);
        if (studentNavigationDto == null) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
        distributedDataService.complete(studentNavigationDto, null);
        return studentNavigationDto;
    }

    public UserSimpleInfo getSimpleInfo(String studentID) throws ResponseException {
        UserSimpleInfo info = studentRepository.getSimpleInfo(studentID);
        if (info == null) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
        return info;
    }

    public LatLon getLatLon(String studentID) {
        return studentRepository.getLatLon(studentID);
    }

    public StudentQueryActiveJobInfo getQueryActiveJobInfo(String studentID) {
        return studentRepository.getQueryActiveJobInfo(studentID);
    }

    //    @Transactional(rollbackFor = Exception.class)
    public void updateStudentInfo(String studentID, UpdateStudentInfoDto updateInfo,
                                  MultipartFile avatarImage, MultipartFile coverImage) throws Exception {
        Student student = getStudent(studentID);

        RegionAddress regionAddress = null;
        if (updateInfo.getLat() != null && updateInfo.getLon() != null) {
            regionAddress = locationService.getRegionAddress(updateInfo.getLat(), updateInfo.getLon());
        }

        student.update(updateInfo, regionAddress);
        studentRepository.save(student);

        if (avatarImage != null) {
            String avatarUrl = uploadStudentAvatar(studentID, avatarImage, student.getAvatarUrl());
            if (avatarUrl != null) {
                student.setAvatarUrl(avatarUrl);
            }
        }

        if (coverImage != null) {
            String coverUrl = uploadStudentCover(studentID, coverImage, student.getCoverUrl());
            if (coverUrl != null) {
                student.setCoverUrl(coverUrl);
            }
        }

//        firestoreService.publishFirestoreTask(ChatRoomTaskFactory
//                .updateCandidateInfo(candidateID, candidate.getAvatarUrl(),
//                        candidate.getFirstName(), candidate.getLastName()));
    }

    public AvatarUrlDto updateStudentAvatar(String studentID, MultipartFile imageFile)
            throws ResponseException, IOException {
        checkStudentExist(studentID);
        String oldAvatarUrl = studentRepository.getStudentAvatarUrl(studentID);
        String newAvatarUrl = uploadStudentAvatar(studentID, imageFile, oldAvatarUrl);
        studentRepository.updateStudentAvatar(studentID, newAvatarUrl);
        return new AvatarUrlDto(newAvatarUrl);
    }

    private String uploadStudentAvatar(String studentID, MultipartFile imageFile,
                                       String oldAvatarUrl) throws IOException, ResponseException {
        String newAvatarUrl = uploadImageFile(imageFile, studentStorageDirectory + studentID,
                StringConstants.AVATAR_IMAGE_NAME + "_" + new Date().getTime());
        try {
            fileStorageService.deleteFileByUrl(oldAvatarUrl);
        } catch (Exception e) {
            logger.error("[File Storage Service] Delete file(s) failed: " + oldAvatarUrl, e);
        }
        return newAvatarUrl;
    }

    public CoverUrlDto updateStudentCover(String studentID, MultipartFile imageFile)
            throws IOException, ResponseException {
        checkStudentExist(studentID);
        String oldCoverUrl = studentRepository.getStudentAvatarUrl(studentID);
        String coverUrl = uploadStudentCover(studentID, imageFile, oldCoverUrl);
        studentRepository.updateStudentCover(studentID, coverUrl);
        return new CoverUrlDto(coverUrl);
    }

    private String uploadStudentCover(String studentID, MultipartFile imageFile,
                                    String oldCoverUrl) throws IOException, ResponseException {
        String coverUrl = uploadImageFile(imageFile, studentStorageDirectory + studentID,
                StringConstants.COVER_IMAGE_NAME + "_" + new Date().getTime());
        try {
            fileStorageService.deleteFileByUrl(oldCoverUrl);
        } catch (Exception e) {
            logger.error("[File Storage Service] Delete file(s) failed: " + oldCoverUrl, e);
        }
        return coverUrl;
    }

    public IdentityCardImageDto updateStudentIdentityCardImageUrl(String studentID,
                                                                  MultipartFile frontImage, MultipartFile backImage)
            throws IOException, ResponseException {
        checkStudentExist(studentID);
        IdentityCardImageDto cardImageDto = studentRepository.getStudentIdentityCardUrls(studentID);

        boolean newFront = true;
        String newFrontUrl = uploadImageFile(frontImage, studentStorageDirectory + studentID,
                StringConstants.ID_CARD_FRONT_IMAGE_NAME + "_" + new Date().getTime());
        if (newFrontUrl == null) {
            newFront = false;
            newFrontUrl = cardImageDto.getIdentityCardFrontImageUrl();
        }

        boolean newBack = true;
        String newBackUrl = uploadImageFile(backImage, studentStorageDirectory + studentID,
                StringConstants.ID_CARD_BACK_IMAGE_NAME + "_" + new Date().getTime());
        if (newBackUrl == null) {
            newBack = false;
            newBackUrl = cardImageDto.getIdentityCardBackImageUrl();
        }

        studentRepository.updateStudentIdentityCardUrls(studentID, newFrontUrl, newBackUrl);

        try {
            FileStorageService.FileStorageBatch batch = fileStorageService.newBatch();
            if (newFront && cardImageDto.getIdentityCardFrontImageUrl() != null) {
                batch.deleteFileByUrl(cardImageDto.getIdentityCardFrontImageUrl());
            }

            if (newBack && cardImageDto.getIdentityCardBackImageUrl() != null) {
                batch.deleteFileByUrl(cardImageDto.getIdentityCardBackImageUrl());
            }
            batch.submit();
        } catch (Exception e) {
            logger.error("[File Storage Service] Delete file(s) failed: [" +
                    cardImageDto.getIdentityCardFrontImageUrl() + "," +
                    cardImageDto.getIdentityCardBackImageUrl() + "]", e);
        }

        cardImageDto.setIdentityCardFrontImageUrl(newFrontUrl);
        cardImageDto.setIdentityCardBackImageUrl(newBackUrl);
        return cardImageDto;
    }

    private String uploadImageFile(MultipartFile imageFile, String dir, String fileName) throws IOException, ResponseException {
        String url = null;
        if (imageFile != null) {
            String contentType = imageFile.getContentType();
            if (!FileChecker.checkType(imageFile, FileChecker.IMAGE_TYPE, null)) {
                throw new ResponseException(ResponseValue.CONTENT_TYPE_NOT_ALLOWED);
            }
            url = fileStorageService.uploadFile(dir, fileName,
                    imageFile.getBytes(), contentType);
        }
        return url;
    }

    public void updateStudentLookingForJob(String studentID, boolean lookingForJob) throws ResponseException {
        checkStudentExist(studentID);
        studentRepository.updateStudentLookingForJob(studentID, lookingForJob);
    }

    public void updateStudentDescription(String studentID, String description) throws ResponseException {
        checkStudentExist(studentID);
        studentRepository.updateStudentDescription(studentID, description);
    }

    public StudentJobApplyInfo getStudentApplyInfo(String studentID) throws ResponseException {
        StudentJobApplyInfo applyInfo = studentRepository.getStudentJobApplyInfo(studentID);
        if (applyInfo == null) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
        return applyInfo;
    }

    public StudentJobOfferInfo getStudentOfferInfo(String studentID) throws ResponseException {
        StudentJobOfferInfo offerInfo = studentRepository.getStudentJobOfferInfo(studentID);
        if (offerInfo == null) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
        return offerInfo;
    }


    public void checkStudentExist(String studentID) throws ResponseException {
        if (!studentRepository.existsById(studentID)) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
    }

    public void checkSchoolStudentExist(String schoolID, String studentID) throws ResponseException {
        if (!studentRepository.existsByIdAndSchoolID(studentID, schoolID)) {
            throw new ResponseException(ResponseValue.SCHOOL_STUDENT_NOT_FOUND);
        }
    }

    public Student getSchoolStudent(String studentID, String schoolID) throws ResponseException {
        Student student = studentRepository.findFirstByIdAndSchoolID(studentID, schoolID);
        if (student == null) {
            throw new ResponseException(ResponseValue.SCHOOL_STUDENT_NOT_FOUND);
        }
        return student;
    }

    public Student getStudent(String studentID) throws ResponseException {
        Student student = studentRepository.findFirstById(studentID);
        if (student == null) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
        return student;
    }

    public String getStudentSchoolID(String studentID) throws ResponseException {
        String schoolID = studentRepository.getSchoolID(studentID);
        if (schoolID == null) {
            throw new ResponseException(ResponseValue.STUDENT_SCHOOL_NOT_FOUND);
        }
        return schoolID;
    }

    public void checkStudentExists(String studentID) throws ResponseException {
        if (!studentRepository.existsById(studentID)) {
            throw new ResponseException(ResponseValue.STUDENT_NOT_FOUND);
        }
    }

    public void verifyProfile(String studentID, boolean verified) throws ResponseException {
        checkStudentExist(studentID);
        studentRepository.updateStudentProfileVerified(studentID, verified);
        notificationService.publishNotification(NotificationFactory
                .USER_profileVerifiedChange(NotificationGroup.STUDENT, studentID, verified));
    }

    public void deleteList(Set<String> userIDs) {
        if (userIDs != null && !userIDs.isEmpty()) {
            studentRepository.deleteList(userIDs);
        }
    }
}
