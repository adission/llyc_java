package gate.cn.com.lanlyc.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.HttpUtils;
import gate.cn.com.lanlyc.core.po.CheckLog;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.po.Setting;
import gate.cn.com.lanlyc.core.po.ShowProjectPeopleCout;
import gate.cn.com.lanlyc.core.po.WorkersTypes;
import gate.cn.com.lanlyc.core.service.ServiceContainer;
import gate.cn.com.lanlyc.core.service.SettingService;
import gate.cn.com.lanlyc.core.service.WorkersTypesService;

@Component("CloudRequest")
//@Controller
//@Lazy(false)
public class CloudRequestController extends BaseController implements Runnable{
	private static String TOKEN = "";
	private static String region_id = "";
	private static String scrt_key = "";
	private static String request_url = "";
	private static int last_getpson = 0;
	private static int last_getpson_reqrtate = 0;
	private static int last_getworktype = 0;
	private static int last_getworktype_reqrtate = 0;
	private static int last_getupdateuser = 0;
	private static int last_getupdateuser_reqrtate = 0;

	private static int last_getcancelcard = 0;
	private static int last_getcancelcard_reqrtate = 0;
	private static int last_getpause = 0;
	private static int last_getpause_reqrtate = 0;
	@Autowired
    private ServiceContainer service;
	private static CloudRequestController self_CloudRequest;
	/**
	 * 获取 业务类容器
	 * 
	 * @return
	 */
	protected ServiceContainer getService() {
		return self_CloudRequest.service;
	}

	@PostConstruct
	public void init() {
		self_CloudRequest = this;
	}
		

	SettingService settingservice = null;

	Setting system_setting = null;
	//最多三次请求重发策略
	private int request_time=0;

//	@Scheduled(cron = "0/30 * * * * ?")
	public void run(){
		service=this.getService();
		if (settingservice == null) {
			settingservice = service.getSettingservice();
			system_setting = settingservice.getSettingdao().getOneObject();

		}
		if (system_setting != null) {

			TOKEN = system_setting.getToken();
			region_id = system_setting.getRegion_id();
			scrt_key = system_setting.getScrt_key();
			request_url = system_setting.getRequest_url();

		}
		while(true) 
		{
			System.out.println("定时云端交互");
			// 如果提示未登陆则执行登陆后操作
			if (TOKEN == "") {
				TOKEN = this.login();

			} else {
				// 如果当前请求的时间减去上一次的时间 大于云端控制的时间范围内 则向云端请求
				int error = 0;
				int current_time = this.getCurrenTimeStamp();
				if (current_time - last_getworktype > last_getworktype_reqrtate) {
					error = this.getAllWorkers_types();
				}
				current_time = this.getCurrenTimeStamp();
				if (current_time - last_getpson > last_getpson_reqrtate) {
					error = this.personlist();
				}
				current_time = this.getCurrenTimeStamp();
				if (current_time - last_getupdateuser > last_getupdateuser_reqrtate) {
					this.GetupdateUser();	
				}
				current_time = this.getCurrenTimeStamp();
				if (current_time - last_getcancelcard > last_getcancelcard_reqrtate) {
					this.Cancelcard();
				}
				current_time = this.getCurrenTimeStamp();
				if (current_time - last_getpause > last_getpause_reqrtate) {
					this.Pause();
				}
				
				// 上报大屏数据
				this.UploadShowdatas();
			}
		}
		
	}

	/**
	 * 登陆接口login
	 * 
	 */
	public String login() {
		System.out.println("登录");
		String module = "login";
		String url = request_url + "/gate/" + module;
		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&scrt_key=" + scrt_key + "&seq_no=" + seq_no;
		
		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);
		TOKEN = json.getString("TOKEN");
		int error = json.getInteger("error");
		if (error == 0) 
		{
			request_time=0;
			ChangeLastLogintime();
		}else 
		{
			request_time=request_time+1;
			if(request_time<3) 
			{
				this.login();
			}
		}
		

