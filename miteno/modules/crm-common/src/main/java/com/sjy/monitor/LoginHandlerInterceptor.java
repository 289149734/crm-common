/**
 * 
 */
package com.sjy.monitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sjy.constant.SessionParamType;

/**
 * 校验是否登录拦截器
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年3月13日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
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
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO HttpSession session = request.getSession();
		//Long orgId = (Long) session.getAttribute(SessionParamType.ORGID);
		//Long userId = (Long) session.getAttribute(SessionParamType.USERID);
		//log.debug("[{}]当前登录机构ID：{}, 当前登录操作员ID: {}", request.getRequestURI(), orgId, userId);
		//if (orgId == null || userId == null) return false;
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
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		// TODO Auto-generated method stub
		// log.debug("LoginHandlerInterceptor_postHandle");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		// log.debug("LoginHandlerInterceptor_afterCompletion");
	}

}
