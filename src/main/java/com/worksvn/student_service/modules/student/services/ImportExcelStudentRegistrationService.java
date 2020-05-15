package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.constants.RegexPattern;
import com.worksvn.common.exceptions.ISResponseException;
import com.worksvn.common.modules.common.enums.Gender;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.common.modules.common.responses.RegionAddress;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import com.worksvn.common.services.excel.import_excel.ImportExcelService;
import com.worksvn.common.services.excel.import_excel.exception.InvalidCellDataException;
import com.worksvn.common.services.excel.import_excel.exception.MissingCellDataException;
import com.worksvn.common.services.excel.import_excel.models.*;
import com.worksvn.student_service.modules.common.services.MajorService;
import com.worksvn.student_service.modules.school.services.SchoolEducationService;
import com.worksvn.student_service.modules.school.services.SchoolService;
import com.worksvn.student_service.modules.services.geocoding.LocationService;
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
    private StudentService studentService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SchoolEducationService schoolEducationService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ImportExcelService importExcelService;

    public List<ImportErrorLog> importExcelAddress(String schoolID, MultipartFile excelFile,
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
        CellProcessorAdapters adapters = new CellProcessorAdapters();

        CellProcessorAdapter adapter = new CellProcessorAdapter() {
            String address;
            String email;

            @Override
            public boolean nextCell(int row, int column, CellWrapper cell) throws Exception {
                switch (column) {
                    case 1: {
                        address = cell.getValue(String.class);
                        return true;
                    }

                    case 2: {
                        email = cell.getValue(String.class);
                        return true;
                    }

                    default: {
                        break;
                    }
                }
                return false;
            }

            @Override
            public void onEndOfRow(int row) throws Exception {
                if (email != null && !email.isEmpty()) {
                    if (address != null && !address.isEmpty()) {
                        RegionAddress regionAddress = locationService.findRegionAddress(address);
                        List<String> studentIDs = studentService.getStudentIDs(email);
                        if (studentIDs == null || studentIDs.isEmpty()) {
                            throw new Exception("Student not found: " + email);
                        } else {
                            studentService.updateStudentAddress(schoolID, regionAddress);
                        }
                    }
                }
            }
        };

        adapters.newProcessorAdapter(adapter);

        return importExcelService.importExcel(excelFile, config, adapters);
    }

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
            config.setStartRow(4);
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
            public boolean nextCell(int row, int column, CellWrapper cell) throws Exception {
                switch (column) {
//                    case 1: {
//                        username = cell.getValue(String.class);
//                        if (username == null || username.isEmpty()) {
//                            throw new Exception("Username is missing");
//                        }
//                        if (!username.matches(RegexPattern.USERNAME_REGEX)) {
//                            throw new Exception("Username is invalid");
//                        }
//                    }
//                    break;

//                    case 2: {
//                        password = cell.getValue(String.class);
//                        if (password != null && !password.matches(RegexPattern.PASSWORD_REGEX)) {
//                            throw new Exception("Password is invalid");
//                        }
//                    }
//                    break;

                    case 1: {
                        email = cell.getValue(String.class);
                        if (email == null || email.isEmpty()) {
                            throw new MissingCellDataException("Email");
                        }
                        if (!email.matches(RegexPattern.EMAIL_REGEX)) {
                            throw new InvalidCellDataException("Email", email, null);
                        }
                        return true;
                    }

                    case 2: {
                        phone = cell.getValue(String.class);
                        if (phone == null || phone.isEmpty()) {
                            throw new MissingCellDataException("Số điện thoại");
                        }
                        if (!phone.matches(RegexPattern.PHONE_REGEX)) {
                            throw new InvalidCellDataException("Số điện thoại", phone, null);
                        }
                        password = phone;
                        return true;
                    }

                    case 3: {
                        lastName = cell.getValue(String.class);
                        if (lastName == null || lastName.isEmpty()) {
                            throw new MissingCellDataException("Họ (đệm)");
                        }
                        return true;
                    }

                    case 4: {
                        firstName = cell.getValue(String.class);
                        if (firstName == null || firstName.isEmpty()) {
                            throw new MissingCellDataException("Tên");
                        }
                        return true;
                    }

                    case 5: {
                        String genderString = cell.getValue(String.class);
                        if (genderString != null && !genderString.isEmpty()) {
                            if (genderString.equalsIgnoreCase(Gender.FEMALE.getValue())) {
                                gender = Gender.FEMALE;
                            } else if (genderString.equalsIgnoreCase(Gender.MALE.getValue())) {
                                gender = Gender.MALE;
                            } else {
                                throw new InvalidCellDataException("Giới tính", genderString, null);
                            }
                        }
                        return true;
                    }

                    case 6: {
                        schoolYearStart = cell.getValue(Integer.class);
                        if (schoolYearStart == null) {
                            throw new MissingCellDataException("Năm nhập học");
                        }
                        if (schoolYearStart < 1970) {
                            throw new InvalidCellDataException("Năm nhập học", schoolYearStart, "year >= 1970");
                        }
                        return true;
                    }

                    case 7: {
                        schoolYearEnd = cell.getValue(Integer.class);
                        if (schoolYearEnd == null) {
                            throw new MissingCellDataException("Năm tốt nghiệp");
                        }
                        if (schoolYearEnd < schoolYearStart) {
                            throw new Exception("Năm tốt nghiệp phải lớn hơn năm nhập học");
                        }
                        return true;
                    }

                    case 8: {
                        String majorName = cell.getValue(String.class);
                        if (majorName == null || majorName.isEmpty()) {
                            throw new MissingCellDataException("Chuyên nghành");
                        }
                        MajorDto major = majorService.findMajor(majorName);
                        if (major == null) {
                            throw new Exception("Chuyên nghành '" + majorName + "'không tồn tại");
                        }
                        try {
                            schoolEducationService.checkSchoolMajorExists(schoolID, major.getId());
                        } catch (ISResponseException e) {
                            if (insertNotFoundMajor) {
                                schoolEducationService.addSchoolMajor(schoolID,
                                        Stream.of(major.getId()).collect(Collectors.toSet()));
                            } else {
                                throw new Exception("Trường không đào tạo chuyên nghành '" + majorName + "'");
                            }
                        }
                        majorID = major.getId();
                        return true;
                    }

                    default: {
                        return false;
                    }
                }
            }

            @Override
            public void onEndOfRow(int row) throws Exception {
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
            }
        };
        adapters.newProcessorAdapter(adapter);

        return importExcelService.importExcel(excelFile, config, adapters);
    }
}
