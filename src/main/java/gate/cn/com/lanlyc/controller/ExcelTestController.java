package gate.cn.com.lanlyc.controller;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.base.util.ExcelUtils;
import cn.com.lanlyc.core.util.Response;
import gate.cn.com.lanlyc.core.po.WorkersTypes;

@Controller
@RequestMapping("/ExcelTest")
public class ExcelTestController extends HttpServlet {
	
	
//	@SuppressWarnings("rawtypes")
	@RequestMapping("/add")
	@ResponseBody
	public void add(HttpServletRequest request, HttpServletResponse response) 
	{
//		File file = new File("C://Users//jike//Desktop//book.jpg");

	      response.setContentType("octets/stream");

	      response.addHeader("Content-Disposition", "attachment;filename=test.xls");

	      //测试图书

	      ExcelUtils<List> ex = new ExcelUtils<List>();

	      String[] headers = { "图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN",

	            "图书出版社", "封面图片" ,"老人再莎莉哦啊"};

	      List<List> dataset = new ArrayList<List>();

	      try {

//	         BufferedInputStream bis = new BufferedInputStream(
//
//	               new FileInputStream(file));

//	         byte[] buf = new byte[bis.available()];

//	         while ((bis.read(buf)) != -1) {

	            //将图片数据存放到缓冲数组中

//	         }

	         dataset.add(new ArrayList(){{
	             add("string1");
	             add("string2");
	             add("stringN");
	             add("stringN");
	             add("stringN");
	             add("stringN");
	             add("stringN");
	             add("stringN");
	             add("stringN");
//	             add(buf);
	         }});

	         dataset.add(new ArrayList(){{
	             add("string1");
	             add("string2");
	             add("stringN");
	         }});

	         dataset.add(new ArrayList(){{
	             add("string1");
	             add("string2");
	             add("stringN");
	         }});

	         dataset.add(new ArrayList(){{
	             add("string1");
	             add("string2");
	             add("stringN");
	         }});

	         OutputStream out = response.getOutputStream();

	         ex.exportExcel(headers, dataset, out);

	         out.close();

	         System.out.println("excel导出成功！");

	      } catch (FileNotFoundException e) {

	         // TODO Auto-generated catch block

	         e.printStackTrace();

	      } catch (IOException e) {

	         // TODO Auto-generated catch block

	         e.printStackTrace();

	      }
	}	
	
}
