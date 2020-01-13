package com.jjh.membership.service;

import com.jjh.common.config.ConfigCommon;
import com.jjh.common.util.ComEncDecUtil;
import com.jjh.common.util.CommonUtil;
import com.jjh.common.util.CookieUtil;
import com.jjh.membership.config.ConfigMembership;
import com.jjh.membership.dao.MemberTokenDao;
import com.jjh.membership.dao.MembershipDao;
import com.jjh.membership.dto.model.UserInfoVO;
import com.jjh.membership.dto.model.UserTokenVO;
import com.jjh.membership.dto.req.InsertMemberTokenReq;
import com.jjh.membership.dto.res.SessionRes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ConfigMembership configMembership;

	@Autowired
	MembershipDao membershipDao;

	@Autowired
	MemberTokenDao memberTokenDao;

	@Autowired
	ConfigCommon configCommon;

	/**
	 * JWT AccessToken 생성
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DecoderException
	 */
    public String createJwtBuilder(Map<String, Object> map) {

		try {
			String signatureKey = configMembership.getJwt().getSignature();

			long expireMinute = TimeUnit.HOURS.toMillis(configMembership.getJwt().getExpireHour());
			//long expireMinute = TimeUnit.MINUTES.toMillis(1);

			long nowMillis = System.currentTimeMillis();
			Date now = new Date(nowMillis + expireMinute);

			String accessToken = Jwts.builder()
					.setClaims(map)
					.setExpiration(now)
					.signWith(
							SignatureAlgorithm.HS256,
							signatureKey.getBytes("UTF-8")
					)
					.compact();

			logger.info("createJwtBuilder[accessToken]", accessToken);
			return accessToken;

		} catch (Exception e) {
			logger.error("createJwtBuilder[Exception]", e);
             return null;
		}
	}

	/**
	 * AccessToken Parse 회원 정보 조회
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public SessionRes getNewJwtParse(String accessToken) throws Exception {
		SessionRes res = new SessionRes();
		String signatureKey = configMembership.getJwt().getSignature();

		String[] array = accessToken.split("[.]");
		String refreshToken = ComEncDecUtil.getDecrypted(array[1], configCommon.getAes128Key());
		String decAccessToken = array[0] + "." + refreshToken + "." + array[2];

		Claims claims = Jwts.parser().setSigningKey(signatureKey.getBytes("UTF-8")).parseClaimsJws(decAccessToken).getBody();

		res.setUserSeq(Integer.parseInt(claims.get("userSeq").toString()));
		res.setUserId(claims.get("userId").toString());
		res.setAuthorityLevel(Integer.parseInt(claims.get("authority").toString()));
		res.setUserName(claims.get("userName").toString());

		res.setExp(claims.getExpiration().getTime());

		return res;
	}

	/**
	 * AccessToken Parse 회원 정보 조회
	 * @param accessToken
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SessionRes getJwtParse(String accessToken, HttpServletRequest request, HttpServletResponse response) {
		SessionRes res = null;
		String refreshToken = "";

    	try {
    		if(StringUtils.isNotEmpty(accessToken)) {
				String[] array = accessToken.split("[.]");
				refreshToken = ComEncDecUtil.getDecrypted(array[1], configCommon.getAes128Key());
			}
			res = getNewJwtParse(accessToken);

		} catch (ExpiredJwtException e) {

			String forever = CookieUtil.getCookie(request, "TFO");
			if(CommonUtil.isNotEmpty(forever) && "Y".equals(forever)) {
				//새로운 토큰 발급
				try {
					String newAccesstoken = getAccessTokenToRefresh(refreshToken, response);
					res = getNewJwtParse(newAccesstoken);
				} catch (Exception ex) {
					logger.error("getJwtParse[Exception][getAccessTokenToRefresh]", ex);
				}
			} else {
				CookieUtil.removeCookie(response, "TH", configMembership.getCookie().getDomain());
				CookieUtil.removeCookie(response, "TPL", configMembership.getCookie().getDomain());
				CookieUtil.removeCookie(response, "TSIGN", configMembership.getCookie().getDomain());
			}
		} catch (Exception e) {
    		logger.error("getJwtParse[Exception]", e);
		}

		return res;
	}

	/**
	 * AccessToken 쿠키 저장 및 DB 저장
	 * @param jwtString
	 * @param response
	 * @param expire
	 * @param userSeq
	 * @throws Exception
	 */
	public String setJwtCookie(String jwtString, HttpServletResponse response, int expire, int userSeq) {
    	String newAccessToken = "";
    	try {
    		if(StringUtils.isNotEmpty(jwtString)) {
				String[] array = jwtString.split("[.]");
				String th = array[0];
				String tpl = array[1];
				String encTpl = ComEncDecUtil.getEncrypted(tpl, configCommon.getAes128Key());
				String tsign = array[2];

				CookieUtil.setCookie(response, "TH", th, configMembership.getCookie().getDomain(), expire);
				CookieUtil.setCookie(response, "TPL", encTpl, configMembership.getCookie().getDomain(), expire);
				CookieUtil.setCookie(response, "TSIGN", tsign, configMembership.getCookie().getDomain(), expire);

				newAccessToken = th + "." + encTpl + "." + tsign;

				//토큰 저장
				InsertMemberTokenReq tokenReq = new InsertMemberTokenReq();
				tokenReq.setUserSeq(userSeq);
				tokenReq.setRefreshToken(tpl);
				tokenReq.setCreId(userSeq);
				memberTokenDao.insertUserToken(tokenReq);
			}
		} catch(Exception e) {
    		logger.error("setJwtCookie[Exception]", e);
		}

		return newAccessToken;
	}

	/**
	 * 리프레쉬 토큰으로 AccessToken 조회
	 * @param refreshToken
	 * @return
	 * @throws Exception
	 */
	public String getAccessTokenToRefresh(String refreshToken, HttpServletResponse response) throws Exception {
    	String newAccessToken = "";

    	if(StringUtils.isNotEmpty(refreshToken)) {
			UserTokenVO userTokenVO= memberTokenDao.selectMemberToken(refreshToken);

			Map<String, Object> map = new HashMap<>();
			map.put("userSeq", userTokenVO.getUserSeq());
			UserInfoVO userInfoVO = membershipDao.selectUserInfo(map);

            map.put("userId", userInfoVO.getUserId());
            map.put("authority", userInfoVO.getAuthority());

			newAccessToken = createJwtBuilder(map);

			//쿠키저장 및 토큰 저장
			int expire = configMembership.getCookie().getExpire();
			newAccessToken = setJwtCookie(newAccessToken, response, expire, userTokenVO.getUserSeq());
		}

		return newAccessToken;
	}

	/**
	 * 세션 삭제
	 * @param response
	 */
	public void removeSession(HttpServletResponse response) {
		CookieUtil.removeCookie(response, "TFO", configMembership.getCookie().getDomain());
		CookieUtil.removeCookie(response, "TH", configMembership.getCookie().getDomain());
		CookieUtil.removeCookie(response, "TPL", configMembership.getCookie().getDomain());
		CookieUtil.removeCookie(response, "TSIGN", configMembership.getCookie().getDomain());
	}

}
