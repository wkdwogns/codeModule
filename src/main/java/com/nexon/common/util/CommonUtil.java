package com.nexon.common.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by MJ on 2018. 8. 23..
 */

public class CommonUtil {

    /**
     * client IP 가져오기
     * @param request
     * @return String
     * @throws Exception
     */
    public static String getClientIP(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        if (clientIp.contains(",")) {
            return clientIp.split(",")[0].trim();
        }

        return clientIp;

    }


    public static void setCookie(HttpServletResponse response, String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*24*30); //30일
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            //cmd for xls injection...
            Pattern.compile("=cmd(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    };

    public static String htmlXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            //value = ESAPI.encoder().canonicalize(value);


            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }


        }
        return value;
    }


    /**
     * obj mapping 함수
     *
     * @param obj
     * @return
     */
    public static boolean isExist(Object obj) {
        if(obj == null || obj.toString().isEmpty())
        {
            return false;
        }

        return true;
    }

    /**
     * str mapping 함수
     *
     * @param str
     * @return
     */
    public static boolean isExist(String str) {
        if(str == null || str.toString().isEmpty())
        {
            return false;
        }

        return true;
    }


    /**
     * obj mapping 함수
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List) return obj == null || ((List<?>) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map<?, ?>) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }

    /**
     * obj mapping 함수
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     *  시간 정보를 String 형태로 리턴 한다.
     *
     * @param formatType
     * @return
     */
    public String getTimeByString(String formatType) {

        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat(formatType);
        dayTime.setTimeZone( TimeZone.getTimeZone( "UTC" ) );

        String nowTime = dayTime.format(new Date(time));

        return nowTime;
    }

    /**
     *
     * 시간 정보를 DataTime 으로 리턴한다.
     *
     * @return
     */
    public SimpleDateFormat getTimeByDate() {

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dayTime.setTimeZone( TimeZone.getTimeZone( "UTC" ) );

        return dayTime;
    }

    /**
     * 영무+숫자 랜덤 번호 생성
     * @return
     */
    public static String getRandomEnNumber(int length) {
        String result = "";
        Random rnd =new Random();

        StringBuffer buf =new StringBuffer();

        for(int i=0;i<length;i++) {
            if (rnd.nextBoolean()) {
                buf.append((char) ((int) (rnd.nextInt(26)) + 97));
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }

        result = buf.toString();

        return result;

    }

    /**
     * 숫자 랜덤 번호 생성
     * @return
     */
    public static String getRandomNumber(int length) {
        String result = "";
        Random rnd =new Random();

        StringBuffer buf =new StringBuffer();

        for(int i=0;i<length;i++) {
            buf.append((rnd.nextInt(10)));
        }

        result = buf.toString();

        return result;

    }

}
