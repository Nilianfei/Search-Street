package com.graduation.ss.service;

import com.graduation.ss.dto.WechatAuthExecution;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
	/**
	 * 通过openId查找平台对应的微信帐号
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * 注册本平台的微信帐号
	 * 
	 * @param wechatAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth, PersonInfo personInfo) throws WechatAuthOperationException;
}
