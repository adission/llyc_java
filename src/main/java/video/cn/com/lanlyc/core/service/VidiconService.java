package video.cn.com.lanlyc.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.Page;
import video.cn.com.lanlyc.core.dao.GroupDao;
import video.cn.com.lanlyc.core.dao.GroupInterfaceDao;
import video.cn.com.lanlyc.core.dao.GroupScreenInterfaceDao;
import video.cn.com.lanlyc.core.dao.ScreenDao;
import video.cn.com.lanlyc.core.dao.ScreenVidiconDao;
import video.cn.com.lanlyc.core.dao.ScreenVidiconInterfaceDao;
import video.cn.com.lanlyc.core.dao.VidiconDao;
import video.cn.com.lanlyc.core.dto.VidiconInfoDto;
import video.cn.com.lanlyc.core.po.GroupScreen;
import video.cn.com.lanlyc.core.po.Screen;
import video.cn.com.lanlyc.core.po.ScreenVidicon;
import video.cn.com.lanlyc.core.po.VidiconInfo;

/**
 * @author:chupeng
 * @version:v1.0
 * @discription:摄像头管理service层
 * @date:2017年9月8日 下午4:11:40
 */
@Service
public class VidiconService {
	
	@Autowired
	VidiconDao vidiconDao;
	
	@Autowired
	ScreenVidiconDao screenVidiconDao;
	
	@Autowired
	ScreenDao sd;
	
	@Autowired
	NetSdkLogin netSdkLogin;
	
	@Autowired
	GroupInterfaceDao gDao;
	
	@Autowired
	GroupScreenInterfaceDao gsDao;
	
	@Autowired
	ScreenVidiconInterfaceDao svDao;
	
	@Autowired
	GroupScreenVidiconService gsv;
	
	@Autowired
	ScreenVidiconDao svd;
	
	@Autowired
	GroupDao groupDao;
	
	int errorCode;
	int initStatusCode;
	int loginStatusCode;
	
