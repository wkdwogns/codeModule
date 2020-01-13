package com.jjh.membership.dto.model;

import lombok.Data;

@Data
public class UserInfoVO {

    // User
    private Integer userSeq;  // 유저 번호
    private String mnNo;   // 관리 번호
    private String device;  // 회원 가입 장치(PC/MOBILE/APP)
    private String userId; //  userId
    private String email; // 이메일
    private String userPwd; // 비밀번호
    private Integer authority; // 권한
    private String loginType; // 로그인 타입
    private String resignYn; // 회원 탈퇴
    private String resignReason; // 회원 탈퇴 이유
    private String resignDt; // 회원 탈퇴 일시
    private String pendingYn; // 정지 여부
    private String pendingDt; // 정지 일시
    private String pendingPeriod; // 정지 기간
    private String verifyKey; // 인증번호
    private String verifyKeySendDt; // 인증번호만료시간
    private String certificateYn; // 인증여부
    private String verifyPwdKey; // 비밀번호변경 인증번호
    private Integer creId; // 작성자
    private String creDt; // 작성 시간
    private Integer updId; // 작성자
    private String updDt; // 작성 시간

    // Profile
    private String nickName;
    private String userName;
}