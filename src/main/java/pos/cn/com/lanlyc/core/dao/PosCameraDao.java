package pos.cn.com.lanlyc.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.persistence.MySQLMapper;
import cn.com.lanlyc.base.util.Page;
import pos.cn.com.lanlyc.core.dto.PosCameraDto;
import video.cn.com.lanlyc.core.po.VidiconInfo;

@Service
public class PosCameraDao extends MySQLMapper<VidiconInfo>{
	
	/*
	 * 分页查询所有的/某一个图层的摄像头列表
	 */
	public Page<PosCameraDto> getPosCameraListByPage(Page<PosCameraDto> page,String layer_id,String keyWords){
		/*StringBuffer sql=new StringBuffer("select c.*,f.layer_name layerName from"
				+ "(select * from video_t_vidicon_info)c,pos_t_floor f where 1=1 and c.layer_id=f.id ");
		if(layer_id != null && !"".equals(layer_id)) {
			sql.append(" and layer_id=:layer_id ");
		}*/
		StringBuffer sql=new StringBuffer(" ");
		if(layer_id != null && !"".equals(layer_id)){
			sql=sql.append(" select c.id,c.vidicon_number,c.vidicon_name,c.geo_y,c.geo_x,f.layer_name layerName from"
					+ " (select id,vidicon_number,vidicon_name,	geo_y,geo_x,layer_id"
					+ " from video_t_vidicon_info)c,pos_t_floor f where 1=1 and c.layer_id=f.id  and c.layer_id=:layer_id");
		}else{
			sql=sql.append("select c.id,c.vidicon_number,c.vidicon_name,c.geo_y,c.geo_x,c.layer_id,f.layer_name layerName "
					+ "from video_t_vidicon_info c LEFT JOIN pos_t_floor f on c.layer_id=f.id where 1=1 ");
		}
		System.out.println(keyWords);
		if(StringUtils.isNotEmpty(keyWords)){
			String val = StringEscapeUtils.escapeSql(keyWords.toLowerCase());
			sql.append(" and ( LOWER(c.vidicon_name) LIKE '%").append(val)
			.append("%' OR LOWER(c.vidicon_number) LIKE '%").append(val)
			.append("%' )");

		}
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("layer_id", layer_id);
		Page<PosCameraDto> result = getPage(sql, paramMap, page, PosCameraDto.class);
		System.out.println(result);
		return result;
	}

	/*
	 * 通过图层id 查找这个图层里面所有的摄像头
	 */
    public List<VidiconInfo> selectCameraByLayerId(String layer_id){
    	String sql="select id,geo_x,geo_y from video_t_vidicon_info where layer_id=:layer_id";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("layer_id", layer_id);
		List<VidiconInfo> result = findList(sql, paramMap, VidiconInfo.class);	
		if(result!=null && result.size()>0){
			return result;
		}else{
			return null;
		}
	}
    
    /*
	 * 通过id获取摄像头
	 */
	public VidiconInfo getPosCameraById(String id) {
		String sql="select id from video_t_vidicon_info where id=:id";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		List<VidiconInfo> result = findList(sql, paramMap, VidiconInfo.class);	
		if(result!=null && result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	/*
	 * 通过id,geo_x,geo_y,layer_id 查找摄像头
	 */
    public VidiconInfo getPosCamera(String id,String geo_x,String geo_y,String layer_id){
    	String sql="select id from video_t_vidicon_info where id=:id and geo_x=:geo_x and geo_y=:geo_y and layer_id=:layer_id ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("geo_x", geo_x);
		paramMap.put("geo_y", geo_y);
		paramMap.put("layer_id", layer_id);
		List<VidiconInfo> result = findList(sql, paramMap, VidiconInfo.class);	
		if(result!=null && result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
    
    /**
     * 修改摄像头
     */   
    public int updateCamera(String id,String geo_x,String geo_y,String layer_id){
    	String sql = "UPDATE video_t_vidicon_info set geo_x=:geo_x,geo_y=:geo_y,layer_id=:layer_id where id=:id";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("geo_x", geo_x);
		paramMap.put("geo_y", geo_y);
		paramMap.put("layer_id", layer_id);
		int num = execute(sql, paramMap);
		return num;
    }
    
    
}
