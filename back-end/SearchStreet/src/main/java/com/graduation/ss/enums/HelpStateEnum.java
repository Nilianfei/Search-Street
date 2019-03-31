package com.graduation.ss.enums;

public enum HelpStateEnum {
	SUCCESS(0, "操作成功"), INNER_ERROR(-1001, "内部系统错误"), NULL_HELPID(-1002, "HelpId为空"), NULL_HELP(-1003, "help信息为空"),
	NULL_APPEAL(-1004, "appeal为空"), NULL_USERID(-1005, "userId为空"), AREADY_HELP(-1006, "不能重复帮助"),
	NOT_USER_APPEAL(-1007, "求助不是该用户的"), NULL_ADDITIONCOIN(-1008, "追加赏金为空"), NOT_ADDCOIN(-1009, "不能追赏"),
	NOT_ADDCOIN_AGAIN(-1010, "不能多次追赏"), NULL_APPEALER(-1011, "求助者不存在"), LACK_COIN(-1012, "搜币不足"),
	NULL_HELPER(-1013, "帮助者不存在");
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
