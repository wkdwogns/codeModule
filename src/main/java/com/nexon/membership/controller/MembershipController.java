package com.nexon.membership.controller;

import com.nexon.common.dto.res.ResponseHandler;
import com.nexon.common.file.config.ConfigFile;
import com.nexon.common.file.service.FileService;
import com.nexon.common.session.SessionCheck;
import com.nexon.common.type.ReturnType;
import com.nexon.common.util.CommonUtil;
import com.nexon.membership.config.ConfigMembership;
import com.nexon.membership.dto.req.*;
import com.nexon.membership.dto.res.*;
import com.nexon.membership.service.MembershipInfo;
import com.nexon.membership.service.MembershipService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 *  멤버쉽 Rest Controller class
 *
 *  사용자를 제어하기 위한 REST API가 정의 되어 있다.
 *
 *
 */
@RestController
@RequestMapping(value="/membership" )
public class MembershipController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConfigMembership configMembership;

	@Autowired
	MembershipService membershipService;

	@Autowired
	FileService fileService;

	@Autowired
	ConfigFile configFile;

	@PostMapping(value = "/check/live/session")
	@ApiOperation(value = "checkLiveSession", notes = "세션이 열려있는지 확인")
	public ResponseHandler<SessionRes> checkLiveSession() {

		ResponseHandler<SessionRes> result = new ResponseHandler<>();

		try {

			MembershipInfo sessionInfo = membershipService.currentSessionUserInfo();

			//1. 토큰 유효성 체크
			if (sessionInfo != null) {
				result.setReturnCode(ReturnType.RTN_TYPE_OK);
				SessionRes sessionRes = new SessionRes();
				sessionRes.setUserSeq(sessionInfo.getUserSeq());
				sessionRes.setAuthorityLevel(sessionInfo.getAuthoritiesLevel());
				sessionRes.setAuthorityStr(sessionInfo.getAuthoritiesStr());
				sessionRes.setSuspended(sessionInfo.getIsSuspended());
				result.setData(sessionRes);

			} else {
				result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
			}
		} catch (Exception e) {
			logger.error("[checkLiveSession][Exception] " + e.toString());
			result.setReturnCode(ReturnType.RTN_TYPE_NG);
		}

		return result;
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/change/pwd")
	@ApiOperation(value = "비밀번호 변경")
	@SessionCheck
	public ResponseHandler<?> changePassword(@Valid @RequestBody(required = false) final ChangePasswordReq req) {
		ResponseHandler<?> result = new ResponseHandler<>();
		ReturnType rtn;

		try {
			if (CommonUtil.isNotEmpty(req.getUserSeq())) {
				rtn = membershipService.changePwd(req);
				result.setReturnCode(rtn);
			} else {
				result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
			}
		} catch (Exception e) {
			logger.error("[CheckId][Exception] " + e.toString());
			result.setReturnCode(ReturnType.RTN_TYPE_NG);
		}

		return result;
	}


	/**
	 * 회원 가입
	 *
	 * @param : membership 설정값
	 * @return : ReturnType
	 */
	@ApiOperation(value = "회원가입")
	@PostMapping(value = "/signUp")
	public ResponseHandler<?> signUp(@Valid @RequestBody(required = false) final SignupReq req) {
		final ResponseHandler<?> result = new ResponseHandler<>();
		ReturnType rtn;
		try {
			rtn = membershipService.signUp(req);
			result.setReturnCode(rtn);
		} catch (Exception e) {
			logger.error("[SignUp][Exception] " + e.toString());
			result.setReturnCode(ReturnType.RTN_TYPE_NG);
		}

		return result;
	}

	/**
	 * 비밀번호 찾기
	 *
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/find/pwd")
	public ResponseHandler<?> findPwd(@Valid FindPwdReq req) {
		final ResponseHandler<?> result = new ResponseHandler<>();
		try {
			ReturnType rtn = membershipService.findPwd(req);
			result.setReturnCode(rtn);
		} catch (Exception e) {
			logger.error("[findPwd][Exception] " + e.toString());
			result.setReturnCode(ReturnType.RTN_TYPE_NG);
			e.printStackTrace();
		}

		return result;
	}



    /**
     * 비밀번호 찾기 후 인증 번호로 변경 시도
     * @param :
     * @return :
     */
    @PutMapping(value="/find/pwd/verify")
    @ApiOperation(value="비밀번호 찾기 이후 변경")
    public ResponseHandler<?> chgPwdByVerifyKey(@Valid @RequestBody(required=false) final ChangePasswordByCodeReq req) {
        final ResponseHandler<?> result = new ResponseHandler<>();

        ReturnType rtn;

        try {
            //1. 토큰 유효성 체크
            rtn = membershipService.changePwdByVerifyKey(req);
            result.setReturnCode(rtn);
        }
        catch(Exception e) {
            logger.error("[CheckId][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    /**
	 * 이메일 인증 메일 발송
	 *
	 * @return
	 */
	@PostMapping(value = "/send/verifyKey")
	public ResponseHandler<?> sendEmailVerifySession() {

		ResponseHandler<?> result = membershipService.sendEmailVerifySession();

		return result;
	}

	/**
	 * 이메일 인증
	 *
	 * @return
	 */
	@PostMapping(value = "/email/certify")
	public ResponseHandler<?> emailCertify(@Valid @RequestBody(required=false) EmailVerify req) {

		ResponseHandler<?> result = membershipService.emailCertify(req);

		return result;
	}

	/**
	 * 마이페이지
	 *
	 * @return
	 */
	@GetMapping(value = "/myPage")
	public ResponseHandler<?> myPage(@Valid @RequestBody(required=false) MyPageReq req) {
		ResponseHandler<?> result = membershipService.myPage(req);
		return result;
	}

	/**
	 * 유저정보 업데이트
	 *
	 * @return
	 */
	@PostMapping(value = "/myPage/user")
	public ResponseHandler<?> UpdateMyPageUser(@Valid @RequestBody(required=false) UserReq req) {
		ResponseHandler<?> result = membershipService.UpdateUser(req);
		return result;
	}

	/**
	 * 프로필 업데이트
	 *
	 * @return
	 */
	@PostMapping(value = "/myPage/profile")
	public ResponseHandler<?> UpdateMyPageProfile(@Valid @RequestBody(required=false) ProfileReq req) {
		ResponseHandler<?> result = membershipService.UpdateProfile(req);
		return result;
	}

	/**
	 * 비밀번호 변경
	 *
	 * @return
	 */
	@PostMapping(value = "/updateMyPwd")
	public ResponseHandler<?> UpdateMyPwd(@Valid @RequestBody(required=false) ChangePasswordReq req) {
		ResponseHandler<?> result = membershipService.UpdateMyPwd(req);
		return result;
	}

	@GetMapping(value = "/session")
	public ResponseHandler<SessionRes> getSessionInfo(SessionReq req, HttpServletRequest request, HttpServletResponse response) {
		ResponseHandler<SessionRes> result = membershipService.getSessionInfo(req, request, response);

		return result;
	}

}


