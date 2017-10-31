package gate.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.dao.PapTypesDao;
import gate.cn.com.lanlyc.core.po.PapTypes;

@Service
public class PapTypesService {
	@Autowired
	private PapTypesDao papTypesDao;

	public PapTypesDao getPapTypesDao() {
		return papTypesDao;
	}
	/**
	 * 查询所有的证件类型 
	 * 
	 * @return response
	 */
	public List<PapTypes> getAllPapTypes() {
		return papTypesDao.getAllPapTypes();
	}
	
	
	/**
	 * 根据证件名称新建证件类型
	 * @param name 新建的证件名称
	 * @author jiangyanyan
	 * @return  2(新建成功) 3(新建失败) 1(输入的证件类型名称已经存在)
	 */
	public int isCreatePapTypes(String name) {
		boolean isExist = this.getPapTypesDao().isCreatePapTypes(name);
		//假如不存在
		if(!isExist) {
			PapTypes papTypes = new PapTypes();
			papTypes.setId(DataUtils.getUUID());
			papTypes.setName(name);
			int saveR = this.getPapTypesDao().save(papTypes);
			if(saveR==1) {
				//新建成功
				return 2;
			}else {
				//新建失败
				return 3;
			}
		}else {
			//输入的证件类型名称已经存在
			return 1; 
		}
	}
	
	
	 /**
	  * 根据证件类型的旧名称修改证件类型的新名称
	  * @param newName 证件类型的新名称
	  * @param oldName 证件类型的旧名称
	  * @author jiangyanyan
	  * @return 2(编辑成功) 3(编辑失败) 1(输入的证件类型名称已经存在)
	  */
	public int aditPapTypes(String newName,String oldName) {
		boolean isExist = this.getPapTypesDao().isCreatePapTypes(newName);
		if(!isExist) {
			boolean isAdit = this.getPapTypesDao().isAditPapTypes(newName, oldName);
			if(isAdit) {
				//编辑成功
				return 2;
			}else {
				//编辑失败
				return 3;
			}
		}else {
			//输入的证件类型名称已经存在
			return 1; 
		}
	}
	

}
