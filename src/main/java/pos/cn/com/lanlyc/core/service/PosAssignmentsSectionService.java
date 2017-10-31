package pos.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pos.cn.com.lanlyc.core.dao.PosAssignmentsSectionDao;
import pos.cn.com.lanlyc.core.dto.PosAssignmentsSectionDto;
import pos.cn.com.lanlyc.core.po.PosAssignmentsSection;

@Service
public class PosAssignmentsSectionService {

	@Autowired
	private PosAssignmentsSectionDao posAssignmentsSectionDao;

	public PosAssignmentsSectionDao getPosAssignmentsSectionDao() {
		return posAssignmentsSectionDao;
	}
	
	/*
	 * 查询指定图层上指定类型的区域列表
	 */
    public List<PosAssignmentsSectionDto> selectAssignmentsSectionList(String type,String layer_id){
    	return posAssignmentsSectionDao.selectAssignmentsSectionList(type,layer_id);
	}
    
    /**
     * 保存一个区域到区域列表中
     */
    public int saveAssignmentsSection(PosAssignmentsSection ps){
    	try {
    		return posAssignmentsSectionDao.save(ps);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
    }
	
}
