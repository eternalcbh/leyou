package com.leyou;

/**
 * @author cbh
 * @PackageName:com.leyou
 * @ClassName:LyItemApplication
 * @Description:
 * @date 2021-01-27 9:48
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 商品微服务
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.item.mapper") //mybatis-plus扫描Mapper接口所在目录
public class LyItemApplication {
	public static void main(String[] args) {
		SpringApplication.run(LyItemApplication.class,args);
	}
}