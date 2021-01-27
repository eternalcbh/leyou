package com.leyou.gateway.fallback;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cbh
 * @PackageName:com.leyou.gateway.fallback
 * @ClassName:FallbackController
 * @Description:
 * @date 2021-01-25 21:58
 */
@RestController
public class FallbackController {
	/**
	 * 降级方法
	 */
	@RequestMapping("/fallback")
	public String fallback() {
		return "服务器繁忙，请稍后再试.......";
	}
}
