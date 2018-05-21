package com.movie.ycsaddas.common;

/**
 * 结果处理
 *
 * @author walker
 * @date 2018/05/18
 */
public class JsonResult {

	// 请求返回状态码 200 OK；404 NOT FOUND；500 服务器错误
	private int code;

	// 请求返回数据
	private Object data;

	// 请求返回消息
	private String msg;

	/**
	 * 请求成功
	 * @param data 请求成功数据
	 * @return
	 */
	public static JsonResult success(Object data) {
		JsonResult result = new JsonResult();
		result.code = 200;
		result.data = data;
		result.msg = "success";
		return result;
	}

	/**
	 * 请求成功
	 * @return
	 */
	public static JsonResult success() {
		JsonResult result = new JsonResult();
		result.code = 200;
		result.data = "";
		result.msg = "success";
		return result;
	}

	/**
	 * 请求失败
	 * @param error 请求失败说明
	 * @return
	 */
	public static JsonResult failture(String error) {
		JsonResult result = new JsonResult();
		result.code = 404;
		result.data = null;
		result.msg = error;
		return result;
	}

	public int getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}
}
