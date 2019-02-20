package com.graduation.ss.interceptor.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
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
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(null != token) {
			UserCode2Session userCode2Session = JWT.unsign(token, UserCode2Session.class);
			if(null != userCode2Session && null != userCode2Session.getOpenId() && null != userCode2Session.getSession_key()){
				//System.out.println("成功越过拦截器");
				return true;
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "token无效");
				String jsonObjectStr = JSONObject.toJSONString(modelMap);
                returnJson(response,jsonObjectStr);
				//System.out.println(jsonObjectStr);
				return false;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "token为空");
			String jsonObjectStr = JSONObject.toJSONString(modelMap);
            returnJson(response,jsonObjectStr);
			//System.out.println(jsonObjectStr);
			return false;
		}
	}
	private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
 
        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
