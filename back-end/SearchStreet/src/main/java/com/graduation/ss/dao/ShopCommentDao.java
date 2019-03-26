package com.graduation.ss.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.graduation.ss.entity.ShopComment;

public interface ShopCommentDao {
	/**
	 * 分页查询服务，可输入的条件有：店铺id，用户id,
	 * 
	 * @param shopCommentCondition
	 * @param rowIndex
	 *            从第几行开始取数据
	 * @param pageSize
	 *            返回的条数
	 * @return
	 */
	public List<ShopComment> queryShopCommentList(@Param("shopCommentCondition")ShopComment shopCommentCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	public List<ShopComment> queryShopCommentList2(@Param("shopCommentCondition")ShopComment shopCommentCondition);
	/**
	 * 返回queryShopCommentList总数
	 * 
	 * @param shopCommentCondition
	 * @return
	 */
	public int queryShopCommentCount(@Param("shopCommentCondition")ShopComment shopCommentCondition);
	
	/**
	 * 通过shopCommentId查询评论
	 * @param shopCommentId
	 * @return
	 */
	public ShopComment queryByShopCommentId(long shopCommentId);
	/**
	 * 通过orderId查询评论
	 * @param orderId
	 * @return
	 */
	public ShopComment queryByOrderId(long orderId);
	/**
	 * 通过shop id查询评论
	 * @param shopId
	 * @return
	 */
//	public List<ShopComment> queryByShopId(long shopId);
	/**
	 * 添加评论
	 * @param shopCommentInfo
	 * @return
	 */
	public int  insertShopComment(ShopComment shopCommentInfo);
	/**
	 * 批量添加评论
	 * @param shopCommentInfo
	 * @return
	 */
	public int  insertShopCommentInfo(List<ShopComment> shopCommentList);
	
	/**
	 * 更新评论信息
	 * @param shopCommentInfo
	 * @return
	 */
	public int updateShopComment(ShopComment shopCommentInfo);

	/**
	 * 删除评论信息
	 * @param shopCommentInfo
	 * @return
	 */
	public int deleteShopComment(ShopComment shopCommentInfo);

}
