package com.nexon.common.util;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void setCookie(HttpServletResponse response, String key, String value, String domain, int expire){
        // 회원번호를 쿠키에 지정한다
		Cookie cookie = new Cookie(key, value) ;

	    // 쿠키 유효기간을 설정한다. 초단위 : 60*60*24= 1일
		cookie.setMaxAge(expire);
		cookie.setDomain(domain);
	    cookie.setPath("/");
	    // 응답헤더에 쿠키를 추가한다.
	    response.addCookie(cookie) ;
    }

    /**
	 * 쿠키가져오기
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String cookieName) {
		// 쿠키값 가져오기
	    Cookie[] cookies = request.getCookies() ;
	    String cValue = "";

	    if(cookies != null){
	        for(int i=0; i < cookies.length; i++){
	            Cookie c = cookies[i] ;
	            // 저장된 쿠키 이름을 가져온다
	            String cName = c.getName();
	            if(StringUtils.isNotEmpty(cookieName) && cookieName.equals(cName)) {
	            	// 쿠키값을 가져온다
		            cValue = c.getValue() ;
	            }
	        }
	    }

	    return cValue;
	}

	public static void removeCookie(HttpServletResponse response, String cookieName, String domain) {
		Cookie cookie = new Cookie(cookieName, null) ;
		cookie.setDomain(domain);
		cookie.setPath("/");
		cookie.setMaxAge(0) ;
 	    response.addCookie(cookie);
	}

	public static boolean isAjax(HttpServletRequest request) {
        String isAjax = request.getHeader("AJAX");
        if(Boolean.valueOf(isAjax).booleanValue()) {
            return true;
        } else {
            return false;
        }
    }



}
