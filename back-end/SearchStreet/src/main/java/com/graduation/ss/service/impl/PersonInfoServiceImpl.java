package com.graduation.ss.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.cache.JedisUtil;
import com.graduation.ss.dao.PersonInfoDao;
import com.graduation.ss.dao.WechatAuthDao;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.PersonInfoExecution;
import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.PersonInfoStateEnum;
import com.graduation.ss.exceptions.PersonInfoOperationException;
import com.graduation.ss.service.PersonInfoService;
import com.graduation.ss.util.ImageUtil;
import com.graduation.ss.util.PageCalculator;
import com.graduation.ss.util.PathUtil;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
	@Autowired
	private PersonInfoDao personInfoDao;
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	public PersonInfo getPersonInfoByUserId(Long userId) {
		return personInfoDao.queryPersonInfoByUserId(userId);
	}

	@Override
	@Transactional
	public PersonInfoExecution modifyPersonInfo(PersonInfo personInfo, ImageHolder thumbnail)
			throws PersonInfoOperationException {
		// 空值判断
		if (personInfo == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.NULL_PERSONINFO);
		}
		if (personInfo.getUserId() == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.NULL_USERID);
		}
		try {
			if (personInfo.getEnableStatus() != null) {
				WechatAuth wechatAuth = wechatAuthDao.queryWechatByUserId(personInfo.getUserId());
				if(wechatAuth!=null) {
					String openId = wechatAuth.getOpenId();
					jedisKeys.del(openId);
				}
			}
			personInfo.setLastEditTime(new Date());
			if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null
					&& !"".equals(thumbnail.getImageName())) {
				PersonInfo tempPersonInfo = personInfoDao.queryPersonInfoByUserId(personInfo.getUserId());
				if (tempPersonInfo.getProfileImg() != null) {
					ImageUtil.deleteFileOrPath(tempPersonInfo.getProfileImg());
				}
				addProfileImg(personInfo, thumbnail);
			}
			// 修改个人信息
			int effectedNum = personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new PersonInfoOperationException("个人信息修改失败");
			}
		} catch (Exception e) {
			throw new PersonInfoOperationException("modifyPersonInfo error:" + e.getMessage());
		}
		return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
	}

	private void addProfileImg(PersonInfo personInfo, ImageHolder thumbnail) {
		// 获取图片目录的相对值路径
		String dest = PathUtil.getuserProfileImgPath(personInfo.getUserId());
		String imgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		personInfo.setProfileImg(imgAddr);
	}

	@Override
	public PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize) {
		// 页转行
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 获取用户信息列表
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(personInfoCondition, rowIndex, pageSize);
		int count = personInfoDao.queryPersonInfoCount(personInfoCondition);
		PersonInfoExecution se = new PersonInfoExecution();
		if (personInfoList != null) {
			se.setPersonInfoList(personInfoList);
			se.setCount(count);
		} else {
			se.setState(PersonInfoStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	@Transactional
	public PersonInfoExecution addPersonInfo(PersonInfo personInfo, ImageHolder thumbnail)
			throws PersonInfoOperationException {
		// 空值判断
		if (personInfo == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.NULL_PERSONINFO);
		}
		try {
			personInfo.setCreateTime(new Date());
			if(personInfo.getSouCoin()==null)
				personInfo.setSouCoin(100l);
			int effectedNum = personInfoDao.insertPersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new PersonInfoOperationException("个人信息添加失败");
			}
			
			if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null
					&& !"".equals(thumbnail.getImageName())) {
				addProfileImg(personInfo, thumbnail);
			}
			// 更新头像的图片地址
			effectedNum = personInfoDao.updatePersonInfo(personInfo);
			if (effectedNum <= 0) {
				throw new PersonInfoOperationException("更新图片地址失败");
			}
		} catch (Exception e) {
			throw new PersonInfoOperationException("addPersonInfo error:" + e.getMessage());
		}
		return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
	}

}
