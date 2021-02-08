package com.leyou.item.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cbh
 * @PackageName:com.leyou.item.config
 * @ClassName:PageHelperConfiguration
 * @Description:配置分页拦截器
 * @date 2021-01-28 12:11
 */
@Configuration
public class PageHelperConfiguration {

	@Bean
	public PaginationInterceptor paginationInterceptor(){
		return new PaginationInterceptor();
	}
}
