package com.jjh.membership.service;

import com.jjh.common.dto.res.ResponseHandler;
import com.jjh.common.type.ReturnType;
import com.jjh.membership.dto.model.UserIdVO;
import com.jjh.membership.dto.req.*;
import com.jjh.membership.dto.res.SessionRes;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 *  LoginService를 위한 IF Class
 *  Spring Security사용을 위해 UserDetailsService를 Override 해야 한다.
 *
 *
 */
public interface MembershipService extends UserDetailsService {

    // 회원가입
    ReturnType signUp(SignupReq req) throws  Exception;

    // 로그인 후 비밀 번호 변경
    public ReturnType changePwd(ChangePasswordReq req) throws Exception;

    // 비밀번호 찾기 이후 비밀 번호 변경
    public ReturnType changePwdByVerifyKey(ChangePasswordByCodeReq req) throws Exception;

    // 비밀 번호 찾기
    public ReturnType findPwd(FindPwdReq req) throws Exception;

    // password 암호화
    public PasswordEncoder passwordEncoder();

    // 현재 로그인 한 유저의 세션 정보
    public MembershipInfo currentSessionUserInfo() throws Exception;

    // ID & Pwd check
    ReturnType checkIdPwd(UserIdVO userIdVO) throws  Exception;

    ResponseHandler sendEmailVerifySession();

    ResponseHandler emailCertify(EmailVerify req);

    ResponseHandler<?> myPage(@Valid MyPageReq req);

    ResponseHandler<?> UpdateUser(@Valid UserReq req);

    ResponseHandler<?> UpdateProfile(@Valid ProfileReq req);

    ResponseHandler<?> UpdateMyPwd(@Valid ChangePasswordReq req);

    ResponseHandler<SessionRes> getSessionInfo(SessionReq req, HttpServletRequest request, HttpServletResponse response);
}
