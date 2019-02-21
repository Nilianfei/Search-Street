package com.graduation.ss.enums;

public enum HelpStateEnum {
	SUCCESS(0, "操作成功"), INNER_ERROR(-1001, "内部系统错误"), NULL_HELPID(-1002, "HelpId为空"), NULL_HELP(-1003, "help信息为空"),
	NULL_APPEALID(-1004, "appealId为空"), NULL_USERID(-1005, "userId为空");
	private int state;
	private String stateInfo;

	private HelpStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static HelpStateEnum stateOf(int state) {
		for (HelpStateEnum stateEnum : values()) {
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
