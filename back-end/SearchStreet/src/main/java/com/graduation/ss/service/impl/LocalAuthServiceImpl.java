package com.graduation.ss.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.cache.JedisUtil;
import com.graduation.ss.dao.LocalAuthDao;
import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.dao.WechatAuthDao;
import com.graduation.ss.dto.LocalAuthExecution;
import com.graduation.ss.entity.LocalAuth;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.LocalAuthStateEnum;
import com.graduation.ss.exceptions.LocalAuthOperationException;
import com.graduation.ss.service.LocalAuthService;
import com.graduation.ss.util.MD5;
import com.graduation.ss.util.PageCalculator;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	@Autowired
	private LocalAuthDao localAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
		LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
		PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(localAuth.getUserId());
		localAuth.setPersonInfo(personInfo);
		return localAuth;
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(userId);
		PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(localAuth.getUserId());
		localAuth.setPersonInfo(personInfo);
		return localAuth;
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		// 空值判断，传入的localAuth 帐号密码，用户信息特别是userId不能为空，否则直接返回错误
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUserName() == null
				|| localAuth.getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		// 查询此用户是否已绑定过平台帐号
		LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getUserId());
		if (tempAuth != null) {
			// 如果绑定过则直接退出，以保证平台帐号的唯一性
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(localAuth.getUserId());
		if (personInfo == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.PERSONINFO_ERR);
		}
		List<String> userNameList = localAuthDao.queryUserName();
		for (String userName : userNameList) {
			if (userName.equals(localAuth.getUserName())) {
				return new LocalAuthExecution(LocalAuthStateEnum.USER_NAME_ERR);
			}
		}
		try {
			WechatAuth wechatAuth = wechatAuthDao.queryWechatByUserId(personInfo.getUserId());
			if(wechatAuth!=null) {
				String openId = wechatAuth.getOpenId();
				jedisKeys.del(openId);
			}
			//修改用户类型为管理员
			personInfo.setUserType(1);
			int effectedNum =personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("用户类型修改失败");
			}
			// 如果之前没有绑定过平台帐号，则创建一个平台帐号与该用户绑定
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			// 对密码进行MD5加密
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			effectedNum = localAuthDao.insertLocalAuth(localAuth);
			// 判断创建是否成功
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("帐号绑定失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch (Exception e) {
			throw new LocalAuthOperationException("insertLocalAuth error: " + e.toString());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword)
			throws LocalAuthOperationException {
		// 非空判断，判断传入的用户Id,帐号,新旧密码是否为空，新旧密码是否相同，若不满足条件则返回错误信息
		if (userId != null && userName != null && password != null && newPassword != null
				&& !password.equals(newPassword)) {
			try {
				// 更新密码，并对新密码进行MD5加密
				int effectedNum = localAuthDao.updateLocalAuth(userId, userName, MD5.getMd5(password),
						MD5.getMd5(newPassword), new Date());
				// 判断更新是否成功
				if (effectedNum <= 0) {
					throw new LocalAuthOperationException("更新密码失败");
				}
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			} catch (Exception e) {
				throw new LocalAuthOperationException("更新密码失败:" + e.toString());
			}
		} else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

	@Override
	public LocalAuthExecution getLocalAuthList(int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的本地账号列表
		List<LocalAuth> localAuthList = localAuthDao.queryLocalList(rowIndex, pageSize);
		// 依据相同的查询条件，返回本地账户总数
		int count = localAuthDao.queryLocalCount();
		LocalAuthExecution lae = new LocalAuthExecution();
		if (localAuthList != null) {
			lae.setLocalAuthList(localAuthList);
			lae.setCount(count);
		} else {
			lae.setState(LocalAuthStateEnum.INNER_ERROR.getState());
		}
		return lae;
	}

}
