package com.graduation.ss.entity;

public class Help {

	/**
	 * 帮助id 主键
	 */
	private Long helpId;
	/**
	 * 求助id
	 */
	private Long appealId;
	/**
	 * 帮助者用户id
	 */
	private Long userId;
	/**
	 * 帮助状态 0求助用户未确定帮助对象，1已接受帮助， 2未接受帮助，3已结束
	 * 
	 */
	private Integer helpStatus;
	/**
	 * 完成程度分，范围：0-5分整数
	 */
	private Integer completion;
	/**
	 * 效率分，范围：0-5分整数
	 */
	private Integer efficiency;
	/**
	 * 态度分，范围：0-5分整数
	 */
	private Integer attitude;
	// 追加赏金
	private Long additionalCoin;

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

	public Long getAdditionalCoin() {
		return additionalCoin;
	}

	public void setAdditionalCoin(Long additionalCoin) {
		this.additionalCoin = additionalCoin;
	}

	@Override
	public String toString() {
		return "Help [helpId=" + helpId + ", appealId=" + appealId + ", userId=" + userId + ", helpStatus=" + helpStatus
				+ ", completion=" + completion + ", efficiency=" + efficiency + ", attitude=" + attitude
				+ ", additionalCoin=" + additionalCoin + "]";
	}

}
