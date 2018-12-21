package com.graduation.ss.entity;

public class Service {

	/**
	 * 服务id 主键
	 */
	private Long serviceId;
	/**
	 * 店铺id Foreign key
	 */
	private Long shopId;
	private String serviceName;
	private Long servicePrice;
	private String serviceDesc;
	private String serviceContent;
	private String serviceImg;
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
	public Long getServicePrice() {
		return servicePrice;
	}
	public void setServicePrice(Long servicePrice) {
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
	public String getServiceImg() {
		return serviceImg;
	}
	public void setServiceImg(String serviceImg) {
		this.serviceImg = serviceImg;
	}
	
	
}
