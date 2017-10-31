package gate.cn.com.lanlyc.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.dao.GateUserDao;
import gate.cn.com.lanlyc.core.dao.GateVisitDao;
import gate.cn.com.lanlyc.core.dto.GateExtraUserInfoDto;
import gate.cn.com.lanlyc.core.po.GateExtraUserInfo;
import gate.cn.com.lanlyc.core.po.GateUser;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.dto.PosCameraDto;

@Service
@Transactional
public class GateVisitService {
	@Autowired
	private GateUserDao gateUserDao;
	@Autowired
	private GateVisitDao gateVisitDao;
	
	@Autowired
	private GateUserService gateUserService;
	
	public GateUserDao getGateUserDao() {
		return gateUserDao;
	}

	public GateVisitDao getGateVisitDao() {
		return gateVisitDao;
	}
	
	/*
	 * 分页查询所有访客列表
	 */
	public Page<GateExtraUserInfoDto> getGateVisitListByPage(Page<GateExtraUserInfoDto> page,String name,String mobile,String pap_number){
        return gateVisitDao.getGateVisitListByPage(page,name,mobile,pap_number);
	}

	/**
	 * 增加一个访客
	 * @param json 
	 * @return
	 * @throws ParseException 
	 */
	public int addVisit(JSONObject object) throws ParseException {
        System.out.println(object);		
		String name = object.getString("name");
		String mobile = object.getString("mobile");
		String car_type = object.getString("car_type");
		String car_number = object.getString("car_number");
		String avatar_img = object.getString("avatar_img");
		String pap_type = object.getString("pap_type");
		String pap_number = object.getString("pap_number");
		String visit_reason = object.getString("visit_reason");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		GateExtraUserInfo visit=new GateExtraUserInfo();
		if(object.containsKey("registration_date") && object.getString("registration_date") !=null &&
				!"".equals(object.getString("registration_date"))){
			String registration_dates = object.getString("registration_date");			
			Date registration_date=sdf.parse(registration_dates);
			visit.setRegistration_date(registration_date);
		}
		sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(object.containsKey("entry_date") && object.getString("entry_date") !=null &&
				!"".equals(object.getString("entry_date"))){			
			String entry_dates = object.getString("entry_date");
			entry_dates=entry_dates.replaceAll ("T", " ");
			Date entry_date=sdf.parse(entry_dates);
			visit.setEntry_date(entry_date);
		}
		if(object.containsKey("leave_date") && object.getString("leave_date") !=null &&
				!"".equals(object.getString("leave_date"))){
			String leave_dates = object.getString("leave_date");
			leave_dates=leave_dates.replaceAll ("T", " ");
			Date leave_date=sdf.parse(leave_dates);
			visit.setLeave_date(leave_date);
		}

		visit.setCar_number(car_number);
		visit.setCar_type(car_type);
		visit.setPap_number(pap_number);
		visit.setPap_type(pap_type);
		visit.setVisit_reason(visit_reason);
		String id = DataUtils.getUUID();
		String user_id = DataUtils.getUUID();
		visit.setId(id);
		visit.setUser_id(user_id);
		int num1=gateVisitDao.save(visit);
		
		String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000);
		//int gonghao=(int)(11111+Math.random()*(9999-1111+1)); 工号可以自增
		GateUser gateUser=new GateUser();
		gateUser.setId(user_id);
		gateUser.setMobile(mobile);
		gateUser.setName(name);
		gateUser.setAvatar_img(avatar_img);
		gateUser.setStaff_visitor("0");
		//gateUser.setGonghao(gonghao);
		System.out.println("pap_type      :"+pap_type+"       pap_number   "+pap_number);
		if(!"1".equals(pap_type)){
		    //当证件类型不是身份证时，要给gate_t_user表添加一个默认的身份证，因为该字段是必须的
			System.out.println("1   :"+pap_type!="1");
			System.out.println("2   :"+"1".equals(pap_type));
            String front=(int)(11+Math.random()*(65-11+1))+"";
            String mid_end=trandNo.toString().substring(0, 4);
            String mid="19830610";
            String pap_number1=front+mid_end+mid+mid_end;
            System.out.println("纯数字        "+pap_number1);
            gateUser.setCid(pap_number1);
		}else{
			//当证件类型是身份证时，直接将身份证添加到gate_t_user表中
			gateUser.setCid(pap_number);
		}
		
