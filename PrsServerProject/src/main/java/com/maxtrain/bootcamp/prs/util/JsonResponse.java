package com.maxtrain.bootcamp.prs.util;

public class JsonResponse {

	private int code;
	private String message;
	private Object data;
	private Object error;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Object getError() {
		return error;
	}
	public void setError(Object error) {
		this.error = error;
	}
	public static JsonResponse getInstance(Object d) {
		return new JsonResponse(0, "Success", d, null);
		
	}
	public JsonResponse(int code, String message, Object data, Object error) {
	 this.code = code;
	 this.message= message;
	 this.data=data;
	 this.error=error;
}
}