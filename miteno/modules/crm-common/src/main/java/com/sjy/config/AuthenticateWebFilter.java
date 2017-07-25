package com.sjy.config;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;

/**
 * @Title: AuthenticateWebFilter.java
 * @Package com.sjy.config
 * @Description: 安全认证过滤器
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年7月24日 下午7:59:42
 * @version V1.0
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "authenticateFilter")
public class AuthenticateWebFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("............初始化【安全认证过滤器】............");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		log.debug("请求路径 :{}, 请求方式:{}", httpRequest.getRequestURI(),
				httpRequest.getMethod());

		Map<String, String[]> requestParams = request.getParameterMap();
		log.debug("请求参数:{}", JSON.toJSONString(requestParams, false));

		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			requestWrapper = new BodyReaderHttpServletRequestWrapper(
					(HttpServletRequest) request);
		}

		if (requestWrapper != null) {
			chain.doFilter(requestWrapper, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		log.debug("............销毁【安全认证过滤器】............");
	}

}
