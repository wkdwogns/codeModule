package com.nexon.membership.customHandler;

import com.nexon.common.type.ReturnType;
import com.nexon.membership.config.ConfigMembership;
import com.nexon.membership.service.MembershipService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *  로그인 실패 핸들러
 *
 *  로그인이 실패한 이유를 확인하고 , 그에 맞는 에러 정보를 리턴 한다.
 *
 */
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    ConfigMembership configMembership;

    //@Autowired
    //ConfigMembershipVar var;

    @Autowired
    MembershipService membershipService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg)
            throws IOException, ServletException {

        Map<String,Object> memberData = new HashMap();

        memberData.put("userId", request.getParameter("userId"));
        memberData.put("userPwd", request.getParameter("userPwd"));

        logger.error("[LoginFailureHandler] ID : " + request.getParameter("userId"));
        logger.error("[LoginFailureHandler] Exception : " + arg.getMessage());

        PrintWriter writer = response.getWriter();

        // 400 error
        response.setStatus(HttpStatus.SC_BAD_REQUEST);

        try {

            //ReturnType rtn2 = membershipService.chkIdPwd(memberData);
            ReturnType rtn = ReturnType.RTN_TYPE_OK;

            switch(rtn) {

                default:
                case RTN_TYPE_MEMBERSSHIP_USERID_NO_EXIST_NG:
                    // 아이디가 존재 하지 않는 경우
                    writer.write(ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_NO_EXIST_NG.getStrValue());
                    logger.error("User id error" );
                    break;

                case RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG:
                    // 패스위드가 틀린 경우
                    writer.write(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG.getStrValue());
                    logger.error("Password match error" );
                    break;

                case RTN_TYPE_OK:
                    // 정지된 계정일 경우
                    writer.write(ReturnType.RTN_TYPE_MEMBERSSHIP_USER_ID_PENDING_NG.getStrValue());
                    logger.error("pending id error" );
                    break;
            }
        }
        catch (Exception e) {
            ;
        }

        try {
            //membershipService.setMyLogInfo(loginConnectionLogReq);
        }
        catch (Exception e) {
            logger.error(e.toString());
        }

        writer.flush();
    }
}
