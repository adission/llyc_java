package cn.com.lanlyc.core.util;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gnu.io.CommPortIdentifier;

/*
 * 工具类，为Main服务
 */
public class GateBoardUtil {
	private List<String> portList;
	private Map comOpreation=new HashMap<String,JavaRs>();//COM号和其操作类JavaRs一一对应
	public GateBoardUtil(){
		scanPorts();
	}
	
	/*
	 * 扫描本机的所有COM端口并创建对应的操作类JavaRs，信息保存在comOpreation中
	 * @luoying 2017-08-16 16:37:22
	 */
    public void scanPorts() {  
    	JavaRs tempRs=new JavaRs();
    	tempRs.scanPorts();
    	this.portList=tempRs.getPortList();
    	for (String name : portList) {
    		JavaRs jrs=new JavaRs(name);
    		this.comOpreation.put(name, jrs);
		}
    	 
    }

	
	/*
	 * 发送预处理，给数据帧加上CRC校验码
	 * @luoying 2017-08-25 09:57:54
	 */
	public static void addCRC(byte[] bs){
//		byte[] tempBs=Arrays.copyOf(bs,bs.length-4);
		byte[] tempBs=Arrays.copyOfRange(bs, 1,bs.length-4);
		
//		byte[] crcBs2=getCRCByte2(tempBs);//CRC16
		byte[] crcBs2=getCRCModbusByte2(tempBs);//CRC16Modbus
		
		bs[bs.length-3]=crcBs2[1];
		bs[bs.length-4]=crcBs2[0];
		
	}
	
	/*
	 * 根据传入的String卡号确定发送数据中的卡号byte表示
	 * 按照id+IC_id+face_id的顺序保存在长度为19的int数组中，缺少则补0
	 * 参数：1、id String 身份证号  长度=18或为空
	 * 	   2、IC_id IC卡号 长度<=10或为空
	 *     3、face_id人脸识别编号 长度<=10或为空
	 * @luoying 2017-08-17 17:27:52
	 */

	public static byte[] setCardsStyle(String id,String IC_id,String face_id) {		
		byte[] bs=new byte[19];
		for (byte b : bs) {
			b=0x00;
		}
		
		byte[] id_bs=null;//保存id转化后的信息
		byte[] IC_id_bs=null;//保存IC_id转化后的信息
		byte[] face_id_bs=null;//保存face_id转化后的信息
		
		//如果IC_id和face_id的长度不足10，则将它们前补0到10位
		StringBuffer sb=null;
		int len0=0;
		if((len0=10-IC_id.length())>0){
			sb=new StringBuffer();
			for(int i=0;i<len0;i++)
				sb.append("0");
			sb.append(IC_id);
			IC_id=sb.toString();
		}
		if((len0=10-face_id.length())>0){
			sb=new StringBuffer();
			for(int i=0;i<len0;i++)
				sb.append("0");
			sb.append(face_id);
			face_id=sb.toString();
		}
		
		if(!"".equals(id))
			id_bs=stringToBytes(id);
		if(!"".equals(IC_id))
			IC_id_bs=stringToBytes(IC_id);
		if(!"".equals(face_id))
			face_id_bs=stringToBytes(face_id);
		
		//按照id+IC_id+face_id的顺序保存在长度为19的byte数组中，缺少则补0
		if(id_bs!=null){
			for(int i=0,j=0;i<=8;i++,j++){
				bs[i]=id_bs[j];
			}
		}
		if(IC_id_bs!=null){
			for(int i=9,j=0;i<=13;i++,j++){
				bs[i]=IC_id_bs[j];
			}
		}
		if(face_id_bs!=null){
			for(int i=14,j=0;i<=18;i++,j++){
				bs[i]=face_id_bs[j];
			}
		}
		
		return bs;
		
	}
	
