package cn.com.lanlyc.base.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;




public class FileUtils {
    /**
     * 获取所有包的所有资源文件，支持表达式
     * @param locationPattern 例如 ： classpath:spring/*.xml 按优先级加载 classpath*:spring/*.xml 对所有jar扫描
     * @return the corresponding Resource objects
     * @throws IOException in case of I/O errors
     */
    public static Resource[] getResources(String locationPattern) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(locationPattern);
        return resources;
    }
    
    /**
     * 获取所有包的所有资源文件，支持表达式
     * @param locationPattern 例如 ： classpath:spring/*.xml 按优先级加载 classpath*:spring/*.xml 对所有jar扫描
     * @return the corresponding Resource object
     * @throws IOException in case of I/O errors
     */
    public static Resource getResource(String locationPattern) throws IOException {
        Resource[] resources = getResources(locationPattern);
        if (resources.length > 0) {
            return resources[0];
        } else {
            return null;
        }
    }
    
    /**
     * 写入内容到指定文件
     * 
     */
    public static boolean write_file(String content,String  filepath) 
    {
    	FileOutputStream out = null;   

        FileOutputStream outSTr = null;   

        BufferedOutputStream Buff=null;   

        FileWriter fw = null;   
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(filepath, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

//        try {   
//
//            out = new FileOutputStream(new File(filepath));   
//
//            long begin = System.currentTimeMillis();   
//
//            
//
//             out.write(content.getBytes());   
//
//            
//
//            out.close();   
//
//            long end = System.currentTimeMillis();   
//
//            System.out.println("FileOutputStream执行耗时:"+ (end - begin) + " 豪秒");   
//        } catch (Exception e) {
//			// TODO: handle exception
//		}
//            

    
    
    	return true;
    }
}