		int num2=gateUserDao.save(gateUser);
		if(num1<=0||num2<=0){
			return 0;
		}
		return 1;
	}
	
	/**
	 * 根据访客id，查询访客信息
	 * @param id 访客的id
	 * @return
	 */
	public GateExtraUserInfoDto selectVisitorById(String id){
		if(DataUtils.isNullOrEmpty(id)) {
			return null;
		}else {
			try {
				return gateVisitDao.selectVisitorById(id);
			} catch (Exception e) {
				return null;
			}
			
		}
	}
	
	/**
	 * 查找身份证
	 * @param cid 身份证号码
	 * @return
	 */
	public Boolean selectPapNumber(String pap_number){
		if(DataUtils.isNullOrEmpty(pap_number)) {
			return false;
		}else {
			try {
				return gateVisitDao.selectPapNumber(pap_number);
			} catch (Exception e) {
				return false;
			}
			
		}
	}
	
	/**
	 * 删除访客(一个或多个)
	 * @param json 
	 * @return
	 * @throws ParseException 
	 */
	public Boolean del_visit_user(String id){
		String ids = id;
		String[] ids1 = ids.split(",");
		for(String visitId : ids1){
			int i=gateUserService.getGateuser().delete(visitId);
			int j=gateVisitDao.deleteVisit(visitId);
			if(i!=1||j!=1){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 修改访客
	 * @param json 
	 * @return
	 * @throws ParseException 
	 */
	public Boolean update_visit_user(JSONObject object) throws ParseException{
		System.out.println("进来了  开始修改");
		String id = object.getString("id");
		String name=object.getString("name");
		String mobile=object.getString("mobile");
		String avatar_img=object.getString("avatar_img");
		String pap_type=object.getString("pap_type");
		String pap_number=object.getString("pap_number");
		String visit_reason=object.getString("visit_reason");
		GateUser gate_user_old = gateUserService.getGateuser().get(id);
		GateUser GU = new GateUser();
		GU.setId(id);
		GU.setName(name);
		GU.setMobile(mobile);
		GU.setGonghao(gate_user_old.getGonghao());
		GU.setAvatar_img(avatar_img);
		if("1".equals(pap_type)){
			GU.setCid(pap_number);
		}
		int userUpdate = gateUserService.getGateuser().update(GU, false);
		
		GateExtraUserInfo visit=new GateExtraUserInfo();
		visit.setPap_type(pap_type);
		visit.setPap_number(pap_number);
		visit.setUser_id(id);
		visit.setVisit_reason(visit_reason);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(object.containsKey("entry_date") && object.getString("entry_date") !=null &&
				!"".equals(object.getString("entry_date"))){			
			String entry_dates = object.getString("entry_date");
			entry_dates=entry_dates.replaceAll ("T", " ");
			Date entry_date=sdf.parse(entry_dates);
			visit.setEntry_date(entry_date);
		}
		if(object.containsKey("leave_date") && object.getString("leave_date") !=null &&
				!"".equals(object.getString("leave_date"))){
			String leave_dates = object.getString("leave_date");
			leave_dates=leave_dates.replaceAll ("T", " ");
			Date leave_date=sdf.parse(leave_dates);
			visit.setLeave_date(leave_date);
		}
		
		if(object.containsKey("car_type") && object.getString("car_type") !=null &&
				!"".equals(object.getString("car_type"))){			
			String car_type = object.getString("car_type");
			visit.setCar_type(car_type);;
		}
		
		if(object.containsKey("car_number") && object.getString("car_number") !=null &&
				!"".equals(object.getString("car_number"))){			
			String car_number = object.getString("car_number");
			visit.setCar_number(car_number);
		}
		GateExtraUserInfo visit2=gateVisitDao.selectVisitorByUserId(id);
		visit.setId(visit2.getId());
		visit.setRegistration_date(visit2.getRegistration_date());
		//int visitUpdate=gateVisitDao.update(visit, false);
		int visitUpdate=gateVisitDao.updateVisit(visit);
		if(visitUpdate!=0 && userUpdate!=0){
			return true;
		}
		return false;
	}
	
}
