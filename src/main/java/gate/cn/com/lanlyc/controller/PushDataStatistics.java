package gate.cn.com.lanlyc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import cn.com.lanlyc.base.util.ConstantUtils;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.dao.GateUserDao;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;
import gate.cn.com.lanlyc.core.service.ServiceContainer;

import com.alibaba.fastjson.JSONObject;

@Component("PushDataStatistics")
@ServerEndpoint(value = "/PushDataStatistics")
public class PushDataStatistics {
	protected final static String SessionContainer_Key = ConstantUtils.getConstant("Session_User_key");
	@Resource
	private ServiceContainer service;
	public static PushDataStatistics testUtils;
	private Session session;
	// 每个Session 对应所属区域 area=>id
	private static final Map<String, Object> area_session = new HashMap<String, Object>();

	private static final Map<String, String> area_sessionid = new HashMap<String, String>();

	protected ServiceContainer getService() {
		return testUtils.service;
	}

	@PostConstruct
	public void init() {
		testUtils = this;
	}

	private static int count = 0;
	// 如果有客户端来连接则存在此数组中

	@OnOpen
	public void open(Session session) {
		this.session = session;

		System.out.println("有客户端进行连接");
	}

	// renyuan area
	public Boolean getlast_enter_person(ShowProjectPeopleCout check_datas, String area, String user_id) {

		//GateUserInfoView gUser = this.getService().getGateuserservice().getGateuser().getUserByuserId(user_id);
		//check_datas.setLast_enter_person(JSONObject.toJSONString(gUser));
		Response response = Response.OK();
		response.setData(check_datas);
		String message = JSONObject.toJSONString(response);

		// 然后给对应的session 发送有人进入的json 格式的message
		this.sendUser(message, area);
		return true;
	}

	/**
	 * 向指定用户发送消息
	 * 
	 * @param msg
	 */
	public static void sendUser(String msg, String area) {
		PushDataStatistics c = null;
		try {
			c = (PushDataStatistics) area_session.get(area);
			c.session.getBasicRemote().sendText(msg);
//			area_session.remove(c);
//			c.session.close();
		} catch (Exception e) {
			try {
				area_session.remove(c);
			} catch (Exception e2) {
			}

			try {
				c.session.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
			}

		}
	}

	@OnMessage
	public void message(String message, Session session) {
		// 如果有客户端发送了连接信息过来则保持session
		if (message.contains("open")) {
			// 切割消息 获取 area
			String area = message.substring(5);
			System.out.println("客户端初始化连接要求获取 区域:" + area);
			area_session.put(area, this);
			area_sessionid.put(area, session.getId());

			ShowProjectPeopleCout res = this.getService().getShowProjectPeopleCoutService()
					.getShowProjectPeopleCoutDao().getCoutByArea(area);
			if(res!=null) 
			{
				if(!StringUtils.isEmpty(res.getLast_enter_person())) 
				{
					//String user_id = res.getLast_enter_person();
					//GateUserInfoView gUser = this.getService().getGateuserservice().getGateuser().getUserByuserId(user_id);
					//res.setLast_enter_person(JSONObject.toJSONString(gUser));
				}
			}
			
			
			
			
			Response response = Response.OK();
			response.setData(res);
			String remessage = JSONObject.toJSONString(response);

			try {
				session.getBasicRemote().sendText(remessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (message.contains("switcharea_")) {
			// 通过 session 找到对应的 area
			String area = message.substring(11);
			System.out.println("客户端切换的分区 区域:" + area);
			String old_area = "";
			String sessionid = session.getId();
			for (Map.Entry entry : area_sessionid.entrySet()) {
				if (sessionid.equals(entry.getValue()))
					old_area = entry.getKey().toString();
			}
			if (!StringUtils.isEmpty(old_area)) {
				area_sessionid.remove(old_area);
				area_session.remove(old_area);
			}

			area_session.put(area, this);
			area_sessionid.put(area, session.getId());
			ShowProjectPeopleCout res = this.getService().getShowProjectPeopleCoutService()
					.getShowProjectPeopleCoutDao().getCoutByArea(area);
			if(res!=null) 
			{
				if(!StringUtils.isEmpty(res.getLast_enter_person())) 
				{
					//String user_id = res.getLast_enter_person();
					//GateUserInfoView gUser = this.getService().getGateuserservice().getGateuser().getUserByuserId(user_id);
					//res.setLast_enter_person(JSONObject.toJSONString(gUser));
				}
			}
			
			
		
			
			Response response = Response.OK();
			response.setData(res);
			String remessage = JSONObject.toJSONString(response);

			try {
				session.getBasicRemote().sendText(remessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//
		System.out.println("客户端发送数据 -> " + message);

		
	}

	// @Message
	public void onsend(Session session, String msg) {
		try {
			session.getBasicRemote().sendText("client" + session.getId() + "say:" + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void close() {
		System.out.println("有客户端关闭了连接");
	}
}