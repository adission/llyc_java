package gate.cn.com.lanlyc.core.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import gate.cn.com.lanlyc.core.po.AdminUser;
import gate.cn.com.lanlyc.core.po.ConnectLog;
import gate.cn.com.lanlyc.core.po.GateList;
@Service
public class ConnectLogDao extends MySQLMapper<ConnectLog>{

	/**
	 * 添加一条考勤设备通讯日志
	 * 
	 * @param connectLog 考勤设备通讯日志实体
	 * @return 成功或失败
	 */
	public boolean addOneConnectLog(ConnectLog connectLog) 
	{
		if(1==this.save(connectLog))
			return true;
		return false;
		
	}
	
	/**
，	 * 
	 * @param connectLog 考勤设备通讯日志实体
	 * @return 成功或失败
	 */
	public boolean delOneConnectLog(ConnectLog connectLog) 
	{
		if(1==this.delete(connectLog))
			return true;
		return false;
		
	}
		
	/**
	 * 查询指定时间内的考勤设备通讯日志
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 符合条件的考勤设备通讯日志分页结果
	 */
	public Page<ConnectLog> getConnectLogList(Page<ConnectLog> page,Map<String, Object> paramMap){
		StringBuffer sql=new StringBuffer("SELECT * from gate_t_connect_log");	
		sql.append(" where  1=1");
		if(paramMap.containsKey("start_time")) 
    	{
    		String start_time=(String) paramMap.get("start_time");
    		if(StringUtils.isNotEmpty(start_time)) 
    		{
    			long start = this.getTimeStamp(start_time);
    			sql.append( " and "+start+" <= unix_timestamp(connect_time)");
    			paramMap.remove("start_time");
    		}
    	}
		
		
		if(paramMap.containsKey("end_time")) 
    	{
    		String end_time=(String) paramMap.get("end_time");
    		if(StringUtils.isNotEmpty(end_time)) 
    		{
    			long end = this.getTimeStamp(end_time);
    			sql.append( " and "+end+" >= unix_timestamp(connect_time)");
    			paramMap.remove("end_time");
    		}
    		
    	}
			
		
		Page<ConnectLog> result=getPage(sql, null, page,ConnectLog.class);
		return result;
	}
	
	/**
	 * 查询指定时间内的考勤设备通讯日志
	 * 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 符合条件的考勤设备通讯日志列表
	 */
	public List<ConnectLog> getConnectLogList(String start_time,String end_time){
		StringBuffer sql=new StringBuffer("SELECT * from gate_t_connect_log");	
		if(StringUtils.isNotEmpty(start_time)) 
		{
			long start = this.getTimeStamp(start_time);
			sql.append( " where "+start+" <= unix_timestamp(connect_time)");
		}else 
		{
			sql.append(" where  1=1");
		}
		if(StringUtils.isNotEmpty(end_time)) 
		{
			long end = this.getTimeStamp(end_time);
			sql.append( " and "+end+" >= unix_timestamp(connect_time)");
		}
			
		
		List<ConnectLog> result=findList(sql,null,ConnectLog.class);
		return result;
	}
	
	/**
	 * 批量删除考勤设备通讯日志
	 * 
	 * @param ids 考勤设备通讯日志主键数组
	 * @return 成功或失败
	 */
	public int delSomeConnectLog(String[] ids){
		String sql="delete from gate_t_connect_log where id in(";
		for(int i=0;i<ids.length;i++){
			if(i!=ids.length-1){
				sql+="'"+ids[i]+"',";
			}else{
				sql+="'"+ids[i]+"')";
			}
				
		}
		
		CharSequence s=sql;
		int count=this.execute(s, null);
		return count;
	}
		
	
	/**
	 * 将时间字符串转化成时间戳
	 * 
	 * @param date 时间字符串
	 * @return 时间戳
	 */
	public long getTimeStamp(String date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		long l=0;
		try {
			l=df.parse(date).getTime()/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l;
	}
}
