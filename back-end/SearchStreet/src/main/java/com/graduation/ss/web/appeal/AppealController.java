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
import com.graduation.ss.entity.Help;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.AppealStateEnum;
import com.graduation.ss.exceptions.AppealOperationException;
import com.graduation.ss.service.AppealService;
import com.graduation.ss.service.HelpService;
import com.graduation.ss.service.WechatAuthService;
import com.graduation.ss.util.HttpServletRequestUtil;
import com.graduation.ss.util.JWT;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/appeal")
@Api(value = "AppealController|对求助操作的控制器")
public class AppealController {
	@Autowired
	private AppealService appealService;
	@Autowired
	private AppealImgDao appealImgDao;
	@Autowired
	private WechatAuthService wechatAuthService;
	@Autowired
	private HelpService helpservice;

	@RequestMapping(value = "/getappeallistbyuserid", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据用户ID和求助状态获取相应的（可增加输入的条件有：求助名（模糊），省份，城市，地区，指定日期范围（大于输入startTime，小于输入endTime），搜币（大于输入搜币））求助信息(分页)", notes = "进行中:appealStatus=1（返回的appealStatus=0表示没有确定帮助者，appealStatus=1表示已确定帮助者）,已完成:appealStatus=2,已失效:appealStatus=3")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的数目", required = true, dataType = "int") })
	private Map<String, Object> getAppealListByUserId(
			@RequestBody @ApiParam(name = "appeal", value = "传入json格式,不用传appealId", required = true) Appeal appeal,
			String token, int pageIndex, int pageSize) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
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
			return modelMap;
		}
		try {
			appeal.setUserId(userId);
			AppealExecution ae = appealService.getAppealListFY(appeal, pageIndex, pageSize);
			if (ae.getState() == AppealStateEnum.SUCCESS.getState()) {
				int pageNum = (int) (ae.getCount() / pageSize);
				if (pageNum * pageSize < ae.getCount())
					pageNum++;
				modelMap.put("appealList", ae.getAppealList());
				modelMap.put("pageNum", pageNum);
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", AppealStateEnum.stateOf(ae.getState()).getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getappealbyid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据求助ID获取求助信息")
	@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long")
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
				return modelMap;
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
			return modelMap;
		}

	}

	@RequestMapping(value = "/registerappeal", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建求助（不添加图片）", notes = "不用传appealId")
	@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String")
	private Map<String, Object> registerAppeal(
			@RequestBody @ApiParam(name = "appeal", value = "传入json格式,不用传appealId", required = true) Appeal appeal,
			String token) {
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
			return modelMap;
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
	@ApiOperation(value = "修改求助信息（不修改图片）", notes = "要传appealId,目前前端好像不需要用")
	@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String")
	private Map<String, Object> modifyAppeal(
			@RequestBody @ApiParam(name = "appeal", value = "传入json格式,不用传appealId", required = true) Appeal appeal) {
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
	@ApiOperation(value = "上传求助图片", notes = "在swagger这个网站上看不了效果,请查看前端已使用过的页面")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助id", required = true, dataType = "Long") })
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
	@ApiOperation(value = "返回用户20km内的所有有效（没确定帮助人、没过时的）求助")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "longitude", value = "用户所在的经度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "latitude", value = "用户所在的纬度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "appealTitle", value = "求助标题", required = false, dataType = "String") })
	private Map<String, Object> searchNearbyAppeals(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		String appealTitle = HttpServletRequestUtil.getString(request, "appealTitle");
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
			AppealExecution ae = appealService.getNearbyAppealList(maxlat, minlat, maxlng, minlng, appealTitle);
			modelMap.put("appealList", ae.getAppealList());
			modelMap.put("minlng", minlng);
			modelMap.put("maxlng", maxlng);
			modelMap.put("minlat", minlat);
			modelMap.put("maxlat", maxlat);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/competeappeal", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "求助者确定完成求助并支付搜币")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long") })
	private Map<String, Object> competeAppeal(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = HttpServletRequestUtil.getString(request, "token");
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Help help = helpservice.getByHelpId(helpId);
		if (help.getAppealId() != appealId) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId和helpId不对应");
			return modelMap;
		}
		Long appealUserId = null;
		UserCode2Session userCode2Session = null;
		// 将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		// 获取个人ID
		String openId = userCode2Session.getOpenId();
		try {
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			appealUserId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		try {
			AppealExecution ae = appealService.completeAppeal(appealId, helpId, appealUserId);
			if (ae.getState() == AppealStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ae.getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/additionsoucoin", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "求助者追赏")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "additionSouCoin", value = "追赏金数", required = true, dataType = "Long") })
	private Map<String, Object> additionSouCoin(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = HttpServletRequestUtil.getString(request, "token");
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Long additionSouCoin = HttpServletRequestUtil.getLong(request, "additionSouCoin");
		Help help = helpservice.getByHelpId(helpId);
		if (help.getAppealId() != appealId) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId和helpId不对应");
			return modelMap;
		}
		Long appealUserId = null;
		UserCode2Session userCode2Session = null;
		// 将token解密成openId 和session_key
		userCode2Session = JWT.unsign(token, UserCode2Session.class);
		// 获取个人ID
		String openId = userCode2Session.getOpenId();
		try {
			WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			appealUserId = wechatAuth.getUserId();
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		try {
			appealService.additionSouCoin(helpId, appealUserId, additionSouCoin);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
