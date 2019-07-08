package com.nexon.common.type;

/**
 * 프로젝트에 전반적으로 사용 되는 리턴 타입
 *
 * 기본 : 0 -> OK, 100 -> NG
 * 추가 : 상세 에러 -> 임의의 번호를 지정하고 에러 메시지와 매칭
 *
 */
public enum ReturnType {

    // General
    RTN_TYPE_OK(0, "Success"),
    RTN_TYPE_NG(100, "Internal Server Error"),
    RTN_TYPE_SESSION(101, "Session error"),
    RTN_TYPE_NO_DATA(102, "No Data"),
    RTN_TYPE_BAD_REQUEST(103, "Bad Request"),

    // Specific Errors
    // Add new error code
    RTN_TYPE_MEMBERSSHIP_USER_EXIST_NG(200, "The element exist."),
    RTN_TYPE_MEMBERSSHIP_USER_NO_EXIST_NG(201, "The element does not exist."),
    RTN_TYPE_MEMBERSSHIP_USERID_EXIST_NG(202, "User id already exist."),
    RTN_TYPE_MEMBERSSHIP_USERID_NO_EXIST_NG(203, "User id does not exist."),
    RTN_TYPE_MEMBERSSHIP_USER_ID_PATTERN_NG(204, "User id pattern error."),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_NO_EXIST_NG(205, "The pwd does not exist."),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_ENC_NG(206, "Password encryption error."),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG(207, "Password pattern error."),
    RTN_TYPE_MEMBERSSHIP_TELEPHONE_NO_EXIST_NG(208, "Telephone does not exist."),
    RTN_TYPE_MEMBERSSHIP_NAME_NO_EXIST_NG(209, "Name does not exist."),
    RTN_TYPE_MEMBERSSHIP_EMAIL_NO_EXIST_NG(210, "Email does not exist."),
    RTN_TYPE_MEMBERSSHIP_EAMIL_EXIST_NG(211, "Email already exist."),
    RTN_TYPE_MEMBERSSHIP_GENDER_NO_EXIST_NG(212, "Gender does not exist."),
    RTN_TYPE_MEMBERSSHIP_ADDRESS_NO_EXIST_NG(213, "Address does not exist."),
    RTN_TYPE_MEMBERSSHIP_COUNTRY_NO_EXIST_NG(214, "Country  does not exist."),
    RTN_TYPE_MEMBERSSHIP_EMAIL_PATTERN_NG(215, "Email pattern error."),
    RTN_TYPE_MEMBERSSHIP_USERID_MATCH_NG(216, "User id does not match."),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG(217, "Password match error."),
    RTN_TYPE_MEMBERSSHIP_AUTHORITY_NG(218, "Authroity error."),
    RTN_TYPE_MEMBERSSHIP_USER_ID_PENDING_NG(219, "The use is suspended."),
    RTN_TYPE_MEMBERSSHIP_ID_EXIST_NG(223, "User id not exist"),
    RTN_TYPE_MEMBERSSHIP_EMAIL_VERIFY_NG(224, "Key is invalid."),
    RTN_TYPE_MEMBERSSHIP_CHGPWD_NO_ID(225, ""),
    RTN_TYPE_MEMBERSSHIP_EMAIL_UNVERIFY(226, "Email unverified"),

    RTN_TYPE_EMAIL_SENDING_NG(300, "Email sending error"),

    RTN_TYPE_DATA_EXISTS(350, "Data already exists."),

    RTN_TYPE_APPLY_EXIST_NG(400, "Application does not exist."),

    RTN_TYPE_FILE_EXTENSION_NG(450, "File extension error."),


    RTN_TYPE_NOT_USE(9999, "LAST_FIELD");

    private int code;
    private String message;

    ReturnType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getValue() {
        return code;
    }
    public String getStrValue()
    {
        return String.valueOf(code);
    }

    public String getMessage() {
        return message;
    }

}
