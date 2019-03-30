package com.graduation.ss.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/superadmin", method = { RequestMethod.GET, RequestMethod.POST })
public class SuperAdminController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login() {
		// 超级管理员登录页
		return "superadmin/login";
	}

	@RequestMapping(value = "/shopmanage", method = RequestMethod.GET)
	private String shopmanage() {
		// 店铺管理页
		return "superadmin/shopmanage";
	}

	@RequestMapping(value = "/appealmanage", method = RequestMethod.GET)
	private String appealmanage() {
		// 求助管理页
		return "superadmin/appealmanage";
	}

	@RequestMapping(value = "/helpmanage", method = RequestMethod.GET)
	private String helpmanage() {
		// 求助管理页
		return "superadmin/helpmanage";
	}

	@RequestMapping(value = "/personinfomanage", method = RequestMethod.GET)
	private String personInfomanage() {
		// 用户信息管理页
		return "superadmin/personinfomanage";
	}

	@RequestMapping(value = "/localauthmanage", method = RequestMethod.GET)
	private String localauthmanage() {
		// 本地账号管理页
		return "superadmin/localauthmanage";
	}

	@RequestMapping(value = "/wechatauthmanage", method = RequestMethod.GET)
	private String wechatauthmanage() {
		// 微信账号管理页
		return "superadmin/wechatauthmanage";
	}

	@RequestMapping(value = "/shopimgmanage", method = RequestMethod.GET)
	private String shopimgmanage() {
		// 商铺图片管理页
		return "superadmin/shopimgmanage";
	}

	@RequestMapping(value = "/appealimgmanage", method = RequestMethod.GET)
	private String appealimgmanage() {
		// 求助图片管理页
		return "superadmin/appealimgmanage";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	private String main() {
		// 超级管理员主页
		return "superadmin/main";
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET)
	private String top() {
		// 超级管理员frame top部分
		return "superadmin/top";
	}
}
