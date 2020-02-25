package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.constants.RegexPattern;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.common.enums.Gender;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import com.worksvn.common.services.excel.import_excel.ImportExcelService;
import com.worksvn.common.services.excel.import_excel.models.*;
import com.worksvn.common.utils.core.JacksonObjectMapper;
import com.worksvn.student_service.modules.common.services.MajorService;
import com.worksvn.student_service.modules.school.services.SchoolEducationService;
import com.worksvn.student_service.modules.school.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImportExcelStudentRegistrationService {
    @Autowired
    private StudentRegistrationService studentRegistrationService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SchoolEducationService schoolEducationService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private ImportExcelService importExcelService;

    public List<ImportErrorLog> importExcel(String schoolID, MultipartFile excelFile,
                                            Boolean insertNotFoundMajor,
                                            ImportExcelConfig config) throws Exception {
        if (config == null) {
            config = new ImportExcelConfig();
        }
        if (config.getStartColumn() == null) {
            config.setStartColumn(1);
        }
        if (config.getStartRow() == null) {
            config.setStartRow(3);
        }

        schoolService.checkSchoolExist(schoolID);

        CellProcessorAdapters adapters = new CellProcessorAdapters();

        CellProcessorAdapter adapter = new CellProcessorAdapter() {
            String username;
            String password;
            String lastName;
            String firstName;
            Gender gender;
            Integer schoolYearStart;
            Integer schoolYearEnd;
            Integer majorID;
            String phone;
            String email;

            @Override
            public void nextCell(int row, int column, CellWrapper cell) throws Exception {
                switch (column) {
                    case 1: {
                        username = cell.getValue(String.class);
                        if (username == null || username.isEmpty()) {
                            throw new Exception("Phone is missing");
                        }
                        if (!username.matches(RegexPattern.USERNAME_REGEX)) {
                            throw new Exception("Username is invalid");
                        }
                    }
                    break;

                    case 2: {
                        password = cell.getValue(String.class);
                        if (password != null && !password.matches(RegexPattern.PASSWORD_REGEX)) {
                            throw new Exception("Password is invalid");
                        }
                    }
                    break;

                    case 3: {
                        lastName = cell.getValue(String.class);
                        if (lastName == null || lastName.isEmpty()) {
                            throw new Exception("Last name is missing");
                        }
                    }
                    break;

                    case 4: {
                        firstName = cell.getValue(String.class);
                        if (firstName == null || firstName.isEmpty()) {
                            throw new Exception("First name is missing");
                        }
                    }
                    break;

                    case 5: {
                        String genderString = cell.getValue(String.class);
                        if (genderString != null && !genderString.isEmpty()) {
                            if (genderString.equalsIgnoreCase(Gender.FEMALE.getValue())) {
                                gender = Gender.FEMALE;
                            } else if (genderString.equalsIgnoreCase(Gender.MALE.getValue())) {
                                gender = Gender.MALE;
                            } else {
                                throw new Exception("Gender '" + genderString + "' is invalid");
                            }
                        }
                    }
                    break;

                    case 6: {
                        schoolYearStart = cell.getValue(Integer.class);
                        if (schoolYearStart == null) {
                            throw new Exception("School year start is missing");
                        }
                        if (schoolYearStart < 1970) {
                            throw new Exception("School year start is invalid, required >= 1970");
                        }
                    }
                    break;

                    case 7: {
                        schoolYearEnd = cell.getValue(Integer.class);
                        if (schoolYearEnd == null) {
                            throw new Exception("School year end is missing");
                        }
                        if (schoolYearEnd < schoolYearStart) {
                            throw new Exception("School year end must be greater or equal school year start");
                        }
                    }
                    break;

                    case 8: {
                        String majorName = cell.getValue(String.class);
                        if (majorName == null || majorName.isEmpty()) {
                            throw new Exception("major name is missing");
                        }
                        MajorDto major = majorService.findMajor(majorName);
                        if (major == null) {
                            throw new Exception("major not found");
                        }
                        try {
                            schoolEducationService.checkSchoolMajorExists(schoolID, major.getId());
                        } catch (ISResponseException e) {
                            if (insertNotFoundMajor) {
                                schoolEducationService.addSchoolMajor(schoolID,
                                        Stream.of(major.getId()).collect(Collectors.toSet()));
                            } else {
                                throw new Exception("school major not found");
                            }
                        }
                        majorID = major.getId();
                    }
                    break;

                    case 9: {
                        phone = cell.getValue(String.class);
                        if (phone == null || phone.isEmpty()) {
                            if (password == null || password.isEmpty()) {
                                throw new Exception("Phone is missing");
                            }
                        } else if (!phone.matches(RegexPattern.PHONE_REGEX)) {
                            throw new Exception("Phone '" + phone + "' is invalid");
                        }
                    }
                    break;

                    case 10: {
                        email = cell.getValue(String.class);
                        if (email != null && !email.matches(RegexPattern.EMAIL_REGEX)) {
                            throw new Exception("Email '" + email + "' is invalid");
                        }
                    }
                    break;
                }
            }

            @Override
            public void onEndOfRow(int row, Exception errorCause) throws Exception {
                if (errorCause == null) {
                    try {
                        NewStudentRegistrationDto registration = new NewStudentRegistrationDto();
                        registration.setUsername(username);
                        registration.setPassword(password);
                        registration.setFirstName(firstName);
                        registration.setLastName(lastName);
                        registration.setGender(gender.name());
                        registration.setSchoolYearStart(schoolYearStart);
                        registration.setSchoolYearEnd(schoolYearEnd);
                        registration.setMajorID(majorID);
                        registration.setPhone(phone);
                        registration.setEmail(email);
                        studentRegistrationService.registerNewStudent(schoolID, registration, true);
                    } catch (Exception e) {
                        if (e instanceof ResponseException) {
                            throw new Exception(JacksonObjectMapper.getInstance().writeValueAsString(((ResponseException) e).getBody()));
                        } else {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                }
            }
        };
        adapters.newProcessorAdapter(adapter);

        return importExcelService.importExcel(excelFile, config, adapters);
    }
}
