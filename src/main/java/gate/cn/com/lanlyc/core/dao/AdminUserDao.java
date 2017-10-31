package gate.cn.com.lanlyc.core.dao;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.EncryptionUtils;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.base.util.QMap;
import gate.cn.com.lanlyc.core.dto.AdminUserDto;
import gate.cn.com.lanlyc.core.po.AdminUser;

@Service
public class AdminUserDao extends MySQLMapper<AdminUser> {
	/**
     * 根据用户名查询用户信息
     * 
     * @param user_name 检索关键字（场馆编号/场馆符合编号/场馆名字）
     * @return 场馆列表
     */
    public AdminUser getUserInfoByname(String user_name)
    {
    	AdminUser u=new AdminUser();
    	Map paramMap = new HashMap();
    	paramMap.put("user_name", user_name);
    	String sql = new String("SELECT * from gate_t_admin_user where user_name=:user_name");
    	AdminUserDto u1=get(sql, paramMap,AdminUserDto.class);
    	
    	return u;
    }
    
    /**
     * 根据用户名，密码查询管理员是否可以登录
     * 
     * @param user_name 检索关键字（管理员名字）
     * @param pass_word 检索关键字（管理员密码）
     * @return 场馆列表
     */
    public String getUserInfoBynameandpass(String user_name,String pass_word)
    {
    	AdminUser u=new AdminUser();
    	Map paramMap = new HashMap();
    	paramMap.put("user_name", user_name);
    	paramMap.put("pass_word", EncryptionUtils.encryptV5(pass_word));
         
    	String sql = new String("SELECT * from gate_t_admin_user where user_name=:user_name and pass_word=:pass_word");
    	AdminUserDto u1=get(sql, paramMap,AdminUserDto.class);
    	
    	if(u1!=null) {
    		return u1.getId();
    	}else {
    		return null;
    	}
    }
    
    /**
     * 根据用户名，密码查询管理员 获取管理员全部信息
     * 
     * 
     * 
     */
    public AdminUserDto getUserBynameandpass(String user_name,String pass_word)
    {
    	AdminUser u=new AdminUser();
    	Map paramMap = new HashMap();
    	paramMap.put("user_name", user_name);
    	paramMap.put("pass_word", EncryptionUtils.encryptV5(pass_word));
         
    	String sql = new String("SELECT * from gate_t_admin_user where user_name=:user_name and pass_word=:pass_word");
    	AdminUserDto u1=get(sql, paramMap,AdminUserDto.class);
    	
    	if(u1!=null) {
    		return u1;
    	}else {
    		return null;
    	}
    }
    /**
     * 根据token 值判断用户是否已经登陆
     * 
     * 
     * @param token
     * @return
     */
    public String getUserInfoByToken(String token) 
    {
    	AdminUser u=new AdminUser();
    	Map paramMap = new HashMap();
    	paramMap.put("token", token);
    	
         
    	String sql = new String("SELECT * from gate_t_admin_user where token=:token");
    	AdminUserDto u1=get(sql, paramMap,AdminUserDto.class);
    	
    	if(u1!=null) {
    		return u1.getId();
    	}else {
    		return null;
    	}
    }
    
    public Page<AdminUser> getAdminList(Page<AdminUser> page,Map<String, Object> paramMap,String orderColumn,String orderDir)
    {
    	StringBuffer sql=new StringBuffer("SELECT id,user_name,last_login_time,mobile,image ,FROM_UNIXTIME(create_time,'%Y-%m-%d %H:%i:%S') as create_time from gate_t_admin_user");
    	if(paramMap.containsKey("user_name")) 
    	{
    		String username=(String) paramMap.get("user_name");
    		if(StringUtils.isNotEmpty(username)) 
    		{
    			sql.append(" where  user_name LIKE '%").append(username)
    				.append("%' ");
    		}
    		paramMap.remove("user_name");
    	}else 
    		{
    			sql.append(" where  1=1");
    		}
    	
    	if(paramMap.containsKey("mobile")) 
    	{
    		String mobile=(String) paramMap.get("mobile");
    		if(StringUtils.isNotEmpty(mobile)) 
    		{
    			sql.append(" and mobile =").append(mobile)
    				;
    		}
    		paramMap.remove("mobile");
    	}

    	if(orderColumn!=null&&orderColumn!=""&& orderDir!=null&&orderDir!="") {
			sql.append(" order by ").append(orderColumn).append(" ").append(orderDir);
		}
    	Page<AdminUser> result=getPage(sql, null, page,AdminUser.class);
		return result;
    	
    }
    
    
    
    public List<AdminUser> getAdminListExcel(Map<String, Object> paramMap,String orderColumn,String orderDir)
    {
    	StringBuffer sql=new StringBuffer("SELECT id,user_name,last_login_time,mobile,image ,FROM_UNIXTIME(create_time,'%Y-%m-%d %H:%i:%S') as create_time from gate_t_admin_user");
    	if(paramMap.containsKey("user_name")) 
    	{
    		String username=(String) paramMap.get("user_name");
    		if(StringUtils.isNotEmpty(username)) 
    		{
    			sql.append(" where  user_name LIKE '%").append(username)
    				.append("%' ");
    		}
    		paramMap.remove("user_name");
    	}else 
    		{
    			sql.append(" where  1=1");
    		}
    	
    	if(paramMap.containsKey("mobile")) 
    	{
    		String mobile=(String) paramMap.get("mobile");
    		if(StringUtils.isNotEmpty(mobile)) 
    		{
    			sql.append(" and mobile =").append(mobile)
    				;
    		}
    		paramMap.remove("mobile");
    	}

    	if(orderColumn!=null&&orderColumn!=""&& orderDir!=null&&orderDir!="") {
			sql.append(" order by ").append(orderColumn).append(" ").append(orderDir);
		}else {
			sql.append(" order by ").append("create_time").append(" ").append("DESC");
		}
    	List<AdminUser> result=findList(sql, null,AdminUser.class); 
		return result;
    	
    }
    
    /**
     * 根据传入的手机号或者用户名判断是否已存在
     * 
     * 
     * @param 
     * @return
     */
    public AdminUser checkUsernameAndMobile(String id,String user_name,String mobile) {
    	StringBuffer sql=new StringBuffer("SELECT * from gate_t_admin_user ");
    	if(StringUtils.isNotEmpty(user_name)) {
    		sql.append(" where user_name='").append(user_name).append("'");
    		if(StringUtils.isNotEmpty(mobile)) {
    			sql.append(" or mobile='").append(mobile).append("'");
    		}
    	}
    	else {
			sql.append(" where mobile='").append(mobile).append("'");
		}
    	if(StringUtils.isNotEmpty(id)) {
    		sql.append(" and id!='").append(id).append("'");
    	}
    	Map paramMap = new HashMap();
    	paramMap.put("id", id);
    	paramMap.put("user_name", user_name);
    	paramMap.put("mobile", mobile);
    	System.out.println(sql);
    	AdminUser num=get(sql, paramMap, AdminUser.class);
    	return num;
    }
    
    
    
    
    
}
