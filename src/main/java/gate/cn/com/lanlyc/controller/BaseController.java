/**
 * 
 */
package gate.cn.com.lanlyc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.ConstantUtils;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.service.ServiceContainer;
import video.cn.com.lanlyc.core.service.VideoServiceContainer;

/**
 */
public class BaseController {
    
    protected final static String SessionContainer_Key = ConstantUtils.getConstant("Session_User_key");
    
    @Autowired
    private ServiceContainer service;
    
    @Autowired
    private VideoServiceContainer videoService;
    

    
    /**
     * 获取 业务类容器
     * @return
     */
    protected ServiceContainer getService() {
        return service;
    }
    
    
    /**
     * 获取 业务类容器
     * @return
     */
    protected VideoServiceContainer getVideoService() {
        return videoService;
    }
    
    /**
     * 
     * 获取当前请求用户id
     * 
     */
    protected String getCurrentuserid() 
    {
    	HttpServletRequest request=this.getRequest();
    	String token=(String) request.getAttribute("token");
    	String user_id=this.getService().getAdminUserService().getAdminuserdao().getUserInfoByToken(token);
		return user_id;
    	
    }
    
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

	/**
	 * 获取当前request 请求的url 并截断 action 返回action
	 * 
	 * @return
	 * 
	 */
	public String getRequestAction() {
		HttpServletRequest request = this.getRequest();
		StringBuffer url = request.getRequestURL();
		String res = url.substring(url.indexOf("lanlyc") + 6);
		return res;

	}

	/**
	 * 添加管理员操作记录 方法
	 * 
	 * @return
	 * 
	 */
	public boolean addUserOperationlog(String operation_desc) {
		String operation_action = this.getRequestAction();// 操作行为
		// String operation_desc = "登陆";//操作描述
		String operation_user = this.getCurrentuserid();// 操作人id、
		boolean is_success = this.getService().getOperationlogService().addlog(operation_action, operation_desc,
				operation_user);

		return is_success;
	}

}
