package pos.cn.com.lanlyc.controller;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * @author cjt
 * 每2s生成所有人的随机坐标，并存入数据库
 */
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.lanlyc.base.util.DataUtils;
import gate.cn.com.lanlyc.core.po.GateUser;
import gate.cn.com.lanlyc.core.service.GateUserService;
import pos.cn.com.lanlyc.core.dao.PosFloorDao;
import pos.cn.com.lanlyc.core.dto.PosTrajectoryDto;
import pos.cn.com.lanlyc.core.po.PosFloor;
import pos.cn.com.lanlyc.core.po.PosTrajectory;

@Component("posTask")
@Lazy(false)
public class GetPosionTask extends BaseController{

	@Autowired
	private GateUserService us;
	@Autowired
	private PosFloorDao pd;
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
//	@Scheduled(cron="0/3 * * * * ?")

	public void addTrajectory() {
		UserPosServer up = new UserPosServer();
		
		long l = new Date().getTime();
		List<PosTrajectory> posList = addPos();
		System.out.println(new Date().getTime() - l);
		saveTrajectory(posList);
		up.sendnew_pos(posList,l);
//		fixedThreadPool.execute(new Runnable() {  
//		    public void run() {  
//		     try {  
//		    	 saveTrajectory(posList);
//		     } catch (Exception e) {  
//		    	 e.printStackTrace();  
//		     }  
//		    }  
//		});  
	}
	
	//将数据处理并反馈
		private List<PosTrajectory> addPos() {
			// TODO Auto-generated method stub
			List<PosTrajectory> posList = new ArrayList<PosTrajectory>();
			List<GateUser> users = us.getGateuser().getAll();
			for (GateUser gu : users) {
				if(!DataUtils.isNullOrEmpty(gu.getCard_sn())) {
					PosTrajectory pt = getSingleTrajectory(gu.getId());
					posList.add(pt);
				}
			}
			return posList;
		}
		//数据并写入数据库
		public void saveTrajectory(List<PosTrajectory> posList) {
			if(posList.size()>0){
			    this.getService().getPosTrajectoryService().getDao().save(posList);
			}
		}
		
		public PosTrajectory getSingleTrajectory(String userId) {
			
			List<String> userIds = new ArrayList<String>();
			userIds.add(userId);
			List<PosTrajectoryDto> tds = this.getService().getPosTrajectoryService().getCurrentPos(userIds);
			BigDecimal x,y=new BigDecimal(0);
			
			PosTrajectory pt = new PosTrajectory();
			pt.setId(DataUtils.getUUID());
			pt.setPerId(userId);
			pt.setStationId("22561d26b50542d38bc8c68ff5a761a5");//floorList.get(index).getId());  // 图层id
			int[] symbolparam = {50,-20,-80,60};
			if(tds!=null && tds.size()>0) {
				PosTrajectoryDto td = tds.get(0);
				int stamp = symbolparam[(int)(4*(Math.random()))];
				x = td.getX().add(new BigDecimal(Math.random()*stamp));
				if(x.doubleValue()>1000) {
					x = new BigDecimal(1000*Math.random());
				}
				if(x.doubleValue()<0) {
					x = new BigDecimal(1000*Math.random());
				}
				stamp = symbolparam[(int)(4*(Math.random()))];
				y = td.getY().subtract(new BigDecimal(Math.random()*stamp));
				if(y.doubleValue()>1000) {
					y = new BigDecimal(1000*Math.random());
				}
				if(y.doubleValue()<0) {
					y = new BigDecimal(1000*Math.random());
				}
			}else {
				x = new BigDecimal(1000*Math.random());
				y = new BigDecimal(1000*Math.random());
//				x = new BigDecimal(114.302643+0.0001*Math.random());
//				y = new BigDecimal(30.606844+0.0001*Math.random());
			}
			
			pt.setPerLatitude(y);
			pt.setPerLongitude(x);
			pt.setTimeStamp(new Date());
			return pt;
			
		}
	
	
}
