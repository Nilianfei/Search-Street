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
	WechatAuth getWechatAuthByOpenId(String openId) throws WechatAuthOperationException;

	/**
	 * 注册本平台的微信帐号
	 * 
	 * @param wechatAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth, PersonInfo personInfo) throws WechatAuthOperationException;

	/**
	 * 更新用户信息
	 * 
	 * @param wechatAuth
	 * @param personInfo
	 * @return
	 * @throws WechatAuthOperationException
	 */
	void updatePersonInfo(WechatAuth wechatAuth, PersonInfo personInfo) throws WechatAuthOperationException;

	/**
	 * 分页获取微信账号
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	WechatAuthExecution getWechatAuthList(int pageIndex, int pageSize);

	/**
	 * 通过用户ID获取微信账号
	 * 
	 * @param userId
	 * @return
	 */
	WechatAuth getWechatAuthByUserId(long userId);
}
