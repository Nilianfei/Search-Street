package com.graduation.ss.service;


import com.graduation.ss.dto.ShopCommentExecution;
import com.graduation.ss.exceptions.ShopCommentOperationException;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.entity.ShopComment;

public interface ShopCommentService {
	/**
	 * 根据shopCommentCondition分页返回相应评论列表
	 * 
	 * @param shopCommentCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopCommentExecution getShopCommentList(ShopComment shopCommentCondition, int pageIndex, int pageSize);

	/**
	 * 通过shopId获取评论信息
	 * 
	 * @param shopId
	 * @return
	 */
	public ShopCommentExecution getByShopId(long shopId, int pageIndex, int pageSize);
	public ShopCommentExecution getByShopId2(long shopId);
	/**
	 * 通过shopId获取服务评分，星级评分平均分
	 * 
	 * @param shopId
	 * @return
	 */
	public Shop getAvgByShopId(long shopId);
	/**
	 * 通过userId获取评论信息
	 * 
	 * @param userId
	 * @return
	 */
	public ShopCommentExecution getByUserId(long userId, int pageIndex, int pageSize);
	public ShopCommentExecution getByUserId2(long userId);
	/**
	 * 通过评论Id获取评论信息
	 * 
	 * @param shopCommentId
	 * @return
	 */
	public ShopComment getByShopCommentId(long shopCommentId);
	/**
	 * 更新服务信息
	 * 
	 * @param shopComment
	 * 
	 * @return
	 * @throws ShopCommentOperationException
	 */
	public ShopCommentExecution modifyShopComment(ShopComment shopComment) throws ShopCommentOperationException;

	/**
	 * 添加服务信息
	 * 
	 * @param shopComment
	 * @param shopCommentImgInputStream
	 * @return
	 * @throws ShopCommentOperationException
	 */
	public ShopCommentExecution addShopComment(ShopComment shopComment) throws ShopCommentOperationException;
	/**
	 * 删除服务信息
	 * 
	 * @param shopComment
	 * @return
	 * @throws ShopCommentOperationException
	 */
	public ShopCommentExecution deleteShopComment(ShopComment shopComment) throws ShopCommentOperationException;
}
