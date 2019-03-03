package com.graduation.ss.entity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class Help {

	/**
	 * 帮助id 主键
	 */
	@ApiModelProperty(value = "帮助ID，创建时不填，修改信息时必填")
	private Long helpId;
	/**
	 * 求助id
	 */
	@ApiModelProperty(value = "求助ID", required = true)
	private Long appealId;
	
	@ApiModelProperty(value = "求助标题", required = true)
	private String appealTitle;
	/**
	 * 帮助者用户id
	 */
	@ApiModelProperty(value = "帮助者用户id", hidden = true)
	private Long userId;
	/**
	 * 帮助状态 0求助用户未确定帮助对象，1已接受帮助， 2已完成，3已失效
	 * 
	 */
	@ApiModelProperty(value = "帮助状态 0求助用户未确定帮助对象，1已接受帮助， 2已完成，3已失效")
	private Integer helpStatus;
	/**
	 * 完成程度分，范围：0-5分整数
	 */
	@ApiModelProperty(value = "完成程度分，范围：0-5分整数")
	private Integer completion;
	/**
	 * 效率分，范围：0-5分整数
	 */
	@ApiModelProperty(value = "效率分，范围：0-5分整数")
	private Integer efficiency;
	/**
	 * 态度分，范围：0-5分整数
	 */
	@ApiModelProperty(value = "态度分，范围：0-5分整数")
	private Integer attitude;
	/**
	 * 之前的平均完成程度分，范围：0-5分保留一位小数
	 */
	@ApiModelProperty(value = "之前的平均完成程度分，范围：0-5分保留一位小数", hidden = true)
	private Float avgCompletion;
	/**
	 * 之前的平均效率分，范围：0-5分保留一位小数
	 */
	@ApiModelProperty(value = "之前的平均完成效率分，范围：0-5分保留一位小数", hidden = true)
	private Float avgEfficiency;
	/**
	 * 之前的平均态度分，范围：0-5分保留一位小数
	 */
	@ApiModelProperty(value = "之前的平均完成态度分，范围：0-5分保留一位小数", hidden = true)
	private Float avgAttitude;
	// 追加赏金
	@ApiModelProperty(value = "追加赏金")
	private Long additionalCoin;

	@ApiModelProperty(value = "失效时间", hidden = true)
	private Date endTime;

	public Float getAvgCompletion() {
		return avgCompletion;
	}

	public void setAvgCompletion(Float avgCompletion) {
		this.avgCompletion = avgCompletion;
	}

	public Float getAvgEfficiency() {
		return avgEfficiency;
	}

	public void setAvgEfficiency(Float avgEfficiency) {
		this.avgEfficiency = avgEfficiency;
	}

	public Float getAvgAttitude() {
		return avgAttitude;
	}

	public void setAvgAttitude(Float avgAttitude) {
		this.avgAttitude = avgAttitude;
	}

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

	public String getAppealTitle() {
		return appealTitle;
	}

	public void setAppealTitle(String appealTitle) {
		this.appealTitle = appealTitle;
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

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Help [helpId=" + helpId + ", appealId=" + appealId + ", appealTitle=" + appealTitle + ", userId="
				+ userId + ", helpStatus=" + helpStatus + ", completion=" + completion + ", efficiency=" + efficiency
				+ ", attitude=" + attitude + ", avgCompletion=" + avgCompletion + ", avgEfficiency=" + avgEfficiency
				+ ", avgAttitude=" + avgAttitude + ", additionalCoin=" + additionalCoin + ", endTime=" + endTime + "]";
	}

	
}
