package com.leyou.common.exception;

import lombok.Getter;

/**
 * @author cbh
 * @PackageName:com.leyou.common.exception.pojo
 * @ClassName:LyException
 * @Description:自定义异常类，封装自定义异常信息
 * @date 2021-01-26 19:55
 */
@Getter
public class LyException extends RuntimeException {
	private Integer status;

	public LyException(Integer status, String message) {
		super(message);
		this.status = status;
	}

	public LyException(ExceptionEnum exceptionEnum){
		super(exceptionEnum.getMessage());
		this.status = exceptionEnum.getStatus();
	}
}
