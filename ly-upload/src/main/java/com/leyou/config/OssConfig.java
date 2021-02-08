package com.leyou.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cbh
 * @PackageName:com.leyou.config
 * @ClassName:OssConfig
 * @Description:
 * @date 2021-01-29 21:07
 */
@Configuration
public class OssConfig {
	@Bean
	public OSS createOss(OssProperties ossProperties) {
		return new OSSClientBuilder().build(ossProperties.getEndpoint(),
				ossProperties.getAccessKeyId(),
				ossProperties.getAccessKeySecret());
	}
}
