package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author cbh
 * @PackageName:com.leyou
 * @ClassName:LySearchApplication
 * @Description:
 * @date 2021-02-03 20:53
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LySearchApplication {
	public static void main(String[] args) {
		SpringApplication.run(LySearchApplication.class, args);
	}
}
