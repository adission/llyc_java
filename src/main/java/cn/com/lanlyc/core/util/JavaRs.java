package cn.com.lanlyc.core.util;

/* 单个串口通讯类*/
import java.awt.BorderLayout;  
import java.awt.Button;  
import java.awt.Color;  
import java.awt.Font;  
import java.awt.GridLayout;  
import java.awt.Image;  
import java.awt.TextArea;  
import java.awt.TextField;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;  
import java.util.List;  
import java.util.TooManyListenersException;  
  
import gnu.io.CommPortIdentifier;  
import gnu.io.NoSuchPortException;  
import gnu.io.PortInUseException;  
import gnu.io.SerialPort;  
import gnu.io.SerialPortEvent;  
import gnu.io.SerialPortEventListener;  
import gnu.io.UnsupportedCommOperationException;

import javax.imageio.ImageIO;  
import javax.swing.JComboBox;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JOptionPane;  
import javax.swing.JPanel;  
import javax.swing.SwingConstants;  
import javax.swing.border.EmptyBorder;  
  
public class JavaRs implements SerialPortEventListener {  
  
    /** 
     * JDK Serial Version UID 
     */  
    private static final long serialVersionUID = -7270865686330790103L;        
      
    protected CommPortIdentifier portId;  
      
    protected Enumeration<?> ports;  
      
    protected List<String> portList = new ArrayList<String>();  //保存本机的所有COM端口
  
    protected SerialPort serialPort;  
  
    protected OutputStream outputStream = null;   
  
    protected InputStream inputStream = null;   

	private String portname;
	
	
	/*设置串口初始化参数，依次是波特率，数据位，停止位和校验*/
	private int rate=9600;
	private int data=SerialPort.DATABITS_8;
	private int stop=SerialPort.STOPBITS_1;
	private int parity=SerialPort.PARITY_NONE;
	
	
	private boolean portFlag=false;//串口是否正常打开的信号，0否1是，默认为0没有正常开启
	
	/*保存各种操作的接受到的数据*/
	private String reciHandMes=""; //以String形式保存接受到的握手返回信息
	private byte[] mesg2 = null; //设置闸机 的返回数据
	private byte[] mesg3 = null; //查询通道状态 的返回数据
	private byte[] mesg4 = null; //批量下传授权信息 的返回数据
	private byte[] mesg5 = null; //批量上传授权信息 的单个返回数据
	private List<byte[]> mesg5List = new ArrayList<>();//批量上传授权信息缓存区，保存所有上传的授权信息 ，避免接收和处理数据的速率不一致
	private byte[] mesg6 = null; //增加单个授权信息 的返回数据
	private byte[] mesg7 = null; //删除单个授权信息 的返回数据
	private byte[] mesg8 = null; //查询是否授权信息 的返回数据
	private byte[] mesg9 = null; //替换一个授权信息 的返回数据
	private byte[] mesg10 = null; //清除所有授权信息 的返回数据
	private byte[] mesg11 = null; //上传一个刷卡信息 的返回数据
	private byte[] mesg12 = null; //查询刷卡信息 的返回数据
	private List<byte[]> mesg12List = new ArrayList<>();
	private byte[] mesg13 = null; //手工打开通道 的返回数据
	private byte[] mesg14 = null; //查询授权总数 的返回数据
	private byte[] mesg15 = null; //设置控制器时间 的返回数据	
	private byte[] mesg16 = null; //查询刷卡信息 的返回数据
	private List<byte[]> mesg16List = new ArrayList<>();
	
	private List<byte[]> bufs=new ArrayList<>();//串口监听接收数据的缓冲区，避免接收和处理数据的速率不一致
	private List<Integer> bufsLen=new ArrayList<>();//保存bufs中对应每个byte数组中储存数据的长度
	
	private List<byte[]> checkBytes=new ArrayList<>();//保存所有未处理的实时刷卡记录的帧，处理一条删除一条
	private List<byte[]> lostcheckBytes=new ArrayList<>();
	
	//保存所有数据帧中的所有字节
	private List<Byte> allBs=new ArrayList<>();//将其中完整的数据帧提取出来后，删除
	private boolean recivFlag=false;
	private int i=0;
	private int j=0;
	private byte[] tempi=new byte[250];//保存上一条处理之后的帧
	private byte[] tempj=new byte[250];//保存上一条处理之后的帧
	
	private String synOpenFlag="";//打开串口的同步信号量

	/** 
     * 默认构造函数 
     */  
    public JavaRs() {}  
	
