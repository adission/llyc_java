package video.cn.com.lanlyc.core.dao;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.base.util.QMap;
import video.cn.com.lanlyc.core.dto.VidiconInfoDto;
import video.cn.com.lanlyc.core.po.VidiconInfo;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:摄像头管理对应的Dao层
 * @date:2017年9月8日 下午5:24:27
 */
@Service
public class VidiconDao extends MySQLMapper<VidiconInfo>{
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:添加一条摄像头数据dao层接口
	 * @param:vidicon：摄像头实体
	 * @return:受影响行数
	 * @date:2017年9月11日 上午11:45:14
	 */
	public int addVidiconDao(VidiconInfo vidicon){
		int recordNum = this.save(vidicon);
		return recordNum;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:查询摄像头中摄像头编号的最大值
	 * @param:无
	 * @return:编号最大值
	 * @date:2017年9月18日 下午4:34:54
	 */
	public int getMaxVidiconNumber() {
		String sql = new String("select max(vidicon_number) maxNum from video_t_vidicon_info");
		Number maxNum = this.getNumber(sql, null);
		if(maxNum == null){
			maxNum = 0;
		}
		return maxNum.intValue();
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:查询摄像头的总数接口
	 * @param:无
	 * @return:摄像头的数目
	 * @date:2017年9月11日 上午11:36:46
	 */
	public int getVidiconCount(){
		String sql = new String("select count(id) as num from video_t_vidicon_info");
		Number num = this.getNumber(sql, null);
		return num.intValue();
		
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据关键字去检索摄像头
	 * @param:page：分页实体，keyWord：关键字
	 * @return:分页后的相关对象
	 * @date:2017年9月11日 上午9:41:49
	 */
	public Page<VidiconInfoDto> getVidiconDaoListByPage(Page<VidiconInfoDto> page, String keyWord) {
        StringBuilder sql = new StringBuilder("SELECT v.id, v.vidicon_number, v.vidicon_name, v.vidicon_ip, v.vidicon_port,"
        		+ "v.vidicon_username,v.vidicon_desc,v.vidicon_type,v.whether_important,v.vidicon_add_time");
        sql.append(" FROM video_t_vidicon_info v");
        if (StringUtils.isNotEmpty(keyWord)) {
            String val = StringEscapeUtils.escapeSql(keyWord.toLowerCase());
            sql.append(" WHERE v.vidicon_number LIKE '%").append(val)
                    .append("%'");
        }
        sql.append(" ORDER BY v.vidicon_number");
        System.out.println("---------------"+sql);
        Page<VidiconInfoDto> ret = getPage(sql, null, page, VidiconInfoDto.class);
        return ret;
    }
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据摄像头名称查询摄像头单条数据
	 * @param:map：由查询关键字组成的map集合（只支持单条件）
	 * @return:查询成功返回单条数据实体，查无数据返回null
	 * @date:2017年9月11日 下午1:52:17
	 */
	public VidiconInfo getVidiconOneByVidiconName(Map<String,Object> map){
		StringBuffer sql = new StringBuffer("select v.id, v.vidicon_number, v.vidicon_name,"
				+ " v.vidicon_ip, v.vidicon_port,v.vidicon_username,v.vidicon_desc,v.vidicon_type,"
				+ "v.whether_important,v.vidicon_add_time from video_t_vidicon_info v");
		QMap mp = null;
		if(map.containsKey("id")){
			sql.append(" where v.id = :id");
			mp = new QMap("id", map.get("id"));
		}else if(map.containsKey("vidicon_name")){
			sql.append(" where v.vidicon_name = :vidicon_name");
			mp = new QMap("vidicon_name", map.get("vidicon_name"));
		}else if(map.containsKey("vidicon_port")){
			sql.append(" where v.vidicon_port = :vidicon_port");
			mp = new QMap("vidicon_port", map.get("vidicon_port"));
		}else if(map.containsKey("vidicon_ip")){
			sql.append(" where v.vidicon_ip = :vidicon_ip");
			mp = new QMap("vidicon_ip", map.get("vidicon_ip"));
		}
        List<VidiconInfo> list = findList(sql, mp);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
	}

	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:批量删除与摄像头相关联的其他信息
	 * @param:ids：包含摄像头id的list集合
	 * @return:无
	 * @date:2017年9月11日 下午4:20:05
	 */
	public void deleteVidiconRelatedInfo(List<String>  ids) {
		Map<String, ?>[] params = new QMap[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            QMap map = new QMap();
            map.put("id", ids.get(i));
            params[i] = map;
        }
		
        String sql = "DELETE FROM video_t_vidicon_group_relation WHERE vidicon_id = :id";
        System.out.println("---sql----"+sql);
        batchExecute(sql, params);
        
		sql = "DELETE FROM video_t_alarm_info WHERE vidicon_id = :id";
		batchExecute(sql, params);
		
	}

	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:删除摄像头数据表数据dao层接口
	 * @param:ids：包含摄像头id的list集合
	 * @return:对应集合的执行成功的记录数数组
	 * @date:2017年9月11日 下午4:22:02
	 */
	public int deleteVidiconDao(List<String> ids) {
		String sql="delete from video_t_vidicon_info where id in(";
		for(int i=0;i<ids.size();i++){
			if(i!=ids.size()-1){
				sql+="'"+ids.get(i)+"',";
			}else{
				sql+="'"+ids.get(i)+"')";
			}
				
		}
		int count=this.execute(sql, null);
		return count;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:修改摄像头数据表数据dao层接口
	 * @param:vidicon：包含摄像头id的摄像头实体
	 * @return:对应集合的执行成功的记录数数组
	 * @date:2017年9月11日 下午4:28:02
	 */
	public int updateVidiconDao(Map<String,Object> map){
		String sql = "update video_t_vidicon_info set vidicon_name=:vidicon_name, vidicon_desc=:vidicon_desc,"
				+ "vidicon_type=:vidicon_type,whether_important=:whether_important where id=:id";
		QMap qMap = new QMap();
		qMap.put("id", map.get("id"));
		qMap.put("vidicon_name", map.get("vidicon_name"));
		qMap.put("vidicon_desc", map.get("vidicon_desc"));
		qMap.put("vidicon_type", map.get("vidicon_type"));
		qMap.put("whether_important", map.get("whether_important"));
		int affectedRows = execute(sql, qMap);
		return affectedRows;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据是否重点筛选摄像头
	 * @param:whether_important：是否重点
	 * @return:摄像头列表集合
	 * @date:2017年9月26日 上午9:51:41
	 */
	public List<VidiconInfo> getAllVidiconDao(int whether_important) {
		QMap map = new QMap();
		String sql = "";
		List<VidiconInfo> vidiconList = null;
		if(whether_important != 0 && whether_important != 1){
			sql = "SELECT v.id, v.vidicon_name,v.whether_important,"
				+ "v.vidicon_ip, v.vidicon_port,v.vidicon_username,v.vidicon_password from video_t_vidicon_info v";
			vidiconList = findList(sql, null);
		}else{
			sql = "SELECT v.id, v.vidicon_name from video_t_vidicon_info v where whether_important=:whether_important";
			map.put("whether_important", whether_important);
			vidiconList = findList(sql, map);
		}
		return vidiconList;
	}

	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:通过摄像头id数组获取所有id的ip、端口号、用户名、密码dao层接口
	 * @param:paramJson：id组成的数组
	 * @return:包含相关摄像头信息的map集合
	 * @date:2017年10月13日 下午2:22:38
	 */
	public List<VidiconInfo> getPartFieldFromIdDao(Object [] ids){
		List<VidiconInfo> vidiconList = null;
		StringBuffer sql = new StringBuffer("select id,vidicon_name,vidicon_ip,vidicon_port,vidicon_username,vidicon_password from video_t_vidicon_info where id in(");
		for(int i=0;i<ids.length;i++){
			if(i==ids.length-1){
				sql.append("\'"+(String)ids[i]+"\'");
				break;
			}
			sql.append("\'"+(String)ids[i]+"\',");
		}
		sql.append(")");
		vidiconList = findList(sql, null);
		return vidiconList;
	}
	
	
	public List<VidiconInfo> getAllNvrDao(){
		QMap mp = null;
		StringBuffer sql = new StringBuffer("select distinct v.nvr_ip, v.nvr_username, v.nvr_password,"
				+ " v.nvr_port from video_t_vidicon_info v");
		List<VidiconInfo> list = findList(sql, mp);
        if (list.size() > 0) {
            return list;
        }
        return null;
	}

	public List<VidiconInfo> getLimitVidiconDao(int start, int size) {
		StringBuffer sql = new StringBuffer("SELECT v.id, v.vidicon_name,v.whether_important,"
				+ "v.vidicon_ip, v.vidicon_port,v.vidicon_username,v.vidicon_password from video_t_vidicon_info v ");
		sql.append("limit "+start+", "+size);
		List<VidiconInfo> list = findList(sql, null);
		return list;
	}
	
}
