package com.jjh.common.session;

import com.jjh.common.dto.req.CommonReq;
import com.jjh.common.type.ReturnType;
import com.jjh.common.util.CommonUtil;
import com.jjh.common.util.CookieUtil;
import com.jjh.membership.config.ConfigMembership;
import com.jjh.membership.dto.res.SessionRes;
import com.jjh.membership.service.JwtService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class SessionCheckAspect {
    @Autowired
    JwtService jwtService;

    @Autowired
    ConfigMembership configMembership;

    @Around(value="@annotation(sessionCheck)")
    public Object target(ProceedingJoinPoint joinPoint, SessionCheck sessionCheck) throws Throwable {
        Object result = null;

        Object[] params = joinPoint.getArgs();
        for(int i=0; i < params.length; i++){
            System.out.println("args[" + i + "] : " + params[i].toString());
        }



        if(params[0] != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();

            String th = CookieUtil.getCookie(request, "TH");
            String tpl = CookieUtil.getCookie(request, "TPL");
            String tsign = CookieUtil.getCookie(request, "TSIGN");
            String accessToken = th + "." + tpl + "." + tsign;

            String isApp = request.getHeader("isApp");
            if(CommonUtil.isNotEmpty(isApp) && "Y".equals(isApp)) {
                accessToken = request.getHeader("accessToken");
            }

            SessionRes sessionRes = jwtService.getJwtParse(accessToken, request, response);

            if(sessionRes != null) {
                ((CommonReq)params[0]).setUserSeq(sessionRes.getUserSeq());
                ((CommonReq)params[0]).setUserId(sessionRes.getUserId());
            } else {
                if(CookieUtil.isAjax(request)) {
                    jwtService.removeSession(response);
                    response.sendError(ReturnType.RTN_TYPE_SESSION.getValue());
                }
                throw new Exception();
            }
        }

        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        }

        return result;

    }

}
