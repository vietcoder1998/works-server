package com.worksvn.student_service.components.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.components.communication.ISApi;
import com.worksvn.common.components.communication.ISHost;
import com.worksvn.common.modules.common.responses.*;
import com.worksvn.common.modules.school.responses.SchoolShortNameDto;
import com.worksvn.common.modules.school.responses.SimpleSchoolPreview;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;

public class APIs {
    // AUTH SERVICE ====================================================================================================

    // PUBLIC SERVICE ==================================================================================================

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

    // SCHOOL SERVICE ==================================================================================================

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
}
