package com.nexon.common.VaildationCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 프론트단에서 validation check rest api
 *
 * 프론트단에서 validation check를 back과 동일하게 하는 경우.
 * REST API를 통해 진행 할 수 있다.
 * 장점 : front 단에 할 경우, 사용자가 임의의 skip할 수 있는 가능성을 배제.
 *
 */
@RestController
@RequestMapping(value="/validationCheck" , method = RequestMethod.POST)
public class ValidationCheckController {

    @Autowired
    private ConfigValidationCheck configValidationCheckCheck;

    @RequestMapping(value="/getAllRegex" , method = RequestMethod.GET)
    public Map getAllRegex() {
        Map<String, Object> result = new HashMap<>();
        ConfigValidationCheck.Id idMap = configValidationCheckCheck.getId();
        ConfigValidationCheck.Pwd pwdMap = configValidationCheckCheck.getPwd();
        ConfigValidationCheck.Email emailMap = configValidationCheckCheck.getEmail();
        ConfigValidationCheck.Phone phoneMap = configValidationCheckCheck.getPhone();
        ConfigValidationCheck.Nickname nicknameMap = configValidationCheckCheck.getNickname();

        result.put("idValidInfo", idMap);
        result.put("pwdValidInfo", pwdMap);
        result.put("emailInfo", emailMap);
        result.put("phoneInfo", phoneMap);
        result.put("nickNameInfo", nicknameMap);
        return result;
    }

    @RequestMapping("/checkIdRegex")
    public Map checkIdRegex(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put("result", ""+configValidationCheckCheck.checkId(request.getParameter("id")));
        return result;
    }

    @RequestMapping("/checkPwdRegex")
    public Map checkPwdRegex(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put("result", ""+configValidationCheckCheck.checkPwd(request.getParameter("pwd")));
        return result;
    }

    @RequestMapping("/checkPhoneRegex")
    public Map checkPhoneRegex(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put("result", ""+configValidationCheckCheck.checkPhone(request.getParameter("phone")));
        return result;
    }

    @RequestMapping("/checkEmailRegex")
    public Map checkEmailRegex(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put("result", ""+configValidationCheckCheck.checkEmail(request.getParameter("email")));
        return result;
    }
}
