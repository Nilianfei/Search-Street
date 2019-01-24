package com.graduation.ss.dto;

import java.util.List;

import com.graduation.ss.entity.Help;
import com.graduation.ss.enums.HelpStateEnum;

public class HelpExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 帮助数量
	private int count;

	// 操作的help(增删改帮助的时候用到)
	private Help help;

	// help列表(查询帮助列表的时候使用)
	private List<Help> helpList;

	public HelpExecution() {

	}

	// 帮助操作失败的时候使用的构造器
	public HelpExecution(HelpStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 帮助操作成功的时候使用的构造器
	public HelpExecution(HelpStateEnum stateEnum, Help help) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.help = help;
	}

	// 帮助操作成功的时候使用的构造器
	public HelpExecution(HelpStateEnum stateEnum, List<Help> helpList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.helpList = helpList;
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

	public Help getHelp() {
		return help;
	}

	public void setHelp(Help help) {
		this.help = help;
	}

	public List<Help> getHelpList() {
		return helpList;
	}

	public void setHelpList(List<Help> helpList) {
		this.helpList = helpList;
	}
}
