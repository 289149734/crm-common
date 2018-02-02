/**
 * 
 */
package com.sjy.monitor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sjy.constant.SessionParamType;

import lombok.extern.slf4j.Slf4j;

/**
 * 校验是否登录拦截器
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * 
 * @since 2017年3月13日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@Aspect
public class LoginHandlerInterceptor implements HandlerInterceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Map<String, String[]> rparams = request.getParameterMap();
		rparams.forEach((k, v) -> {
			log.debug("{} = {}", k, JSON.toJSONString(v, false));
		});
		String sessionId = null;
		List<String> cookies = Collections.list(request.getHeaders("cookie"));
		for (String cookie : cookies) {
			log.debug("cookie>>>>>>[{}]", cookie);
			String[] params = cookie.split("; ");
			for (String param : params) {
				if (param.startsWith("SESSION=")) {
					sessionId = param.replace("SESSION=", "");
					log.debug("session>>>>>>>[{}]", sessionId);
				}
			}
		}

		Long orgId = (Long) request.getSession().getAttribute(SessionParamType.ORGID);
		Long userId = (Long) request.getSession().getAttribute(SessionParamType.USERID);
		log.info("session[{}]--->>[{}]当前登录机构ID：{}, 当前登录操作员ID: {}", sessionId, request.getRequestURI(), orgId, userId);
		// if (orgId == null || userId == null) return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		// log.debug("LoginHandlerInterceptor_postHandle");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		// log.debug("LoginHandlerInterceptor_afterCompletion");
	}

}
