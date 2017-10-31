/***
 * created by JiangMin at 2014-3-25
 * Response用户返回数据，包括状态，数据对象(List, Entity)
 */
package cn.com.lanlyc.core.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@SuppressWarnings("serial")
public class Response extends HashMap<String, Object> {

    private static String CODE = "code";
    private static String DATA = "data";
    private static String TOTAL = "total";
    private static String MESSAGE = "message";
    private static String ENTITY = "entity";
    private static String TOTAL_PAGE = "totalPage";

    private Logger logger = LoggerFactory.getLogger(Response.class);

    private Response() {
        this.put(CODE, Codes.SUCCESS);
        this.put(MESSAGE, "成功");
    }

    public static Response newResponse() {
        return new Response();
    }

    public static Response set(String key, Object value) {
        Response response = Response.newResponse();
        response.put(key, value);
        return response;
    }

    public Response setEntity(Object obj) {
        this.put(ENTITY, obj);
        return this;
    }

    public static Response set(Object data) {
        Response response = Response.newResponse();
        response.put(DATA, data);
        response.put(CODE, Codes.SUCCESS);
        response.put(MESSAGE, "ok");
        return response;
    }

    public Object getEntity() {
        return this.get(ENTITY);
    }

    /**
     * @param data
     * @author JiangMin
     */
    public void setData(Object data) {
        this.put(DATA, data);
    }

    public Response put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Response moveTo(String fromKey, String toKey) {
        Object val = this.get(fromKey);
        this.put(toKey, val);
        this.remove(fromKey);
        return this;
    }

    public Boolean isOK() {
        return this.getCode() == Codes.SUCCESS;
    }

    public Boolean isFail() {
        return !isOK();
    }

    public Response ok(Object value) {
        super.put(DATA, value);
        return this;
    }

    public Response ok(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Response setResults(Integer total, Object rows) {
        this.setTotal(total);
        this.ok(rows);
        return this;
    }

    public Response results(Integer total, Object rows) {
        this.setTotal(total);
        this.ok(rows);
        return this;
    }

    private Response setCode(int code) {
        this.put(CODE, code);
        return this;
    }

    private static Response code(int code) {
        Response result = new Response();
        result.setCode(code);

        return result;
    }

    public int getCode() {
        return Integer.parseInt(this.get(CODE).toString());
    }

    public Response error(Exception e) {
        e.printStackTrace();
        logger.error("ExceptionError", e);
        logger.error("Exception", e.toString() + e.getLocalizedMessage() + e.getMessage());

        System.out.println("出现了异常");
        //MailUtil.sendException(e);
        return Response.SERVER_ERROR();
    }

    public Response rest_error(Exception e) {
        e.printStackTrace();
        logger.error("ExceptionError", e);
        logger.error("Exception", e.toString() + e.getLocalizedMessage() + e.getMessage());

        //发送邮件
        //MailUtil.sendException(e);

        return Response.SERVER_ERROR();
    }

    public Response setTotal(Integer total) {
        this.put(TOTAL, total);
        return this;
    }

    public Response setTotalPage(Integer totalPage) {
        this.put(TOTAL_PAGE, totalPage);
        return this;
    }
    
    public static Response set(Integer total, Object rows) {
        Response response = new Response();
        response.setTotal(total);
        response.ok(rows);

        return response;
    }
    
    /**
     * 未登陆
     *
     * @return
     */
    public static Response NOT_LOGIN() {
        Response response = Response.code(Codes.NOT_LOGIN);
        response.put(MESSAGE, "用户未登录");
        return response;
    }
    
    /**
     * @return 账号密码不正确
     */
    public static Response ACCOUNT_PASS_ERROR() {
        Response response = Response.code(Codes.ACCOUNT_PASS_ERROR);
        response.put(MESSAGE, "账号密码不正确");
        return response;
    }
    
    /**
     * 参数错误
     *
     * @return
     */
    public static Response PARAM_ERROR() {
        Response response = Response.code(Codes.PARAM_ERROR);
        response.put(MESSAGE, "参数错误");
        return response;
    }

    public static Response set(Integer total, Object rows, Integer pageSize) {
        Response response = new Response();
        response.setTotal(total);
        response.ok(rows);
        
        // 分页数
 		int totalPage = 0;
 		
 		if (total > 0) {
 			if (total % pageSize == 0) {
 				totalPage = total / pageSize;
 			} else {
 				totalPage = total / pageSize + 1;
 			}
 		}
 		
 		response.setTotalPage(totalPage);

        return response;
    }
    
    //============状态===================================================================================

    /**
     * 成功
     *
     * @return
     */
    public static Response OK() {
        Response response = Response.code(Codes.SUCCESS);
        response.put(MESSAGE, "ok");
        return response;
    }

    /**
     * 服务器错误
     *
     * @return
     */
    public static Response SERVER_ERROR() {
        Response response = Response.code(Codes.SERVER_ERROR);
        response.put(MESSAGE, "服务器错误");
        return response;
    }

    /**
     * 共通错误设置
     * @param code 错误code
     * @param msg 错误信息
     * @return
     */
    public static Response ERROR(int code,String msg){
    	Response response = Response.code(code);
        response.put(MESSAGE, msg);
        return response;
    }
    
    /**
     * 验证码错误
     *
     * @return
     */
    public static Response CODE_ERROR() {
        Response response = Response.code(Codes.CODE_ERROR);
        response.put(MESSAGE, "验证码错误");
        return response;
    }
}