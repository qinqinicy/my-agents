package com.sqx.common.exception;

import com.sqx.common.utils.*;
import org.slf4j.*;
import org.springframework.dao.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

/**
 * 异常处理器
 *
 */
@RestControllerAdvice
public class SqxExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(SqxException.class)
	public Result handleException(SqxException e){
		logger.error("{} - 处理异常",BusiContext.get().getId(),e);
		Result r = new Result(e.getCode(),e.getMessage());
		return r;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public Result handlerNoFoundException(Exception e) {
		logger.error("{} - 处理异常",BusiContext.get().getId(),e.getMessage(), e);
		return Result.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result handleDuplicateKeyException(DuplicateKeyException e){
		logger.error("{} - 处理异常",BusiContext.get().getId(),e.getMessage(), e);
		return Result.error("数据库中已存在该记录");
	}

	/*@ExceptionHandler(AuthorizationException.class)
	public Result handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return Result.error("没有权限，请联系管理员授权");
	}*/

	@ExceptionHandler(MissingRequestHeaderException.class)
	public Result handleMissingRequestHeaderException(Exception e){
		logger.error("{} - 处理异常",BusiContext.get().getId(),e.getMessage(), e);
		return Result.error("用户token为空，请联系管理员");
	}

	@ExceptionHandler(Exception.class)
	public Result handleException(Exception e){
		logger.error("{} - 处理异常",BusiContext.get().getId(),e.getMessage(), e);
		return Result.error();
	}


}
