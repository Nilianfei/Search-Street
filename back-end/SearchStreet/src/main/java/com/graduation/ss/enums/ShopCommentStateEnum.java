package com.graduation.ss.enums;

import com.graduation.ss.enums.ShopCommentStateEnum;

public enum ShopCommentStateEnum {
	 SUCCESS(3, "操作成功"), INNER_ERROR(-1001, "内部系统错误"),
	NULL_SHOPID(-1002, "ShopId为空"),  NULL_ShopComment(-1003, "ShopComment为空"),NULL_USERID(-1004, "userId为空"),
	NULL_STARRATING(-1005, "starRating为空"),NULL_SERVICERATING(-1006, "serviceRating为空"),NULL_SHOPCOMMENTID(-1007, "ShopCommentId参数错误"),;
	private int state;
	private String stateInfo;

	private ShopCommentStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static ShopCommentStateEnum stateOf(int state) {
		for (ShopCommentStateEnum stateEnum : values()) {
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
