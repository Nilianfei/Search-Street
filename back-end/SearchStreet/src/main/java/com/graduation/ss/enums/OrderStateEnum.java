package com.graduation.ss.enums;

import com.graduation.ss.enums.OrderStateEnum;

public enum OrderStateEnum {
	ORDERED(0, "已下单"), UNFINISHED(1, "未完成"), CANCELLED(2, "已取消"), TOBECOMMENTED(3, "待评价"), COMMENTED(4, "已完成"), SUCCESS(5, "操作成功"),INNER_ERROR(-1001, "内部系统错误"),
	NULL_SHOPID(-1002, "ShopId为空"),  NULL_ORDER(-1003, "Order为空"),NULL_USERID(-1004, "userId为空"),
	NULL_ORDERID(-1005, "OrderId为空"),INVALID_PARAMETER(-1006, "参数不合理");
	private int state;
	private String stateInfo;

	private OrderStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static OrderStateEnum stateOf(int state) {
		for (OrderStateEnum stateEnum : values()) {
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
