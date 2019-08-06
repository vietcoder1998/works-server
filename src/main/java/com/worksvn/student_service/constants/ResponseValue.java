package com.worksvn.student_service.constants;

import org.springframework.http.HttpStatus;

public enum ResponseValue {
    //200x OK
    SUCCESS(HttpStatus.OK, "thành công"),

    //400x Bad request
    MISSING_REQUEST_PARAMS(HttpStatus.BAD_REQUEST, 4001, "thiếu request param"),
    INVALID_OR_MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, 4002, "request body thiếu hoặc không hợp lệ"),
    FIELD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 4003, "lỗi validation trường thông tin"),
    REGION_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, 4004, "địa điểm, khu vực không được hỗ trợ"),

    //401x Unauthorized
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, 4011, "truy cập yêu cầu access token để xác thực"),
    WRONG_CLIENT_ID_OR_SECRET(HttpStatus.UNAUTHORIZED, 4012, "client id hoặc secret không đúng"),
    WRONG_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, 4013, "sai tên đăng nhập hoặc mật khẩu"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 4014, "token không hợp lệ"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 4015, "token đã hết hạn"),
    USER_BANNED(HttpStatus.UNAUTHORIZED, 4016, "tài khoản đã bị vô hiệu hóa"),

    // 403x Forbidden
    TOKEN_CANNOT_ACCESS_THIS_RESOURCE_SERVER(HttpStatus.FORBIDDEN, 4031, "token không có quyền truy cập tài nguyên trên máy chủ này"),
    CANNOT_DELETE_HEADQUARTERS(HttpStatus.FORBIDDEN, 4032, "không thể xóa cơ sở chính"),
    CANNOT_DELETE_HAS_JOB_BRANCH(HttpStatus.FORBIDDEN, 4033, "không thể xóa chi nhánh đã có bài đăng"),

    //404x Not found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4041, "không tìm thấy người dùng"),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, 4042, "không tìm thấy quyền"),

    //409x Conflict
    CLIENT_ID_EXISTS(HttpStatus.CONFLICT, 4091, "client id đã tồn tại"),
    USERNAME_EXISTS(HttpStatus.CONFLICT, 4092, "tên đăng nhập đã tồn tại"),
    ROLE_EXISTS(HttpStatus.CONFLICT, 4093, "quyễn đã tồn tại"),

    //500x Internal server error
    UNEXPECTED_ERROR_OCCURRED(HttpStatus.INTERNAL_SERVER_ERROR, "lỗi hệ thống"),

    ;

    private HttpStatus httpStatus;
    private String message;
    private int specialCode;

    ResponseValue(HttpStatus httpStatus, int specialCode, String message) {
        this.httpStatus = httpStatus;
        this.specialCode = specialCode;
        this.message = message;
    }

    ResponseValue(HttpStatus httpStatus, String message) {
        this(httpStatus, httpStatus.value(), message);
    }

    ResponseValue(HttpStatus httpStatus) {
        this(httpStatus, httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public int specialCode() {
        return specialCode;
    }

    public String message() {
        return message;
    }
}
