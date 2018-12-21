package com.graduation.ss.entity;

public class Help {


	/**
	 * 帮助者id 主键
	 */
	private Long helpId;
	/**
	 * 求助者id Foreign key
	 */
	private Long appealId;
	/**
	 * 用户id Foreign key
	 */
	private Long userId;
	/**
	 * 帮助状态
	 * 0求助用户未确定帮助对象，1已接受帮助，
	 * 2未接受帮助，3已结束
	 * 
	 */
	private Integer helpStatus;
	private Integer completion;
	private Integer efficiency;
	private Integer attitude;
	private Long additional_coin;
	public Long getHelpId() {
		return helpId;
	}
	public void setHelpId(Long helpId) {
		this.helpId = helpId;
	}
	public Long getAppealId() {
		return appealId;
	}
	public void setAppealId(Long appealId) {
		this.appealId = appealId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getHelpStatus() {
		return helpStatus;
	}
	public void setHelpStatus(Integer helpStatus) {
		this.helpStatus = helpStatus;
	}
	public Integer getCompletion() {
		return completion;
	}
	public void setCompletion(Integer completion) {
		this.completion = completion;
	}
	public Integer getEfficiency() {
		return efficiency;
	}
	public void setEfficiency(Integer efficiency) {
		this.efficiency = efficiency;
	}
	public Integer getAttitude() {
		return attitude;
	}
	public void setAttitude(Integer attitude) {
		this.attitude = attitude;
	}
	public Long getAdditional_coin() {
		return additional_coin;
	}
	public void setAdditional_coin(Long additional_coin) {
		this.additional_coin = additional_coin;
	}
	
	
}
