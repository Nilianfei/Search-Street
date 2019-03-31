package com.graduation.ss.config.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.graduation.ss.interceptor.superadmin.SuperAdminLoginInterceptor;
import com.graduation.ss.interceptor.wechat.TokenInterceptor;

/**
 * 开启Mvc,自动注入spring容器。 WebMvcConfigurerAdapter：配置视图解析器
 * 当一个类实现了这个接口（ApplicationContextAware）之后，这个类就可以方便获得ApplicationContext中的所有bean
 *
 */
@SuppressWarnings("deprecation")
@Configuration
// 等价于<mvc:annotation-driven/>
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {
	// Spring容器
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (ApplicationContext) applicationContext;
	}

	/**
	 * 静态资源配置
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
		// registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/baidu/work/image/upload/");
	}

	/**
	 * 定义默认的请求处理器
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * 创建viewResolver
	 * 
	 * @return
	 */
	@Bean(name = "viewResolver")
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		// 设置Spring 容器
		viewResolver.setApplicationContext((org.springframework.context.ApplicationContext) this.applicationContext);
		// 取消缓存
		viewResolver.setCache(false);
		// 设置解析的前缀
		viewResolver.setPrefix("/WEB-INF/html/");
		// 设置试图解析的后缀
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

	/**
	 * 文件上传解析器
	 * 
	 * @return
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		// 1024 * 1024 * 20 = 20M
		multipartResolver.setMaxUploadSize(20971520);
		multipartResolver.setMaxInMemorySize(20971520);
		return multipartResolver;
	}

	/**
	 * 添加拦截器配置
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> interceptPaths = new ArrayList<String>();
		interceptPaths.add("/wechat/**");
		interceptPaths.add("/shopadmin/**");
		interceptPaths.add("/appeal/**");
		interceptPaths.add("/help/**");
		// 注册拦截器
		InterceptorRegistration tokenIR = registry.addInterceptor(new TokenInterceptor());
		// 配置拦截的路径
		tokenIR.addPathPatterns(interceptPaths);
		tokenIR.excludePathPatterns("/wechat/login");
		tokenIR.excludePathPatterns("/shopadmin/searchnearbyshops");
		tokenIR.excludePathPatterns("/shopadmin/getshopbyid");
		tokenIR.excludePathPatterns("/appeal/searchnearbyappeals");
		tokenIR.excludePathPatterns("/appeal/getappealbyid");
		tokenIR.excludePathPatterns("/help/gethelplistbyappealid");
		tokenIR.excludePathPatterns("/help/gethelpbyhelpid");

		/** 超级管理员系统拦截部分 **/
		String interceptPath = "/superadmin/**";
		// 注册拦截器
		InterceptorRegistration superAdminIR = registry.addInterceptor(new SuperAdminLoginInterceptor());
		// 配置拦截的路径
		superAdminIR.addPathPatterns(interceptPath);
		superAdminIR.excludePathPatterns("/superadmin/login");
		superAdminIR.excludePathPatterns("/superadmin/logincheck");
		superAdminIR.excludePathPatterns("/superadmin/main");
		superAdminIR.excludePathPatterns("/superadmin/top");
//		superAdminIR.excludePathPatterns("/superadmin/register");
	}

}
