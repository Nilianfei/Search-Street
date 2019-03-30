package com.graduation.ss.dto;

import java.util.List;

import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.enums.OrderStateEnum;

public class OrderExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 订单数量
	private int count;
	// 序号
		private int index;
	
	// 操作的order(增删订单的时候用到)
	private OrderInfo order;
	// 评论列表(查询订单列表的时候使用)
	private List<OrderInfo> orderList;

	public OrderExecution() {

	}

	public OrderExecution(int index) {
		this.index=index;

	}
	// 评论操作失败的时候使用的构造器
	public OrderExecution(OrderStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	// 评论操作失败的时候使用的构造器
	public OrderExecution(OrderStateEnum stateEnum,int index) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	// 增删评论操作成功的时候使用的构造器
	public OrderExecution(OrderStateEnum stateEnum, OrderInfo order) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.order = order;
	}
	
	// 查询评论操作成功的时候使用的构造器
	public OrderExecution(OrderStateEnum stateEnum, List<OrderInfo> orderList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.orderList = orderList;
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

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}

	public List<OrderInfo> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderInfo> orderList) {
		this.orderList = orderList;
	}
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
