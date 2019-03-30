package com.graduation.ss.entity;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;

public class OrderInfo {

	/**
	 * 订单id 主键
	 */
	@ApiModelProperty(value = "订单id 主键", required = true)
	private Long orderId;
	/**
	 * 服务id Foreign key
	 */
	@ApiModelProperty(value = "服务id", required = true)
	private Long serviceId;
	/**
	 * 用户id Foreign key
	 */
	@ApiModelProperty(value = "用户id", required = true)
	private Long userId;
	@ApiModelProperty(value = "服务数量", required = true)
	private Long serviceCount;
	/**
	 * 订单状态
	 * 0已下单，1完成订单，
	 * 2已取消订单,3已删除
	 * 
	 */
	@ApiModelProperty(value = "订单状态（0已下单，1未完成，2已取消,3待评价，4已完成）", required = true)
	private Integer orderStatus;
	@ApiModelProperty(value = "订单创建时间")
	private LocalDateTime createTime;
	@ApiModelProperty(value = "订单结束时间")
	private LocalDateTime overTime;
	@ApiModelProperty(value = "服务名称")
	private String serviceName;
	@ApiModelProperty(value = "订单价格", required = true)
	private double orderPrice;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getServiceCount() {
		return serviceCount;
	}
	public void setServiceCount(Long serviceCount) {
		this.serviceCount = serviceCount;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	public LocalDateTime getOverTime() {
		return overTime;
	}
	public void setOverTime(LocalDateTime overTime) {
		this.overTime = overTime;
	}
	public double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	@Override
	public String toString() {
		return "OrderInfo [orderId=" + orderId + ", serviceId=" + serviceId + ", userId=" + userId + ", serviceCount="
				+ serviceCount + ", orderStatus=" + orderStatus + ", createTime=" + createTime + ", overTime="
				+ overTime + ", serviceName=" + serviceName + ", orderPrice=" + orderPrice + "]";
	}
	
}
