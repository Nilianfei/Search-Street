package com.graduation.ss.enums;

import com.graduation.ss.enums.AppealStateEnum;

public enum AppealStateEnum {
	SUCCESS(0, "操作成功"), INNER_ERROR(-1001, "内部系统错误"), NULL_APPEALID(-1002, "AppealId为空"),
	NULL_APPEAL(-1003, "appeal信息为空"), NULL_USERID(-1004, "userId为空"), NULL_HELPID(-1005, "缺少helpId"),
	SOUCOIN_LACK(-1006, "搜币不够"), COMPLETE_ERR(-1007, "不能改为完成状态"), NOT_USER_APPEAL(-1008, "求助不是该用户的"),
	NOT_CANCEL(-1009, "不能撤销求助"), NOT_DISABLE(-1010, "不能使求助失效"), NOT_HELPID(-1011, "帮助不存在"), NOT_HELPER(-1012, "帮助者不存在"),
	NULL_APPEALIMGID(-1013, "求助图片Id为空");
	private int state;
	private String stateInfo;

	private AppealStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static AppealStateEnum stateOf(int state) {
		for (AppealStateEnum stateEnum : values()) {
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
