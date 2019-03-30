package com.graduation.ss.entity;

import io.swagger.annotations.ApiModelProperty;

public class ShopComment {
	@ApiModelProperty(value = "服务评论Id", required = true)
	private Long shopCommentId;
	@ApiModelProperty(value = "商家Id", required = true)
	private Long shopId;
	@ApiModelProperty(value = "订单Id", required = true)
	private Long orderId;
	@ApiModelProperty(value = "用户Id", required = true)
	private Long userId;
	@ApiModelProperty(value = "评论内容")
	private String commentContent;
	//服务分
	@ApiModelProperty(value = "服务评分", required = true)
	private Integer serviceRating;
	//星级评分
	@ApiModelProperty(value = "星级评分", required = true)
	private Integer starRating;
	@ApiModelProperty(value = "商家回复")
	private String commentReply;
	public String getCommentReply() {
		return commentReply;
	}
	public void setCommentReply(String commentReply) {
		this.commentReply = commentReply;
	}
	public Long getShopCommentId() {
		return shopCommentId;
	}
	public void setShopCommentId(Long shopCommentId) {
		this.shopCommentId = shopCommentId;
	}
	
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Integer getServiceRating() {
		return serviceRating;
	}
	public void setServiceRating(Integer serviceRating) {
		this.serviceRating = serviceRating;
	}
	public Integer getStarRating() {
		return starRating;
	}
	public void setStarRating(Integer starRating) {
		this.starRating = starRating;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "ShopComment [shopCommentId=" + shopCommentId + ", shopId=" + shopId + ", orderId=" + orderId
				+ ", userId=" + userId + ", commentContent=" + commentContent + ", serviceRating=" + serviceRating
				+ ", starRating=" + starRating + ", commentReply=" + commentReply + "]";
	}
	
}
