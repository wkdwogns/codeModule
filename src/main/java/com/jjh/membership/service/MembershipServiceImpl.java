package com.jjh.membership.service;

import com.jjh.common.VaildationCheck.ConfigValidationCheck;
import com.jjh.common.dto.res.ResponseHandler;
import com.jjh.common.mail.MailHandler;
import com.jjh.common.mail.dto.res.MailReq;
import com.jjh.common.password.PasswordHandler;
import com.jjh.common.type.ReturnType;
import com.jjh.common.util.*;
import com.jjh.membership.config.ConfigMembership;
import com.jjh.membership.dao.MembershipDao;
import com.jjh.membership.dto.model.UserIdVO;
import com.jjh.membership.dto.model.UserInfoVO;
import com.jjh.membership.dto.req.*;
import com.jjh.membership.dto.res.SessionRes;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.Semaphore;

@Service
public class MembershipServiceImpl implements MembershipService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MembershipDao membershipDao;

    @Autowired
    ConfigMembership configMembership;

    @Autowired
    PasswordHandler passwordHandler;

    @Autowired
    MailHandler mailHandler;

    @Autowired
    ConfigValidationCheck configValidationCheck;

    @Autowired
    GetNowTime getNowTime;

    @Autowired
    JwtService jwtService;

    CommonUtil commonUtil = new CommonUtil();

    static Semaphore signUpSemaphore = new Semaphore(1);

    /**
     * 아이디와 비밀번호를 확인 한다.
     *
     * @param userIdVO
     * @return
     * @throws Exception
     */
    @Override
    public ReturnType checkIdPwd(UserIdVO userIdVO) throws Exception {

        Map<String, Object> params = ConvertUtil.convertObjectToMap(userIdVO);
        UserInfoVO userInfoVO =membershipDao.selectUserInfo(params);

        if(!commonUtil.isExist(userInfoVO)) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_NO_EXIST_NG;
        }

        if (!passwordHandler.matches(userIdVO.getUserPwd(),userInfoVO.getUserPwd())) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_ENC_NG;
        }

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     * 스프링 시큐리티 로그인 정보 저장 함수
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Map req = new HashMap();
        req.put("userId", username);
        UserInfoVO userInfoVO = membershipDao.selectUserInfo(req);

        MembershipInfo member = new MembershipInfo();

        // 해당 유저가 존재 하지 않을 때
        if (!commonUtil.isExist(userInfoVO)) {
            throw new UsernameNotFoundException("member info empty");
        }

        // 아이디 패스워드
        member.setPassword(userInfoVO.getUserPwd());
        member.setUsername(userInfoVO.getUserId());

        // Pending
        if (commonUtil.isNotEmpty(userInfoVO.getPendingYn()) && userInfoVO.getPendingYn().equals("Y")) {
            member.setAccountNonLocked(false);
        } else {
            member.setAccountNonLocked(true);
        }

        // Spring Security 에러 처리 부분
        member.setAccountNonExpired(true);
        member.setCredentialsNonExpired(true);
        member.setCredentialsNonExpired(true);
        member.setEnabled(true);

        member.setLoginTime(getNowTime.getTimeByDate());

        // 권한 저장
        member.setAuthorities(getAuthorities(userInfoVO.getAuthority()));
        member.setAuthoritiesLevel(userInfoVO.getAuthority());
        member.setAuthoritiesStr(chgAuthoritiesToStr(member.getAuthoritiesLevel()));

        // 유저 시퀀스 저장
        member.setUserSeq(userInfoVO.getUserSeq());

        return member;
    }

    /**
     * 패스워드 변경 함수 .
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public ReturnType changePwd(ChangePasswordReq req) throws Exception {

        //  시규 패스워드 pattern 확인
        if (configValidationCheck.checkPwd(req.getNewUserPwd()) != 0) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG;
        }

        Map<String, Object> params = ConvertUtil.convertObjectToMap(req);
        UserInfoVO userInfoVO = membershipDao.selectUserInfo(params);

        if (userInfoVO == null) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_NO_EXIST_NG;
        }

        // 기존 비밀번호 확인
        UserIdVO userIdVO = new UserIdVO();
        userIdVO.setUserId(userInfoVO.getUserId());
        userIdVO.setUserPwd(req.getUserPwd());

        if (this.checkIdPwd(userIdVO) != ReturnType.RTN_TYPE_OK) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG;
        }

        userInfoVO.setUserPwd(passwordHandler.encode(req.getNewUserPwd()));
        Map<String, Object> params2 = ConvertUtil.convertObjectToMap(userInfoVO);

        // 패스워드 갱신
        membershipDao.updateUser(params2);

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     *  인증 키를 이용한 비밀번호 찾기
     *
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public ReturnType changePwdByVerifyKey(ChangePasswordByCodeReq req) throws Exception {

        // 유저정보 확인
        Map<String, Object> params = ConvertUtil.convertObjectToMap(req);
        UserInfoVO userInfoVO = membershipDao.selectUser(params);

        // 해당 유저가 없을 때
        if (userInfoVO == null) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_NO_EXIST_NG;
        }

        // 인증키가 맞지 않을 때
        if(!req.getVerifyCode().equals(userInfoVO.getVerifyKey())) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_AUTHORITY_NG;
        }

        //  신규 패스워드 pattern 확인
        if (configValidationCheck.checkPwd(req.getNewUserPwd()) != 0) {
            return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG;
        }

        // 인증 키 초기 화
        params.clear();
        params.put("userSeq",userInfoVO.getUserSeq());
        params.put("userPwd",passwordHandler.encode(req.getNewUserPwd()));
        params.put("verifyKey","Y");
        membershipDao.updateUser(params);

        return ReturnType.RTN_TYPE_OK;
    }

    /**
     * Spring Security 암호화 함수 Override
     *
     * @return
     */
    @Override
    public PasswordEncoder passwordEncoder() {
        return passwordHandler.getPasswordEncoder();
    }

    /**
     *
     * 현재 로그인한 유저의 세션 정보를 가져 온다.
     *
     * @return
     * @throws Exception
     */
    @Override
    public MembershipInfo currentSessionUserInfo() throws Exception {

        if (!commonUtil.isExist(SecurityContextHolder.getContext())) {
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!commonUtil.isExist(authentication)) {
            return null;
        }

        if (authentication.getPrincipal() == null) {
            return null;
        }

        if (authentication.getPrincipal().equals("anonymousUser") ||
                (authentication.getPrincipal() == null)) {
            return null;
        }

        MembershipInfo user = (MembershipInfo) authentication.getPrincipal();

        return user;
    }



    /**
     * 회원 가입
     * 회원 가입을 위한 함수
     *
     * @param req
     * @return
     */
    public ReturnType signUp(SignupReq req) throws Exception {
        logger.info("[Service][SignUp]");

        // 로직을 다른 누군가 실행 중일 때는 대기 .
        signUpSemaphore.acquire();
        try {
            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 1. User id 검증
            ///////////////////////////////////////////////////////////////////////////////////////////////
            Map<String, Object> checkIdMap = new HashMap<>();
            checkIdMap.put("userId", req.getUserId());
            if (this.checkExistInUser(checkIdMap)) {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_EXIST_NG;
            }

            // 2) email의 pattern
            if (configValidationCheck.checkEmail(req.getUserId()) != 0) {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_EMAIL_PATTERN_NG;
            }

            logger.info("[Service][SignUp] USER ID OK");
            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 2. Password 검증 및 암호화
            ///////////////////////////////////////////////////////////////////////////////////////////////
            String pwd = req.getUserPwd();

            // 2) Id의 pattern
            if (configValidationCheck.checkPwd(pwd) != 0) {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG;
            }

            // 3) 암호화
            String encPassword = passwordHandler.encode(pwd);

            if (!passwordHandler.matches(pwd, encPassword)) {
                logger.error("[Service][SignUp]PWD ENC ERR");
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_ENC_NG;
            }
            req.setUserPwd(encPassword);


            logger.info("[Service][SignUp] USER PWD OK");
            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 3. 디폴트 설정
            ///////////////////////////////////////////////////////////////////////////////////////////////
            // authority level
            req.setAuthority(configMembership.getSelectAuthorityLevelDefault());


            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 4. 데이터 저장
            ///////////////////////////////////////////////////////////////////////////////////////////////
            int mnNo = membershipDao.getUserMnNoCnt(configMembership.getSelectAuthorityLevelDefault()) + 1;
            req.setMnNo(String.format("u%06d", mnNo));

            Map<String, Object> params = ConvertUtil.convertObjectToMap(req);
            membershipDao.insertUser(params);
            membershipDao.insertProfile(params);

            //인증메일 발송
            sendEmailVerify(params);

        } catch (Exception e) {
            logger.error("signUp[Exception]", e);
            signUpSemaphore.release();
            throw new Exception(e);
        }

        signUpSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }

    /**
     * 인증 메일 발송
     *
     * @return
     * @throws Exception
     */
    public ResponseHandler sendEmailVerify(Map<String, Object> params) {

        logger.info("sendEmailVerify");
        ResponseHandler result = new ResponseHandler();

        try {
            /*MembershipInfo session = this.currentSessionUserInfo();
            if(session == null || session.getUserSeq() == 0) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return result;
            }*/

            String emailVerify = CommonUtil.getRandomNumber(4);
            String userId = params.get("userId").toString();
            String url = configMembership.getEmailUrlHostName() + configMembership.getEmailVerifyUrl() + "?userId=" + userId + "&verifyKey=" + emailVerify;
            params.put("emailVerify", url);

            String fromEmail = "sutax1@naver.com";
            String toEmail = userId;
            String subject = "[PLATHOME] 이메일 인증";
            String contents = VelocityUtil.getEmailVerifyContents(params);
            mailHandler.sendEmail(fromEmail, toEmail, subject, contents);

            Map<String, Object> paramMember = new HashMap<>();
            paramMember.put("userSeq", params.get("userSeq"));
            paramMember.put("verifyKey", emailVerify);
            paramMember.put("emailVerifyEndTime", configMembership.getEmailVerifyEndTime());
            membershipDao.updateUser(paramMember);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);

        } catch(Exception e) {
            logger.error("sendEmailVerify[Exception]", e);
            logger.error("sendEmailVerify[Exception]" + e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);

        }

        return result;

    }

    /**
     * 인증 메일 발송
     *
     * @return
     * @throws Exception
     */
    public ResponseHandler sendEmailVerifySession() {

        logger.info("sendEmailVerifySession");
        ResponseHandler result = new ResponseHandler();

        try {
            MembershipInfo session = this.currentSessionUserInfo();
            if(session == null || session.getUserSeq() == 0) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return result;
            }

            Map<String, Object> params = new HashMap<>();

            String emailVerify = CommonUtil.getRandomEnNumber(6);
            String url = configMembership.getEmailUrlHostName() + configMembership.getEmailVerifyUrl() + "?userId=" + session.getUsername() + "&verifyKey=" + emailVerify;
            params.put("emailVerify", url);

            String fromEmail = "sutax1@naver.com";
            String toEmail = session.getUsername();
            String subject = "[PLATHOME] 이메일 인증";
            String contents = VelocityUtil.getEmailVerifyContents(params);
            mailHandler.sendEmail(fromEmail, toEmail, subject, contents);

            Map<String, Object> paramMember = new HashMap<>();
            paramMember.put("userSeq", session.getUserSeq());
            paramMember.put("verifyKey", emailVerify);
            paramMember.put("emailVerifyEndTime", configMembership.getEmailVerifyEndTime());
            membershipDao.updateUser(paramMember);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);

        } catch(Exception e) {
            logger.error("sendEmailVerifySession[Exception]", e);
            logger.error("sendEmailVerifySession[Exception]" + e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);

        }

        return result;

    }

    /**
     * 이메일 인증 처리
     *
     * @return
     * @throws Exception
     */
    public ResponseHandler emailCertify(EmailVerify req) {

        logger.info("emailCertify");
        ResponseHandler result = new ResponseHandler();

        try {

            Map<String, Object> params = new HashMap<>();
            params.put("userId", req.getUserId());
            if("Y".equals(req.getVerifyKey())) {
                result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_EMAIL_VERIFY_NG);
                return result;
            }
            params.put("verifyKey", req.getVerifyKey());

            UserInfoVO userInfo = membershipDao.selectUser(params);
            if(userInfo == null) {
                result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_EMAIL_VERIFY_NG);
                return result;
            }

            Map<String, Object> paramMember = new HashMap<>();
            paramMember.put("userSeq", userInfo.getUserSeq());
            paramMember.put("certificateYn", "Y");
            membershipDao.updateUser(paramMember);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);

        } catch(Exception e) {
            logger.error("emailCertify[Exception]", e);
            logger.error("emailCertify[Exception]" + e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);

        }

        return result;

    }

    @Override
    public ResponseHandler<?> myPage(@Valid MyPageReq req) {
        ResponseHandler result = new ResponseHandler();

        try {
            if(this.currentSessionUserInfo()==null){
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return result;
            }

            int userSeq = this.currentSessionUserInfo().getUserSeq();
            Map params = new HashMap();
            params.put("userSeq", userSeq );
            UserInfoVO userInfoVO = membershipDao.selectUser(params);
            userInfoVO.setUserPwd(null);
            userInfoVO.setAuthority(null);
            userInfoVO.setUpdDt(null);
            userInfoVO.setUpdId(null);
            userInfoVO.setCreDt(null);
            userInfoVO.setCreDt(null);
            userInfoVO.setUserSeq(null);

            Map profile = membershipDao.selectProfile(params);

            Map mapInfo = new HashMap();
            mapInfo.put("user",userInfoVO);
            mapInfo.put("profile",profile);

            result.setData(mapInfo);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch(Exception e) {
            logger.error("myPage[Exception]", e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    @Override
    public ResponseHandler<?> UpdateUser(@Valid UserReq req) {
        ResponseHandler result = new ResponseHandler();

        try {
            if(this.currentSessionUserInfo()==null){
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return result;
            }

            int userSeq = this.currentSessionUserInfo().getUserSeq();
            Map params = ConvertUtil.convertObjectToMap(req);
            params.put("userSeq",userSeq);
            membershipDao.updateUser(params);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch(Exception e) {
            logger.error("UpdateUser[Exception]", e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    @Override
    public ResponseHandler<?> UpdateProfile(@Valid ProfileReq req) {
        ResponseHandler result = new ResponseHandler();

        try {
            if(this.currentSessionUserInfo()==null){
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return result;
            }

            int userSeq = this.currentSessionUserInfo().getUserSeq();
            Map params = ConvertUtil.convertObjectToMap(req);
            params.put("userSeq",userSeq);
            membershipDao.updateProfile(params);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch(Exception e) {
            logger.error("UpdateProfile[Exception]", e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    @Override
    public ResponseHandler<?> UpdateMyPwd(@Valid ChangePasswordReq req) {
        ResponseHandler result = new ResponseHandler();

        try {
            if (configValidationCheck.checkPwd(req.getNewUserPwd()) != 0) {
                result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG);
                return result;
            }

            Map params = new HashMap();
            params.put("userSeq",req.getUserSeq());
            params.put("userPwd",passwordHandler.encode(req.getNewUserPwd()));

            membershipDao.updateUser(params);
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
        } catch(Exception e) {
            logger.error("UpdateMyPwd[Exception]", e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    /**
     * 비밀번호 찾기
     *
     * @param req
     * @return
     * @throws Exception
     */
    public ReturnType findPwd(FindPwdReq req) throws Exception {

        logger.info("[Service][FindPwd]");

        Map<String, Object> params = ConvertUtil.convertObjectToMap(req);

        // Get User Id data
        // DB에서 유저 정보를 읽어 온다.
        UserInfoVO userInfoVO = membershipDao.selectUser(params);

        // 해당 유저가 존재 하지 않을 때
        if (!commonUtil.isExist(userInfoVO)) {
            logger.error("[Service][FindPwd] no userId");
            return ReturnType.RTN_TYPE_MEMBERSSHIP_ID_EXIST_NG;
        }

        String userId = req.getUserId();
        int userSeq = userInfoVO.getUserSeq();
        String email = userInfoVO.getEmail();

        logger.info("[Service][FindPwd] email:" + email);

        String verifyKey = passwordHandler.makeRandomKey(6);

        HashMap tempUser = new HashMap();
        tempUser.put("userSeq", userSeq);
        tempUser.put("verifyKey", verifyKey);

        membershipDao.updateUser(tempUser);

        //////////////////////////////////////////////////////////////////////////////////

        String param = String.format("?userId=%s&verifyKey=%s", userId, verifyKey);

        //비밀번호 재설정 URL
        String url = configMembership.getEmailUrlHostName() + configMembership.getPasswordConfirmUrl() + param;
        // 임시 비밀번호를 전송한다.
        MailReq mailReq = new MailReq();
        mailReq.setMailTitle("비밀번호를 재설정하세요.");
        mailReq.setToEmail(userId);
        mailReq.setVelocityPath("templates/velocity/pwdSetting.vm");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("url", url);
        mailHandler.sendVolocityMail(mailReq, velocityContext);
        //////////////////////////////////////////////////////////////////////////////////

        return ReturnType.RTN_TYPE_OK;

    }

    /**
     *
     * 유저 존재를 확인
     *
     * @param params
     * @return
     */
    private boolean checkExistInUser(Map<String, Object> params) {

        // user 데이터에서 찾기
        UserInfoVO resMemberData = membershipDao.selectUser(params);

        if (commonUtil.isNotEmpty(resMemberData)) {
            return true;
        }

        return false;
    }


    /**
     *  인증 레벨을 인증 스트링으로 변환
     *
     * @param authorityLevel
     * @return
     */
    private List<String> chgAuthoritiesToStr(int authorityLevel) {

        List<String> authorities = new ArrayList<String>();

        // Check : unauthority
        if (authorityLevel == configMembership.getSelectUnauthorityUserNo()) {
            authorities.add(configMembership.getSelectUnauthorityUserStr());
        } else {

            // Check : General User authority
            if ((authorityLevel & configMembership.getSelectAuthorityUserNo()) > 0) {
                authorities.add(configMembership.getSelectAuthorityUserStr());
            }

            // Check : Contents Admin authority
            if ((authorityLevel & configMembership.getSelectAuthorityContentsAdminNo()) > 0) {
                authorities.add(configMembership.getSelectAuthorityContentsAdminStr());
            }

            // Check : General Admin authority
            if ((authorityLevel & configMembership.getSelectAuthorityAdminNo()) > 0) {
                authorities.add(configMembership.getSelectAuthorityAdminStr());
            }

            // Check : Super Admin authority
            if ((authorityLevel & configMembership.getSelectAuthoritySuperAdminNo()) > 0) {
                authorities.add(configMembership.getSelectAuthoritySuperAdminStr());
            }
        }
        return authorities;
    }

    /**
     * 사용자의 권한을 설정 한다.
     * Session context에 담길 user 권한을 String Array 형태로 저장 한다.
     *
     * @param authorityLevel : User 권한 값
     * @return authorities : 권한 Array 값.
     */
    private Collection<GrantedAuthority> getAuthorities(int authorityLevel) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        // Check : unauthority
        if (authorityLevel == configMembership.getSelectUnauthorityUserNo()) {
            authorities.add(new SimpleGrantedAuthority(configMembership.getSelectUnauthorityUserStr()));
        } else {

            // Check : General User authority
            if ((authorityLevel & configMembership.getSelectAuthorityUserNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthorityUserStr()));
            }

            // Check : Contents Admin authority
            if ((authorityLevel & configMembership.getSelectAuthorityContentsAdminNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthorityContentsAdminStr()));
            }

            // Check : General Admin authority
            if ((authorityLevel & configMembership.getSelectAuthorityAdminNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthorityAdminStr()));
            }

            // Check : Super Admin authority
            if ((authorityLevel & configMembership.getSelectAuthoritySuperAdminNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthoritySuperAdminStr()));
            }
        }

        return authorities;
    }

    public ResponseHandler<SessionRes> getSessionInfo(SessionReq req, HttpServletRequest request, HttpServletResponse response) {
        ResponseHandler<SessionRes> result = new ResponseHandler();
        try {

            String th = CookieUtil.getCookie(request, "TH");
            String encTpl = CookieUtil.getCookie(request, "TPL");
            String tsign = CookieUtil.getCookie(request, "TSIGN");

            String newAccessToken = th + "." + encTpl + "." + tsign;

            SessionRes res = jwtService.getJwtParse(newAccessToken, request, response);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            result.setData(res);

        } catch(Exception e) {
            logger.error("getSessionInfo[Exception]", e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            return result;
        }

        return result;
    }

}