package com.graduation.ss.web.appeal;

import java.io.IOException;
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

import com.graduation.ss.dao.AppealImgDao;
import com.graduation.ss.dto.AppealExecution;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.entity.Appeal;
import com.graduation.ss.entity.AppealImg;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.AppealStateEnum;
import com.graduation.ss.exceptions.AppealOperationException;
import com.graduation.ss.service.AppealService;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.HttpServletRequestUtil;
import com.graduation.ss.util.JWT;

@RestController
@RequestMapping("/appeal")
public class AppealController {
	@Autowired
	private AppealService appealService;
	@Autowired
	private AppealImgDao appealImgDao;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/getappeallistbyuserid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAppealListByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = HttpServletRequestUtil.getString(request, "token");
		Long userId = null;
		UserCode2Session userCode2Session = null;
		// 将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		// 获取个人信息
		String openId = userCode2Session.getOpenId();
		try {
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			userId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		try {
			Appeal appealCondition = new Appeal();
			appealCondition.setUserId(userId);
			AppealExecution ae = appealService.getAppealList(appealCondition, 0, 100);
			modelMap.put("appealList", ae.getAppealList());
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getappealbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAppealByAppealId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		if (appealId > 0) {
			try {
				// 获取求助信息
				Appeal appeal = appealService.getByAppealId(appealId);
				// 获取求助详情图列表
				List<AppealImg> appealImgList = appealImgDao.getAppealImgList(appealId);
				appeal.setAppealImgList(appealImgList);
				modelMap.put("appeal", appeal);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
		}
		return modelMap;
	}

	@RequestMapping(value = "/registerappeal", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerAppeal(@RequestBody Appeal appeal, String token) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 添加求助
		Long userId = null;
		UserCode2Session userCode2Session = null;
		// 将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		// 获取个人ID
		String openId = userCode2Session.getOpenId();
		try {
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			userId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		appeal.setUserId(userId);
		AppealExecution se;
		try {
			se = appealService.addAppeal(appeal);
			if (se.getState() == AppealStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("appealId", se.getAppeal().getAppealId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}
		} catch (AppealOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyappeal", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyAppeal(@RequestBody Appeal appeal) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		AppealExecution ae;
		try {
			ae = appealService.modifyAppeal(appeal);
			if (ae.getState() == AppealStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("appealId", ae.getAppeal().getAppealId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ae.getStateInfo());
			}
		} catch (AppealOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	private void handleImage(HttpServletRequest request, ImageHolder appealImg) throws IOException {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile appealImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("appealImg");
		if (appealImgFile != null && appealImg != null) {
			appealImg.setImage(appealImgFile.getInputStream());
			appealImg.setImageName(appealImgFile.getOriginalFilename());
		}
	}

	@RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> uploadImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接收并转化相应的参数，包括求助id以及图片信息
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		ImageHolder appealImg = new ImageHolder("", null);
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, appealImg);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			System.out.println(e.getMessage());
			return modelMap;
		}
		// 2.上传求助图片
		try {
			appealService.uploadImg(appealId, appealImg);
		} catch (AppealOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		modelMap.put("success", true);
		return modelMap;
	}

	@RequestMapping(value = "/searchnearbyappeals", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> searchNearbyAppeals(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		float minlat = 0f;// 定义经纬度四个极限值
		float maxlat = 0f;
		float minlng = 0f;
		float maxlng = 0f;

		// 先计算查询点的经纬度范围
		float r = 6371;// 地球半径千米
		float dis = 20;// 距离（单位：千米），查询范围20km内的所有求助
		float dlng = (float) (2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(latitude * Math.PI / 180)));
		dlng = (float) (dlng * 180 / Math.PI);
		float dlat = dis / r;
		dlat = (float) (dlat * 180 / Math.PI);
		if (dlng < 0) {
			minlng = longitude + dlng;
			maxlng = longitude - dlng;
		} else {
			minlng = longitude - dlng;
			maxlng = longitude + dlng;
		}
		minlat = latitude - dlat;
		maxlat = latitude + dlat;
		try {
			AppealExecution se = appealService.getNearbyAppealList(maxlat, minlat, maxlng, minlng);
			modelMap.put("appealList", se.getAppealList());
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
}
