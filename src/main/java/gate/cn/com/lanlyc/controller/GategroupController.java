package gate.cn.com.lanlyc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.Page;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.dto.GateGroupDto;
import gate.cn.com.lanlyc.core.po.AdminUser;
import gate.cn.com.lanlyc.core.po.GateGroup;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("/Gategroup")
public class GategroupController extends BaseController {
	@RequestMapping("/add")
	@ResponseBody
	/**
	 * 新增考勤设备分组
	 * 
	 * 访问url Gategroup/add?paramJson={"group_name":"a"}
	 * 
	 * @param user_name
	 *            用户名
	 * @param pass_word
	 *            密码
	 * @return
	 */
	public Response add(String paramJson) {
		JSONObject object = JSONObject.parseObject(paramJson);
		if (!object.containsKey("group_name")) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String group_name = object.getString("group_name");
		String welcome = object.getString("welcome");
		// 进行相应的数据验证
		if (welcome.equals("") || welcome == null) {
			Response response = Response.ERROR(-1, "欢迎语不能为空！");
			return response;
		}
		if (group_name.equals("") || group_name == null) {
			Response response = Response.ERROR(-1, "分区名称不能为空！");
			return response;
		} else {
			GateGroup outCome = this.getService().getGategroupservice().testGroup(group_name, null);
			if (outCome != null) {
				Response response = Response.ERROR(-1, "分区名称不能重复！");
				return response;
			}
		}
		Response response = Response.OK();
		GateGroup dt = new GateGroup();
		String id = DataUtils.getUUID();
		dt.setId(id);
		dt.setGroup_name(group_name);
		dt.setWelcome(welcome);
		this.addUserOperationlog("新增考勤设备分组：" + group_name);
		this.getService().getGategroupservice().getGategroupdao().add(dt);
		return response;
	}

	/**
	 * 
	 * 访问url Gategroup/list?paramJson={"length":"10","start":"0","draw":"0"}
	 * 
	 * @param paramJson
	 *            获取分组列表数据
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Response list(String paramJson, int length, int start, int draw) {
		JSONObject object = JSONObject.parseObject(paramJson);
		String keywords = "";
		if (object.containsKey("group_name")) {
			keywords = object.getString("group_name");
		}

		// String types=object.getString("type");

		String orderColumn = "";
		String orderDir = "";
		JSONArray filterColumn = null;
		JSONArray filter = null;
		if (object.containsKey("orderColumn")) {
			orderColumn = object.getString("orderColumn");
		}
		if (object.containsKey("orderDir")) {
			orderDir = object.getString("orderDir");
		}
		int currentPage = start / length + 1;
		Page<GateGroup> page = new Page<GateGroup>(currentPage);
		page.setPageSize(length);
		// Page<VersionDto>result=this.getService().getVersionService().getVersionListByPage(page,
		// keywords,type);

		Page<GateGroup> result = this.getService().getGategroupservice().getGategroupdao().getGateGroupList(page,
				keywords, orderColumn, orderDir, "");

		Response response = Response.OK();
		response.put("recordsTotal", result.getTotalRow());
		response.put("recordsFiltered", result.getTotalRow());
		response.setData(result.getResultRows());
		response.put("recordsFiltered", result.getTotalRow());
		response.put("draw", draw);
		response.put("total", result.getTotalRow());
		return response;
	}

	/**
	 * 修改分组名称 请求url
	 * update?paramJson={"group_name":"ali","id":"dfacb21fe4b34604b72381163d88699a"}
	 * 
	 * @param paramJson
	 *            paramJson={"group_name":"a","id":"xsdfa"}
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Response updates(String paramJson) {
		if (paramJson == null || "".equals(paramJson)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		JSONObject object = JSONObject.parseObject(paramJson);
		String id = object.getString("id");
		String group_name = object.getString("group_name");
		String welcome = object.getString("welcome");
		// 进行相应的数据验证
		if (welcome.equals("") || welcome == null) {
			Response response = Response.ERROR(-1, "欢迎语不能为空！");
			return response;
		}
		if (group_name.equals("") || group_name == null) {
			Response response = Response.ERROR(-1, "分区名称不能为空！");
			return response;
		} else {
			GateGroup outCome = this.getService().getGategroupservice().testGroup(group_name, id);
			if (outCome != null) {
				Response response = Response.ERROR(-1, "分区名称不能重复！");
				return response;
			}

		}

		GateGroup gategroup = new GateGroup();
		gategroup.setGroup_name(group_name);
		gategroup.setId(id);
		gategroup.setWelcome(welcome);
		this.addUserOperationlog("修改分组名称：" + group_name);
		int num = this.getService().getGategroupservice().getGategroupdao().update(gategroup);
		if (num == 0) {
			// 同时update大屏的欢迎 语

			Response response = Response.PARAM_ERROR();
			response.put("code", 300);
			response.put("message", "数据库操作错误,修改未完成");
			return response;
		} else {
			this.getService().getShowProjectPeopleCoutService().getShowProjectPeopleCoutDao().updateCout(welcome, id);
		}

		Response response = Response.OK();
		return response;
	}

	/**
	 * 删除分组
	 * 
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Response delete(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}
		String[] ids = id.split(",");
		int dt = 0;
		int num = 0;
		StringBuffer strb = new StringBuffer("删除分组：");
		for (String i : ids) {
			try {
				GateGroup group = this.getService().getGategroupservice().getGategroupdao().get(i);
				strb.append(group.getGroup_name() + " ");
			} catch (Exception e) {
				// TODO: handle exception
			}

			num = this.getService().getGategroupservice().getGategroupdao().delete(i);
		}

		if (num == 0) {
			Response response = Response.PARAM_ERROR();
			response.put("code", 300);
			response.put("message", "数据库操作失败,删除未完成");
			return response;
		}
		Response response = Response.OK();
		this.addUserOperationlog(strb.toString());
		return response;
	}

	/**
	 * 获取分组详情
	 * 
	 */
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping("/getinfo")
	@ResponseBody
	public Response getinfo(String id) {
		if (id == null || "".equals(id)) {
			Response response = Response.PARAM_ERROR();
			return response;
		}

		GateGroup gategroup = this.getService().getGategroupservice().getGategroupdao().get(id);
		Response response = Response.OK();
		response.setData(gategroup);
		return response;

	}

	/**
	 * 获取分组所有数据
	 * 
	 */
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping("/getAll")
	@ResponseBody
	public Response getAll() {
		List<GateGroup> gategroup = this.getService().getGategroupservice().getGategroupdao().getAll();
		Response response = Response.OK();
		response.setData(gategroup);
		return response;
	}
}
