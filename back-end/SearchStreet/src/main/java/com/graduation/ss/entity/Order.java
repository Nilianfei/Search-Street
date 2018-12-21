package com.graduation.ss.entity;

public class Order {

	/**
	 * 订单id 主键
	 */
	private Long orderId;
	/**
	 * 服务id Foreign key
	 */
	private Long serviceId;
	/**
	 * 用户id Foreign key
	 */
	private Long userId;
	private Long serviceCount;
	/**
	 * 订单状态
	 * 0已下单，1完成订单，
	 * 2已取消订单,3已删除
	 * 
	 */
	private Integer orderStatus;
	private Date createTime;
	private Date overTime;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	
	
}
