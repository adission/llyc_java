package pos.cn.com.lanlyc.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Codes;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateUserInfoView;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pos.cn.com.lanlyc.core.dto.PosCardDto;
import pos.cn.com.lanlyc.core.po.PosIndentificationCard;

/**
 * 批量制卡，人员与卡绑定，删除，修改，解绑Controller
 * @author jiangyanyan
 * @date 2017年9月15日 11:10:23
 * @version 1.0
 *
 */

@Controller
@RequestMapping("/poscard")
public class PosCardController extends BaseController {

	
	/**
	 * 批量制卡接口 
	 * @param startNum 开始卡号编号
	 * @param endNum 结束卡号编号
	 * @author jiangyanyan
	 * @return 返回制卡成功与失败的json数据
	 */
	@RequestMapping(value="/batchFabrication")
	@ResponseBody
	public Response batchFabrication(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String startNum = jObject.getString("startNum"); 
		String endNum = jObject.getString("endNum"); 
		Response response = Response.PARAM_ERROR();
		if(!jObject.containsKey("startNum") && !jObject.containsKey("endNum")) {
			return response;
		}
		if(startNum==null || "".equals(startNum)||endNum==null ||"".equals(endNum)) {
			return response;
		}
		try {
			//判断输入的定位卡是否存在
			boolean judge = this.getService().getPosCardService().getPosCardDao().judgementBatchFabricationCard(startNum, endNum);
			//假如存在
			if(judge) {
				String message = new String("输入的卡号范围在数据库中已经存在！");
				return Response.ERROR(1001, message);
			}else {
				int s = Integer.parseInt(startNum);
				int e = Integer.parseInt(endNum);
				Map<String,Integer> addCard = this.getService().getPosCardService().getPosCardDao().batchFabricationCard(s, e);
				Response resp = Response.OK();
				resp.setData(addCard);
				return resp;
			}
		}catch(Exception e){
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
		
	}
	
	
	/**
	 * 新增一张卡与人员绑定
	 * @param param
	 * @author jiangyanyan
	 * @return 返回新增一张定位卡成功与失败的json数据
	 */
	@RequestMapping(value="/cardUserBinding")
	@ResponseBody
	public Response addASingleCardAndBindingPer(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		if(!jObject.containsKey("card_sn") && !jObject.containsKey("user_id")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String card_sn = jObject.getString("card_sn");
		String user_id = jObject.getString("user_id");
		if(card_sn==null || "".equals(card_sn)||user_id==null ||"".equals(user_id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		try {
			Integer code = Codes.ROLE_EXISTS;
			//根据卡号判断此卡号是否已经在数据表中存在
			boolean card = this.getService().getPosCardService().getPosCardDao().judgementCardIsExist(card_sn);
			//假如不存在
			if(!card) {
				//根据卡号新建卡
				PosIndentificationCard p = new PosIndentificationCard(card_sn);
				p.setId(DataUtils.getUUID());
				int saveR = this.getService().getPosCardService().getPosCardDao().save(p);
				//假如根据卡号新增一张卡成功
				if(saveR==1) {
					//根据人员id获取人员实体
					 List<GateUserInfoView> infoView = this.getService().getPosPerConstructService().getPosPerConstructDao().getUserByUserId(user_id);
					 GateUserInfoView userInfo = null;
					 if(infoView.size() !=0) {
						 userInfo = infoView.get(0);
						 userInfo.setCard_sn(card_sn);
					 }else {
						 return Response.ERROR(code, "人员不存在！");
					 }
					 //更新人员属性
					 boolean numj = this.getService().getPosPerConstructService().getPosPerConstructDao().updateUserByUserId(user_id, card_sn);
					 if(numj) {
						 return Response.OK();
					 }else {
						 return Response.ERROR(code, "新增卡成功但是卡号与人员绑定失败!");	  
					 }
				}else {
					return Response.ERROR(code, "新建一张卡失败！");	  
				}
			}else {
				return Response.ERROR(code, "卡号已存在!");	
			}	
		}catch(Exception e){
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}	
	}
	
	
	/**
	 * 人卡解绑
	 * @param user_id 人员id
	 * @author jiangyanyan
	 * @return 人卡解绑成功与失败的json数据
	 */
	@RequestMapping(value="/cardUserUnbinding")
	@ResponseBody
	public Response unbundlingUserAndCard(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String user_id = jObject.getString("user_id");
		//判断是否包函参数或者参数是否为空
		if(!jObject.containsKey("user_id")||user_id==null||"".equals(user_id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		Integer code = Codes.ROLE_EXISTS;
		//更新人员实体的属性
		 boolean numj = this.getService().getPosPerConstructService().getPosPerConstructDao().updateUserByUserId(user_id, "");
		 if(!numj) {
			 return Response.ERROR(code, "修改卡号与人员绑定失败!");	  
		 }else {
			 return Response.OK();
		 }
	}
	
	
	/**
	 * 人员与卡解绑并且删除定位卡
	 * @param user_id 人员id
	 * @author jiangyanyan
	 * @return 返回成功与失败的json数据
	 */
	@RequestMapping(value="/cardUserUnbindDel")
	@ResponseBody
	public Response  unbundlingUserAndDeleteCard(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String user_id = jObject.getString("user_id");
		String card_id = jObject.getString("card_id");
		//判断是否包函参数或者参数是否为空
		if(!jObject.containsKey("card_id")||card_id==null||"".equals(card_id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}else {
			try {
				Integer code = Codes.ROLE_EXISTS;
				if(!"".equals(user_id)) {
					//更新人员实体的属性
					 boolean numj = this.getService().getPosPerConstructService().getPosPerConstructDao().updateUserByUserId(user_id, "");
					 if(!numj) {
						 return Response.ERROR(code, "修改卡号与人员绑定失败!");	  
					 }
				}
				 //根据卡号id删除卡号
				 boolean res = this.getService().getPosCardService().getPosCardDao().deleteCardByCardId(card_id);
				 if(res) {
					 return Response.OK();
				 }else {
					 return Response.ERROR(code, "删除人员定位卡失败!");	  
				 }
			}catch(Exception e) {
				e.printStackTrace();
				return Response.SERVER_ERROR();
			}
		}
	}
	
	
	/**
	 * 修改人员与卡的绑定
	 * @param user_id_g 旧人员id
	 * @param user_id 新的人员id
	 * @param card_sn 定位卡号
	 * @author jiangyanyan
	 * @return 返回成功与失败的json数据
	 */
	@RequestMapping(value="/userCardUpdate")
	@ResponseBody
	public Response updateUserAndCard(String paramJson) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		String user_id_g = jObject.getString("user_id_g"); //有可能为空，有可能不为空
		String user_id = jObject.getString("user_id");
		String card_sn = jObject.getString("card_sn");
		//判断是否包函参数或者参数是否为空
		if(!jObject.containsKey("user_id")||user_id==null||"".equals(user_id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		try {
			Integer code = Codes.ROLE_EXISTS;
			//根据人员id获取人员实体
			 List<GateUserInfoView> infoView = this.getService().getPosPerConstructService().getPosPerConstructDao().getUserByUserId(user_id);
			 GateUserInfoView userInfo = null;
			 if(infoView.size() !=0) {
				 userInfo = infoView.get(0);
				 userInfo.setCard_sn(card_sn);
			 }else {
				 return Response.ERROR(code, "人员不存在！");
			 }
			 //旧人员卡号置为空
			 boolean numO = true;
			 if(!"".equals(user_id_g)) {
				 numO = this.getService().getPosPerConstructService().getPosPerConstructDao().updateUserByUserId(user_id_g, ""); 
			 }
			//更新新人员实体的属性
			 boolean numj = this.getService().getPosPerConstructService().getPosPerConstructDao().updateUserByUserId(user_id, card_sn);
			 if(numj&&numO) {
				 return Response.OK();
			 }else {
				 return Response.ERROR(code, "修改卡号与人员绑定失败!");	  
			 }
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	/**
	 * 分页查询定位卡&根据关键字分页查询定位卡
	 * @param start 每页以第几条开始
	 * @param length 每页的长度
	 * @param draw 第几页
	 * @author jiangyanyan
	 * @return 返回指定页数指定数量的定位卡json数据
	 */
	@RequestMapping(value="/queryCard",method=RequestMethod.POST)
	@ResponseBody
	public Response queryCardAndPage(String paramJson,Integer start,Integer length,Integer draw) {
		JSONObject jObject = JSONObject.fromObject(paramJson);
		System.out.println(jObject);
		String keyword = "";
		if(jObject.has("keyword")) {
			keyword = jObject.getString("keyword");
		}
		try {
			int currentPage = start/length+1;
			Page<PosCardDto> page = new Page<PosCardDto>(currentPage);
			page.setPageSize(length);
			Page<PosCardDto> result=this.getService().getPosCardService().getPosCardDao().queryUserAndCard(page,keyword);
			Response response=Response.OK();
			response.put("recordsTotal", result.getTotalRow());
	        response.put("recordsFiltered", result.getTotalRow());
	        response.setData(result.getResultRows());
	        response.put("recordsFiltered", result.getTotalRow());
	        response.put("draw", draw);
	        response.put("total", result.getTotalRow());
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
	
	
	/**
	 * 根据定位卡列表删除定位卡，并且解绑与定位卡绑定的人
	 * @param param  cardIds 定位卡列表
	 * @author jiangyanyan
	 * @return 返回成功与失败的json数据
	 */
	@RequestMapping(value="/delCardAndUnbind")
	@ResponseBody
	public Response deleteCardsByCardIdsAndUserIds(String paramJson){
		JSONObject jObject = JSONObject.fromObject(paramJson);
		JSONArray card_ids = jObject.getJSONArray("cards_ids");
		JSONArray user_ids = jObject.getJSONArray("users_ids");
		try {
			boolean deleteS = this.getService().getPosCardService().batchDeleteCardsByCardIdArr2(card_ids,user_ids);
			Response response = Response.OK();
			response.setData(deleteS);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return Response.SERVER_ERROR();
		}
	}
		
}
