package cn.com.lanlyc.base.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.io.Resource;


public class ConstantUtils {
    /** 资源文件夹 */
    private static final String LINK_PATH = "classpath*:conf/constant-*-link.properties";
    
    /** 存放在constant资源目录下所有properties文件数据集合 */
    private static final Map<String, String> CONSTANT = new HashMap<String, String>();
    
    /**
     * 获取常量属性
     * @param key 键
     * @return 常量未配置时，返回空
     */
    public static String getConstant(String key) {
        return CONSTANT.get(key);
    }
    
    /**
     * 加载资源文件到常量
     * @param path 文件路径 必须是properties文件
     */
    public static void load(String path) {
        try {
            // 获取资源目录所有文件
            Resource[] resources = FileUtils.getResources(path);
            // 遍历资源文件并加载到内存
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[resources.length-i-1];
                // 获取资源文件名
                String filename = resource.getFilename();
                filename = filename.substring(0, filename.lastIndexOf("."));
                Properties properties = new Properties();
                properties.load(resource.getInputStream());
                Set<Object> keys = properties.keySet();
                // 遍历每一个资源文件的键值队
                for (Object keyObj : keys) {
                    String key = keyObj.toString();
                    // 文件名.key=value格式
                    String filenameAndKey = String.format("%s.%s", filename, key);
                    String value = properties.getProperty(key).trim();
                    CONSTANT.put(filenameAndKey, properties.getProperty(key, ""));
                    // key=value格式
                    CONSTANT.put(key, properties.getProperty(key, ""));
                }
            }
        } catch (IOException e) {
            System.out.println("无法加载资源文件失败");
        }
    }
    
    // 加载所有常量到内存
    static {
        try {
            // 获取资源常量所有link文件
            Resource[] resources = FileUtils.getResources(LINK_PATH);
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[resources.length-i-1];
                Properties properties = new Properties();
                properties.load(resource.getInputStream());
                for (Object key : properties.keySet()) {
                    load(properties.getProperty(key.toString()));
                }
            }
        } catch (IOException e) {
            System.out.println("无法加载资源文件失败");
        }
    }
}
