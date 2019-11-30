package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.constants.StringConstants;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.common.responses.*;
import com.worksvn.common.modules.employer.responses.UserSimpleInfo;
import com.worksvn.common.modules.student.requests.NewStudentInfoDto;
import com.worksvn.common.modules.student.requests.StudentFilterDto;
import com.worksvn.common.modules.student.responses.*;
import com.worksvn.common.services.file_storage.FileStorageService;
import com.worksvn.common.services.internal_service.DistributedDataService;
import com.worksvn.common.utils.core.FileChecker;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.constants.NumberConstants;
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

@Service
public class StudentService {
    public static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private JPAQueryExecutor queryExecutor;

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
    @Autowired
    private MajorService majorService;

    @Value("${application.firebase.file-storage.student-dir.name:students/}")
    private String studentStorageDirectory;

    public PageDto<StudentPreview> getStudentPreviews(List<String> sortType, List<String> sortBy,
                                                      int pageIndex, int pageSize,
                                                      StudentFilterDto filterDto) throws Exception {
        if (filterDto == null) {
            filterDto = new StudentFilterDto();
        }

        JPAQueryBuilder<StudentPreview> queryBuilder = new JPAQueryBuilder<>();

        JPAQueryBuilder<StudentPreview>.Condition whereCondition = queryBuilder.newCondition();

        String unlockedID = "0";
        if (filterDto.getEmployerID() != null) {
            queryBuilder.joinOn(JPAQueryBuilder.JoinType.LEFT_JOIN, StudentUnlocked.class, "su",
                    queryBuilder.newCondition()
                            .condition("su.student.id", "=", "s.id")
                            .and()
                            .paramCondition("su.employerID", "=", filterDto.getEmployerID()));
            unlockedID = "su.id";

            if (filterDto.getUnlocked() != null) {
                whereCondition.and().nullCondition("su.id", !filterDto.getUnlocked());
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
                unlockedID, "s.schoolID", "s.majorID", "s.studentCode")
                .from(Student.class, "s")
                .joinOn(JPAQueryBuilder.JoinType.LEFT_JOIN, StudentAverageRating.class, "sar",
                        queryBuilder.newCondition().condition("sar.studentID", "=", "s.id"));

        boolean groupByID = false;
        if (filterDto.getSchoolID() != null && !filterDto.getSchoolID().isEmpty()) {
            whereCondition.and().paramCondition("s.schoolID", "=", filterDto.getSchoolID());
        }
        if (filterDto.getRegionID() != null) {
            whereCondition.and().paramCondition("s.regionID", "=", filterDto.getRegionID());
        }
        if (filterDto.getEmail() != null && !filterDto.getEmail().isEmpty()) {
            whereCondition.and().paramCondition("s.email", "=", filterDto.getEmail());
        }
        if (filterDto.getPhone() != null && !filterDto.getPhone().isEmpty()) {
            whereCondition.and().paramCondition("s.phone", "=", filterDto.getPhone());
        }
        if (filterDto.getGender() != null) {
            whereCondition.and().paramCondition("s.gender", "=", filterDto.getGender());
        }
        if (filterDto.getLookingForJob() != null) {
            whereCondition.and().paramCondition("s.lookingForJob", "=", filterDto.getLookingForJob());
        }
        if (filterDto.getProfileVerified() != null) {
            whereCondition.and().paramCondition("s.profileVerified", "=", filterDto.getProfileVerified());
        }
        Integer startYear = filterDto.getBirthYearStart();
        Integer endYear = filterDto.getBirthYearEnd();
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
        if (filterDto.getSkillIDs() != null && !filterDto.getSkillIDs().isEmpty()) {
            queryBuilder.join(JPAQueryBuilder.JoinType.LEFT_JOIN, "s.skills", "sk");
            whereCondition.and().paramCondition("sk.SkillID", "IN", filterDto.getSkillIDs());
            groupByID = true;
        }
        if (filterDto.getMajorIDs() != null && !filterDto.getMajorIDs().isEmpty()) {
            whereCondition.and().paramCondition("s.majorID", "IN", filterDto.getMajorIDs());
        }
        if (filterDto.getIds() != null && !filterDto.getIds().isEmpty() ) {
            whereCondition.and().paramCondition("s.id", "IN", filterDto.getIds());
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

        List<SkillDto> sks = studentSkillService.getStudentSkillDtos(id);

        List<StudentLanguageSkillDto> lks = studentLanguageSkillService
                .getListStudentLanguageSkillDtos(id,
                        Collections.singletonList("slk." + StudentLanguageSkill.CREATED_DATE),
                        Collections.singletonList("asc"));

        List<StudentExperienceDto> exps = studentExperienceService
                .getListStudentExperienceDtos(id,
                        Arrays.asList("se." + StudentExperience.STARTED_DATE, "se." + StudentExperience.FINISHED_DATE),
                        Arrays.asList("desc", "asc"));

        Boolean unlocked = null;
        if (employerID != null) {
            unlocked = studentUnlockService.checkStudentUnlockedByEmployer(id, employerID);
        }

        StudentProfileDto profileDto = new StudentProfileDto(s.getId(), s.getFirstName(), s.getLastName(),
                s.getBirthday(), s.getAvatarUrl(),
                s.getEmail(), s.getPhone(), s.getGender(),
                s.getRegionID(), s.getAddress(), s.getLat(), s.getLon(),
                s.getProfileVerified(), s.getLookingForJob(), s.getCompletePercent(),
                car, unlocked, s.getSchoolID(), s.getMajorID(), s.getStudentCode(),
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
    public void updateStudentInfo(String studentID, NewStudentInfoDto updateInfo) throws Exception {
        Student student = getStudent(studentID);

        // check major exist
        majorService.getMajorDto(updateInfo.getMajorID());

        RegionAddress regionAddress = null;
        if (updateInfo.getLat() != null && updateInfo.getLon() != null) {
            regionAddress = locationService.getRegionAddress(updateInfo.getLat(), updateInfo.getLon());
        }

        student.update(updateInfo, regionAddress);
        studentRepository.save(student);

//        firestoreService.publishFirestoreTask(ChatRoomTaskFactory
//                .updateCandidateInfo(candidateID, candidate.getAvatarUrl(),
//                        candidate.getFirstName(), candidate.getLastName()));
    }

    public AvatarUrlDto updateStudentAvatar(String studentID, MultipartFile imageFile)
            throws ResponseException, IOException {
        checkStudentExist(studentID);
        String oldAvatarUrl = studentRepository.getStudentAvatarUrl(studentID);

        String newAvatarUrl = uploadImageFile(imageFile, studentStorageDirectory + studentID,
                StringConstants.AVATAR_IMAGE_NAME + "_" + new Date().getTime());

        studentRepository.updateStudentAvatar(studentID, newAvatarUrl);

        try {
            fileStorageService.deleteFileByUrl(oldAvatarUrl);
        } catch (Exception e) {
            logger.error("[File Storage Service] Delete file(s) failed: " + oldAvatarUrl, e);
        }

        return new AvatarUrlDto(newAvatarUrl);
    }

    public CoverUrlDto updateStudentCover(String studentID, MultipartFile imageFile)
            throws IOException, ResponseException {
        checkStudentExist(studentID);
        String oldCoverUrl = studentRepository.getStudentAvatarUrl(studentID);

        String coverUrl = uploadImageFile(imageFile, studentStorageDirectory + studentID,
                StringConstants.COVER_IMAGE_NAME + "_" + new Date().getTime());

        studentRepository.updateStudentCover(studentID, coverUrl);

        try {
            fileStorageService.deleteFileByUrl(oldCoverUrl);
        } catch (Exception e) {
            logger.error("[File Storage Service] Delete file(s) failed: " + oldCoverUrl, e);
        }

        return new CoverUrlDto(coverUrl);
    }

    public IdentityCardImageDto updateStudentIdentityCardImageUrl(String studentID,
                                                                  MultipartFile frontImage, MultipartFile backImage)
            throws IOException, ResponseException {
        checkStudentExist(studentID);
        IdentityCardImageDto cardImageDto = studentRepository.getStudentIdentityCardUrls(studentID);

        String newFrontUrl = uploadImageFile(frontImage, studentStorageDirectory + studentID,
                StringConstants.ID_CARD_FRONT_IMAGE_NAME + "_" + new Date().getTime());

        String newBackUrl = uploadImageFile(backImage, studentStorageDirectory + studentID,
                StringConstants.ID_CARD_BACK_IMAGE_NAME + "_" + new Date().getTime());


        studentRepository.updateStudentIdentityCardUrls(studentID, newFrontUrl, newBackUrl);

        try {
            fileStorageService.newBatch()
                    .deleteFileByUrl(cardImageDto.getIdentityCardFrontImageUrl())
                    .deleteFileByUrl(cardImageDto.getIdentityCardBackImageUrl())
                    .submit();
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
}
