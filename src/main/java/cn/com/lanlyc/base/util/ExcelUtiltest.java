package cn.com.lanlyc.base.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExcelUtiltest {
	//相应的导出excel的方法
	public static void Test(HttpServletRequest request, HttpServletResponse response, String[] strarry, List<List> dataset1) {
		// File file = new File("C://Users//jike//Desktop//book.jpg");

		response.setContentType("octets/stream");

		response.addHeader("Content-Disposition", "attachment;filename=test.xlsx");

		// 测试图书

		ExcelUtils<List> ex = new ExcelUtils<List>();

		String[] headers = strarry;

		List<List> dataset = dataset1;

		try {

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
