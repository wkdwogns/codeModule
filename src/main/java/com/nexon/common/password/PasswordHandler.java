package com.nexon.common.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 패스워드 암호화 및 비교를 위한 클래스
 * 패스워드를 암호화 한다. 또한 암호화 된 문자와 입력된 패스워드를 비교 한다.
 * 인코딩 방식 : BCrypt
 */
@Service
public class PasswordHandler implements PasswordEncoder{

    private PasswordEncoder passwordEncoder;

    public PasswordHandler() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public PasswordHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * password를 암호화 한다.
     *
     * @param rawPassword 암호화 대상 문자열
     * @return 암호화 된 문자열
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 비밀번호를 비교 한다.
     *
     * @param rawPassword
     * @param encodedPassword
     * @return true = 동일 비번, false = 다른 비번
     */
    @Override public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     *  makeRandomKey
     *  숫자, 영문자, 특수문자로 이루어진 랜덤키 생성 함수
     *
     * @param
     * @return
     */
    public String makeRandomKey2() {
        String  pswd = "";
        StringBuffer sb = new StringBuffer();
        StringBuffer sc = new StringBuffer("!@#$%^&*-=?~");  // 특수문자 모음, {}[] 같은 비호감문자는 뺌
        // 대문자 4개를 임의 발생
        sb.append((char)((Math.random() * 26)+65));  // 첫글자는 대문자, 첫글자부터 특수문자 나오면 안 이쁨
        for( int i = 0; i<3; i++) {
            sb.append((char)((Math.random() * 26)+65));  // 아스키번호 65(A) 부터 26글자 중에서 택일
        }
        // 소문자 4개를 임의발생
        for( int i = 0; i<4; i++) {
            sb.append((char)((Math.random() * 26)+97)); // 아스키번호 97(a) 부터 26글자 중에서 택일
        }
        // 숫자 2개를 임의 발생
        for( int i = 0; i<2; i++) {
            sb.append((char)((Math.random() * 10)+48)); //아스키번호 48(1) 부터 10글자 중에서 택일
        }
        // 특수문자를 두개  발생시켜 랜덤하게 중간에 끼워 넣는다
        sb.setCharAt(((int)(Math.random()*3)+1), sc.charAt((int)(Math.random()*sc.length()-1))); //대문자3개중 하나
        sb.setCharAt(((int)(Math.random()*4)+4), sc.charAt((int)(Math.random()*sc.length()-1))); //소문자4개중 하나
        pswd = sb.toString();
        return pswd;
    }

    /**
     *  makeRandomKey
     *  숫자, 영문자로 이루어진 랜덤키 생성 함수
     *
     * @param sizeOfData
     * @return
     */
    public String makeRandomKey(int sizeOfData) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, sizeOfData);
    }
}
