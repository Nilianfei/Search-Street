package com.graduation.ss.dto;

import java.util.List;

import com.graduation.ss.entity.ShopImg;
import com.graduation.ss.enums.ShopStateEnum;

public class ShopImgExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;
	// 商铺图片数量
	private int count;

	// shopImg列表(查询商铺图片列表的时候使用)
	private List<ShopImg> shopImgList;

	public ShopImgExecution() {

	}

	// 商铺图片操作失败的时候使用的构造器
	public ShopImgExecution(ShopStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 商铺图片操作成功的时候使用的构造器
	public ShopImgExecution(ShopStateEnum stateEnum, List<ShopImg> shopImgList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopImgList = shopImgList;
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

	public List<ShopImg> getShopImgList() {
		return shopImgList;
	}

	public void setShopImgList(List<ShopImg> shopImgList) {
		this.shopImgList = shopImgList;
	}

	
}
