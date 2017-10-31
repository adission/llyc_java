package cn.com.lanlyc.core.util;

/*
 * 闸机控制板信息实体类
 * Main中接口返回信息时调用
 * @luoying 2017-08-18 18:40:49
 * */
public class GateReturnMessage {
	private int code;//操作结果码
	private String message="";//提示信息
	private Object data=null;//返回数据
	
	public GateReturnMessage(){}
	
	/*操作成功时的返回数据
	 * @luoying 2017-08-18 18:45:28*/
	public void rightMesg(String message,Object data) {
		this.code=200;
		this.message=message;
		this.data=data;
	}
	
	/*操作错误时的返回数据
	 * @luoying 2017-08-18 18:46:31*/
	public void errorMesg(String message,Object data) {
		this.code=-1;
		this.message=message;
		this.data=data;
	}
	
	/*参数传递出错时的返回数据
	 * @luoying 2017-08-18 18:46:31*/
	public void paramErrorMesg(String message) {
		this.code=500;
		this.message=message;
	}
	
	/*超时的返回数据
	 * @luoying 2017-08-18 18:46:31*/
	public void commuErrorMesg() {
		this.code=0;
		this.message="超时";
	}
	
	/*数据已存在时的返回数据
	 * 比如增加操作时
	 * @luoying 2017-08-18 18:50:26*/
	public void dateExsitMesg(String message) {
		this.code=202;
		this.message=message;
	}
	
	/*数据不存在时的返回数据
	 * 比如删除修改操作时
	 * @luoying 2017-08-18 18:51:53*/
	public void dateNotExsitMesg(String message) {
		this.code=203;
		this.message=message;
	}

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
	
	
}
