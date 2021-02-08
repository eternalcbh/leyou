package com.leyou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cbh
 * @PackageName:com.leyou.config
 * @ClassName:OssProperties
 * @Description:
 * @date 2021-01-29 21:04
 */
@ConfigurationProperties(prefix = "ly.oss")
@Component
@Data
public class OssProperties {
	private String accessKeyId;
	private String accessKeySecret;
	private String host;
	private String endpoint;
	private String dir;
	private Long expireTime;
	private Long maxFileSize;
}
