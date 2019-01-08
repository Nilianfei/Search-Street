package com.graduation.ss.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.dao.WechatAuthDao;
import com.graduation.ss.dto.WechatAuthExecution;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.WechatAuthStateEnum;
import com.graduation.ss.exceptions.WechatAuthOperationException;
import com.graduation.ss.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		return wechatAuthDao.queryWechatByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth, PersonInfo personInfo) throws WechatAuthOperationException {
		// 空值判断
		if (wechatAuth == null || wechatAuth.getOpenId() == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			// 设置创建时间
			wechatAuth.setCreateTime(new Date());
			// 如果微信帐号里夹带着用户信息并且用户Id为空，则认为该用户第一次使用平台(且通过微信登录)
			// 则自动创建用户信息
			if (wechatAuth.getUserId() == null) {
				try {
					personInfo.setCreateTime(new Date());
					personInfo.setEnableStatus(1);
					personInfo.setUserType(0);
					int effectedNum = personInfoDao.insertPersonInfo(personInfo);
					wechatAuth.setUserId(personInfo.getUserId());
					if (effectedNum <= 0) {
						throw new WechatAuthOperationException("添加用户信息失败");
					}
				} catch (Exception e) {
					throw new WechatAuthOperationException("insertPersonInfo error: " + e.getMessage());
				}
			}
			// 创建专属于本平台的微信帐号
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if (effectedNum <= 0) {
				throw new WechatAuthOperationException("帐号创建失败");
			} else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
			}
		} catch (Exception e) {
			throw new WechatAuthOperationException("insertWechatAuth error: " + e.getMessage());
		}
	}

}
