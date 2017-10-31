/* ----------------------------------------------------------
 * 文件名称：FXMLDocumentController.java
 * 
 * 作者：杨威
 * 
 * 微信：yangwei728118
 * 
 * 
 * 开发环境：
 *      NetBeans 8.1
 *      JDK 8u92
 *      
 * 版本历史：
 *      V1.0    2014年09月04日
 *              接收卡点数据
------------------------------------------------------------ */

package gate.cn.com.lanlyc.core.service;

import Com.FirstSolver.Security.Utils;
import Com.FirstSolver.Splash.FaceId_Item;
import Com.FirstSolver.Splash.ISocketServerThreadTask;
import Com.FirstSolver.Splash.NetworkStreamPlus;
import Com.FirstSolver.Splash.TcpListenerPlus;
import io.netty.util.ByteProcessor.IndexOfProcessor;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.fxml.Initializable;

/**
 *
 * @author yangwei
 */
public class FaceListenTest implements Initializable, ISocketServerThreadTask {
    
    private final String DeviceCharset = "GBK";
    
    private boolean IsServerRunning = false;
    
    public TcpListenerPlus TcpServer = null;
   
    private static List<Map<String,String>> check_list=new ArrayList<Map<String,String>>();
    
    private static List<Map<String,String>> user_list=new ArrayList<Map<String,String>>();
    
    FaceService fs = new FaceService();
    
    private FaceListenTest() 
    {
    	
    }
    private static FaceListenTest facelisten=null;
    public static FaceListenTest getInstance() 
    {
    	if(facelisten==null) 
    	{
    		facelisten=new FaceListenTest();
    	}
    	return facelisten;
    }
    /*
     * 这个是存所有的人脸的考勤数据
     * 
     */
    public static List<Map<String,String>> getcheck_list() 
    {
    	return check_list;
    }
    
    
    /*
     * 这个是存所有用户的相关信息
     * 
     */
    public static List<Map<String,String>> getuser_list() 
    {
    	return user_list;
    }
    
    
    //监听时的初始化方法
    public void init_listen() throws IOException, Exception {
    	FaceListenTest ft = new FaceListenTest();
    	
    	ft.test1();
    }
    
    
	//开始监听时的操作
    private void test1() throws IOException, Exception {
        if(IsServerRunning)//先将之前的服务给关掉
        {
            if(TcpServer != null)
            {
                TcpServer.close();
                TcpServer = null;
            }
            IsServerRunning = false;
            System.out.println("---------------------");
        }
        else
        {   
        	String address = "";
        	try
        	{
        		String ip = InetAddress.getLocalHost().toString();
        		int i = ip.indexOf("/");
        		address = ip.substring(i+1);
        		System.out.println("本机的IP = " + ip.substring(i+1));
        	} catch (UnknownHostException e)
        	{
        		e.printStackTrace();
        	}
        	
            // 创建侦听服务器（获取数据是 端口号、ip、是否通信加密）
        	TcpServer = new TcpListenerPlus(Integer.parseInt("9922"), 0, InetAddress.getByName(address), false);
            
            // 设置通信线程任务委托
            TcpServer.ThreadTaskDelegate = this;
            
            // 开启侦听服务
            TcpServer.StartListenThread(null, 0, 0);
            
            IsServerRunning = true;
//            buttonStartListener.setText("停止侦听");            
        }
    }
    
