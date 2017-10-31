package cn.com.lanlyc.base.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;


public class FormFileUpload {
	
//	private static Logger log = Logger.getLogger(FormFileUpload.class);
	
	/**
	 * 上传
	 * @param 	formFile	Struts2中表单文件类
	 * @param	rootPath	根目录
	 * @param 	childDir	文件存放路径
	 * @return	String		文件存储后用于存储数据库中的路径
	 */
	public static String upload(File file,String fileFileName,String rootPath,String childDir)
	{
		try
		{
			if(file == null || fileFileName == null) 
				return "";
			
			//获取上传文件扩展名
			String extName = fileFileName.substring(fileFileName.lastIndexOf("."));
			
			//创建硬盘目录
			String diskDirectory = rootPath + childDir + "/";
			File diskFile = new File(diskDirectory);
			if(!diskFile.exists()) 
				diskFile.mkdirs();
			
			//文件名
			String fileName = System.currentTimeMillis() + extName;
			
			//开始上传文件
			String savePath = diskDirectory + fileName;
			InputStream is = new FileInputStream(file);
			OutputStream os = new FileOutputStream(savePath);
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, length);
			}
			is.close();
			os.flush();
			os.close();
			
			return "/upload"+childDir + "/" + fileName;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 
	 * @param file  文件
	 * @param path  
	 * @param dataurl  保存在数据库里的路径
	 * @return
	 * path+dataurl  存放服务器的路径
	 */
	public static void upload(MultipartFile mfile,String path,String dataurl)
	{
        File targetFile = new File(path, dataurl); 
        if(!targetFile.exists()){ //不存在就创建此目录 
            targetFile.mkdirs();  
        }  
        	//保存  
        	try {  
        		mfile.transferTo(targetFile);
        	} catch (Exception e) {  
        		e.printStackTrace();  
        	} 
        	
	}
	
	/**
	 * 方法描述：保存活动图片
	 * @param upFile			上传的图片对象
	 * @param path				服务器上的图片保存路径
	 * vti-fei	2014-1-3		上午2:11:33
	 */
	public static boolean uploadFile(File upFile, String path) {
		
		if (null == upFile) {
			return false;
		}
		
		/**
		 * 先将原文件清除
		 */
		ImageSlimUtil.deleteFilePathImg(path);
		
		File fileTmp = new File(path);
		File dir = fileTmp.getParentFile();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			FileOutputStream out = new FileOutputStream(fileTmp);
			
			DataInputStream in = new DataInputStream(new FileInputStream(upFile));
	        int bytes = 0;
	        byte[] bufferOut = new byte[1024];
	        while ((bytes = in.read(bufferOut)) != -1) {
	            out.write(bufferOut, 0, bytes);
	        }
	        in.close();
	        out.flush();
	        out.close();
	        return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		File ring = new File("E:\\Music\\爱乐团 - 秋天的童话.mp3");
//		File ring = new File("F:\\2013-12-06\\alarmclock\\tmp.aac");
		
//		String fileName = ring.getName();
//		String fileType = fileName.substring(fileName.length()-fileName.lastIndexOf("."));
		uploadFile(ring, "D:\\vti_work\\naoyinao\\WebRoot\\ring\\"+ System.currentTimeMillis()+".aac");
	}
	
	public static void test(StringBuilder str) {
		str.append("111");
	}
}
