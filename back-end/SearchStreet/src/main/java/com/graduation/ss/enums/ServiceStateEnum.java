package com.graduation.ss.enums;

import com.graduation.ss.enums.ServiceStateEnum;

public enum ServiceStateEnum {
	 SUCCESS(3, "操作成功"), INNER_ERROR(-1001, "内部系统错误"),
	NULL_SHOPID(-1002, "ShopId为空"),  NULL_SeriveId(-1003, "ServiceId信息为空"),NULL_Service(-1004, "service信息为空"),
	NULL_ServiceImg(-1005, "service图片为空"),NULL_SERVICEIMG_CREATETIME(-1006, "Service图片创建时间信息为空");
	private int state;
	private String stateInfo;

	private ServiceStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static ServiceStateEnum stateOf(int state) {
		for (ServiceStateEnum stateEnum : values()) {
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
