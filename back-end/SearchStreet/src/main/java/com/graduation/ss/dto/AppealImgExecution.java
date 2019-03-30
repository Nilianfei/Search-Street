package com.graduation.ss.dto;

import java.util.List;

import com.graduation.ss.entity.AppealImg;
import com.graduation.ss.enums.AppealStateEnum;

public class AppealImgExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;
	// 求助图片数量
	private int count;

	// appealImg列表(查询求助图片列表的时候使用)
	private List<AppealImg> appealImgList;

	public AppealImgExecution() {

		}

	// 求助图片操作失败的时候使用的构造器
	public AppealImgExecution(AppealStateEnum stateEnum) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
		}

	// 求助图片操作成功的时候使用的构造器
	public AppealImgExecution(AppealStateEnum stateEnum, List<AppealImg> appealImgList) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.appealImgList = appealImgList;
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

	public List<AppealImg> getAppealImgList() {
		return appealImgList;
	}

	public void setAppealImgList(List<AppealImg> appealImgList) {
		this.appealImgList = appealImgList;
	}
}
