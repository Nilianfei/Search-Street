package com.graduation.ss.web.superadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.graduation.ss.dto.AppealExecution;
import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.entity.Help;
import com.graduation.ss.enums.AppealStateEnum;
import com.graduation.ss.service.AppealService;
import com.graduation.ss.service.HelpService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class AppealSuperController {
	@Autowired
	private AppealService appealService;
	@Autowired
	private HelpService helpService;

	/**
	 * 获取求助列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listappeals", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listAppeals(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		AppealExecution ae = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			Appeal appealCondition = new Appeal();
			int appealStatus = HttpServletRequestUtil.getInt(request, "appealStatus");
			if (appealStatus >= 0) {
				// 若传入可用状态，则将可用状态添加到查询条件里
				appealCondition.setAppealStatus(appealStatus);
			}
			String appealTitle = HttpServletRequestUtil.getString(request, "appealTitle");
			if (appealTitle != null) {
				try {
					// 若传入求助名称，则将求助名称解码后添加到查询条件里，进行模糊查询
					appealCondition.setAppealTitle(URLDecoder.decode(appealTitle, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}
			try {
				// 根据查询条件分页返回求助列表
				ae = appealService.getAppealListFY(appealCondition, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (ae.getAppealList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, ae.getAppealList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, ae.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Appeal>());
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
	 * 根据id返回求助信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchappealbyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchAppealById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Appeal appeal = null;
		// 从请求中获取求助Id
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		if (appealId != null && appealId > 0) {
			try {
				// 根据Id获取求助实例
				appeal = appealService.getByAppealId(appealId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (appeal != null) {
				List<Appeal> appealList = new ArrayList<Appeal>();
				appealList.add(appeal);
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, appealList);
				modelMap.put(ConstantForSuperAdmin.TOTAL, 1);
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Appeal>());
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
	 * 添加求助信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addappeal", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addAppeal(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long userId=HttpServletRequestUtil.getLong(request, "userId");
		String appealTitle=HttpServletRequestUtil.getString(request, "appealTitle");
		String phone=HttpServletRequestUtil.getString(request, "phone");
		String appealContent=HttpServletRequestUtil.getString(request, "appealContent");
		String province=HttpServletRequestUtil.getString(request, "province");
		String city=HttpServletRequestUtil.getString(request, "city");
		String district=HttpServletRequestUtil.getString(request, "district");
		String fullAddress=HttpServletRequestUtil.getString(request, "fullAddress");
		String appealMoreInfo=HttpServletRequestUtil.getString(request, "appealMoreInfo");
		Long souCoin=HttpServletRequestUtil.getLong(request, "souCoin");
		int appealStatus=HttpServletRequestUtil.getInt(request, "appealStatus");
		Float latitude=HttpServletRequestUtil.getFloat(request, "latitude");
		Float longitude=HttpServletRequestUtil.getFloat(request, "longitude");
		Date startTime=HttpServletRequestUtil.getDate(request, "startTime");
		Date endTime=HttpServletRequestUtil.getDate(request, "endTime");
		Appeal appeal = new Appeal();
		appeal.setAppealContent(appealContent);
		appeal.setAppealMoreInfo(appealMoreInfo);
		appeal.setAppealStatus(appealStatus);
		appeal.setAppealTitle(appealTitle);
		appeal.setCity(city);
		appeal.setDistrict(district);
		appeal.setPhone(phone);
		appeal.setProvince(province);
		appeal.setUserId(userId);
		appeal.setEndTime(endTime);
		appeal.setSouCoin(souCoin);
		appeal.setLatitude(latitude);
		appeal.setLongitude(longitude);
		appeal.setFullAddress(fullAddress);
		appeal.setStartTime(startTime);
		
		// 空值判断
		try {
			AppealExecution ae = appealService.addAppeal(appeal);
			if (ae.getState() == AppealStateEnum.SUCCESS.getState()) {
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
	 * 修改求助信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyappeal", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyAppeal(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId=HttpServletRequestUtil.getLong(request, "appealId");
		Long userId=HttpServletRequestUtil.getLong(request, "userId");
		String appealTitle=HttpServletRequestUtil.getString(request, "appealTitle");
		String phone=HttpServletRequestUtil.getString(request, "phone");
		String appealContent=HttpServletRequestUtil.getString(request, "appealContent");
		String province=HttpServletRequestUtil.getString(request, "province");
		String city=HttpServletRequestUtil.getString(request, "city");
		String district=HttpServletRequestUtil.getString(request, "district");
		String fullAddress=HttpServletRequestUtil.getString(request, "fullAddress");
		String appealMoreInfo=HttpServletRequestUtil.getString(request, "appealMoreInfo");
		Long souCoin=HttpServletRequestUtil.getLong(request, "souCoin");
		int appealStatus=HttpServletRequestUtil.getInt(request, "appealStatus");
		Float latitude=HttpServletRequestUtil.getFloat(request, "latitude");
		Float longitude=HttpServletRequestUtil.getFloat(request, "longitude");
		Date startTime=HttpServletRequestUtil.getDate(request, "startTime");
		Date endTime=HttpServletRequestUtil.getDate(request, "endTime");
		Appeal appeal = new Appeal();
		appeal.setAppealContent(appealContent);
		appeal.setAppealId(appealId);
		appeal.setAppealMoreInfo(appealMoreInfo);
		appeal.setAppealStatus(appealStatus);
		appeal.setAppealTitle(appealTitle);
		appeal.setCity(city);
		appeal.setDistrict(district);
		appeal.setPhone(phone);
		appeal.setProvince(province);
		appeal.setUserId(userId);
		appeal.setEndTime(endTime);
		appeal.setSouCoin(souCoin);
		appeal.setLatitude(latitude);
		appeal.setLongitude(longitude);
		appeal.setFullAddress(fullAddress);
		appeal.setStartTime(startTime);
		
		// 空值判断
		if (appeal != null && appeal.getAppealId() != null) {
			try {
				AppealExecution ae = appealService.modifyAppeal(appeal);
				if (appeal.getAppealStatus() != null && appeal.getAppealStatus() == 4) {
					Help helpCondition = new Help();
					helpCondition.setAppealId(appeal.getAppealId());
					List<Help> helps = helpService.getHelpList(helpCondition).getHelpList();
					for (Help help : helps) {
						help.setHelpStatus(4);
						helpService.modifyHelp(help);
					}
				}
				if (ae.getState() == AppealStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入求助信息");
		}
		return modelMap;
	}
}
