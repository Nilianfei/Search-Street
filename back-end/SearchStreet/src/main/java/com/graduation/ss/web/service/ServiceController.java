package com.graduation.ss.web.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.graduation.ss.dao.ServiceImgDao;
import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ServiceExecution;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.enums.ServiceStateEnum;
import com.graduation.ss.exceptions.ServiceOperationException;
import com.graduation.ss.service.SService;
import com.graduation.ss.util.HttpServletRequestUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/service")
@Api(value = "ServiceController|对服务操作的控制器")
public class ServiceController {
	@Autowired
	private SService sService;
	@Autowired
	private ServiceImgDao serviceImgDao;

	//通过店铺id获取服务列表 分页 
	@RequestMapping(value = "/getservicelistbyshopid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据shopID获取其所有服务信息（分页）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "2"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的服务数目", required = true, dataType = "int") })
	private Map<String, Object> getServiceListByShopId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		try {
			ServiceInfo serviceCondition = new ServiceInfo();
			serviceCondition.setShopId(shopId);
			ServiceExecution se = sService.getServiceList(serviceCondition, pageIndex, pageSize);
			//ServiceExecution se = sService.getByShopId(shopId, pageIndex, pageSize);
			int pageNum = (int) (se.getCount() / pageSize);
			if (pageNum * pageSize < se.getCount())
				pageNum++;
			List<ServiceInfo> serviceList=se.getServiceList();
			modelMap.put("serviceList", serviceList);
			modelMap.put("pageSize", pageSize);
			modelMap.put("pageNum", pageNum);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		
		return modelMap;
	}
	//根据查询条件获取服务列表 分页
	@RequestMapping(value = "/listService", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据查询条件获取其所有服务信息（分页）")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的服务数目", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "serviceName", value = "服务名称", required = true, dataType = "String", example = "测试service店铺")})
	private Map<String, Object> listService(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceExecution se = null;
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
			try {
				// 根据查询条件分页返回服务列表
				se = sService.getServiceList(serviceCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (se.getServiceList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, se.getServiceList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, se.getCount());
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
	@RequestMapping(value = "/searchservicebyid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据serviceId获取该服务信息")
	@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> searchServiceById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceInfo service = null;
		// 从请求中获取serviceId
		long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		Date date=new Date();
		System.out.println(date.toString()+" serviceId:"+serviceId);
		if (serviceId > 0) {
			try {
				// 根据Id获取服务实例
				service = sService.getByServiceId(serviceId);
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (service != null) {
				//ServiceImg serviceImg=serviceImgDao.getServiceImg(serviceId);
				//service.setServiceImgAddr(serviceImg.getImgAddr());
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
	//添加服务
	@RequestMapping(value = "/addservice", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "添加服务信息")
	private Map<String, Object> addService(
			@RequestBody @ApiParam(name = "ServiceInfo", value = "传入json格式", required = true) ServiceInfo serviceInfo, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 空值判断
		if (serviceInfo != null ) {
			try {
				//添加服务
				ServiceExecution ae = sService.addService(serviceInfo);
				if (ae.getState() == ServiceStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					modelMap.put("serviceId", serviceInfo.getServiceId());
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
	//更新服务
		@RequestMapping(value = "/modifyservice", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "修改服务信息（不修改图片）")
		private Map<String, Object> modifyService(
				@RequestBody @ApiParam(name = "ServiceInfo", value = "传入json格式,要传serviceId", required = true) ServiceInfo serviceInfo, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			// 空值判断
			if (serviceInfo != null && serviceInfo.getServiceId() != null) {
				try {
					//更新服务
					ServiceExecution ae = sService.modifyService(serviceInfo);
					if (ae.getState() == ServiceStateEnum.SUCCESS.getState()) {
						modelMap.put("serviceId", serviceInfo.getServiceId());
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
			System.out.println(serviceInfo.toString());
			return modelMap;
		}
		

		//删除服务
		@RequestMapping(value = "/deleteservice", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "删除服务信息")
		@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3")
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
		private void handleImage(HttpServletRequest request, ImageHolder serviceImg) throws IOException {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile serviceImgFile = (CommonsMultipartFile) multipartRequest.getFile("serviceImg");
			if (serviceImgFile != null && serviceImg != null) {
				serviceImg.setImage(serviceImgFile.getInputStream());
				serviceImg.setImageName(serviceImgFile.getOriginalFilename());
			}
			
		}
		@RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "上传服务相关图片")
		@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3"),
			@ApiImplicitParam(paramType = "query", name = "createTime", value = "图片创建时间", required = true, dataType = "Date")
		})
		private Map<String, Object> uploadImg(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			// 1.接收并转化相应的参数，包括服务id以及图片信息
			Long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
			Date createTime = HttpServletRequestUtil.getDate(request, "createTime");
			ImageHolder serviceImg = new ImageHolder("", null);
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			try {
				if (commonsMultipartResolver.isMultipart(request)) {
					handleImage(request, serviceImg);
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				System.out.println(e.getMessage());
				System.out.println(createTime);
				return modelMap;
			}
			// 2.上传服务图片
			ServiceExecution se;
			try {
				se = sService.uploadImg(serviceId, serviceImg,createTime);
				//先更新服务其他信息，在更新服务照片
				ServiceInfo service=sService.getByServiceId(serviceId);
				service.setServiceImgAddr(serviceImgDao.getServiceImg(serviceId).getImgAddr());
				sService.modifyService(service);
				if (se.getState() == ServiceStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					modelMap.put("serviceImgAddr", service.getServiceImgAddr());
				} else {
					modelMap.put("success", false);
					System.out.println("uploadImg失败");
					modelMap.put("errMsg", se.getStateInfo());
					System.out.println(se.getStateInfo());
				}
			} catch (ServiceOperationException e) {
				System.out.println("uploadImg失败:"+e.getMessage());
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
			}
			System.out.println("uploadImg成功");
			return modelMap;
		}


}
