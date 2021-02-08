package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cbh
 * @PackageName:com.leyou
 * @ClassName:LyUploadApplication
 * @Description:
 * @date 2021-01-29 12:06
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LyUploadApplication {
	public static void main(String[] args) {
		SpringApplication.run(LyUploadApplication.class, args);
	}
}
