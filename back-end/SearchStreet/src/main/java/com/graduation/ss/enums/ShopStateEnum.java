package com.graduation.ss.enums;

import com.graduation.ss.enums.ShopStateEnum;

public enum ShopStateEnum {
	CHECK(0, "审核中"), OFFLINE(2, "非法店铺"), SUCCESS(3, "操作成功"), PASS(1, "通过认证"), INNER_ERROR(-1001, "内部系统错误"),
	NULL_SHOPID(-1002, "ShopId为空"), NULL_SHOP(-1003, "shop信息为空"),
	NULL_SHOPIMG_CREATETIME(-1004, "shopImg的createTime为空"), NULL_SHOPIMGID(-1005, "shopImg的Id为空");
	private int state;
	private String stateInfo;

	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static ShopStateEnum stateOf(int state) {
		for (ShopStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

}
