package com.sqx.modules.decision.entity.third;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.sqx.common.utils.*;
import io.swagger.annotations.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.apache.http.*;

/**
 * 返回数据
 *
 */
@Data
@ApiModel
@Slf4j
@JsonInclude(JsonInclude.Include.ALWAYS)
public class OrderResult<T>{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "code")
	private  int code;
	/**
	 * 执行情况描述
	 */
	@ApiModelProperty(value = "message")
	private  String message;


	@ApiModelProperty(value = "返回数据")
	private T  data;

	public OrderResult(){

	}
	public OrderResult(T t){
		this.data= t;
	}


	public OrderResult(int code, String msg, T t) {
		this.code = code;
		this.message = msg;
		this.data=t;
	}

	public OrderResult(int code, String message){
		this.code=code;
		this.message=message;
	}

	public OrderResult upStatus(Integer rows) {
		return rows > 0 ? success() : error();
	}
	public  static OrderResult error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static OrderResult error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static OrderResult error(int code, String msg) {
		OrderResult r = new OrderResult(code,msg);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String s = objectMapper.writeValueAsString(r);
			BusiContext busiContext = BusiContext.get();
			busiContext.setResponseBody(s);
		}catch (Exception e){
			log.error("返回为空",e);
		}
		return r;
	}

	public static OrderResult success(String msg) {
		OrderResult r = new OrderResult(0,msg);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String s = objectMapper.writeValueAsString(r);
			BusiContext busiContext = BusiContext.get();
			busiContext.setResponseBody(s);
		}catch (Exception e){
			log.error("返回为空",e);
		}
		return r;
	}
	
	public OrderResult success(T t) {
		OrderResult r = new OrderResult(t);
		r.setCode(0);
		r.setMessage("success");
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
	
	public static OrderResult success() {
		OrderResult r = new OrderResult(0, "success");
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String s = objectMapper.writeValueAsString(r);
			BusiContext busiContext = BusiContext.get();
			busiContext.setResponseBody(s);
		}catch (Exception e){
			log.error("返回为空",e);
		}
		return r;
	}



}
