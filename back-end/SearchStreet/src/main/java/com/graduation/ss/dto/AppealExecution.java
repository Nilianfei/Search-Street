package com.graduation.ss.dto;

import java.util.List;

import com.graduation.ss.entity.Appeal;
import com.graduation.ss.enums.AppealStateEnum;

public class AppealExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 求助数量
	private int count;

	// 操作的appeal(增删改求助的时候用到)
	private Appeal appeal;

	// appeal列表(查询求助列表的时候使用)
	private List<Appeal> appealList;

	public AppealExecution() {

	}

	// 求助操作失败的时候使用的构造器
	public AppealExecution(AppealStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 求助操作成功的时候使用的构造器
	public AppealExecution(AppealStateEnum stateEnum, Appeal appeal) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.appeal = appeal;
	}

	// 求助操作成功的时候使用的构造器
	public AppealExecution(AppealStateEnum stateEnum, List<Appeal> appealList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.appealList = appealList;
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

	public Appeal getAppeal() {
		return appeal;
	}

	public void setAppeal(Appeal appeal) {
		this.appeal = appeal;
	}

	public List<Appeal> getAppealList() {
		return appealList;
	}

	public void setAppealList(List<Appeal> appealList) {
		this.appealList = appealList;
	}
}
