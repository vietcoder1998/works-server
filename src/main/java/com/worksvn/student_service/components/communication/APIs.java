package com.worksvn.student_service.components.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISApi;
import com.worksvn.common.components.communication.ISHost;
import com.worksvn.common.modules.admin.enums.AnnouncementCommentTarget;
import com.worksvn.common.modules.admin.enums.AnnouncementTarget;
import com.worksvn.common.modules.admin.requests.AnnouncementCommentFilter;
import com.worksvn.common.modules.admin.requests.NewAnnouncementCommentDto;
import com.worksvn.common.modules.admin.requests.VisibleAnnouncementFilterDto;
import com.worksvn.common.modules.admin.responses.AnnouncementCommentDto;
import com.worksvn.common.modules.admin.responses.AnnouncementDto;
import com.worksvn.common.modules.admin.responses.AnnouncementPreview;
import com.worksvn.common.modules.admin.responses.AnnouncementTypeDto;
import com.worksvn.common.modules.auth.requests.UserFilter;
import com.worksvn.common.modules.auth.responses.UserDto;
import com.worksvn.common.modules.common.enums.RequestState;
import com.worksvn.common.modules.common.responses.*;
import com.worksvn.common.modules.employer.enums.EmployerRatingUserType;
import com.worksvn.common.modules.employer.enums.JobApplyUserType;
import com.worksvn.common.modules.employer.enums.JobHomePriority;
import com.worksvn.common.modules.employer.enums.JobSavedUserType;
import com.worksvn.common.modules.employer.requests.*;
import com.worksvn.common.modules.employer.responses.*;
import com.worksvn.common.modules.school.requests.SchoolEventEmployerFilter;
import com.worksvn.common.modules.school.requests.SchoolEventFilter;
import com.worksvn.common.modules.school.responses.*;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;

public class APIs {

    //##################################################################################################################
    // AUTH SERVICE
    //##################################################################################################################

    public static ISApi<UserFilter, PageDto<UserDto>> AUTH_queryUsers(UserFilter filter,
                                                                      List<String> sortBy, List<String> sortType,
                                                                      Integer pageIndex, Integer pageSize) {
        ISApi<UserFilter, PageDto<UserDto>> api = new ISApi<>(ISHost.AUTH_SERVICE,
                HttpMethod.POST, "api/internal/users/query" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<UserDto>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    //##################################################################################################################
    // PUBLIC SERVICE
    //##################################################################################################################

    // LOCATION ========================================================================================================

    public static ISApi<Object, RegionAddress> PUBLIC_getRegionAddress(double lat, double lon) {
        ISApi<Object, RegionAddress> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.GET, "api/location/regionAddress?lat={lat}&lon={lon}",
                null,
                new TypeReference<RegionAddress>() {
                },
                true);
        api.addParam("lat", lat + "");
        api.addParam("lon", lon + "");
        return api;
    }

    // MAJOR ===========================================================================================================


    public static ISApi<Object, PageDto<MajorDto>> PUBLIC_queryMajors(Integer branchID,
                                                                      String name, Boolean matchingName,
                                                                      Set<Integer> ids,
                                                                      List<String> sortBy, List<String> sortType,
                                                                      Integer pageIndex, Integer pageSize) {
        ISApi<Object, PageDto<MajorDto>> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.POST, "api/internal/majors/query" +
                "?branchID={branchID}&name={name}&matchingName={matchingName}" +
                "&sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                ids,
                new TypeReference<PageDto<MajorDto>>() {
                },
                true);
        api.addParam("branchID", branchID);
        api.addParam("name", name);
        api.addParam("matchingName", matchingName);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, MajorDto> PUBLIC_getMajor(int id) {
        ISApi<Object, MajorDto> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.GET, "api/internal/majors/{id}",
                null,
                new TypeReference<MajorDto>() {
                },
                true);
        api.addParam("id", id);
        return api;
    }

    // REGION ==========================================================================================================

