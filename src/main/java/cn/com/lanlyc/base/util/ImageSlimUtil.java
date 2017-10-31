package cn.com.lanlyc.base.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 生成缩略图类
 * 
 * @author ChenGuangQuan
 * @version 1.0
 */
public class ImageSlimUtil {

	public static void main(String arg[]) {
		try {
			// System.out.print(ImageSlimUtil.GetImageStr());
//		    String strImg = "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";
//		    GenerateImage(strImg, "D:\\wangyc.jpg");

			String outPath = "D:\\test2.jpg";
			
			String imgStr="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" + 
					"AABMDgAAUuL//70kAABt4f//";
//			imgStr=imgStr.replaceAll(regex, replacement)
			GenerateImage(imgStr, outPath);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述：删除指定目录filePath对应的文件或文件夹下内容
	 * @param filePath			文件路径
	 * vti-fei	2013-12-31		下午2:44:36
	 */
	public static void deleteFilePathImg(String filePath) {
		File local = new File(filePath);
		if (local.isFile())//如果是文件，删除文件
			local.delete();
		if (local.isDirectory()) {//如果是目录，将目录清空
			File[] lists = local.listFiles();
			for (File f : lists)
				f.delete();

			local.delete();
		}
	}

	public static boolean GenerateImage(String imgStr, String filePath) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		
		/**
		 * 清除原文件
		 */
		deleteFilePathImg(filePath);
		
		BASE64Decoder decoder = new BASE64Decoder();
		OutputStream out = null;
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			out = new FileOutputStream(filePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static String GetImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile = "D:\\vti_work\\qutees\\WebRoot\\user_logo\\img.gif";// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
	
	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author: 
	 * @CreateTime: 
	 * @return
	 */
	public static String getImageStr(String imgFile) {
	    InputStream inputStream = null;
	    byte[] data = null;
	    try {
	        inputStream = new FileInputStream(imgFile);
	        data = new byte[inputStream.available()];
	        inputStream.read(data);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // 加密
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(data);
	}
	/**
	 *获取指定图片URL base64字符串
	 * 
	 */
	public static String getImageFileBase64code(String urlStr,HttpServletRequest request) 
	{
	   String logoPathDir = "/html/upload";
	   String base64code="";
        /** 得到文件保存目录的真实路径* */
       String savePath = request.getSession().getServletContext()
                .getRealPath(logoPathDir);
		if(urlStr.contains("http://")) 
		{
			try {
				String file_path=downLoadFromUrl(urlStr,savePath);
				File img = new File(file_path);
				FileOutputStream fos = new FileOutputStream(file_path);
				ImgTools.thumbnail_w_h(img, 50, 0, fos);
				base64code=getImageStr(file_path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else
		{
			if(urlStr.contains(logoPathDir)) 
			{
				String file_path=savePath+urlStr.substring(12);
				File img = new File(file_path);
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(file_path);
					ImgTools.thumbnail_w_h(img, 50, 0, fos);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				base64code=getImageStr(file_path);
				
			} 
		}
		return base64code;
	}
	
	/** 
     * 从网络Url中下载文件 
     * @param urlStr 
     * @param fileName 
     * @param savePath 
     * @throws IOException 
     */  
    public static String  downLoadFromUrl(String urlStr,String savePath) throws IOException{  
	 String suffix = urlStr.substring(urlStr.lastIndexOf("."));
        /** 使用UUID生成文件名称* */
        String fileName =DataUtils.randomString(5)+"_"+ (new Date().getTime()) + suffix;// 构建文件名称
        /** 构建文件保存的目录* */
//        String logoPathDir = "/html/upload";
//        /** 得到文件保存目录的真实路径* */
//        String savePath = request.getSession().getServletContext()
//                .getRealPath(logoPathDir);
        /** 根据真实路径创建目录* */
        
       
        
    	
        URL url = new URL(urlStr);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        conn.setConnectTimeout(3*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //获取自己数组  
        byte[] getData = readInputStream(inputStream);      
  
        //文件保存位置  
        File saveDir = new File(savePath);  
        if(!saveDir.exists()){  
            saveDir.mkdir();  
        }  
        File file = new File(saveDir+File.separator+fileName);      
        FileOutputStream fos = new FileOutputStream(file);       
        fos.write(getData);   
        if(fos!=null){  
            fos.close();    
        }  
        if(inputStream!=null){  
            inputStream.close();  
        }  
  
        return saveDir+File.separator+fileName;
        
  
    }  
  
  
  
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }
    
   
	
}
