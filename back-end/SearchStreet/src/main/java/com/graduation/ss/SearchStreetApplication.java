package com.graduation.ss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SearchStreetApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SearchStreetApplication.class, args);
	}
	/**
	 * 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
	 */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(SearchStreetApplication.class);
    }
}