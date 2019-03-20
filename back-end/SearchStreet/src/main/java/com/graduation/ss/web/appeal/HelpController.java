package com.graduation.ss.web.appeal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.graduation.ss.dto.HelpExecution;
import com.graduation.ss.dto.UserCode2Session;
import com.graduation.ss.entity.Help;
import com.graduation.ss.entity.WechatAuth;
import com.graduation.ss.enums.HelpStateEnum;
import com.graduation.ss.exceptions.HelpOperationException;
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
@RequestMapping("/help")
@Api(value = "HelpController|对帮助操作的控制器")
public class HelpController {
	@Autowired
	private HelpService helpService;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/gethelplistbyappealid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据求助ID获取求助信息列表(分页)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的列数", required = true, dataType = "int") })
	private Map<String, Object> getHelpListByAppealId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if (appealId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
		} else {
			try {
				Help helpCondition = new Help();
				helpCondition.setAppealId(appealId);
				HelpExecution helpExecution = helpService.getHelpListFY(helpCondition, null, null, pageIndex, pageSize);
				int pageNum = (int) (helpExecution.getCount() / pageSize);
				if (pageNum * pageSize < helpExecution.getCount())
					pageNum++;
				modelMap.put("helpList", helpExecution.getHelpList());
				modelMap.put("pageNum", pageNum);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		return modelMap;
	}

	@RequestMapping(value = "/queryishelp", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询是否帮助了这个求助")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String") })
	private Map<String, Object> queryisHelp(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long appealId = HttpServletRequestUtil.getLong(request, "appealId");
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
			return modelMap;
		}
		if (appealId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
		} else {
			try {
				Help helpCondition = new Help();
				helpCondition.setAppealId(appealId);
				helpCondition.setUserId(userId);
				HelpExecution helpExecution = helpService.getHelpListFY(helpCondition, null, null, 0, 100);
				if (helpExecution.getCount() > 0) {
					modelMap.put("ishelp", true);
					modelMap.put("success", true);
				} else {
					modelMap.put("ishelp", false);
					modelMap.put("success", true);
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		return modelMap;
	}

	@RequestMapping(value = "/gethelplistbyuserid", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据用户ID和帮助状态查询帮助（可增加输入的条件有：帮助状态，指定日期范围，搜币（大于等于输入搜币）信息(分页)", notes = "进行中:helpStatus=1（返回的helpStatus=0表示还没有被选中，helpStatus=1表示已被选中）,已完成:helpStatus=2,已失效:helpStatus=3")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "startTime", value = "时间范围（下限）", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "endTime", value = "时间范围（上限）", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的数目", required = true, dataType = "int") })
	private Map<String, Object> getHelpListByUserId(
			@RequestBody @ApiParam(name = "help", value = "传入json格式,不用传helpId", required = true) Help help,
			String token, String startTime, String endTime, int pageIndex, int pageSize) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTimeD;
		Date endTimeD;
		try {
			startTimeD = sdf.parse(startTime);
			endTimeD = sdf.parse(endTime);
		} catch (ParseException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}

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
			help.setUserId(userId);
			HelpExecution helpExecution = helpService.getHelpListFY(help, startTimeD, endTimeD, pageIndex, pageSize);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				int pageNum = (int) (helpExecution.getCount() / pageSize);
				if (pageNum * pageSize < helpExecution.getCount())
					pageNum++;
				modelMap.put("helpList", helpExecution.getHelpList());
				modelMap.put("pageNum", pageNum);
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", HelpStateEnum.stateOf(helpExecution.getState()).getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/gethelpbyhelpid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据帮助ID获取帮助信息")
	@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助Id", required = true, dataType = "Long")
	private Map<String, Object> getHelpByHelpId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		if (helpId > 0) {
			try {
				// 获取帮助信息
				Help help = helpService.getByHelpId(helpId);
				modelMap.put("help", help);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "helpId无效");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addHelp", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建帮助")
	@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String")
	private Map<String, Object> addHelp(
			@RequestBody @ApiParam(name = "help", value = "传入json格式,只传appealId和appealTitle就好了", required = true) Help help,
			String token) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long userId = null;
		System.out.println(help.toString());
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
		help.setUserId(userId);
		HelpExecution helpExecution;
		try {
			helpExecution = helpService.addHelp(help);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("helpId", helpExecution.getHelp().getHelpId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", helpExecution.getStateInfo());
			}
		} catch (HelpOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/*@RequestMapping(value = "/modifyhelp", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改帮助", notes = "目前前端好像不需要用")
	@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String")
	private Map<String, Object> modifyHelp(
			@RequestBody @ApiParam(name = "help", value = "传入json格式,要传helpId", required = true) Help help) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		HelpExecution helpExecution;
		try {
			helpExecution = helpService.modifyHelp(help);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("helpId", helpExecution.getHelp().getHelpId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", helpExecution.getStateInfo());
			}
		} catch (HelpOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}*/

	@RequestMapping(value = "/selectHelper", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "选择帮助的人")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "token", value = "包含用户信息的token", required = true, dataType = "String") })
	private Map<String, Object> selectHelper(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		try {
			helpService.selectHelp(helpId, appealId);
		} catch (Exception e) {
			modelMap.put("success", true);
			modelMap.put("errMsg", e.getMessage());
		}
		modelMap.put("success", true);
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
		Help help = helpService.getByHelpId(helpId);
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
			helpService.additionSouCoin(helpId, appealUserId, additionSouCoin);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
