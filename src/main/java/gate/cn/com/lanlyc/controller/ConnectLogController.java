package gate.cn.com.lanlyc.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.DateUtils;
import cn.com.lanlyc.base.util.FileUtils;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.ConnectLog;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/GateConnectLog")
public class ConnectLogController extends BaseController {
	 
	
	/**
	 * 获取一段时间内的连接记录
	 * 访问url GateConnectLog/getConnectLog?start_time=2017-09-01 10:20:20&end_time=2017-09-01 19:00:00&length=10&start=0
	 * 
	 * @param paramJson(以下是相应的json字段)
	 * 
	 * @param start_time("2017-08-01")
	 *            开始时间
	 * @param end_time（"2017-08-01"）
	 *            结束时间
	 * @param length
	 *            
	 * @param start
	 *            
	 */
	
	@RequestMapping("/getConnectLog")
	@ResponseBody
	public Response getConnectLog(String start_time,String end_time, int length, int start) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		System.out.println(start_time);
		System.out.println(end_time);
		if (StringUtils.isNotEmpty(start_time)) {
			paramMap.put("start_time", start_time);
		}

		if (StringUtils.isNotEmpty(end_time)) {
			paramMap.put("end_time", end_time);
		}
		
		int currentPage = start / length + 1;
		Page<ConnectLog> page = new Page<ConnectLog>(currentPage);
		page.setPageSize(length);
		Page<ConnectLog> result = this.getService().getConnectLogService().getConnectLogDao().getConnectLogList(page,
				paramMap);
		Response response = Response.OK();
		System.out.println("result.getTotalRow()" + result.getTotalRow());
		response.put("recordsTotal", result.getTotalRow());
		response.put("recordsFiltered", result.getTotalRow());
		response.setData(result.getResultRows());
		response.put("recordsFiltered", result.getTotalRow());
		response.put("draw", currentPage);
		response.put("total", result.getTotalRow());
		response.put("data", page);
		return response;
	}
	
	
	/**
	 * 通过时间删除连接记录  访问url 
	 * GateConnectLog/delete?paramJson={"start_time":"2017-08-01","end_time":"2017-09-01"}
	 * 
	 * @param paramJson
	 * {"start_time":"2017-08-01","end_time":"2017-09-01"} 
	 * 
	 * @return response
	 * {"total":2,"code":200,"message":"ok"} total为删除的条数，对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	@RequestMapping("/deletebytime")
	@ResponseBody
	public Response deleteConnectLogByTime(String start_time,String end_time) {
		List<ConnectLog> dellist=this.getService().getConnectLogService().getConnectLogDao().getConnectLogList(start_time, end_time);
		StringBuffer temp=new StringBuffer();
		for(ConnectLog log: dellist) {
			String id=log.getId();
			temp.append(id+",");
		}	
		this.deleteConnectLogById(temp.toString());
		Response response = Response.OK();
		return response;
	}
	
	/**
	 * 通过id删除连接记录  访问url 
	 * GateConnectLog/deletebyid?id=1111111,111111,111111,111111
	 * 
	 * @param paramJson
	 *id=1111111,111111,111111,111111 代表connectlog对应的id列表
	 * 
	 * @return response
	 * {"total":2,"code":200,"message":"ok"} total为删除的条数，对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 */
	
	@RequestMapping("/deletebyid")
	@ResponseBody
	public Response deleteConnectLogById(String id) {
		if(id==null||"".equals(id))
		{ 
			Response response=Response.PARAM_ERROR();
			return response;
		}
		//删除可以批量
		StringBuffer wrString=new StringBuffer();
		try {
			String delid=id;
			String[] ids=delid.split(",");
			for(String idsplit:ids) {
				ConnectLog connectLog=this.getService().getConnectLogService().getConnectLogDao().get(idsplit);
				String temp_str=connectLog.getConnect_time()+"    "+connectLog.getConnect_fail_reson()+"\\r\\n";
				wrString.append(temp_str);
			}
			
			HttpServletRequest request = this.getRequest();
			String logoPathDir = "/log/";
			/** 得到文件保存目录的真实路径* */
	        String logoRealPathDir = request.getSession().getServletContext()
	                .getRealPath(logoPathDir);
	        String today=DateUtils.dateToStr(new Date(),11);
	        String file_path =logoRealPathDir+today + ".log";// 构建文件名称
	        /** 根据真实路径创建目录* */
	        File logoSaveFile = new File(logoRealPathDir);
	        if (!logoSaveFile.exists())
	            logoSaveFile.mkdirs();
	        
	        Boolean is_write=FileUtils.write_file(wrString.toString(), file_path);
			System.out.println(is_write);
			int num=this.getService().getConnectLogService().getConnectLogDao().delSomeConnectLog(ids);
	        Response response = Response.OK();
	        response.put("total", num);
			return response;
		}catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

}