    /** 带参构造函数
     * 通过参数确定操作的串口*/
    public JavaRs(String portname) {
    	this.portname=portname;
    }  
    
    /**
     * 扫描本机的所有COM端口
     */
    public void scanPorts() {  
        //获得当前所有可用串口
        Enumeration<CommPortIdentifier> en = CommPortIdentifier.getPortIdentifiers();  
        CommPortIdentifier portId;  
        while(en.hasMoreElements()){  
            portId = (CommPortIdentifier) en.nextElement();  
            if(portId.getPortType() == CommPortIdentifier.PORT_SERIAL){  
                String name = portId.getName();  
                if(!portList.contains(name)) {  
                    portList.add(name); 
                }  
            }  
        }  
        if(null == portList   
                || portList.isEmpty()) {  
            System.out.println("未找到可用的串行端口号!");
        }  
    }
     
    /** 
     * 打开串行端口 	 
     * @since 2012-3-23 上午12:03:07 
     */  
    public boolean openSerialPort() {
    	//防止多个线程同时打开同一个COM口造成的java虚拟机报错
    	synchronized (synOpenFlag) {
    		// 获取要打开的端口  
            try {  
                portId = CommPortIdentifier.getPortIdentifier(portname);  
            } catch (NoSuchPortException e) {  
                System.out.println("抱歉,没有找到"+portname+"串行端口号!");
                return false;  
            }  
            // 打开端口  
            try {          	
            	serialPort = (SerialPort) portId.open("llyc_gate", 2000); //第一个参数为应用程序名；第二个参数是在端口打开时阻塞等待的毫秒数
                System.out.println(portname+"串口已经打开!");
                portFlag=true;
            } catch (PortInUseException e) {  
                System.out.println(portname+"端口已被占用,请检查!");
                return false;  
            }  
              
            // 设置端口参数  
            try {   
                serialPort.setSerialPortParams(rate,data,stop,parity);  
            } catch (UnsupportedCommOperationException e) {  
                System.out.println(e.getMessage()); 
                return false;  
            }  
      
            // 打开端口的IO流管道   
            try {   
                outputStream = serialPort.getOutputStream();   
                inputStream = serialPort.getInputStream();   
            } catch (IOException e) {  
            	System.out.println(e.getMessage());  
            	return false;  
            }   
      
            // 给端口添加监听器  
            try {   
                serialPort.addEventListener(this);   
            } catch (TooManyListenersException e) {  
                System.out.println(e.getMessage());
                return false;  
            }   
      
            serialPort.notifyOnDataAvailable(true);   
            return true;
    	}
    }   
      
    /** 
     * 给串行端口发送数据 
     * @since 2012-3-23 上午12:05:00 
     */  
    public void sendDataToSeriaPort(byte[] bs) {   
    	if(portFlag){
    		try {   
            	outputStream.write(bs);   
                outputStream.flush();  
      
            } catch (IOException e) {   
                System.err.println(e.getMessage());
            }  
    	}
    	else{
    		System.out.println("串口没有正常打开，不能发送数据");
    	}
    	
         
    }   
       
    
    /** 
     * 关闭串行端口 
     * @since 2012-3-23 上午12:05:28 
     */  
    public boolean closeSerialPort() {   
        try {   
            if(outputStream != null)  
                outputStream.close();  
            if(serialPort != null)  
                serialPort.close();   
            serialPort = null;  
            System.out.println(portname+"串口已经关闭!");
            
            portFlag=false;
            
            return true;
        } catch (Exception e) {   
            System.out.println(e.getMessage());
            return false;
        }   
    }     
  
    /** 
     * 端口事件监听 
     */  
    public void serialEvent(SerialPortEvent event) {

		if (portFlag) {
			switch (event.getEventType()) {
			case SerialPortEvent.BI:/*Break interrupt,通讯中断*/				
			case SerialPortEvent.OE:/*Overrun error，溢位错误*/
			case SerialPortEvent.FE:/*Framing error，传帧错误*/
			case SerialPortEvent.PE:/*Parity error，校验错误*/
			case SerialPortEvent.CD:/*Carrier detect，载波检测*/
			case SerialPortEvent.CTS:/*Clear to send，清除发送*/
			case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/
			case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/
				break;
			case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
				byte[] readBuffer = new byte[128];

				try {
					while (inputStream.available() > 0) {
						int nbyte = inputStream.read(readBuffer);
						for (int i=0;i<nbyte;i++) {
							byte b=readBuffer[i];
							System.out.print(b+"、");
						}
						System.out.println();
						
						synchronized (this) {
							if (nbyte > 0) {

								bufs.add(readBuffer);
								bufsLen.add(nbyte);
							}
						}
					}
				} catch (IOException e) {
					System.out.println(e.getMessage());
					this.closeSerialPort();
				}
				this.recivFlag = true;
			}
		}
		else {
			this.closeSerialPort();
		}
		
    } 
    