	/*将string型的id转化成byte数组
	*string中的两个字符对应一个byte
	*string字符个数为偶数
	*@luoying 2017-08-17 18:30:56
	*/
	public static byte[] stringToBytes(String s) {	
		if("".equals(s))
			return null;
		
		byte[] bs=new byte[s.length()/2];
		byte[] tempBs=new byte[s.length()];
		String[] strings=null;
		
		strings=s.split("");
		for (int i = 0; i < strings.length; i++) {
			if (i == strings.length - 1 && "X".equals(strings[i])) {
				tempBs[i]=0X0B;//如果身份证号的最后一位是X,则以十六进制的B表示
			} else
				tempBs[i] = (byte) Integer.parseInt(strings[i]);

		}
    	for(int i=0,j=0;i<bs.length;i++,j+=2){
    		int a=tempBs[j];
    		int b=tempBs[j+1];
    		int t=a*16+b;
    		int d=t<=127?t:t-256;
    		
    		bs[i]=(byte) (d);
    	}
    	
    	return bs;
	}
	
	/*	
	 * 根据传入的byte数组，提取出id String 身份证号、IC_id IC卡号、face_id人脸识别编号
	 * 参数:1、bs byte[] 处理过后的id信息
	 * 返回值：ids String[] 保存格式标准的三种id，ids[0]为身份证号、ids[1]为IC卡号、ids[2]为人脸识别编号，如果没有则为""
	 * @luoying 2017-08-18 14:59:58
	 */
	public static String[] getIds(byte[] bs) {
		String[] ids=new String[3];
		for (String string : ids) {
			string="";
		}
		
		byte[] bs1=new byte[9];//提取出保存id的部分
		byte[] bs2=new byte[5];//提取出保存IC_id的部分
		byte[] bs3=new byte[5];//提取出保存face_id的部分
		int count1=0;
		int count2=0;
		int count3=0;
		for(int i=0;i<bs.length;i++){
			if(i<=8){
				bs1[count1++]=bs[i];
			}else if(i>8 && i<=13){
				bs2[count2++]=bs[i];
			}else if(i>13 && i<=18){
				bs3[count3++]=bs[i];
			}
		}
		
		String id=bytesToString(bs1);
		String IC_id=bytesToString(bs2);
		String face_id=bytesToString(bs3);
		
		ids[0]=id;
		ids[1]=IC_id;
		ids[2]=face_id;
		
		return ids;
	}
	
	/*将byte数组转化成string型的id
	*一个byte对应string中的两个字符
	*@luoying 2017-08-18 15:14:59
	*/
	public static String bytesToString(byte[] bs) {
		StringBuilder s = new StringBuilder(""); 
		for(int i=0;i<bs.length;i++){
			int temp=bs[i]>=0?bs[i]:bs[i]+256;
			int int1,int2;
			int1=temp/16;//保存十六进制数的十位
			int2=temp%16;//保存十六进制数的个位
			s.append(int1);
			if(bs.length-1==i && 11==int2){
				s.append("X");
			}else
				s.append(int2);
		}
		
		return new String(s);
	}
	
	/*
	 * 将int数组转换成byte数组
	 * @huangju 2017-08-18 11:50:00
	 */
	public static int stringToInt_Authority(String authority) {
		BigInteger src = new BigInteger(authority, 2);//转换为BigInteger类型 
    	return src.intValue();
	}
    
	/*
	 * 将传入的byte转化成8位的01字符串，表示权限位
	 * 参数：b byte 表示权限位的字节
	 * @luoying 2017-08-21 11:00:52
	 */
	public static String byteToBinString(byte b) {
		int i=b>=0?b:b+256;
		StringBuffer temp=new StringBuffer();
		String s=Integer.toBinaryString(i);
		
		//补0
		for(int j=0;j<8-s.length();j++)
			temp.append("0");
		temp.append(s);
		
		return temp.toString();
	}
		
