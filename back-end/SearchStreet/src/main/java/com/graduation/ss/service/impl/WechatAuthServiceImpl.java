package com.graduation.ss.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.ss.cache.JedisUtil;
import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.dao.WechatAuthDao;
import com.graduation.ss.dto.WechatAuthAndEnableStatus;
import com.graduation.ss.dto.WechatAuthExecution;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.WechatAuthStateEnum;
import com.graduation.ss.exceptions.WechatAuthOperationException;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.PageCalculator;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;

	@Override
	@Transactional
	public WechatAuth getWechatAuthByOpenId(String openId) throws WechatAuthOperationException {
		// 定义redis的key
		String key = openId;
		// 定义接收对象
		WechatAuthAndEnableStatus wechatAuthAndEnableStatus = new WechatAuthAndEnableStatus();
		// 定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();

		WechatAuth wechatAuth;
		// 判断key是否存在
		if (!jedisKeys.exists(key)) {
			// 若不存在，则从数据库里面取出相应数据
			wechatAuth = wechatAuthDao.queryWechatByOpenId(openId);
			PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(wechatAuth.getUserId());
			wechatAuthAndEnableStatus.setWechatAuth(wechatAuth);
			wechatAuthAndEnableStatus.setEnableStatus(personInfo.getEnableStatus());
			// 将相关的实体类集合转换成string,存入redis里面对应的key中
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(wechatAuthAndEnableStatus);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new WechatAuthOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		} else {
			// 若存在，则直接从redis里面取出相应数据
			String jsonString = jedisStrings.get(key);
			try {
				// 将相关key对应的value里的的string转换成对象的实体类集合
				wechatAuthAndEnableStatus = mapper.readValue(jsonString, WechatAuthAndEnableStatus.class);
				wechatAuth = wechatAuthAndEnableStatus.getWechatAuth();
			} catch (JsonParseException e) {
				e.printStackTrace();
				throw new WechatAuthOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				throw new WechatAuthOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new WechatAuthOperationException(e.getMessage());
			}
		}

		int enableStatus = wechatAuthAndEnableStatus.getEnableStatus();
		if (enableStatus == 0) {
			throw new WechatAuthOperationException("禁止此用户使用");
		}
		return wechatAuth;
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth, PersonInfo personInfo)
			throws WechatAuthOperationException {
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

	@Override
	public void updatePersonInfo(WechatAuth wechatAuth, PersonInfo personInfo) throws WechatAuthOperationException {
		// 空值判断
		if (wechatAuth == null || wechatAuth.getOpenId() == null) {
			throw new WechatAuthOperationException(
					"updatePersonInfo error: " + WechatAuthStateEnum.NULL_AUTH_INFO.getStateInfo());
		}
		try {
			if (wechatAuth.getUserId() != null) {
				try {
					//该方法不能修改用户类型
					personInfo.setUserType(null);
					personInfo.setUserId(wechatAuth.getUserId());
					personInfo.setLastEditTime(new Date());
					int effectedNum = personInfoDao.updatePersonInfo(personInfo);
					wechatAuth.setUserId(personInfo.getUserId());
					if (effectedNum <= 0) {
						throw new WechatAuthOperationException("修改用户信息失败");
					}
				} catch (Exception e) {
					throw new WechatAuthOperationException("updatePersonInfo error: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			throw new WechatAuthOperationException("updatePersonInfo error: " + e.getMessage());
		}
	}

	@Override
	public WechatAuthExecution getWechatAuthList(int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的微信账号列表
		List<WechatAuth> wechatAuthList = wechatAuthDao.queryWechatList(rowIndex, pageSize);
		// 依据相同的查询条件，返回微信账户总数
		int count = wechatAuthDao.queryWechatCount();
		WechatAuthExecution wae = new WechatAuthExecution();
		if (wechatAuthList != null) {
			wae.setWechatAuthList(wechatAuthList);
			wae.setCount(count);
		} else {
			wae.setState(WechatAuthStateEnum.INNER_ERROR.getState());
		}
		return wae;
	}

	@Override
	public WechatAuth getWechatAuthByUserId(long userId) {
		return wechatAuthDao.queryWechatByUserId(userId);
	}

}
