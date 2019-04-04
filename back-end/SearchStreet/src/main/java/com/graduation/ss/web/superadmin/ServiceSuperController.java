package com.graduation.ss.web.superadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.ServiceExecution;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.enums.ServiceStateEnum;
import com.graduation.ss.service.SService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class ServiceSuperController {
	@Autowired
	private SService sService;

	/**
	 * 获取服务列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listservices", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listServices(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceExecution ae = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			ServiceInfo serviceCondition = new ServiceInfo();
			String serviceName = HttpServletRequestUtil.getString(request, "serviceName");
			if (serviceName != null) {
				try {
					// 若传入服务名称，则将服务名称解码后添加到查询条件里，进行模糊查询
					serviceCondition.setServiceName(URLDecoder.decode(serviceName, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			String serviceContent = HttpServletRequestUtil.getString(request, "serviceContent");
			if (serviceContent != null) {
				try {
					// 若传入服务内容，则将服务内容解码后添加到查询条件里，进行模糊查询
					serviceCondition.setServiceContent(URLDecoder.decode(serviceContent, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			try {
				// 根据查询条件分页返回评论列表
				ae = sService.getServiceList(serviceCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (ae.getServiceList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, ae.getServiceList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, ae.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ServiceInfo>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	/**
	 * 根据id返回服务信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchservicebyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchServiceById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceInfo service = null;
		// 从请求中获取serviceId
		Long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		if (serviceId != null && serviceId > 0) {
			try {
				// 根据Id获取服务实例
				service = sService.getByServiceId(serviceId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (service != null) {
				List<ServiceInfo> serviceList = new ArrayList<ServiceInfo>();
				serviceList.add(service);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, serviceList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ServiceInfo>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	/**
	 * 根据shopId返回服务信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchservicebyshopid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchServiceByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceExecution ae = null;
		// 从请求中获取shopId
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
	    int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		if (shopId != null && shopId > 0) {
			try {
				ServiceInfo serviceCondition = new ServiceInfo();
				// 根据shopId获取服务实例
				serviceCondition.setShopId(shopId);
				ae = sService.getServiceList(serviceCondition, pageIndex, pageSize);
				//ae = sService.getByUserId(shopId,pageIndex,pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (ae.getServiceList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, ae.getServiceList());
				modelMap.put(ConstantForSuperAdmin.TOTAL,ae.getCount());
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ServiceInfo>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}
	/**
	 * 添加服务信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addservice", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addService(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		String serviceName = HttpServletRequestUtil.getString(request, "serviceName");
		double servicePrice=HttpServletRequestUtil.getDouble(request, "servicePrice");
		Long servicePriority=HttpServletRequestUtil.getLong(request, "servicePriority");
		String serviceDesc=HttpServletRequestUtil.getString(request, "serviceDesc");
		String serviceContent=HttpServletRequestUtil.getString(request, "serviceContent");
		ServiceInfo service = new ServiceInfo();
		service.setShopId(shopId);
		service.setServiceName(serviceName);
		service.setServicePrice(servicePrice);
		service.setServicePriority(servicePriority);
		service.setServiceDesc(serviceDesc);
		service.setServiceContent(serviceContent);
		
		// 空值判断
		try {
			ServiceExecution ae = sService.addService(service);
			if (ae.getState() == ServiceStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ae.getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		return modelMap;
	}

	/**
	 * 修改订单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyservice", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyService(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		Long serviceId=HttpServletRequestUtil.getLong(request, "serviceId");
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		String serviceName = HttpServletRequestUtil.getString(request, "serviceName");
		double servicePrice=HttpServletRequestUtil.getDouble(request, "servicePrice");
		Long servicePriority=HttpServletRequestUtil.getLong(request, "servicePriority");
		String serviceDesc=HttpServletRequestUtil.getString(request, "serviceDesc");
		String serviceContent=HttpServletRequestUtil.getString(request, "serviceContent");
		ServiceInfo service = new ServiceInfo();
		service.setServiceId(serviceId);
		service.setShopId(shopId);
		service.setServiceName(serviceName);
		service.setServicePrice(servicePrice);
		service.setServicePriority(servicePriority);
		service.setServiceDesc(serviceDesc);
		service.setServiceContent(serviceContent);
		// 空值判断
		if (service != null && service.getServiceId() != null) {
			try {
				ServiceExecution ae = sService.modifyService(service);
				if (ae.getState() == ServiceStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入服务信息");
		}
		return modelMap;
	}
	//删除服务
	@RequestMapping(value = "/deleteservice", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> deleteService(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		// 空值判断
		if (serviceId>0) {
			try {
				//删除服务
				ServiceExecution ae = sService.deleteService(serviceId);
				if (ae.getState() == ServiceStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "错误的serviceId");
		}
		return modelMap;
	}
}