	/* 合并多个byte类型的数组
	 * 
	 * 参数：byte[] 数组
	 * 返回值：byte[]数组
	 * @huangju 2017-08-18 19:08:00
	 */
	public static byte[] concatByte(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
    
	/* 提取数据帧中的卡号和权限位
	 * @luoying 2017-08-23 17:28:08*/
	public static String[] getNoAndAuthByFrame(byte[] mesg) {
//		for (byte b : mesg) {
//			System.out.print(b+". ");
//		}
		
		
		byte[] tempBs=new byte[19];//保存单个数据帧中的19位卡号
		for(int j=0,k=3;j<19;j++,k++){
			tempBs[j]=mesg[k];
		}
		String[] ids=getIds(tempBs);//三个卡号
		String authority=byteToBinString((byte) mesg[22]);//权限位
		
		String[] info=new String[4];//保存单个人员的卡号和权限位
		for(int j=0;j<3;j++)
			info[j]=ids[j];
		info[3]=authority;
		
//		System.out.println(info[0]+","+info[1]+","+info[2]+","+info[3]);
		
		return info;

	}
	
	/*
	 * CRC16校验
	 * huangwencai 2017-08-23 18:15:31
	 */
	public static short crc16(byte[] addr,int num ,short crc)
	{
		for(int j=0;j<num;j++)
		{
			crc^=(addr[j]<<8);
			for(int i=0;i<8;i++)
			{
				
				if((crc & 0x8000)!=0)
				{
				crc=(short) ((crc<<1) ^ (0x1021)); 
				}
				else crc<<=1;
				
			}
			crc&=0XFFFF;
			
		}
		
		return crc;
	}
	
	/**
	 * crc16modbus校验
	 * @param bytes
	 * @return
	 */
	public static int getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        
//        System.out.println(Integer.toHexString(CRC));
        return CRC;
    }
	
	/*
	 * 将int转换成byte数组
	 * luoying 2017-08-23 18:15:56
	 */
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[2];

		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位

