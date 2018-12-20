package com.graduation.ss.entity;

public class ShopComment {
	private Long shopCommentId;
	private Shop shop;
	private PersonInfo personInfo;
	private String commentContent;
	private Integer serviceRating;
	private Integer starRating;
	
	public Long getShopCommentId() {
		return shopCommentId;
	}
	public void setShopCommentId(Long shopCommentId) {
		this.shopCommentId = shopCommentId;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
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