    public static ISApi<Object, RegionDto> PUBLIC_getRegion(int id) {
        ISApi<Object, RegionDto> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.GET, "api/internal/regions/{id}",
                null,
                new TypeReference<RegionDto>() {
                },
                true);
        api.addParam("id", id);
        return api;
    }

    // LANGUAGE ========================================================================================================

    public static ISApi<Object, LanguageDto> PUBLIC_getLanguage(int id) {
        ISApi<Object, LanguageDto> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.GET, "api/internal/languages/{id}",
                null,
                new TypeReference<LanguageDto>() {
                },
                true);
        api.addParam("id", id);
        return api;
    }

    // SKILL ===========================================================================================================

    public static ISApi<Object, PageDto<SkillDto>> PUBLIC_querySkills(Set<Integer> ids,
                                                                      List<String> sortBy, List<String> sortType,
                                                                      Integer pageIndex, Integer pageSize) {
        ISApi<Object, PageDto<SkillDto>> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.POST, "api/internal/skills/query" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                ids,
                new TypeReference<PageDto<SkillDto>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Set<Integer>, Set<Integer>> PUBLIC_getExistSkillIDs(Set<Integer> ids) {
        ISApi<Set<Integer>, Set<Integer>> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.POST, "api/internal/skills/exists",
                ids,
                new TypeReference<Set<Integer>>() {
                },
                true);
        return api;
    }

    // MAJOR JOB NAMES =================================================================================================

    public static ISApi<Set<Integer>, Set<Integer>> PUBLIC_getJobNameIDsByMajorIDs(Set<Integer> majorIDs) {
        ISApi<Set<Integer>, Set<Integer>> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.POST, "api/internal/majors/jobNames/ids",
                majorIDs,
                new TypeReference<Set<Integer>>() {
                },
                true);
        return api;
    }

    // JOB NAME ========================================================================================================

    public static ISApi<Set<Integer>, PageDto<JobNameDto>> PUBLIC_getJobNames(
            String name, Boolean matchingName,
            List<String> sortBy, List<String> sortType,
            Integer pageIndex, Integer pageSize,
            Set<Integer> ids) {
        ISApi<Set<Integer>, PageDto<JobNameDto>> api = new ISApi<>(ISHost.PUBLIC_SERVICE,
                HttpMethod.POST, "api/internal/jobNames/query" +
                "?name={name}&matchingName={matchingName}&sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                ids,
                new TypeReference<PageDto<JobNameDto>>() {
                },
                true);
        api.addParam("name", name);
        api.addParam("matchingName", matchingName);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    //##################################################################################################################
    // SCHOOL SERVICE
    //##################################################################################################################

    // SCHOOL ==========================================================================================================

    public static ISApi<Object, SchoolShortNameDto> SCHOOL_getSchoolShortName(String schoolID) {
        ISApi<Object, SchoolShortNameDto> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/shortName",
                null,
                new TypeReference<SchoolShortNameDto>() {
                },
                true);
        api.addParam("id", schoolID);
        return api;
    }

    public static ISApi<Object, SimpleSchoolPreview> SCHOOL_getSchoolSimpleInfo(String schoolID) {
        ISApi<Object, SimpleSchoolPreview> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/simple",
                null,
                new TypeReference<SimpleSchoolPreview>() {
                },
                true);
        api.addParam("id", schoolID);
        return api;
    }

    public static ISApi<Object, Object> SCHOOL_checkSchoolExists(String schoolID) {
        ISApi<Object, Object> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/exists",
                null,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("id", schoolID);
        return api;
    }

    // SCHOOL EDUCATION ================================================================================================

    public static ISApi<Object, PageDto<BranchDto>> SCHOOL_getSchoolBranches(String schoolID,
                                                                             List<String> sortBy, List<String> sortType,
                                                                             Integer pageIndex, Integer pageSize) {
        ISApi<Object, PageDto<BranchDto>> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/education/branches" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                null,
                new TypeReference<PageDto<BranchDto>>() {
                },
                true);
        api.addParam("id", schoolID);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, PageDto<MajorDto>> SCHOOL_getSchoolMajors(String schoolID, Integer branchID,
                                                                          String name, Boolean matchingName,
                                                                          List<String> sortBy, List<String> sortType,
                                                                          Integer pageIndex, Integer pageSize) {
        ISApi<Object, PageDto<MajorDto>> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/education/majors" +
                "?branchID={branchID}&name={name}&matchingName={matchingName}" +
                "&sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                null,
                new TypeReference<PageDto<MajorDto>>() {
                },
                true);
        api.addParam("id", schoolID);
        api.addParam("branchID", branchID);
        api.addParam("name", name);
        api.addParam("matchingName", matchingName);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, Object> SCHOOL_checkSchoolMajorExists(String schoolID, int majorID) {
        ISApi<Object, Object> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{sid}/education/majors/{mid}",
                null,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("sid", schoolID);
        api.addParam("mid", majorID);
        return api;
    }

    public static ISApi<Object, Set<Integer>> SCHOOL_addNewSchoolMajors(String schoolID, Set<Integer> majorIDs) {
        ISApi<Object, Set<Integer>> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.POST, "api/internal/schools/{id}/education/majors",
                majorIDs,
                new TypeReference<Set<Integer>>() {
                },
                true);
        api.addParam("id", schoolID);
        return api;
    }

    // SCHOOL SURVEY FORM ==============================================================================================

    public static ISApi<Object, SchoolSurveyFormDto> SCHOOL_getSchoolSurveyForm(String schoolID) {
        ISApi<Object, SchoolSurveyFormDto> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/surveys",
                null,
                new TypeReference<SchoolSurveyFormDto>() {
                },
                true);
        api.addParam("id", schoolID);
        return api;
    }

    // SCHOOL EVENT ====================================================================================================

    public static ISApi<SchoolEventFilter, PageDto<SchoolEventPreviewDto>> SCHOOL_querySchoolEvents(
            SchoolEventFilter filter,
            List<String> sortBy, List<String> sortType,
            Integer pageIndex, Integer pageSize) {
        ISApi<SchoolEventFilter, PageDto<SchoolEventPreviewDto>> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.POST, "api/internal/schools/events/query?" +
                "sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<SchoolEventPreviewDto>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, SchoolEventDto> SCHOOL_getSchoolEvent(String schoolID, String eventID,
                                                                      Boolean activeCheck) {
        ISApi<Object, SchoolEventDto> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/events/{id}?" +
                "schoolID={schoolID}&activeCheck={activeCheck}",
                null,
                new TypeReference<SchoolEventDto>() {
                },
                true);
        api.addParam("id", eventID);
        api.addParam("schoolID", schoolID);
        api.addParam("activeCheck", activeCheck);;
        return api;
    }

    // SCHOOL EVENT EMPLOYER ================================================================================

    public static ISApi<SchoolEventEmployerFilter, PageDto<SchoolEventEmployerDto>> SCHOOL_querySchoolEventEmployers(
            String schoolID, String eventID,
            SchoolEventEmployerFilter filter,
            List<String> sortBy, List<String> sortType,
            Integer pageIndex, Integer pageSize) {
        ISApi<SchoolEventEmployerFilter, PageDto<SchoolEventEmployerDto>> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.POST, "api/internal/schools/{sid}/events/{eid}/employers/query?" +
                "sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<SchoolEventEmployerDto>>() {
                },
                true);
        api.addParam("sid", schoolID);
        api.addParam("eid", eventID);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, SchoolEventEmployerDto> SCHOOL_getSchoolEventEmployer(String schoolID, String eventID,
                                                                                      String employerID) {
        ISApi<Object, SchoolEventEmployerDto> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/events/{evid}/employers/{emid}?" +
                "schoolID={schoolID}",
                null,
                new TypeReference<SchoolEventEmployerDto>() {
                },
                true);
        api.addParam("schoolID", schoolID);
        api.addParam("evid", eventID);
        api.addParam("emid", employerID);
        return api;
    }

    // ACTIVE EMPLOYER SCHOOL EVENT JOB ================================================================================

    public static ISApi<ActiveJobFilter, PageDto<JobPreview>> SCHOOL_queryActiveEmployerSchoolEventJobs(
            String schoolID, String eventID,
            ActiveJobFilter filter,
            List<String> sortBy, List<String> sortType,
            Integer pageIndex, Integer pageSize) {
        ISApi<ActiveJobFilter, PageDto<JobPreview>> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.POST, "api/internal/schools/{sid}/events/{eid}/jobs/active/query?" +
                "sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<JobPreview>>() {
                },
                true);
        api.addParam("sid", schoolID);
        api.addParam("eid", eventID);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, JobDto> SCHOOL_getActiveEmployerSchoolEventJob(
            String schoolID, String eventID, String jobID,
            String userID, String userType,
            Double centerLat, Double centerLon) {
        ISApi<Object, JobDto> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{sid}/events/{eid}/jobs/{jid}/active" +
                "?userID={userID}&userType={userType}" +
                "&centerLat={centerLat}&centerLon={centerLon}",
                null,
                new TypeReference<JobDto>() {
                },
                true);
        api.addParam("sid", schoolID);
        api.addParam("eid", eventID);
        api.addParam("jid", jobID);
        api.addParam("userID", userID);
        api.addParam("userType", userType);
        api.addParam("centerLat", centerLat);
        api.addParam("centerLon", centerLon);
        return api;
    }

    //##################################################################################################################
    // EMPLOYER SERVICE
    //##################################################################################################################

    // EMPLOYER ========================================================================================================

    public static ISApi<Object, Object> EMPLOYER_checkEmployerExists(String employerID) {
        ISApi<Object, Object> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.GET, "api/internal/employers/{id}/exists",
                null,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("id", employerID);
        return api;
    }

    // JOB =============================================================================================================

    public static ISApi<HomeActiveJobFilter, PageDto<JobPreview>> EMPLOYER_getHomeActiveJobs(List<String> sortBy, List<String> sortType,
                                                                                             Integer pageIndex, Integer pageSize,
                                                                                             HomeActiveJobFilter filter, JobHomePriority priority) {
        ISApi<HomeActiveJobFilter, PageDto<JobPreview>> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/active/home" +
                "?priority={priority}" +
                "&sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<JobPreview>>() {
                },
                true);
        api.addParam("priority", priority);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<SearchActiveJobFilter, PageDto<JobPreview>> EMPLOYER_searchActiveJobs(List<String> sortBy, List<String> sortType,
                                                                                              Integer pageIndex, Integer pageSize,
                                                                                              SearchActiveJobFilter filter) {
        ISApi<SearchActiveJobFilter, PageDto<JobPreview>> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/active/search" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<JobPreview>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<ActiveJobFilter, PageDto<JobPreview>> EMPLOYER_queryActiveJobs(List<String> sortBy,
                                                                                       List<String> sortType,
                                                                                       Integer pageIndex, Integer pageSize,
                                                                                       ActiveJobFilter filter) {
        ISApi<ActiveJobFilter, PageDto<JobPreview>> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/active/query" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<JobPreview>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, JobDto> EMPLOYER_getActiveJobDetail(String jobID, String userID, String userType,
                                                                    String schoolID, Boolean passSchoolIgnore,
                                                                    Double centerLat, Double centerLon) {
        ISApi<Object, JobDto> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.GET, "api/internal/employers/jobs/{id}/active" +
                "?userID={userID}&userType={userType}&schoolID={schoolID}&passSchoolIgnore={passSchoolIgnore}" +
                "&centerLat={centerLat}&centerLon={centerLon}",
                null,
                new TypeReference<JobDto>() {
                },
                true);
        api.addParam("id", jobID);
        api.addParam("userID", userID);
        api.addParam("userType", userType);
        api.addParam("schoolID", schoolID);
        api.addParam("passSchoolIgnore", passSchoolIgnore);
        api.addParam("centerLat", centerLat);
        api.addParam("centerLon", centerLon);
        return api;
    }

    // JOB APPLY REQUEST ===============================================================================================

    public static ISApi<JobApplyRequestFilter, PageDto<JobApplyRequestPreview>> EMPLOYER_queryJobApplyRequestPreviews(List<String> sortBy, List<String> sortType,
                                                                                                                      Integer pageIndex, Integer pageSize,
                                                                                                                      JobApplyRequestFilter filter) {
        ISApi<JobApplyRequestFilter, PageDto<JobApplyRequestPreview>> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/apply/query" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<JobApplyRequestPreview>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<NewJobApplyRequestDto, JobApplyResult> EMPLOYER_applyJob(String jobID,
                                                                                 NewJobApplyRequestDto newRequest) {
        ISApi<NewJobApplyRequestDto, JobApplyResult> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/{id}/apply",
                newRequest,
                new TypeReference<JobApplyResult>() {
                },
                true);
        api.addParam("id", jobID);
        return api;
    }

    public static ISApi<Object, Boolean> EMPLOYER_checkJobApplyState(String userID, JobApplyUserType userType,
                                                                     String employerID, RequestState state) {
        ISApi<Object, Boolean> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.GET, "api/internal/employers/jobs/apply/state/{state}" +
                "?userID={userID}&userType={userType}&employerID={employerID}",
                null,
                new TypeReference<Boolean>() {
                },
                true);
        api.addParam("state", state);
        api.addParam("employerID", employerID);
        api.addParam("userID", userID);
        api.addParam("userType", userType);
        return api;
    }

    // JOB OFFER REQUEST ===============================================================================================

    public static ISApi<JobOfferRequestFilter, PageDto<JobOfferRequestPreview>> EMPLOYER_queryJobOfferRequestPreviews(List<String> sortBy, List<String> sortType,
                                                                                                                      Integer pageIndex, Integer pageSize,
                                                                                                                      JobOfferRequestFilter filter) {
        ISApi<JobOfferRequestFilter, PageDto<JobOfferRequestPreview>> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/offer/query" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<JobOfferRequestPreview>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }


    public static ISApi<NewJobOfferRequestState, Object> EMPLOYER_updateJobOfferState(String jobID, RequestState state,
                                                                                      NewJobOfferRequestState newState) {
        ISApi<NewJobOfferRequestState, Object> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.PUT, "api/internal/employers/jobs/{id}/offer/state/{state}",
                newState,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("id", jobID);
        api.addParam("state", state);
        return api;
    }

    // JOB SAVED =======================================================================================================

    public static ISApi<JobSavedFilter, PageDto<JobSavedPreview>> EMPLOYER_queryJobSavedPreviews(List<String> sortBy,
                                                                                                 List<String> sortType,
                                                                                                 Integer pageIndex, Integer pageSize,
                                                                                                 JobSavedFilter filter) {
        ISApi<JobSavedFilter, PageDto<JobSavedPreview>> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/saved/query" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<JobSavedPreview>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, Object> EMPLOYER_saveJob(String jobID, String userID, JobSavedUserType userType) {
        ISApi<Object, Object> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/jobs/{id}/saved" +
                "?userID={userID}&userType={userType}",
                null,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("id", jobID);
        api.addParam("userID", userID);
        api.addParam("userType", userType);
        return api;
    }

    public static ISApi<Set<String>, Object> EMPLOYER_deleteJobSaves(Set<String> ids, String userID, JobSavedUserType userType) {
        ISApi<Set<String>, Object> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.DELETE, "api/internal/employers/jobs/saved" +
                "?userID={userID}&userType={userType}",
                ids,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("userID", userID);
        api.addParam("userType", userType);
        return api;
    }

    // EMPLOYER RATING ===============================================================================================

    public static ISApi<Object, EmployerRatingDto> EMPLOYER_getEmployerRating(
            String employerID, String userID, EmployerRatingUserType userType) {
        ISApi<Object, EmployerRatingDto> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.GET, "api/internal/employers/{id}/rating" +
                "?userID={userID}&userType={userType}",
                null,
                new TypeReference<EmployerRatingDto>() {
                },
                true);
        api.addParam("id", employerID);
        api.addParam("userID", userID);
        api.addParam("userType", userType);
        return api;
    }

    public static ISApi<NewEmployerRatingDto, EmployerAverageRatingDto> EMPLOYER_rateEmployer(
            String employerID, NewEmployerRatingDto newRating) {
        ISApi<NewEmployerRatingDto, EmployerAverageRatingDto> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.POST, "api/internal/employers/{id}/rating",
                newRating,
                new TypeReference<EmployerAverageRatingDto>() {
                },
                true);
        api.addParam("id", employerID);
        return api;
    }

    //##################################################################################################################
    // ADMIN SERVICE
    //##################################################################################################################

    // ANNOUNCEMENT ====================================================================================================

    public static ISApi<VisibleAnnouncementFilterDto, PageDto<AnnouncementPreview>> ADMIN_getAnnouncementPreviews(
            List<String> sortBy, List<String> sortType,
            Integer pageIndex, Integer pageSize,
            VisibleAnnouncementFilterDto filter,
            AnnouncementTarget target) {
        ISApi<VisibleAnnouncementFilterDto, PageDto<AnnouncementPreview>> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.POST, "api/internal/admins/announcements/query" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}&target={target}",
                filter,
                new TypeReference<PageDto<AnnouncementPreview>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        api.addParam("target", target);
        return api;
    }

    public static ISApi<Object, AnnouncementDto> ADMIN_getAnnouncementDto(String id, AnnouncementTarget target) {
        ISApi<Object, AnnouncementDto> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.GET, "api/internal/admins/announcements/{id}" +
                "?target={target}",
                null,
                new TypeReference<AnnouncementDto>() {
                },
                true);
        api.addParam("id", id);
        api.addParam("target", target);
        return api;
    }

    // ANNOUNCEMENT TYPE ===============================================================================================

    public static ISApi<Object, PageDto<AnnouncementTypeDto>> ADMIN_getAnnouncementTypes(List<String> sortBy, List<String> sortType,
                                                                                         Integer pageIndex, Integer pageSize,
                                                                                         AnnouncementTarget target) {
        ISApi<Object, PageDto<AnnouncementTypeDto>> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.GET, "api/internal/admins/announcementTypes" +
                "?sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}&target={target}",
                null,
                new TypeReference<PageDto<AnnouncementTypeDto>>() {
                },
                true);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        api.addParam("target", target);
        return api;
    }

    // ANNOUNCEMENT COMMENT ============================================================================================

    public static ISApi<AnnouncementCommentFilter, PageDto<AnnouncementCommentDto>> ADMIN_getAnnouncementComments(
            String announcementID, AnnouncementTarget target,
            List<String> sortBy, List<String> sortType,
            Integer pageIndex, Integer pageSize,
            AnnouncementCommentFilter filter) {
        ISApi<AnnouncementCommentFilter, PageDto<AnnouncementCommentDto>> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.POST, "api/internal/admins/announcements/{id}/comments/query" +
                "?target={target}" +
                "&sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                filter,
                new TypeReference<PageDto<AnnouncementCommentDto>>() {
                },
                true);
        api.addParam("id", announcementID);
        api.addParam("target", target);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    public static ISApi<Object, AnnouncementCommentDto> ADMIN_getAnnouncementComment(
            String announcementID, int commentID, AnnouncementTarget target) {
        ISApi<Object, AnnouncementCommentDto> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.GET, "api/internal/admins/announcements/{aid}/comments/{cid}" +
                "?target={target}",
                null,
                new TypeReference<AnnouncementCommentDto>() {
                },
                true);
        api.addParam("aid", announcementID);
        api.addParam("cid", commentID);
        api.addParam("target", target);
        return api;
    }

    public static ISApi<Object, AnnouncementCommentDto> ADMIN_getUserAnnouncementComment(
            String announcementID, AnnouncementTarget target,
            String userID, AnnouncementCommentTarget commentTarget) {
        ISApi<Object, AnnouncementCommentDto> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.GET, "api/internal/admins/announcements/{aid}/comments" +
                "?target={target}&userID={userID}&commentTarget={commentTarget}",
                null,
                new TypeReference<AnnouncementCommentDto>() {
                },
                true);
        api.addParam("aid", announcementID);
        api.addParam("target", target);
        api.addParam("userID", userID);
        api.addParam("commentTarget", commentTarget);
        return api;
    }

    public static ISApi<NewAnnouncementCommentDto, Object> ADMIN_createNewAnnouncementComment(
            String announcementID, AnnouncementTarget target,
            NewAnnouncementCommentDto newComment) {
        ISApi<NewAnnouncementCommentDto, Object> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.POST, "api/internal/admins/announcements/{id}/comments" +
                "?target={target}",
                newComment,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("id", announcementID);
        api.addParam("target", target);
        return api;
    }

    public static ISApi<Set<Integer>, Object> ADMIN_deleteAnnouncementComments(
            String announcementID, AnnouncementTarget target,
            String userID, AnnouncementCommentTarget userType,
            Set<Integer> ids) {
        ISApi<Set<Integer>, Object> api = new ISApi<>(ISHost.ADMIN_SERVICE,
                HttpMethod.DELETE, "api/internal/admins/announcements/{id}/comments" +
                "?target={target}" +
                "&userID={userID}&userType={userType}",
                ids,
                new TypeReference<Object>() {
                },
                true);
        api.addParam("id", announcementID);
        api.addParam("target", target);
        api.addParam("userID", userID);
        api.addParam("userType", userType);
        return api;
    }
}
