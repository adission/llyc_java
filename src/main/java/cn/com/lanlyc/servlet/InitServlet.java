package cn.com.lanlyc.servlet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.lanlyc.base.util.ConstantUtils;
import cn.com.lanlyc.core.util.ClockMonitor;
import cn.com.lanlyc.core.util.GateBoardController;
import cn.com.lanlyc.core.util.GetLostMonitor;
import cn.com.lanlyc.core.util.WgRunnable;
import gate.cn.com.lanlyc.controller.GateController;
import gate.cn.com.lanlyc.core.po.GateList;
import gate.cn.com.lanlyc.core.service.FaceListenTest;
import gate.cn.com.lanlyc.core.service.ServiceContainer;
import gate.cn.com.lanlyc.core.service.WgListener;

/**
 * 初始化servlet
 * @author
 */
@Component("InitServlet")
public class InitServlet extends HttpServlet {
    
    private static final long serialVersionUID = 712033593536300766L;
    
    @Resource
    private ServiceContainer myservice;
//	public static InitServlet testUtils;
    /**
     * 获取 业务类容器
     * @return
     */
//    protected ServiceContainer getService() {
//        return testUtils.myservice;
//    }

    
    public void destroy() {
        super.destroy();
    }
//    @PostConstruct
//    public void inits() {    
//        testUtils = this;
//    } 
    
    @Override
//    @PostConstruct
    public void init() throws ServletException {
//    	testUtils = this;
    	
        // TODO Auto-generated method stub
        super.init();

        String strcmd = "cmd /c start d:/start_webservice/webservice/start.bat";  //调用准备好的bat文件，启动紫峰webservice
//        run_cmd(strcmd);  //调用上面的run_cmd方法执行操作
        System.out.println("-----考勤管理系统后台启动成功-----");
        
        FaceListenTest ft = FaceListenTest.getInstance();
        
        
		this.getAllUsefulPort();
        
        
        try {
			ft.init_listen();
			System.out.println("-----人脸识别监听器启动成功-----");
			new Thread(new WgRunnable(), "WgRunnable").start();
			
			System.out.println("-------------微耕监听器启动成功---------");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /**
	 * 获取正在使用的控制板的端口号，打开控制板并保存在GateBoardController的usefulPortList中
	 */
	public void getAllUsefulPort(){		
		GateBoardController m = GateBoardController.getInstance();
		
		List<String> comList=myservice.getGatelistservice().getGatelistdao().getAllDiffLlycCOM();//所有正在使用的不重复的COM号
		List<String> usefulPortList = new ArrayList<>();//正在使用的且已连接的COM号
		ConcurrentHashMap<String, String> usefulPortList2=new ConcurrentHashMap<>();
		for (String com : comList) {
			if(m.getPortList().contains(com)){
				usefulPortList.add(com);
				m.getIdleFlag().put(com, true);
				m.getSetTimeFlag().put(com, false);
				
				List<String> idList=myservice.getGatelistservice().getGatelistdao().getAllNoByCOM(com);
				for (String string : idList) {
					usefulPortList2.put(com, string);
				}
			}
		}
		m.setUsefulPortList(usefulPortList);
		m.setUsefulPortList2(usefulPortList2);
		// COM口初始状态全部为关闭
		ConcurrentHashMap<String,Boolean> openFlag=new ConcurrentHashMap<>();//每个COM口是否打开的标志
		for (String port : usefulPortList) {
			openFlag.put(port, false);
		}
		m.setOpenFlag(openFlag);
		
		for (String port : m.getUsefulPortList2().keySet()) {
			m.setGate_id(m.getUsefulPortList2().get(port));
			m.openAPort(port);
			
		}
		
//		m.confirmIdleFlag();
//		for(Map.Entry<String, Boolean> entry:m.getIdleFlag().entrySet()){
//			if(entry.getValue())
//				myservice.getGateboradservie().getLostCheckLog(entry.getKey());
//		}
	}
	//启动紫峰webservice发布程序
	public void run_cmd(String strcmd) {
		//
        Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
        try {
        	
            ps = rt.exec(strcmd);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
            ps.waitFor();  //等待子进程完成再往下执行。
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int i = ps.exitValue();  //接收执行完毕的返回值
        if (i == 0) {
            System.out.println("执行完成.");
        } else {
            System.out.println("执行失败.");
        }

        ps.destroy();  //销毁子进程
        ps = null;   
    }	
		
}
