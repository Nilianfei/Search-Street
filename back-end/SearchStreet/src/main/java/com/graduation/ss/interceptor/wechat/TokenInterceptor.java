package com.graduation.ss.interceptor.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.util.JWT;

/**
 * 
 * 拦截token
 *
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding("utf-8");
		String token = request.getParameter("token");
		if(null != token) {
			UserCode2Session userCode2Session = JWT.unsign(token, UserCode2Session.class);
			if(null != userCode2Session && null != userCode2Session.getOpenId() && null != userCode2Session.getSession_key()){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
