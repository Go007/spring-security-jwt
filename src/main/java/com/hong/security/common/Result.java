package com.hong.security.common;

import lombok.Data;

@Data
public class Result<T> {

	public static final Integer CODE_SUCCESS = 1;
	public static final Integer CODE_FAILURE = 0;
	public static final String MSG_SUCCESS = "操作成功";
	public static final String MSG_FAILURE = "操作失败";
	/**
	 * 返回码
	 */
	private Integer code;
	/**
	 * 返回信息说明 单个String:msg 多个{ "fieldName2": "msg2", "fieldName1": "msg1" }]
	 */
	private String msg;
	/**
	 * 重定向url
	 */
	private String url;
	/**
	 * 业务数据
	 */
	private T data;

	public Result() {
		this.code = CODE_SUCCESS;
		this.msg = MSG_SUCCESS;
	}

	public Result(Integer code) {
		this.code = code;
	}

	public Result(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Result(Integer code, String msg, String url) {
		this.code = code;
		this.msg = msg;
		this.url = url;
	}

	public Result(Integer code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Result(Integer code, String msg, String url, T data) {
		this.code = code;
		this.msg = msg;
		this.url = url;
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result{" + "code=" + code + ", msg=" + msg + ", url='" + url + '\'' + ", data=" + data + '}';
	}
	
}
