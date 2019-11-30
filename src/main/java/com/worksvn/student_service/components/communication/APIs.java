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
import com.worksvn.common.modules.common.responses.*;
import com.worksvn.common.modules.school.responses.SchoolShortNameDto;
import com.worksvn.common.modules.school.responses.SimpleSchoolPreview;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;

public class APIs {

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
                                                                      int pageIndex, int pageSize) {
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

    // SCHOOL EDUCATION ================================================================================================

    public static ISApi<Object, PageDto<BranchDto>> SCHOOL_getSchoolBranches(String schoolID,
                                                                             List<String> sortBy, List<String> sortType,
                                                                             int pageIndex, int pageSize) {
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
                                                                          List<String> sortBy, List<String> sortType,
                                                                          int pageIndex, int pageSize) {
        ISApi<Object, PageDto<MajorDto>> api = new ISApi<>(ISHost.SCHOOL_SERVICE,
                HttpMethod.GET, "api/internal/schools/{id}/education/majors" +
                "?branchID={branchID}&sortBy={sortBy}&sortType={sortType}&pageIndex={pageIndex}&pageSize={pageSize}",
                null,
                new TypeReference<PageDto<MajorDto>>() {
                },
                true);
        api.addParam("id", schoolID);
        api.addParam("branchID", branchID);
        api.addParam("sortBy", sortBy);
        api.addParam("sortType", sortType);
        api.addParam("pageIndex", pageIndex);
        api.addParam("pageSize", pageSize);
        return api;
    }

    //##################################################################################################################
    // EMPLOYER SERVICE
    //##################################################################################################################

    // EMPLOYER ========================================================================================================

    public static ISApi<Object, Boolean> EMPLOYER_checkEmployerExists(String employerID) {
        ISApi<Object, Boolean> api = new ISApi<>(ISHost.EMPLOYER_SERVICE,
                HttpMethod.GET, "api/internal/employers/{id}/exists",
                null,
                new TypeReference<Boolean>() {
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
                                                                                         int pageIndex, int pageSize,
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
