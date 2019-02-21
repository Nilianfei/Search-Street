package com.graduation.ss.dto;

import com.graduation.ss.entity.PersonInfo;
import com.graduation.ss.enums.PersonInfoStateEnum;

public class PersonInfoExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 操作的personInfo(增删改个人信息的时候用到)
	private PersonInfo personInfo;

	public PersonInfoExecution() {

	}

	// 个人信息操作失败的时候使用的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 个人信息操作成功的时候使用的构造器
	public PersonInfoExecution(PersonInfoStateEnum stateEnum, PersonInfo personInfo) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.personInfo = personInfo;
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

	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}

}