		return targets;
	}  
	
	/*
	 * 获取CRC16校验得到的两个字节
	 */
	public static byte[] getCRCByte2(byte[] sourceBytes) {		
		int a=crc16(sourceBytes, sourceBytes.length, (short) 0xffff);
		byte[] targetBytes=int2byte(a);
		
//		for (byte b : targetBytes) {
//			System.out.println(b);
//		}
		
		return targetBytes;
	}
	
	/*
	 * 获取crc16modbus校验得到的两个字节
	 */
	public static byte[] getCRCModbusByte2(byte[] sourceBytes) {		
		int a=getCRC(sourceBytes);
		byte[] targetBytes=int2byte(a);
		
//		for (byte b : targetBytes) {
//			System.out.println(b);
//		}
		
		return targetBytes;
	}
	
	/*
	 * 根据数据帧中的CRC字节码判断接受的数据帧是否正确
	 * @luoying 2017-08-24 17:09:14
	 */
	public static boolean isFrameRight(byte[] bs){
		if(null==bs)
			return false;
		
		//数据帧长度不对
		if(bs.length<3)
			return false;
		if(bs[2]+7!=bs.length)
			return false;
		
		byte[] sourceBytes=Arrays.copyOfRange(bs, 1,bs.length-4);
//		byte[] targetBytes=getCRCByte2(sourceBytes);//CRC16
		byte[] targetBytes=getCRCModbusByte2(sourceBytes);//crc16modbus
		
		if(bs[bs.length-3]!=targetBytes[1] || bs[bs.length-4]!=targetBytes[0])
			return false;//校验不通过
		
		System.out.println("CRC true");
		return true;
	}
	
	/**
	 * 得到帧中的卡号，时间，类型，通道号
	 * @author huangju 2017-08-25 11:40:00
	 * @param byte[] //帧中卡号和时间十进制类型
	 * @return String[] //具体的卡号和时间
	 */
	public static String[] getCardIDAndTime(byte[] a) {
		//提取出卡号id
		int len=(int) a[2];
		//System.out.println("卡号时间长度："+len);
		if(7+len!=a.length)
		{
			System.out.println("帧出错");
			for (byte b : a) {
				System.out.print(b+" ");
			}
			return null;
		}
		byte[] temp = new byte[len-7];
		for(int i=3;i<temp.length+3;i++) {
			temp[i-3]=a[i];
		}
		// 处理后的id字段
		String tempid = bytesToString(temp);
		int type=tempid.length()==18?0:1;
		//System.out.println("tempid:"+tempid);
		
		// 处理时间字段
		int[] temp2 = new int[6];
		for (int i = 0; i < 6; i++) {
			temp2[i] = a[i-4+len]>=0?a[i-4+len]:a[i-4+len]+256;
		}
		// 临时保存处理后的时间字段
		String time = timeTypeChange(temp2);
		byte gateNo = a[a[2] + 7 - 5];
		// byte gateNo=reciBs[14];
//		int gateno = gateNo > 48 ? gateNo - 48 : 0;
		int gateno = gateNo;
		String[] result= {tempid,time,String.valueOf(type),String.valueOf(gateno)};
		//System.out.println(time);
		
		return result;
		
	}

	
	/**
	 * 时间选择，确定刷卡时间是用上传的时间还是系统当前时间
	 * @author huangju 2017-08-23 16:56:00
	 * @param DATE1
	 * @param DATE2
	 * @return 差距小于五分钟返回DATE1，差距大于五分钟返回DATE2，
	 */
	public static String select_time(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            //差距在5分钟之内
            if (dt1.getTime() - dt2.getTime()<(5*60000)&&dt1.getTime() - dt2.getTime()>(-60000*5)) {
                return DATE1;
           
            } else {
                return DATE2;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return DATE2;
	}

	/**
	 * 将时间字符串转化为byte类型数组
	 * @author huangju 2017-08-18 16:36:00
	 * @param time “2017-08-18 17:48:00”类型的字符串
	 * @return byte[] 长度为6
	 */
	public static byte[] stringToIntsTime(String time) {
		int temp[]=new int[3];
		byte res[]=new byte[3];
		temp[0]=Integer.parseInt(time.substring(2, 4));//年
		temp[1]=Integer.parseInt(time.substring(5, 7));//月
		temp[2]=Integer.parseInt(time.substring(8, 10));//日
		
		for (int i=0;i<temp.length;i++) {
			int t=temp[i]>=0?temp[i]:temp[i]+256;
			res[i]=(byte) t;
		}
		
		return res;
	}
	
	/**
	 * 将十进制类型时间数组转化为时间字符串
	 * @author huangju 2017-08-18 16:36:00
	 * @param a int[] 长度为6
	 * @return “2017-08-18 16:36:00”类型的字符串
	 */
	public static String timeTypeChange(int[] a) {
		String[] temp=new String[6];
		for(int i =0;i<temp.length;i++) {
			if(a[i]<10) {
				temp[i]="0"+String.valueOf(a[i]);
			}
			else {
				temp[i]=String.valueOf(a[i]);
			}
		}
		String result="20"+temp[0]+"-"+temp[1]+"-"+temp[2]+" "+temp[3]+":"+temp[4]+":"+temp[5];
		return result;
	}
	
	public static Boolean comArray(byte[] a, byte[] b) {
		if(a.length==b.length) {
			for(int i=0;i<a.length;i++){
				if(a[i]!=b[i]) {
					return false;
				}
			}
			return true;
		}
		return  false;

	}
	
	
	/**
	 * 拼接byte[]成为String
	 *
	 */
	public static String bytes2hex01(byte[] bytes)  
    {   
        BigInteger bigInteger = new BigInteger(1, bytes);  
        return bigInteger.toString(16);  
    }
	
	
	/**
	 * 去除重复帧
	 * @param list
	 * @return
	 */
	public static List<byte[]> removeDuplicateWithOrder(List<byte[]> list) {  
		List<Integer> indexList=new ArrayList<>();
		for(int i=0;i<list.size();i++){
			for(int j=i+1;j<list.size();j++){
				if(Arrays.equals(list.get(i), list.get(j)))
					indexList.add(i);
			}
		}
		
		for (Integer integer : indexList) {
			int i=integer;
			list.remove(i);
		}
		
		return list;
	}  
	
    public List<String> getPortList() {
		return portList;
	}

	public void setPortList(List<String> portList) {
		this.portList = portList;
	}

	public Map getComOpreation() {
		return comOpreation;
	}

	public void setComOpreation(Map comOpreation) {
		this.comOpreation = comOpreation;
	}  
}
