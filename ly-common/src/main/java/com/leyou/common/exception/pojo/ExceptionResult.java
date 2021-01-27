package com.leyou.common.exception.pojo;

import lombok.Getter;
import org.joda.time.DateTime;

/**
 * @author cbh
 * @PackageName:com.leyou.common.exception.pojo
 * @ClassName:ExceptionResult
 * @Description: 封装异常结果信息
 * @date 2021-01-26 20:34
 */
@Getter
public class ExceptionResult {
	private Integer status; //异常状态码
	private String message; //异常消息
	private String timestamp; //异常发生时间

	public ExceptionResult(LyException e){
		this.status = e.getStatus();
		this.message = e.getMessage();
		this.timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
	}
}
