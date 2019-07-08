package com.nexon.common.session;

import com.nexon.common.type.ReturnType;
import com.nexon.common.util.CookieUtil;
import com.nexon.membership.dto.res.SessionRes;
import com.nexon.membership.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JwtService jwtService;

    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {

            String th = CookieUtil.getCookie(request, "TH");
            String tpl = CookieUtil.getCookie(request, "TPL");
            String tsign = CookieUtil.getCookie(request, "TSIGN");

            if(tpl.equals("")){
                response.sendRedirect("/page/admin/login");
                return false;
            }

            String accessToken = th + "." + tpl + "." + tsign;
            SessionRes sessionRes = jwtService.getJwtParse(accessToken, request, response);
            if(sessionRes == null) {
                if(CookieUtil.isAjax(request)) {
                    response.setStatus(ReturnType.RTN_TYPE_SESSION.getValue());
                } else {
                    jwtService.removeSession(response);
                    response.sendRedirect("/page/admin/login");
                }
                return false;
            }
        } catch (Exception e) {
            jwtService.removeSession(response);
            logger.error("AuthInterceptor[Exception]", e);
            return false;
        }

        return true;
    }
}
