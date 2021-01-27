package com.leyou.common.controller;

import com.leyou.common.exception.pojo.ExceptionResult;
import com.leyou.common.exception.pojo.LyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cbh
 * @PackageName:com.leyou.common.controller
 * @ClassName:LyExceptionController
 * @Description: 全局自定义异常处理器
 * @date 2021-01-26 20:30
 */
@ControllerAdvice //该注解回覆盖SpringMvc自带的异常处理器
public class LyExceptionController {
	/**
	 * 自定义异常处理器
	 */
	@ExceptionHandler(value = LyException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResult> handlerException(LyException e){
		return ResponseEntity.status(e.getStatus()).body(new ExceptionResult(e));
	}
}
