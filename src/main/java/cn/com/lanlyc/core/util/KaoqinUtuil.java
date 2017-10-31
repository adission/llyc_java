package cn.com.lanlyc.core.util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Com.FirstSolver.Splash.FaceId;
import Com.FirstSolver.Splash.FaceIdAnswer;
import Com.FirstSolver.Splash.FaceId_ErrorCode;

public class KaoqinUtuil {
	private  static String DeviceCharset = "GBK";   
	//TCP 连接通讯设备并执行命令
	public static String connect(String DeviceIP,String DevicePort,String SecretKey,String TestCommand)
	{
		FaceId tcpClient;
		String answer="";
		try {
			tcpClient = new FaceId(DeviceIP, Integer.parseInt(DevicePort));
			// 设置通信密码
            tcpClient.setSecretKey(SecretKey);   // 注意：密码和设备通信密码一致            
            
            FaceIdAnswer output = new FaceIdAnswer();
            FaceId_ErrorCode ErrorCode = tcpClient.Execute(TestCommand, output, DeviceCharset);
            tcpClient.close();
            if (ErrorCode.equals(FaceId_ErrorCode.Success))
            {
            	answer=output.answer;
                System.out.println(output.answer);
            }
            else
            {
            	System.out.println("TcpClientDemo");

            }
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接设备失败!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接设备失败!");
		}
		return answer;
	}
	
	//转换考勤设备获取数据的数据
	public List<Map> result(String answer)
	{
		List<Map> datas = new ArrayList<Map>();  
		if(answer.length()>8&&answer.isEmpty()!=true)
		{
			String str2=answer.substring(7,answer.length()-1);
			 String[] str1 = str2.split("\""+" ");  
			 
			 
			 for(String list : str1){  
				 if(!list.equals(" ")||list.length()>0||list.isEmpty()!=true)
				 {
					 
					 String[] tmp_str2 = list.split("=");  
					 Map<String,String> key_val=new HashMap<String,String>();
					 if(tmp_str2.length>0)
					 {
						 key_val.put(tmp_str2[0].trim(), tmp_str2[1].replace("\"", "").toString());
						 
				          datas.add(key_val);  
					 }
				 }

				
		           
		    }  
		}

		 
//		System.out.println(datas.toString());
		return datas;
	}
}
