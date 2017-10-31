package gate.cn.com.lanlyc.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.dao.CarTypesDao;
import gate.cn.com.lanlyc.core.po.CarTypes;
import gate.cn.com.lanlyc.core.po.PapTypes;

@Service
public class CarTypesService {
	@Autowired
	private CarTypesDao carTypesDao;

	public CarTypesDao getCarTypesDao() {
		return carTypesDao;
	}
	/**
	 * 查询所有的车辆类型 
	 * 
	 * @return response
	 */
	public List<CarTypes> getAllCarTypes() {
		return carTypesDao.getAllCarTypes();
	}

	
	/**
	 * 根据车辆名称新建车辆类型
	 * @param name 新建的车辆名称
	 * @author jiangyanyan
	 * @return  2(新建成功) 3(新建失败) 1(输入的车辆类型名称已经存在)
	 */
	public int isCreateCarTypes(String name) {
		boolean isExist = this.getCarTypesDao().isCreateCarTypes(name);
		//假如不存在
		if(!isExist) {
			CarTypes papTypes = new CarTypes();
			papTypes.setId(DataUtils.getUUID());
			papTypes.setName(name);
			int saveR = this.getCarTypesDao().save(papTypes);
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
	  * 根据车辆类型的旧名称修改车辆类型的新名称
	  * @param newName 车辆类型的新名称
	  * @param oldName 车辆类型的旧名称
	  * @author jiangyanyan
	  * @return 2(编辑成功) 3(编辑失败) 1(输入的车辆类型名称已经存在)
	  */
	public int aditCarTypes(String newName,String oldName) {
		boolean isExist = this.getCarTypesDao().isCreateCarTypes(newName);
		if(!isExist) {
			boolean isAdit = this.getCarTypesDao().isAditCarTypes(newName, oldName);
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
