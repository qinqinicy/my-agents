package com.sqx.common.utils;

import cn.hutool.log.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import io.swagger.annotations.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.apache.http.HttpStatus;
import org.springframework.cache.interceptor.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 */
@Data
@ApiModel
@Slf4j
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Result<T>{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "code")
	private  int code;
	/**
	 * 执行情况描述
	 */
	@ApiModelProperty(value = "msg")
	private  String msg;


/*	@ApiModelProperty(value = "返回数据")
	private Map<String,T> result;*/

	@ApiModelProperty(value = "返回数据")
	private T result;

	public Result (){

	}
	public Result (T t){
/*	Map<String,T> hashMap = new HashMap();
		hashMap.put("result", t);
		this.result= hashMap;
		*/
		this.result=t;


	}



	public Result(int code, String msg, T t) {
		this.code = code;
		this.msg = msg;
		this.result=t;
	}

	public Result (int code, String message){
		this.code=code;
		this.msg=message;
	}

	public  Result upStatus(Integer rows) {
		return rows > 0 ? success() : error();
	}
	public  static Result error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Result error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static Result error(int code,String msg) {
		Result r = new Result(code,msg);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String s = objectMapper.writeValueAsString(r);
			BusiContext busiContext = BusiContext.get();
			busiContext.setResponseBody(s);
		}catch (Exception e){
			log.error("返回为空");
		}
		return r;
	}

	public static Result success(String msg) {
		Result r = new Result(0,msg);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String s = objectMapper.writeValueAsString(r);
			BusiContext busiContext = BusiContext.get();
			busiContext.setResponseBody(s);
		}catch (Exception e){
			log.error("返回为空");
		}
		return r;
	}
	
	public Result success(T t) {
		Result r = new Result(t);
		r.setCode(0);
		r.setMsg("success");
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String s = objectMapper.writeValueAsString(r);
			BusiContext busiContext = BusiContext.get();
			busiContext.setResponseBody(s);
		}catch (Exception e){
			log.error("返回为空");
		}
		return r;
	}
	
	public static Result success() {
		Result r = new Result(0, "success");
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String s = objectMapper.writeValueAsString(r);
			BusiContext busiContext = BusiContext.get();
			busiContext.setResponseBody(s);
		}catch (Exception e){
			log.error("返回为空");
		}
		return r;
	}



}
