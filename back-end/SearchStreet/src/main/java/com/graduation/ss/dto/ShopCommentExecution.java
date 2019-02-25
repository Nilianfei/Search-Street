package com.graduation.ss.dto;

import java.util.List;

import com.graduation.ss.entity.ShopComment;
import com.graduation.ss.enums.ShopCommentStateEnum;

public class ShopCommentExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 评论数量
	private int count;

	// 操作的shopComment(增删评论的时候用到)
	private ShopComment shopComment;
	// 评论列表(查询评论列表的时候使用)
	private List<ShopComment> shopCommentList;

	public ShopCommentExecution() {

	}

	// 评论操作失败的时候使用的构造器
	public ShopCommentExecution(ShopCommentStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 增删评论操作成功的时候使用的构造器
	public ShopCommentExecution(ShopCommentStateEnum stateEnum, ShopComment shopComment) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopComment = shopComment;
	}
	
	// 查询评论操作成功的时候使用的构造器
	public ShopCommentExecution(ShopCommentStateEnum stateEnum, List<ShopComment> shopCommentList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopCommentList = shopCommentList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ShopComment getShopComment() {
		return shopComment;
	}

	public void setShopComment(ShopComment shopComment) {
		this.shopComment = shopComment;
	}

	public List<ShopComment> getShopCommentList() {
		return shopCommentList;
	}

	public void setShopCommentList(List<ShopComment> shopCommentList) {
		this.shopCommentList = shopCommentList;
	}
	
}
