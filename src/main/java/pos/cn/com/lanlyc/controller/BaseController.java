package pos.cn.com.lanlyc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.ConstantUtils;
import cn.com.lanlyc.core.util.Response;
import pos.cn.com.lanlyc.core.service.PosServiceContainer;

public class BaseController {
	/**
	 * 
	 */
	    
	    protected final static String SessionContainer_Key = ConstantUtils.getConstant("Session_User_key");
	    
	    @Autowired
	    private PosServiceContainer service;
	    

	    
	    /**
	     * 获取 业务类容器
	     * @return
	     */
	    protected PosServiceContainer getService() {
	        return service;
	    }
	    
//	    /**
//	     * 
//	     * 获取当前请求用户id
//	     * 
//	     */
//	    protected String getCurrentuserid() 
//	    {
//	    	HttpServletRequest request=this.getRequest();
//	    	String token=(String) request.getAttribute("token");
//	    	String user_id=this.getService().getAdminUserService().getAdminuserdao().getUserInfoByToken(token);
//			return user_id;
//	    	
//	    }
//	    
	    /**
	     * 获取HttpServletRequest对象
	     * @return
	     * @author jerry_zhou
	     */
	    protected HttpServletRequest getRequest() {
	        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    }
	    
	    /**
	     * 获取HttpSession对象
	     * @return
	     * @author jerry_zhou
	     */
	    public HttpSession getSession() {
	        return getRequest().getSession();
	    }
	    
	    /**
	     * 跳转到url
	     * @param url
	     * @return 跳转值
	     * @author jerry_zhou
	     */
	    protected String forward(String url) {
	        return "forward:" + url;
	    }
	    
	    /**
	     * 重定向到url
	     * @param url
	     * @return
	     * @author jerry_zhou
	     */
	    protected String redirect(String url) {
	        return "redirect:" + url;
	    }

	


}
