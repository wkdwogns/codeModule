package com.nexon.membership.customHandler;

import com.nexon.common.type.ReturnType;
import com.nexon.membership.config.ConfigMembership;
import com.nexon.membership.dao.MembershipDao;
import com.nexon.membership.dto.model.UserInfoVO;
import com.nexon.membership.service.MembershipInfo;
import com.nexon.membership.service.MembershipService;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MembershipService membershipService;

    @Autowired
    MembershipDao membershipDao;

    @Autowired
    ConfigMembership configMembership;

    // 세션에 저장된 리퀘스트들을 가져온다.
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {


        logger.info("Login Success Handler");

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MembershipInfo user = (MembershipInfo) authentication.getPrincipal();
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        ReadableUserAgent agent = parser.parse(request.getHeader("user-agent"));

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();

        if(configMembership.isEmailCeiryfyYn()) {
            //이메일 인증 체크
            Map<String, Object> map = new HashMap<>();
            map.put("userSeq", user.getUserSeq());
            UserInfoVO userVO = membershipDao.selectUser(map);

            if("Y".equals(userVO.getVerifyKey())) {
                writer.write(ReturnType.RTN_TYPE_OK.getStrValue() );
            } else {
                writer.write(ReturnType.RTN_TYPE_MEMBERSSHIP_EMAIL_UNVERIFY.getStrValue() );
            }
        } else {
            writer.write(ReturnType.RTN_TYPE_OK.getStrValue() );
        }

        writer.flush();

        clearAuthenticationAttributes(request);
    }

}
