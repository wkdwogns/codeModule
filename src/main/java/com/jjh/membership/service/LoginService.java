package com.jjh.membership.service;

import com.jjh.common.config.ConfigCommon;
import com.jjh.common.dto.res.ResponseHandler;
import com.jjh.common.password.PasswordHandler;
import com.jjh.common.type.ReturnType;
import com.jjh.common.util.CommonUtil;
import com.jjh.common.util.CookieUtil;
import com.jjh.membership.config.ConfigMembership;
import com.jjh.membership.dao.MembershipDao;
import com.jjh.membership.dto.model.UserInfoVO;
import com.jjh.membership.dto.req.LoginReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MembershipDao membershipDao;

    @Autowired
    ConfigMembership configMembership;

    @Autowired
    ConfigCommon configCommon;

    @Autowired
    PasswordHandler passwordHandler;

    @Autowired
    JwtService jwtService;

    public ResponseHandler loginOauth(LoginReq req, HttpServletResponse response) {
        ResponseHandler result = new ResponseHandler();
        try {

            //UserInfoVO userInfoVO = membershipDao.selectLoginUserInfo(req);
            Map<String, Object> loginMap = new HashMap<>();
            loginMap.put("userId", req.getUserId());
            UserInfoVO userInfoVO = membershipDao.selectUserInfo(loginMap);

            if(ObjectUtils.isEmpty(userInfoVO)){
                result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_USER_NO_EXIST_NG);
                return result;
            }

            String pwd = req.getUserPwd();
            String encPassword = userInfoVO.getUserPwd();

            if(!passwordHandler.matches(pwd, encPassword)) {
                result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_ENC_NG);
                return result;
            }

            if(!"Y".equals(userInfoVO.getCertificateYn())) {
                result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_EMAIL_UNVERIFY);
                return result;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("userSeq", userInfoVO.getUserSeq());
            map.put("userId", userInfoVO.getUserId());
            map.put("authority", userInfoVO.getAuthority());
            map.put("userName", userInfoVO.getUserName());
            String jwtString = jwtService.createJwtBuilder(map);

            int expire = 0;
            if((CommonUtil.isNotEmpty(req.getForever()) && "Y".equals(req.getForever()))) {
                expire = configMembership.getCookie().getExpire();
                CookieUtil.setCookie(response, "TFO", req.getForever(), configMembership.getCookie().getDomain(), expire);
            } else {
                expire = -1;
            }

            //쿠키 저장
            jwtService.setJwtCookie(jwtString, response, expire, userInfoVO.getUserSeq());

            result.setReturnCode(ReturnType.RTN_TYPE_OK);

        } catch(Exception e) {
            logger.error("loginOauth[Exception]", e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            return result;
        }

        return result;
    }

    public ResponseHandler logoutOauth(HttpServletResponse response) {
        ResponseHandler result = new ResponseHandler();
        try {

            //쿠키 저장
            jwtService.removeSession(response);

            result.setReturnCode(ReturnType.RTN_TYPE_OK);

        } catch(Exception e) {
            logger.error("loginOauth[Exception]", e);
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
            return result;
        }

        return result;
    }

}
