package com.graduation.ss.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.graduation.ss.dto.ConstantForSuperAdmin;
import com.graduation.ss.dto.HelpExecution;
import com.graduation.ss.entity.Help;
import com.graduation.ss.enums.HelpStateEnum;
import com.graduation.ss.service.HelpService;
import com.graduation.ss.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class HelpSuperController {
	@Autowired
	private HelpService helpService;

	@RequestMapping(value = "/listhelps", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listHelps(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		HelpExecution he = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			Help helpCondition = new Help();
			int helpStatus = HttpServletRequestUtil.getInt(request, "helpStatus");
			if (helpStatus >= 0) {
				// 若传入状态，则将状态添加到查询条件里
				helpCondition.setHelpStatus(helpStatus);
			}
			Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
			if (appealId != null) {
				// 若传入求助Id，则将求助Id添加到查询条件里
				helpCondition.setAppealId(appealId);
			}
			try {
				// 根据查询条件分页返回求助列表
				he = helpService.getHelpListFY(helpCondition, null, null, pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (he.getHelpList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, he.getHelpList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, he.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<Help>());
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

	@RequestMapping(value = "/addhelp", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addHelp(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		String appealTitle = HttpServletRequestUtil.getString(request, "appealTitle");
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		
		Help help = new Help();
		help.setAppealId(appealId);
		help.setAppealTitle(appealTitle);
		help.setUserId(userId);

		try {
			HelpExecution ae = helpService.addHelp(help);
			if (ae.getState() == HelpStateEnum.SUCCESS.getState()) {
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

	@RequestMapping(value = "/modifyhelp", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyHelp(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		int helpStatus = HttpServletRequestUtil.getInt(request, "helpStatus");
		int completion = HttpServletRequestUtil.getInt(request, "completion");
		int efficiency = HttpServletRequestUtil.getInt(request, "efficiency");
		int attitude = HttpServletRequestUtil.getInt(request, "attitude");
		Long allCoin = HttpServletRequestUtil.getLong(request, "allCoin");
		Long additionalCoin = HttpServletRequestUtil.getLong(request, "additionalCoin");
		Help help = new Help();
		help.setHelpId(helpId);
		help.setHelpStatus(helpStatus);
		help.setCompletion(completion);
		help.setEfficiency(efficiency);
		help.setAttitude(attitude);
		help.setAllCoin(allCoin);
		help.setAdditionalCoin(additionalCoin);

		// 空值判断
		if (helpId != null) {
			try {
				HelpExecution he = helpService.modifyHelp(help);
				if (he.getState() == HelpStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", he.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入帮助信息");
		}
		return modelMap;
	}
}