	Map<String,Object> map = new HashMap<String,Object>();
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:添加一条摄像头数据的service层接口
	 * @param:vidicon：摄像头实体
	 * @return:成功：返回受影响行数，失败：返回错误码以及消息
	 * @date:2017年9月11日 下午1:33:49
	 */
	public Map<String,Object> addVidiconService(VidiconInfo vidicon,String usereId){
		//查询摄像头名称是否存在并调用注册的接口
		Map<String,Object> mp = new HashMap<String,Object>();
		mp.put("vidicon_name", vidicon.getVidicon_name());
		VidiconInfo vid = vidiconDao.getVidiconOneByVidiconName(mp);
		if(vid == null){
			initStatusCode = netSdkLogin.initSDK();
			if(initStatusCode != -1){
				loginStatusCode = netSdkLogin.loginSDK(vidicon.getVidicon_ip(), (short)vidicon.getVidicon_port(), 
						vidicon.getVidicon_username(), vidicon.getVidicon_password());
				loginStatusCode = 2;
				if(loginStatusCode != -1){
					//判断摄像头端口号是否重复
					if(!judgeIpWhetherRepeat(vidicon.getVidicon_ip())){
						map.put("code", 500);
						map.put("message", "该端口号已被占用！");
					}
					//这里需要加的是密码的加密
					vidicon.setId(DataUtils.getUUID());
					vidicon.setVidicon_number(vidiconDao.getMaxVidiconNumber()+1);
					vidicon.setVidicon_add_time(DateUtils.strToDate(DateUtils.getCurrentTimeReduce(0)));
					int ret = vidiconDao.addVidiconDao(vidicon);
					boolean res = autoAddVidiconToGroup(vidicon.getId(),vidicon.getWhether_important(), 1, "最后一屏",usereId);
					if(ret > 0 && res){
						map.put("code", 200);
						map.put("message", "摄像头添加成功！");
					}else{
						map.put("code", 500);
						map.put("message", "摄像头添加失败！");
					}
					
				}else{
					errorCode = netSdkLogin.errorCode();
					map.put("code", errorCode);
					map.put("message", "摄像头注册用户失败！");
				}
			}else{
				errorCode = netSdkLogin.errorCode();
				map.put("code", errorCode);
				map.put("message", "摄像头初始化失败！");
			}
		}else{
			map.put("code", 500);
			map.put("message", "该摄像头名称已被占用！");
		}
		return map;
		
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:删除摄像头service层接口
	 * ids：摄像头id字符串数组["id1","id2",...]
	 * @return:成功或失败的状态码以及消息json串
	 * @url:../vidicon/deleteVidicon
	 * @date:2017年9月11日 下午4:40:48
	 */
	public boolean deleteVidiconService(Object [] ids){
        
        //List<String> pks = new ArrayList<String>();
        
        for (int i = 0; i < ids.length; i++) {
            //pks.add(ids[i]);
            deleteRelatedDatas((String)ids[i]);
            //删除之前应该先停止预览、注销和释放
        }
        // 删除与该摄像头相关的其他信息
        //1、删除分屏与摄像头关联数据
        //2、如果某一分屏只有这一个摄像头，则将该分屏与分组关系删除
        //3、如果某一分组只有这一个分屏，则将分组也删除
        //vidiconDao.deleteVidiconRelatedInfo(pks);
        
        //int ret = vidiconDao.deleteVidiconDao(pks);
        
        return true;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:删除与摄像头id相关联的各种数据
	 * @param:
	 * @return:
	 * @date:2017年9月26日 下午4:24:25
	 */
	public boolean deleteRelatedDatas(String vidicon_id){
		//1、查询摄像头id所对应的分屏
		List<ScreenVidicon> sv = svDao.getSplitScreenId(vidicon_id);
		List<String> list1 = new ArrayList<String>();
		if(sv.size()>1){
			for(int i=0;i<sv.size();i++){
				List<GroupScreen> gs = gsDao.getSplitScreenId(sv.get(i).getSplit_screen_id());
				if(gs.size()>1){
					//直接删除分屏与摄像头关系表数据和摄像头表数据
					screenVidiconDao.deleteScreenVidiconById(sv.get(i).getId());
					list1.add(vidicon_id);
					vidiconDao.deleteVidiconDao(list1);
				}else if(gs.size() == 1){
					//删除分组表中的分组,然后再删除分屏与摄像头关系表数据和摄像头表数据
					groupDao.deleteGroupById(gs.get(0).getId());
					screenVidiconDao.deleteScreenVidiconById(sv.get(i).getId());
					list1.add(vidicon_id);
					vidiconDao.deleteVidiconDao(list1);
				}
			}
		}else if(sv.size() == 1){
			//查询分屏与分组数据的条数
			List<GroupScreen> gs = gsDao.getSplitScreenId(sv.get(0).getSplit_screen_id());
			if(gs.size()>1){
				//直接删除分屏与摄像头关系表数据和摄像头表数据
				screenVidiconDao.deleteScreenVidiconById(sv.get(0).getId());
				list1.add(vidicon_id);
				vidiconDao.deleteVidiconDao(list1);
			}else if(gs.size() == 1){
				//删除分组表中的分组,然后再删除分屏与摄像头关系表数据和摄像头表数据
				groupDao.deleteGroupById(gs.get(0).getId());
				screenVidiconDao.deleteScreenVidiconById(sv.get(0).getId());
				list1.add(vidicon_id);
				vidiconDao.deleteVidiconDao(list1);
			}
		}else{
			//直接删除摄像头id对应的数据即可
			list1.add(vidicon_id);
			vidiconDao.deleteVidiconDao(list1);
		}
		return true;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:修改摄像头数据service层接口
	 * @param:
	 * @return:
	 * @date:2017年9月12日 上午9:53:05
	 */
	public int updateVidiconService(Map<String,Object> map){
		int data = vidiconDao.updateVidiconDao(map);
		return data;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:根据关键字模糊查询所有的摄像头（关键字不存在时即为查询所有）
	 * @param:page：分页实体，keyWord：关键字
	 * @return:
	 * @date:2017年9月11日 上午11:05:25
	 */
	public Page<VidiconInfoDto> getVidiconServiceListByPage(Page<VidiconInfoDto> page, String keyWord){
	        return vidiconDao.getVidiconDaoListByPage(page, keyWord);
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:通过摄像头id获取摄像头单条数据service层接口
	 * @param:摄像头id
	 * @return:摄像头单条数据实体
	 * @date:2017年9月11日 下午5:15:26
	 */
	public VidiconInfo getVidiconById(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		VidiconInfo oneInfo = vidiconDao.getVidiconOneByVidiconName(map);
		return oneInfo;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:通过摄像头ip获取摄像头名称service层接口
	 * @param:摄像头id
	 * @return:摄像头单条数据实体
	 * @date:2017年9月11日 下午5:15:26
	 */
	public VidiconInfo getVidiconByIp(String ip) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("vidicon_ip", ip);
		VidiconInfo oneInfo = vidiconDao.getVidiconOneByVidiconName(map);
		return oneInfo;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:查询出已分组中所有的屏中的所有没有放置摄像头的空位置接口
	 * @param:screenVidiconCount：某一分组模式下一个屏中所能放置的摄像头数量，lis:某一分屏模式下所有的分组id列表
	 * @return:gList：空位置list集合[{"gid":{"sid":xxx}},{...}]
	 * @date:2017年9月15日 下午3:55:40
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> judgeScreenHasNullSpace(int screenVidiconCount,List<String> lis){
		Map<String,Object> gsMap1 = new HashMap<String,Object>();
		Map<String, Object> gsMap = gsDao.getAllScreenByGroupId(lis);//分组id对应的所有的分屏id
		
		List<Integer> allList = new ArrayList<Integer>();
		for(int i=1;i<screenVidiconCount+1;i++){
			allList.add(i);
		}
		
		Iterator<Entry<String, Object>> iterator = gsMap.entrySet().iterator();
		while (iterator.hasNext()) {  
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();  
		    String key = (String)entry.getKey();  
		    List<String> screenList = (List<String>)entry.getValue();
			
		    Map<String,Object> spMap = null;
			for (int j = 0; j < screenList.size(); j++) {
				spMap = new HashMap<String,Object>();
				int realCount = svDao.getAllVidiconByScreenId(screenList.get(j));
				if(screenVidiconCount > realCount){//判断应放摄像头和实放摄像头的数量大小
					List<Integer> nullPositionList = svDao.getNullPosition(screenList.get(j));
					List<Integer> diffList = fromListFindDiff(allList, nullPositionList);
					
					spMap.put(screenList.get(j), diffList);//分屏id对应的所有未放置摄像头的位置
				}else{
					continue;
				}
			}
			gsMap1.put(key, spMap);//指定组id对应的屏和屏对应的空位置
		
		}
		return gsMap1;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:自动添加摄像头到分组中
	 * @param:vidicon_id:新增的摄像头id，set_default：新增分屏是否设置为默认（1：是，0：否），split_screen_name：新增的分屏名称
	 * @return:true:添加成功，false:添加失败
	 * @date:2017年9月18日 上午10:18:21
	 */
	public boolean autoAddVidiconToGroup(String vidicon_id,boolean whether_important, int set_default,String split_screen_name,String usereId){
		
		boolean res1 = true;
		boolean res2 = true;
		boolean res3 = true;
		boolean res4 = true;
		boolean res = true;
		boolean ifHasImportantGroup = gDao.judgeGroupInfo(whether_important);
		if(whether_important){//当新增的摄像头为重点摄像头时，检查是否有重点分组，若没有，则调用初始化分组接口
			
			if(!ifHasImportantGroup){//重点分组不存在，则调用初始化分组接口
				res1 = gsv.addInitGroupScreenVidicon(usereId,vidicon_id,1);
			}else{//重点分组存在，查询所有重点分组和一般分组，将摄像头自动添加其中
				res2 = putVidiconToGroup(vidicon_id,whether_important, set_default,split_screen_name);
			}
		}else{//当新增的摄像头为非重点摄像头时，检查是否有一般分组
			if(ifHasImportantGroup){//存在一般分组，查询出一般分组将摄像头自动添加其中
				res3 = putVidiconToGroup(vidicon_id,whether_important, set_default,split_screen_name);
			}else{//不存在一般分组，则调用初始化分组接口
				res4 = gsv.addInitGroupScreenVidicon(usereId,vidicon_id,2);
			}
		}
			
		if(!res1 || !res2 || !res3 || !res4){
			res = false;
		}
		return res;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:自动添加摄像头到分组中核心调用接口
	 * @param:vidicon_id:新增的摄像头id，set_default：新增分屏是否设置为默认（1：是，0：否），split_screen_name：新增的分屏名称
	 * @return:true:添加成功，false:添加失败
	 * @date:2017年9月29日 上午10:55:21
	 */
	@SuppressWarnings("unchecked")
	public boolean putVidiconToGroup(String vidicon_id,boolean whether_important, int set_default,String split_screen_name){
		List<String> lis = new ArrayList<String>();
		boolean res1 = true;
		boolean res2 = true;
		boolean res3 = true;
		boolean res4 = true;
		boolean res = true;
		
		Map<String, Object> map = gDao.getAllGroup(whether_important);
		Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();  
		
		while (entries.hasNext()) {  
		    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entries.next();  
		  
		    String key = (String)entry.getKey();  
		    int screenVidiconCount = Integer.parseInt(key)*Integer.parseInt(key);//每种分屏模式下的每一屏可以放置的摄像头总数
		    List<String> value = (List<String>)entry.getValue();  
		    lis = (List<String>) map.get(key);//每种分屏模式下所有的分组id列表
		    System.out.println("Key = " + key + ", Value = " + value); 
		    
		    if(key == "1"){
		    	Map<String,Object> nullPositionList = judgeScreenHasNullSpace(screenVidiconCount,lis);
				res1 = putNullPosition(nullPositionList, vidicon_id, set_default, split_screen_name);
			}else if(key == "2"){
				Map<String,Object> nullPositionList = judgeScreenHasNullSpace(screenVidiconCount,lis);
				res2 = putNullPosition(nullPositionList, vidicon_id, set_default, split_screen_name);
				
			}else if(key == "3"){
				Map<String,Object> nullPositionList = judgeScreenHasNullSpace(screenVidiconCount,lis);
				res3 = putNullPosition(nullPositionList, vidicon_id, set_default, split_screen_name);
				
			}else if(key == "4"){
				Map<String,Object> nullPositionList = judgeScreenHasNullSpace(screenVidiconCount,lis);
				res4 = putNullPosition(nullPositionList, vidicon_id, set_default, split_screen_name);
			}
		}
		if(res1 && res2 && res3 && res4){
			res = true;
		}
		return res;
	}
	
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:
	 * @param:nullPositionList:屏幕中没有放置摄像头的空位置集合，vidicon_id：摄像头id，
	 * 			set_default：是否将新增的摄像头设置为默认，split_screen_name：新增屏的屏名称
	 * @return:
	 * @date:2017年9月25日 上午9:56:09
	 */
	@SuppressWarnings("unchecked")
	public Boolean putNullPosition(Map<String,Object> nullPositionList,String vidicon_id,int set_default,String split_screen_name){
		boolean res = true;
		
		if(!nullPositionList.isEmpty()){
			Iterator<Entry<String, Object>> iterator = nullPositionList.entrySet().iterator();
				while (iterator.hasNext()) {  
					Entry<String, Object> obj = iterator.next();
					Map<String,Object> screenMap = (Map<String, Object>) obj.getValue();
					if(screenMap.isEmpty() || screenMap == null){//如果screenMap为空证明该组没有未放置摄像头的空位置，那么就应该新增一组并将新增的摄像头放于其中						
						//直接在相应位置添加一屏
						//在当前组下新增一屏
						Screen screen = new Screen();
						screen.setId(DataUtils.getUUID());
						screen.setSet_default(set_default);
						screen.setSplit_screen_name(split_screen_name);
						sd.addScreen(screen);
						//为新增的一屏新增屏幕摄像头关系表数据
						int ret = getAddSplitVidiconRelationData(vidicon_id,screen.getId(),1);
						
						if(ret>0){
							res = true;
						}else{
							res = false;
						}
						
						
					}else{
						Iterator<Entry<String, Object>> iterator1 = screenMap.entrySet().iterator();  
						while (iterator1.hasNext()) {  
							Entry<String, Object> obj1 = iterator1.next();
							List<Integer> pos = (List<Integer>)obj1.getValue();//当前组中所有的未放置摄像头的位置
							if(pos.isEmpty()){
								//直接在相应位置添加一屏
								//在当前组下新增一屏
								Screen screen = new Screen();
								screen.setId(DataUtils.getUUID());
								screen.setSet_default(set_default);
								screen.setSplit_screen_name(split_screen_name);
								sd.addScreen(screen);
								//为新增的一屏新增屏幕摄像头关系表数据
								int ret = getAddSplitVidiconRelationData(vidicon_id,screen.getId(),1);
								if(ret>0){
									res = true;
								}else{
									res = false;
								}
							}else{
								//随机找一个位置放置新增的摄像头
								int po = pos.get((int)Math.random()*(pos.size())).intValue();
								//修改屏幕摄像头关系表数据
								//为新增的一屏新增屏幕摄像头关系表数据
								int ret = getAddSplitVidiconRelationData(vidicon_id,obj1.getKey(),po);
								if(ret>0){
									res = true;
								}else{
									res = false;
								}
							}
						}	
				}
			}
		}
		return res;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:获取新增分屏组合数据
	 * @param:vidicon_id:摄像头id，split_screen_id：分屏id，pos：空位置（数字表示）
	 * @return:返回受影响行数
	 * @date:2017年9月26日 下午3:54:42
	 */
	public int getAddSplitVidiconRelationData(String vidicon_id,String split_screen_id,int po){
		ScreenVidicon screenVidicon = new ScreenVidicon();
		screenVidicon.setId(DataUtils.getUUID());
		screenVidicon.setVidicon_id(vidicon_id);;
		screenVidicon.setSplit_screen_id(split_screen_id);
		screenVidicon.setScreen_position(po);
		
		int ret = svd.addScreenVidicon(screenVidicon);
		return ret;
	}
	
	
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:自动获取所有的摄像头id和名称（不分页）service层接口
	 * @param:无
	 * @return:所有的摄像头实体
	 * @date:2017年9月18日 下午5:53:01
	 */
	public List<VidiconInfo> getAllVidiconService(int whether_important) {
		List<VidiconInfo> allInfo = vidiconDao.getAllVidiconDao(whether_important);
		return allInfo;
		
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:获取两个list集合这种的不用元素
	 * @param:list1：list集合1，list2：list集合2
	 * @return:不同元素组成的新的list集合
	 * @date:2017年9月23日 下午6:01:33
	 */
	public List<Integer> fromListFindDiff(List<Integer> list1,List<Integer> list2){
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> maxList = list1;
		List<Integer> minList = list2;
		if(list1.size()<list2.size()){
			maxList = list2;
			maxList = list1;
		}
		Map<Integer,Integer> map = new HashMap<Integer,Integer>(maxList.size());
		
		for(Integer k: maxList){
			map.put(k, 1);
		}
		for(Integer k: minList){
			if(map.get(k) != null){
				map.put(k, 2);
				continue;
			}
		}
		for(Map.Entry<Integer, Integer> entry:map.entrySet())  
        {  
            if(entry.getValue()==1)  
            {  
                list.add(entry.getKey());  
            }  
        }  
		
		return list;
	} 
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:判断端口号是否重复
	 * @param:vidicon_port：摄像头端口号
	 * @return:true：端口重复，false：端口没有重复
	 * @date:2017年9月28日 下午3:08:27
	 */
	public boolean judgeNameWhetherRepeat(String vidicon_name){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("vidicon_name", vidicon_name);
		VidiconInfo vInfo = vidiconDao.getVidiconOneByVidiconName(map);
		if(vInfo != null){
			return true;
		}
		return false;
		
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:判断ip是否重复
	 * @param:vidicon_port：摄像头ip
	 * @return:true：ip重复，false：ip没有重复
	 * @date:2017年9月28日 下午4:25:27
	 */
	public boolean judgeIpWhetherRepeat(String ip){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("vidicon_ip", ip);
		VidiconInfo vInfo = vidiconDao.getVidiconOneByVidiconName(map);
		if(vInfo != null){
			return true;
		}
		return false;
		
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:通过摄像头id数组获取所有id的ip、端口号、用户名、密码service层接口
	 * @param:paramJson：id组成的数组
	 * @return:包含相关摄像头信息的map集合
	 * @date:2017年10月13日 下午2:22:38
	 */
	public List<VidiconInfo> getPartFieldFromIdService(Object [] ids){
		List<VidiconInfo> vidiconList = vidiconDao.getPartFieldFromIdDao(ids);
		return vidiconList;
	}
	
	/**
	 * @author:chupeng
	 * @version:v1.0
	 * @discription:获取所有的NVRservice层接口
	 * @param:无
	 * @return:nvr组成的list集合
	 * @date:2017年10月19日 下午4:30:43
	 */
	public List<Object>  getAllNvrService(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Object> list = new ArrayList<Object>();
		List<VidiconInfo> vidiconList = vidiconDao.getAllNvrDao();
		if(vidiconList.size()>0){
			for(int i=0;i<vidiconList.size();i++){
				map.put("nvr_ip", vidiconList.get(i).getNvr_ip());
				map.put("nvr_username", vidiconList.get(i).getNvr_username());
				map.put("nvr_password", vidiconList.get(i).getNvr_password());
				map.put("nvr_port", vidiconList.get(i).getNvr_port());
				list.add(map);
			}
		}
		return list;
	}

	public List<VidiconInfo> getLimitVidiconService(int start, int size) {
		List<VidiconInfo> vis = vidiconDao.getLimitVidiconDao(start, size);
		return vis;
	}
}