		return TOKEN;

	}

	/**
	 * 下载人员
	 * 
	 */
	public int personlist() {
		System.out.println("下载人员");
		String module = "personlist";
		String url = request_url + "/gate/" + module;

		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&scrt_key=" + scrt_key + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN;
		
		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);

		int error = json.getInteger("error");
		if (error == 0) {
			TOKEN = json.getString("TOKEN");
			// last_getpson_reqrtate=json.getIntValue("rate");
			last_getpson_reqrtate = 60;
			last_getpson = this.getCurrenTimeStamp();

			ChangeToken();
			String datas_list = json.getString("person_list");
			// 循环所有工种
			JSONArray datas = json.parseArray(datas_list);
			for (int i = 0; i < datas.size(); i++) {
				JSONObject tmp_json = (JSONObject) datas.get(i);

				String name = tmp_json.getString("name");
				GateUser gu = new GateUser();
				gu.setId(tmp_json.getString("id"));
				GateUser gu2 = service.getGateuserservice().getGateuser().get(gu);
				if (gu2 == null) {
					gu.setName(tmp_json.getString("name"));
					gu.setGender(tmp_json.getString("gender"));
					gu.setTeam(tmp_json.getString("team"));
					gu.setWorkers_type(tmp_json.getString("workers_type"));
					gu.setSate(tmp_json.getString("sate"));
					gu.setMobile(tmp_json.getString("mobile"));
					gu.setAvatar_img(tmp_json.getString("avatar_img"));
					gu.setCid(tmp_json.getString("CID"));
					gu.setPunish_record(tmp_json.getString("punish_record"));
					service.getGateuserservice().getGateuser().save(gu);
				}

			}

		} else {
			this.login();
			int error2 = this.personlist();
		}

		return error;
	}

	/**
	 * 获取所有工种
	 * 
	 */
	public int getAllWorkers_types() {
		System.out.println("获取所有工种");
		String module = "getworktype";
		String url = request_url + "/gate/" + module;
		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&scrt_key=" + scrt_key + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN;

		
		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);

		int error = json.getInteger("error");
		// System.out.println("获取工种时返回数据"+json);
		WorkersTypesService ws = service.getWorkerstypesservice();
		if (error == 0) {
			TOKEN = json.getString("TOKEN");
			// last_getworktype_reqrtate=json.getIntValue("req_rate");
			last_getworktype = this.getCurrenTimeStamp();
			last_getworktype_reqrtate = json.getIntValue("req_rate");

			ChangeToken();
			String datas_list = json.getString("datas_list");
			// 循环所有工种
			JSONArray datas = json.parseArray(datas_list);
			for (int i = 0; i < datas.size(); i++) {
				JSONObject tmp_json = (JSONObject) datas.get(i);

				String name = tmp_json.getString("type_name");
				String value = tmp_json.getString("value");
				WorkersTypes wtes1 = new WorkersTypes();
				// 根据工种value查询数据库中工种是否存在
				try {
					wtes1 = ws.getWorkersTypes().getWorkersTypesinfoByvalue(value);
				} catch (Exception e) {
					wtes1 = new WorkersTypes();
					// TODO: handle exception
				}
				WorkersTypes wtes0 = new WorkersTypes();
				// 根据工种名称查询数据库中工种是否存在
				try {
					wtes0 = ws.getWorkersTypes().getWorkersTypesinfoByname(name);
				} catch (Exception e) {
					wtes0 = new WorkersTypes();
					// TODO: handle exception
				}

				if (wtes1 == null && wtes0 == null) {
					WorkersTypes wtes2 = new WorkersTypes();
					// 如果不存在则添加到系统中
					wtes2.setName(name);
					String id = DataUtils.getUUID();
					// String value=tmp_json.getString("value");
					wtes2.setId(id);
					wtes2.setOrder_by("1000");
					wtes2.setValue(value);

					ws.getWorkersTypes().save(wtes2);
				}

			}

		} else {
			System.out.println("需要登录");
			this.login();
			int error2 = this.getAllWorkers_types();

		}
		return error;
	}

	/**
	 * 获取人员属性变动
	 * 
	 */
	public int GetupdateUser() {

		String module = "get_update_user";
		String url = request_url + "/gate/" + module;

		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&scrt_key=" + scrt_key + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN;
		
		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);

		int error = json.getInteger("error");
		if (error == 0) {
			// 通过身份证号码找到人员信息
			String person_list = json.getString("person_list");
			// 循环所有人员
			JSONArray datas = json.parseArray(person_list);
			for (int i = 0; i < datas.size(); i++) {
				JSONObject tmp_json = (JSONObject) datas.get(i);

				String cid = tmp_json.getString("id_card");
				GateUser gu=service.getGateuserservice().getGateuser().getGateuserbycid(cid);
				if(gu!=null) 
				{
					
					gu.setName(tmp_json.getString("name"));
					gu.setGender(tmp_json.getString("gender"));
					gu.setTeam(tmp_json.getString("team"));
					gu.setWorkers_type(tmp_json.getString("workers_type"));
					
					gu.setMobile(tmp_json.getString("mobile"));
					gu.setAvatar_img(tmp_json.getString("avatar_img"));
					
					gu.setPunish_record(tmp_json.getString("punish_record"));
					service.getGateuserservice().getGateuser().update(gu,false);
				}
			}
			
			
			// 更新人员信息

			TOKEN = json.getString("TOKEN");
			last_getupdateuser = this.getCurrenTimeStamp();
			last_getupdateuser_reqrtate = 1000;

			ChangeToken();
		} else {
			this.login();
			int error2 = this.GetupdateUser();
		}

		return error;

	}





	/**
	 * 销卡
	 * 
	 */
	public int Cancelcard() {

		String module = "cancelcard";
		String url = request_url + "/gate/" + module;

		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN;

	
		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);

		int error = json.getInteger("error");
		if (error == 0) {
			TOKEN = json.getString("TOKEN");

			ChangeToken();
			// 通过身份证号码找到人员信息
			String person_list = json.getString("person_list");
			// 循环所有人员
			JSONArray datas = json.parseArray(person_list);
			List<String> user_ids=new ArrayList<String>();
			for (int i = 0; i < datas.size(); i++) {
				JSONObject tmp_json = (JSONObject) datas.get(i);

				String cid = tmp_json.getString("CID");
				
				GateUser gu=service.getGateuserservice().getGateuser().getGateuserbycid(cid);
				
				if(gu!=null) 
				{
					user_ids.add(gu.getId());
				}
				
			}
			//执行销卡操作
			String user_ids_str=StringUtils.join(user_ids.toArray(), ",");
			if(user_ids.size()>0) 
			{
				service.getUserCardsService().ClearUserCards(user_ids_str);

			}
		} else {
			this.login();
			error = this.Cancelcard();
		}
		System.out.print(error);
		return error;

	}

	/**
	 * 暂停卡/恢复卡
	 * 
	 */
	public int Pause() {
		System.out.println("暂停卡/恢复卡");
		String module = "pause";
		String url = this.request_url + "/gate/" + module;

		String seq_no = MakeSeq();
		String parm = "region_id=" + region_id + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN;
		
		String data = HttpUtils.sendPost(url, parm);
		JSONObject json = JSONObject.parseObject(data);

		int error = json.getInteger("error");
		if (error == 0) {
			this.TOKEN = json.getString("TOKEN");

			ChangeToken();
			// 通过身份证号码找到人员信息
			String person_list = json.getString("person_list");
			// 循环所有人员
			JSONArray datas = json.parseArray(person_list);
			
			List<String> kuifuuser_ids=new ArrayList<String>();
			List<String> pauseuser_ids=new ArrayList<String>();
			for (int i = 0; i < datas.size(); i++) {
				JSONObject tmp_json = (JSONObject) datas.get(i);

				String cid = tmp_json.getString("CID");
				String pause_type=tmp_json.getString("pause_type");
				GateUser gu=service.getGateuserservice().getGateuser().getGateuserbycid(cid);
				if(gu!=null) 
				{
					if(pause_type=="1") 
					{
						//恢复卡
						kuifuuser_ids.add(gu.getId());
					}
					if(pause_type=="2") 
					{
						//暂停卡
						pauseuser_ids.add(gu.getId());
					}
				}
			}
			//执行恢复卡操作
			String user_ids_str=StringUtils.join(pauseuser_ids.toArray(), ",");
			if(pauseuser_ids.size()>0) 
			{
				service.getGateuserauthservie().setUserAuthPause(user_ids_str);
			}
			 user_ids_str=StringUtils.join(kuifuuser_ids.toArray(), ",");
			 if(kuifuuser_ids.size()>0) 
			 {
				service.getGateuserauthservie().setUserAuthNormal(user_ids_str, this.getRequest());

			 }
//			service.getUserCardsService().ClearUserCards(user_ids_str);
			
		} else {
			this.login();
			 error= this.Pause();
		}
		System.out.print(error);
		return error;

	}

	/**
	 * 闸机大屏数据统计上报
	 * 
	 */
	public int UploadShowdatas() {
		System.out.println("大屏数据统计上报");
		String module = "upload_showdatas";
		String url = this.request_url + "/gate/" + module;
		// 获取系统所有分区的大屏数据
		List<ShowProjectPeopleCout> allshowdatas = service.getShowProjectPeopleCoutService()
				.getShowProjectPeopleCoutDao().getAllShow();
		for (ShowProjectPeopleCout i : allshowdatas) {
			// 将所有分区的大屏数据实时上报
			String seq_no = MakeSeq();
			String parm = "region_id=" + region_id + "&seq_no=" + seq_no + "&TOKEN=" + TOKEN;
			parm = parm + "&area=" + i.getArea() + "&enter_person=" + i.getEnter_person() + "&worker_type_person="
					+ i.getWorker_type_person();
			parm = parm + "&worker_person=" + i.getClass_type_person();

			
			String data = HttpUtils.sendPost(url, parm);
			JSONObject json = JSONObject.parseObject(data);

			int error = json.getInteger("error");
			if (error == 0) {
				TOKEN = json.getString("TOKEN");

				ChangeToken();
			} else {
				this.login();
				int error2 = this.personlist();
			}
			System.out.print(error);
		}

		int error = 0;
		return error;

	}

	/**
	 * 
	 * 
	 * 
	 * @api {get} taskRemind/ChangeToken
	 * @apiGroup APIManager
	 * @apiName TODO(这里用一句话描述这个类的作用)
	 * @apiDescription更新DeviceToken
	 * @apiParam
	 * @apiSuccess
	 * @apiVersion 1.0.0
	 * @apiErrorExample {json} Error-Response: { "code" :"", "message": "请登录" }
	 */
	private void ChangeToken() {
		if (system_setting != null) {
			// System.out.println("请求后改变token");
			system_setting.setToken(TOKEN);
			settingservice.getSettingdao().update(system_setting);
		}
	}

	/**
	 * 每次登陆成功后更新数据库的最后登录时间
	 * 
	 */
	private void ChangeLastLogintime() {
		if (system_setting != null) {
			// System.out.println("登陆后改变token");
			system_setting.setToken(TOKEN);
			String last_login_time = DateUtils.getCurrenTimeStamp();

			system_setting.setLast_login_time(last_login_time);
			settingservice.getSettingdao().update(system_setting);

		}
	}

	/**
	 * 生成带时间的请求编号
	 * 
	 * @return
	 */
	public String MakeSeq() {
		Date date = new Date();

		String time = DateUtils.dateToStr(date, 5);
		int radomInt = new Random().nextInt(999999);
		String seq = time + String.valueOf(radomInt);
		return seq;

	}

	public int getCurrenTimeStamp() {
		Date date = new Date();
		int timestamp = (int) (date.getTime() / 1000);
		return timestamp;

	}
}
