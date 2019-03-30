package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.WechatAuth;

public interface WechatAuthDao {

	/**
	 * 通过openId查询对应的WechatAuth
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatByOpenId(@Param("openId") String openId);

	/**
	 * 添加微信账号
	 * 
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);

	/**
	 * 通过userID查询对应的WechatAuth
	 * 
	 * @param userId
	 * @return
	 */
	WechatAuth queryWechatByUserId(Long userId);
	
	/**
	 * 分页查询微信账号
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<WechatAuth>  queryWechatList(@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 查询微信账号总数
	 * @return
	 */
	int queryWechatCount();
}
