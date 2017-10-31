package gate.cn.com.lanlyc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.ExcelUtiltest;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.po.GateOperationlogView;
import gate.cn.com.lanlyc.core.po.OperationLog;
import net.sf.json.JSONObject;

/**
 * 
 * @author yangwei time 2017-8-24 14:10:52 Function 实现管理员相关日志的操作
 * 
 */

@Controller
@RequestMapping("/OperationLog")
public class OperationLogController extends BaseController {

	@RequestMapping("/addLog")
	@ResponseBody
	/**
	 * 新增管理员相关的操作日志
	 * 
	 * 访问url OperationLog/addLog?paramJson =
	 * {"operation_action":"操作行为","operation_desc":"操作描述","operation_user":"操作人id"}
	 * 
	 * @param paramJson
	 *            {"operation_action":"操作行为","operation_desc":"操作描述","operation_user":"操作人id"}
	 *            对应人员类别的名称
	 * 
	 * @return response {"code":200,"message":"ok"} 对应返回数据的各种状态码和信息（案例为成功返回的数据）
	 * 
	 */
	public Response addLog(String paramJson) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);

			String operation_action = json.getString("operation_action");// 操作行为
			String operation_desc = json.getString("operation_desc");// 操作描述
			String operation_user = this.getCurrentuserid();// 操作人id、
			this.getService().getOperationlogService().addlog(operation_action, operation_desc, operation_user);

			Response response = Response.OK();
			return response;
		} catch (Exception e) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
	}

	@RequestMapping("/queryLog")
	@ResponseBody
	/**
	 * 获取所有管理员列表 访问url OperationLog/queryLog
	 * 
	 * @param paramJson(以下是相应的json字段)
	 * 
	 * @param_current_page(传int) 当前页码
	 * @param page_num（传int）
	 *            每页的数据的条数
	 * @param operation_time
	 *            操作时间（对操作时间进行查询）
	 * @param operation_desc
	 *            操作描述（对操作描述进行模糊查询）
	 * @param operation_user
	 *            操作人id（对操作人id进行模糊查询）
	 * @param operation_action
	 *            操作人行为（对操作人行为进行模糊查询）
	 * @param order
	 *            根据某个字段进行数据排序
	 * @param order_by
	 *            正序还是反序
	 * @return
	 */
	public Response queryLog(String paramJson, int length, int start, int draw) {

		JSONObject json = JSONObject.fromObject(paramJson);
		int currentPage = start / length + 1;
		Page<GateOperationlogView> page = new Page<GateOperationlogView>(currentPage);
		page.setPageSize(length);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (json.containsKey("start_time")) {
			String start_time = json.getString("start_time");
			if (StringUtils.isNotEmpty(start_time)) {
				paramMap.put("start_time", start_time);
			}
		}
		if (json.containsKey("end_time")) {
			String end_time = json.getString("end_time");
			if (StringUtils.isNotEmpty(end_time)) {
				paramMap.put("end_time", end_time);
			}
		}

		if (json.containsKey("operation_desc")) {
			String operation_desc = json.getString("operation_desc");
			if (StringUtils.isNotEmpty(operation_desc)) {
				paramMap.put("operation_desc", operation_desc);
			}
		}
		if (json.containsKey("name")) {
			String operation_user = json.getString("name");
			if (StringUtils.isNotEmpty(operation_user)) {
				paramMap.put("operation_user", operation_user);
			}
		}
		if (json.containsKey("operation_action")) {
			String operation_action = json.getString("operation_action");
			if (StringUtils.isNotEmpty(operation_action)) {
				paramMap.put("operation_action", operation_action);
			}
		}

		String order = json.containsKey("order") ? json.getString("order") : null;
		String order_by = json.containsKey("order_by") ? json.getString("order_by") : null;

		Page<GateOperationlogView> result = this.getService().getOperationlogService().getOperationlogdao()
				.queryList(page, paramMap, order, order_by);

		Response response = Response.OK();
		response.put("recordsTotal", result.getTotalRow());
		response.put("recordsFiltered", result.getTotalRow());
		response.setData(result.getResultRows());
		response.put("recordsFiltered", result.getTotalRow());
		response.put("draw", draw);
		response.put("total", result.getTotalRow());

		return response;

	}

	@RequestMapping("/queryLogExcel")
	@ResponseBody
	/**
	 * 获取所有管理员列表 访问url OperationLog/queryLog
	 * 
	 * @param paramJson(以下是相应的json字段)
	 * 
	 * @param_current_page(传int) 当前页码
	 * @param page_num（传int）
	 *            每页的数据的条数
	 * @param operation_time
	 *            操作时间（对操作时间进行查询）
	 * @param operation_desc
	 *            操作描述（对操作描述进行模糊查询）
	 * @param operation_user
	 *            操作人id（对操作人id进行模糊查询）
	 * @param operation_action
	 *            操作人行为（对操作人行为进行模糊查询）
	 * @param order
	 *            根据某个字段进行数据排序
	 * @param order_by
	 *            正序还是反序
	 * @return
	 */
	public void queryLogExcel(HttpServletRequest request, HttpServletResponse response, String paramJson) {
		try {
			JSONObject json = JSONObject.fromObject(paramJson);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			if (json.containsKey("operation_time")) {
				String operation_time = json.getString("operation_time");
				if (StringUtils.isNotEmpty(operation_time)) {
					paramMap.put("operation_time", operation_time);
				}
			}
			if (json.containsKey("operation_desc")) {
				String operation_desc = json.getString("operation_desc");
				if (StringUtils.isNotEmpty(operation_desc)) {
					paramMap.put("operation_desc", operation_desc);
				}
			}
			if (json.containsKey("operation_user")) {
				String operation_user = json.getString("operation_user");
				if (StringUtils.isNotEmpty(operation_user)) {
					paramMap.put("operation_user", operation_user);
				}
			}
			if (json.containsKey("operation_action")) {
				String operation_action = json.getString("operation_action");
				if (StringUtils.isNotEmpty(operation_action)) {
					paramMap.put("operation_action", operation_action);
				}
			}

			String order = json.containsKey("order") ? json.getString("order") : null;
			String order_by = json.containsKey("order_by") ? json.getString("order_by") : null;

			List<GateOperationlogView> page = this.getService().getOperationlogService().getOperationlogdao()
					.queryListExcel(paramMap, order, order_by);
			String[] headers = { "时间", "用户名", "行为", "操作记录" };

			List<List> dataset = new ArrayList<List>();
			for (GateOperationlogView user_info : page) {
				dataset.add(new ArrayList() {
					{
						add(user_info.getOperation_time() != null ? user_info.getOperation_time() : "");
						add(user_info.getUser_name() != null ? user_info.getUser_name() : "");
						add(user_info.getOperation_action() != null ? user_info.getOperation_action() : "");
						add(user_info.getOperation_desc() != null ? user_info.getOperation_desc() : "");

					}
				});
			}
			ExcelUtiltest.Test(request, response, headers, dataset);

		} catch (Exception e) {

		}
	}
}
