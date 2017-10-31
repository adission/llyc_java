package gate.cn.com.lanlyc.core.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.EncryptionUtils;
import gate.cn.com.lanlyc.core.dao.AdminUserDao;
import gate.cn.com.lanlyc.core.dto.AdminUserDto;
import gate.cn.com.lanlyc.core.po.AdminUser;

@Service
@Transactional
public class AdminUserService {
	@Autowired
	private AdminUserDao adminuserdao;

	 
	 public AdminUser getAdminUserInfo(String user_name) 
	 {
		 return adminuserdao.getUserInfoByname(user_name);
	 }

	public AdminUserDao getAdminuserdao() {
		return adminuserdao;
	}

	public void setAdminuserdao(AdminUserDao adminuserdao) {
		this.adminuserdao = adminuserdao;
	}
	
	public Map testadmin(String user_name,String pass_word) {
		Map paramMap = new HashMap();
		AdminUserDto ref = adminuserdao.getUserBynameandpass(user_name,pass_word);
		
		if (ref!=null) {
			String token=maketoken(ref.getId());
			AdminUser dt=new AdminUser();
			
			dt.setId(ref.getId());
			dt.setToken(token);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dt.setLast_login_time(df.format(new Date()));
			this.adminuserdao.update(dt,false);
			paramMap.put("id", ref.getId());
			paramMap.put("user_name", ref.getUser_name());
			paramMap.put("mobile", ref.getMobile());
			paramMap.put("image", ref.getImage());
			paramMap.put("token", token);
		}else {
			paramMap.put("no_admin", "no_admin");
		}
		return paramMap;
	}
	
	public String maketoken(String id) 
	{
		
		String code=id+DateUtils.getCurrenTimeStamp();
		String token=EncryptionUtils.encryptV5(code);
		return token;
	}
	 
}