    @Override
    public void OnServerTaskRequest(NetworkStreamPlus stream) throws Exception {
        // 设备特殊通信密码
        // stream.setSecretKey(textFieldSecretKey.getText());  // 注意：密码要和设备通信密码一致
        
        String SerialNumber = null;    // 设备序列号
        while(true)
        {   
            try {
                // 读取数据
                String Answer = stream.Read(DeviceCharset);
                
//                // 显示读取信息
//                Platform.runLater(() -> {
////                    textAreaAnswer.appendText(Answer + "\r\n");
//                });
                
                System.out.println(Answer + "\r\n");
                
                if(Answer.startsWith("PostRecord"))
                {   // 提取序列号并保存
                    SerialNumber = FaceId_Item.GetKeyValue(Answer, "sn");
                    
                    // 答复已准备好接收考勤记录
                    if (true)
                    {
                        stream.Write("Return(result=\"success\" postphoto=\"true\")", DeviceCharset);
                    }
                    else
                    {
                        stream.Write("Return(result=\"success\" postphoto=\"false\")", DeviceCharset);
                    }
                }
                else if(Answer.startsWith("Record"))
                {   // 读取考勤记录   
                	// SerialNumber = FaceId_Item.GetKeyValue(Answer, "sn");
                	Map<String,String > log_map = new HashMap<String,String>();
                	log_map.put("sn", SerialNumber);
                	log_map.put("id",FaceId_Item.GetKeyValue(Answer, "id"));
                	log_map.put("name",FaceId_Item.GetKeyValue(Answer, "name"));
                	log_map.put("time",FaceId_Item.GetKeyValue(Answer, "time"));
                	log_map.put("photo",FaceId_Item.GetKeyValue(Answer, "photo"));
                	check_list.add(log_map);
                    // 服务器回应
                    stream.Write("Return(result=\"success\")", DeviceCharset);
                }
                else if(Answer.startsWith("PostEmployee"))
                {   // 准备上传人员信息
                	 SerialNumber = FaceId_Item.GetKeyValue(Answer, "sn");
                    // 服务器回应
                    stream.Write("Return(result=\"success\")", DeviceCharset);
                }                
                else if(Answer.startsWith("Employee"))
                {   // 读取人员信息
                    
                	Map<String,String > user_map = new HashMap<String,String>();
                	
//                	
                	System.out.println("-------------1");
                	System.out.println(FaceId_Item.GetKeyValue(Answer, "face_data"));
                	System.out.println("---------------------2");
                	
                	System.out.println(fs.GetKeyValue(Answer, "face_data"));
                	String[] face_datelist = Answer.split("face_data=\"");
                	System.out.println(face_datelist.length);
                	
                	String face_date_all = "";
                	
                	if(!(face_datelist.length==1)) {//代表没有相应的人脸数据
						for (int i = 0; i <= face_datelist.length - 1; i++) {

							if (i == 0) {
								continue;
							} else if (i == face_datelist.length - 1) {
								face_date_all += face_datelist[i].substring(0, face_datelist[i].length() - 2);
							} else {
								face_date_all += face_datelist[i].substring(0, face_datelist[i].length() - 3) + ",";
							}
						}

                	}
                	
                	
                	user_map.put("sn", SerialNumber);
                	user_map.put("id",FaceId_Item.GetKeyValue(Answer, "id"));
                	user_map.put("name",FaceId_Item.GetKeyValue(Answer, "name"));
                	user_map.put("authority",FaceId_Item.GetKeyValue(Answer, "authority"));
                	user_map.put("card_num",FaceId_Item.GetKeyValue(Answer, "card_num"));
                	user_map.put("check_type",FaceId_Item.GetKeyValue(Answer, "check_type"));
                	user_map.put("opendoor_type",FaceId_Item.GetKeyValue(Answer, "opendoor_type"));
                	
                	user_map.put("face_data",face_date_all);
                	user_list.add(user_map);
                	
                	System.out.println();
                	
                	System.out.println("------------------------3");
                			
//                	服务器回应
                    stream.Write("Return(result=\"success\")", DeviceCharset);
                }
                else if (Answer.startsWith("GetRequest"))
                {   // 下发命令
                    Platform.runLater(() -> {
                        String Command = "GetDeviceInfo()";
                        if (!Utils.IsNullOrEmpty(Command))
                        {
                            try 
                            {
                                stream.Write(Command, DeviceCharset);
                            }
                            catch (Exception ex)
                            {
                                
                            }
                            
//                            textAreaCommand.clear();
                        }
                    });
                }
                else if(Answer.startsWith("Quit"))
                {   // 结束会话
                    break;
                }
            }
            catch (Exception ex)
            {
                break;  // 连接断开
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 设置服务器地址
        try
        {
            List<String> IPList = new LinkedList<>();
            Enumeration<NetworkInterface> InterfaceList = NetworkInterface.getNetworkInterfaces();
            while (InterfaceList.hasMoreElements())
            { 
                NetworkInterface iFace = InterfaceList.nextElement();
                if(iFace.isLoopback() || iFace.isVirtual() || iFace.isPointToPoint() || !iFace.isUp()) continue;
                                
                Enumeration<InetAddress> AddrList = iFace.getInetAddresses(); 
                while (AddrList.hasMoreElements())
                { 
                    InetAddress address = AddrList.nextElement(); 
                    if ((address instanceof Inet4Address) || (address instanceof Inet6Address))
                    {
                        IPList.add(address.getHostAddress());                
                    }
                } 
            }
            
            if(!IPList.isEmpty()) 
            {
//                comboBoxServerIP.setItems(FXCollections.observableList(IPList));
//                comboBoxServerIP.setValue(IPList.get(0));
            }            
        }
        catch (SocketException ex) 
        {
            // 异常处理
        }
    }
}
