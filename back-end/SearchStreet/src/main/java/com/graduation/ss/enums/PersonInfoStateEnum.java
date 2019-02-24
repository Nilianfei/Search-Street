package com.graduation.ss.enums;

public enum PersonInfoStateEnum {
	CHECK(0, "审核中"), SUCCESS(3, "操作成功"), PASS(1, "通过认证"), INNER_ERROR(-1001, "内部系统错误"),
	NULL_USERID(-1002, "userId为空"), NULL_PERSONINFO(-1003, "personInfo信息为空");
	private int state;
	private String stateInfo;

	private PersonInfoStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static PersonInfoStateEnum stateOf(int state) {
		for (PersonInfoStateEnum stateEnum : values()) {
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