    /*
	 * 提取出bufs中的数据，并保存在相应变量中
	 * @luoying 2017-08-24 14:05:51
	 */
	public void delBufs() {
		List<byte[]> listBs = new ArrayList<>();
		
//		if(!this.recivFlag)
//			return;
		if (0 == bufs.size() || 0==this.bufsLen.size())
			return;
		// 提取出bufs保存的数据并转化成List<byte[]>类型
		for (int i = 0; i < this.bufs.size() && i < this.bufsLen.size(); i++) {	
			byte[] bs=null;
			int len=0;
			byte[] targetBs=null;
			try {
				bs = this.bufs.get(0);
				this.bufs.remove(0);//将处理过的缓存区清空
				len = bufsLen.get(0);
				targetBs = new byte[len];
				this.bufsLen.remove(0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(null==bs){
				System.out.println("null");
				return;
			}

			for (int j = 0; j < len && j<bs.length; j++) {//提取实际的字节数
				targetBs[j] = bs[j];
			}

			listBs.add(targetBs);
		}

		for (byte[] bs : listBs) {
			for (byte b : bs) {
				allBs.add(b);
			}
		}

		// 根据帧头帧尾将每条数据帧分开保存
		int lastEnd = -1;// 保存最后一条完整数据帧的最后一个字节在allBs中的序号
		List<byte[]> frameList = new ArrayList<>();// 分开保存所有数据帧
		List<Byte> frame = null;// 当前帧
		int frameLen=-1;//当前帧的长度
		int startIndex=-1;//当前帧头的序号
		int byteIndex=-1;//当前帧的当前字节的序号
		boolean frameRigth=true;//当前帧是否正确
		for (int i = 0; i < allBs.size(); i++) {
			if(!frameRigth){//当前帧不正确，删除不正确的部分字节（从当前帧头A5到下一个A5)
				for(int j=startIndex+1;j<allBs.size();j++){
					if(allBs.get(j)==(byte) 0xa5)
					{
						i=j;
						break;
					}
				}
				frame = null;// 帧出现无头异常时，不本条帧数据
				startIndex=-1;// 判断上条帧是否结束的标志，用于校验码中出现A5的情况
				frameRigth=true;
			}
			
			if ((byte) 0xa5 == allBs.get(i)) {// A5
				if(-1==startIndex){// 帧头
					frame = new ArrayList<>();// 新增一个帧
					frame.add(allBs.get(i));
					if(allBs.size()-1<i+2)//如果当前帧的长度码没有储存在缓存区allBs中，则暂时不继续提取帧
						break;
					frameLen=7+allBs.get(i+2)>=7?7+allBs.get(i+2):7+allBs.get(i+2)+256;
					frameLen--;
					startIndex=i;
					byteIndex=0;
				}else{//校验码出现A5
					if(frameLen-->0){
						frame.add(allBs.get(i));
						byteIndex++;
						if(1==byteIndex){//操作码
							int op=allBs.get(i)>=0?allBs.get(i):256+allBs.get(i);
							if(op<=0 || op>22){//操作码不合法
								frameRigth=false;
							}
						}
					}
				}
					
			} else {
				if (null != frame) {
					if(frameLen-->0){
						frame.add(allBs.get(i));
						byteIndex++;
						if(1==byteIndex){//操作码
							int op=allBs.get(i)>=0?allBs.get(i):256+allBs.get(i);
							if(op<=0 || op>22){//操作码不合法
								frameRigth=false;
							}
						}
					}
					if(0==frameLen){//正确长度的帧获取完
						//判断帧尾是否正确
						if (0x0d == allBs.get(i - 1) && 0x0a == allBs.get(i)) {// 帧尾，将当前帧保存
							// 将当前帧转换成byte[]
							byte[] frameByte = new byte[frame.size()];
							for (int j = 0; j < frame.size(); j++)
								frameByte[j] = frame.get(j);

							frameList.add(frameByte);
							lastEnd = i;

							frame = null;// 帧出现无头异常时，不本条帧数据
							startIndex=-1;// 判断上条帧是否结束的标志，用于校验码中出现A5的情况
						}
						else
							frameRigth=false;
					}
					
				}
			}
			

		}
		
		//将allBs中最后一条完整数据帧以及其前面的字节删除，保存最后的错误数据帧，可能会留下最后一条不完整的数据帧（下一次接收可以组合）
		for(int i=0;i<=lastEnd;i++)
			allBs.remove(0);
		
		for (byte[] f : frameList) {
			for (byte b : f) {
				System.out.print(b + " ");
			}
			System.out.println();
		}

		// 通过CRC16校验判断获得的数据帧是否完整
		for (int i = frameList.size() - 1; i >= 0; i--) {
			byte[] f = frameList.get(i);
			if (!GateBoardUtil.isFrameRight(f))
				frameList.remove(i);// 校验不通过，删除该数据帧
		}
		 
		for (byte[] f : frameList) {
			setDifferReciInfo(f);
		}
		
		this.recivFlag=false;
	}
    
    /* 判断数据帧并储存到相应的变量中
     * 参数：bs byte[] 接受到的数据
     * @luoying 2017-08-17 10:52:51*/
	public void setDifferReciInfo(byte[] bs) {
		
		if (null == bs || bs.length < 7)
			return;
		switch (bs[1]) {
		case 0x01:
			this.reciHandMes = "1";// 握手返回消息不为""表示收到握手信息
			break;
		case 0x02:
			this.setMesg2(bs);
			break;
		case 0x03:
			this.setMesg3(bs);
			break;
		case 0x04:
			this.setMesg4(bs);
			break;
		case 0x05://得到一条批量上传的授权信息
			try {
				Thread.sleep(275);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] send5= {(byte)0xa5,0x05,0x02,(byte)0xf0,0x00,0x00,0x00,0x0d,0x0a};
			if(bs[3]==(byte)0xb1) {
				send5[3]=(byte)0xb1;
			}
			send5[4]=(byte)i;
			//GateBoardUtil.addCRC(send);
			this.sendDataToSeriaPort(send5);
			i++;
			if(i==256) {
				i=0;
			}
			this.mesg5List.add(bs);
			break;
		case 0x06:
			this.setMesg6(bs);
			break;
		case 0x07:
			this.setMesg7(bs);
			break;
		case 0x08:
			this.setMesg8(bs);
			break;
		case 0x09:
			this.setMesg9(bs);
			break;
		case 0x10:
			this.setMesg10(bs);
			break;
		case 0x11:
			byte[] send11= {(byte)0xa5,0x11,0x01,(byte)0xf0,0x00,0x00,0x0d,0x0a};
			GateBoardUtil.addCRC(send11);
			this.sendDataToSeriaPort(send11);
			System.out.println("数据回应send");
			this.checkBytes.add(bs);
			this.setMesg11(bs);
			break;
		case 0x12:
			byte[] send12= {(byte)0xa5,0x12,0x02,(byte)0xf0,0x00,0x00,0x00,0x0d,0x0a};
			if(bs[3]==(byte)0xb1) {
				send12[3]=(byte)0xb1;
				this.mesg12List.add(bs);
			}
			byte[] temp12=bs;
			if(GateBoardUtil.comArray(tempi, temp12)==false) {
				i++;
				this.mesg12List.add(bs);
			}
			if(i==256) {
				i=0;
			}
			send12[4]=(byte)i;
			GateBoardUtil.addCRC(send12);
			this.sendDataToSeriaPort(send12);
			System.out.println("send");
			tempi=bs;
			break;
		case 0x13:
			this.setMesg13(bs);
			break;
		case 0x14:
			this.setMesg14(bs);
			break;
		case 0x15:
			this.setMesg15(bs);
			break;
		case 0x16:
			byte[] send16= {(byte)0xa5,0x16,0x02,(byte)0xf0,0x00,0x00,0x00,0x0d,0x0a};
			if(bs[3]==(byte)0xb1) {
				send16[3]=(byte)0xb1;
				this.mesg16List.add(bs);
			}
			
			byte[] temp=bs;
			if(GateBoardUtil.comArray(tempj, temp)==false) {
				j++;
				this.mesg16List.add(bs);
			}
			if(j==256) {
				j=0;
			}
			send16[4]=(byte)j;
			GateBoardUtil.addCRC(send16);
			this.sendDataToSeriaPort(send16);
			tempj=bs;
			this.getLostcheckBytes().add(bs);
			this.setMesg16(bs);
			break;

//			byte[] send16= {(byte)0xa5,0x16,0x01,(byte)0xf0,0x00,0x00,0x0d,0x0a};
//			GateBoardUtil.addCRC(send16);
//			this.sendDataToSeriaPort(send16);
//			this.getLostcheckBytes().add(bs);
//			this.setMesg16(bs);
//			System.out.println("send");
//			break;
		default:
			break;
		}
	}
    
	
    public String getReciHandMes() {
		return reciHandMes;
	}


	public void setReciHandMes(String reciHandMes) {
		this.reciHandMes = reciHandMes;
	}



	public byte[] getMesg2() {
		return mesg2;
	}

	public void setMesg2(byte[] mesg2) {
		this.mesg2 = mesg2;
	}

	public byte[] getMesg3() {
		return mesg3;
	}

	public void setMesg3(byte[] mesg3) {
		this.mesg3 = mesg3;
	}

	public byte[] getMesg4() {
		return mesg4;
	}

	public void setMesg4(byte[] mesg4) {
		this.mesg4 = mesg4;
	}

	public byte[] getMesg5() {
		return mesg5;
	}

	public void setMesg5(byte[] mesg5) {
		this.mesg5 = mesg5;
	}

	public List<byte[]> getMesg5List() {
		return mesg5List;
	}

	public void setMesg5List(List<byte[]> mesg5List) {
		this.mesg5List = mesg5List;
	}

	public byte[] getMesg6() {
		return mesg6;
	}

	public void setMesg6(byte[] mesg6) {
		this.mesg6 = mesg6;
	}

	public byte[] getMesg7() {
		return mesg7;
	}

	public void setMesg7(byte[] mesg7) {
		this.mesg7 = mesg7;
	}

	public byte[] getMesg8() {
		return mesg8;
	}

	public void setMesg8(byte[] mesg8) {
		this.mesg8 = mesg8;
	}

	public byte[] getMesg9() {
		return mesg9;
	}

	public void setMesg9(byte[] mesg9) {
		this.mesg9 = mesg9;
	}

	public byte[] getMesg10() {
		return mesg10;
	}

	public void setMesg10(byte[] mesg10) {
		this.mesg10 = mesg10;
	}

	public byte[] getMesg11() {
		return mesg11;
	}

	public void setMesg11(byte[] mesg11) {
		this.mesg11 = mesg11;
	}

	public byte[] getMesg12() {
		return mesg12;
	}

	public void setMesg12(byte[] mesg12) {
		this.mesg12 = mesg12;
	}

	public List<byte[]> getMesg12List() {
		return mesg12List;
	}

	public void setMesg12List(List<byte[]> mesg12List) {
		this.mesg12List = mesg12List;
	}

	public byte[] getMesg13() {
		return mesg13;
	}

	public void setMesg13(byte[] mesg13) {
		this.mesg13 = mesg13;
	}

	public byte[] getMesg14() {
		return mesg14;
	}

	public void setMesg14(byte[] mesg14) {
		this.mesg14 = mesg14;
	}

	public byte[] getMesg15() {
		return mesg15;
	}

	public void setMesg15(byte[] mesg15) {
		this.mesg15 = mesg15;
	}

	public List<byte[]> getBufs() {
		return bufs;
	}

	public void setBufs(List<byte[]> bufs) {
		this.bufs = bufs;
	}

	public String getPortname() {
		return portname;
	}

	public void setPortname(String portname) {
		this.portname = portname;
	}

	public List<byte[]> getCheckBytes() {
		return checkBytes;
	}

	public void setCheckBytes(List<byte[]> checkBytes) {
		this.checkBytes = checkBytes;
	}

	public List<Byte> getAllBs() {
		return allBs;
	}

	public void setAllBs(List<Byte> allBs) {
		this.allBs = allBs;
	}

	public boolean isRecivFlag() {
		return recivFlag;
	}

	public void setRecivFlag(boolean recivFlag) {
		this.recivFlag = recivFlag;
	}

	public byte[] getMesg16() {
		return mesg16;
	}

	public void setMesg16(byte[] mesg16) {
		this.mesg16 = mesg16;
	}

	public List<byte[]> getMesg16List() {
		return mesg16List;
	}

	public void setMesg16List(List<byte[]> mesg16List) {
		this.mesg16List = mesg16List;
	}

	public List<String> getPortList() {
		return portList;
	}

	public void setPortList(List<String> portList) {
		this.portList = portList;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public List<byte[]> getLostcheckBytes() {
		return lostcheckBytes;
	}

	public void setLostcheckBytes(List<byte[]> lostcheckBytes) {
		this.lostcheckBytes = lostcheckBytes;
	}
	
	
	
     
  
}