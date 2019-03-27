package com.graduation.ss.entity;

import io.swagger.annotations.ApiModelProperty;

public class ServiceInfo {

	/**
	 * 服务id 主键
	 */
	@ApiModelProperty(value = "服务id 主键", required = true)
	private Long serviceId;
	/**
	 * 店铺id 
	 */
	@ApiModelProperty(value = "店铺id", required = true)
	private Long shopId;
	@ApiModelProperty(value = "服务名称", required = true)
	private String serviceName;
	@ApiModelProperty(value = "服务价格", required = true)
	private Double servicePrice;
	@ApiModelProperty(value = "优先级", required = true)
	private Long servicePriority;
	@ApiModelProperty(value = "服务描述")
	private String serviceDesc;
	@ApiModelProperty(value = "服务内容")
	private String serviceContent;
	@ApiModelProperty(value = "服务相关图片")
	private String serviceImgAddr;
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Double getServicePrice() {
		return servicePrice;
	}
	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public String getServiceContent() {
		return serviceContent;
	}
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	public String getServiceImgAddr() {
		return serviceImgAddr;
	}
	public void setServiceImgAddr(String serviceImgAddr) {
		this.serviceImgAddr = serviceImgAddr;
	}
	public Long getServicePriority() {
		return servicePriority;
	}
	public void setServicePriority(Long servicePriority) {
		this.servicePriority = servicePriority;
	}
	@Override
	public String toString() {
		return "ServiceInfo [serviceId=" + serviceId + ", shopId=" + shopId + ", serviceName=" + serviceName
				+ ", servicePrice=" + servicePrice + ", servicePriority=" + servicePriority + ", serviceDesc="
				+ serviceDesc + ", serviceContent=" + serviceContent + ", ServiceImgAddr=" +serviceImgAddr + "]";
	}
	
	
}
