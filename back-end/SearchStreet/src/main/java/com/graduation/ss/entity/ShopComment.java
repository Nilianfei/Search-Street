package com.graduation.ss.entity;

public class ShopComment {
	private Long shopCommentId;
	private Long shopId;
	private Long userId;
	private String commentContent;
	//服务分
	private Integer serviceRating;
	//星级评分
	private Integer starRating;
	
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
}
