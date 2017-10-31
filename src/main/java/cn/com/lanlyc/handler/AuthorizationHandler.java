package cn.com.lanlyc.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.core.util.Response;

/**
 * 拦截器
 * @author Jerry Zhou
 */
@Service
public class AuthorizationHandler extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer url = request.getRequestURL();
       
        if (!url.toString().contains("/login") && !url.toString().contains("/html/")&& !url.toString().contains("/gate_html/")
        		&& !url.toString().contains("/pos_html/")&& !url.toString().contains("/video_html/")&& !url.toString().contains("/pos_html1/")&& !url.toString().contains("/public/")
        		&& !url.toString().contains("/video_html2/")&& !url.toString().contains("/gate_html1/")) {
        	
        	String token=request.getParameter("token");
        	if(StringUtils.isEmpty(token)) 
        	{
        		String paramJson=request.getParameter("paramJson");
        		if(StringUtils.isNotEmpty(paramJson)) 
        		{
        			JSONObject object=JSONObject.parseObject(paramJson);
        			 token=object.getString("token");
                	
        		} 
        		
        	}
        	if(StringUtils.isEmpty(token)) 
        	{
        		Response res=Response.NOT_LOGIN();
        		
        		 response.setContentType("application/json; charset=utf-8");  
        		    PrintWriter out = null;  
        		    try {  
        		        out = response.getWriter();  
        		        String responseJSONObject=JSONObject.toJSONString(res);
						out.append(responseJSONObject.toString());  
        		      
        		    } catch (IOException e) {  
        		        e.printStackTrace();  
        		    } finally {  
        		        if (out != null) {  
        		            out.close();  
        		        }  
        		    }  
    			return false;
        	}else 
        	{
        		request.setAttribute("token", token);
        		return super.preHandle(request, response, handler);
        	}
        	

//        	System.out.println(has_token);
//            System.out.println("----------------进入拦截器-----------------");
//            System.out.println("-request.getRequestURL()-" + request.getRequestURL());
//            System.out.println(request.getSession().getAttribute(ConstantUtils.getConstant("Session_FinancialAdvisor_key")));
//            if (null == request.getSession().getAttribute(ConstantUtils.getConstant("Session_FinancialAdvisor_key"))) {
//                System.out.println("---无---");
//                request.getRequestDispatcher("/htmls/login.html").forward(request, response);
//                return false;
//            }
        }
        // TODO Auto-generated method stub
       
        return super.preHandle(request, response, handler);
    }

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
//		System.out.println("----------------进入拦截器afterCompletion-----------------");
		// TODO Auto-generated method stub

		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("----------------进入拦截器postHandle-----------------");
		super.postHandle(request, response, handler, modelAndView);
	}
}
