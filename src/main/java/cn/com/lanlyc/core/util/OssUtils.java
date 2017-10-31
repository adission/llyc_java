package cn.com.lanlyc.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.Callback.CalbackBodyType;

import cn.com.lanlyc.base.util.ConstantUtils;

import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

/**
 * 阿里OSS工具类
 * @author Jerry Zhou
 */
public class OssUtils {
    
    
    /**
     * 上传文件
     * @param file 上传文件 (MultipartFile)
     * @param fileName 上传文件名
     * @param bucketName ossBucket名
     * @param folderName 文件夹名
     * @param folderBoolean 是否需要创建文件夹
     * @param callbackBoolean 是否回调
     * @param callbackUrl 回调URL
     * @param callbackBody 回调参数
     */
    public static String uploadFile(MultipartFile file, String fileName, String bucketName, String folderName, boolean folderBoolean, boolean callbackBoolean, String callbackUrl,
                                    String callbackBody) {
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(ConstantUtils.getConstant("Endpoint"), ConstantUtils.getConstant("accessKeyId"), ConstantUtils.getConstant("accessKeySecret"));
        // 上传文件流
        try {
            if (folderBoolean) {
                folderName = folderName + "/";
                ossClient.putObject(bucketName, folderName, new ByteArrayInputStream(new byte[0]));
                ossClient.getObject(bucketName, folderName);
            }
            if (callbackBoolean) {
                System.out.println("callbackBody--" + callbackBody);
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + fileName, file.getInputStream());
                Callback callback = new Callback();
                callback.setCallbackUrl(callbackUrl);
                callback.setCallbackHost("oss-cn-shanghai.aliyuncs.com");
                callback.setCallbackBody(callbackBody);
                callback.setCalbackBodyType(CalbackBodyType.JSON);
                
                putObjectRequest.setCallback(callback);
                
                // ossClient.putObject(putObjectRequest);
                
                PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
                // 读取上传回调返回的消息内容
                
                System.out.println("上传成功：" + putObjectResult.getETag());
                
                byte[] buffer = new byte[1024];
                putObjectResult.getCallbackResponseBody().read(buffer);
                // 一定要close，否则会造成连接资源泄漏
                
                System.out.println("--putObjectResult.getETag()-" + putObjectResult.getETag());
                System.out.println("--putObjectResult.getCallbackResponseBody()-" + putObjectResult.getCallbackResponseBody());
                System.out.println("--putObjectResult.getRequestId()-" + putObjectResult.getRequestId());
                
                putObjectResult.getCallbackResponseBody().close();
                
                // 关闭client
                ossClient.shutdown();
                return putObjectResult.getETag();
            } else {
                ossClient.putObject(bucketName, folderName + fileName, file.getInputStream());
                // 关闭client
                ossClient.shutdown();
                return "1";
            }
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "1";
    }
    
    /**
     * 上传文件
     * @param file 上传文件
     * @param fileName 上传文件名
     * @param bucketName ossBucket名
     * @param folderName 文件夹名
     * @param folderBoolean 是否需要创建文件夹
     * @param callbackBoolean 是否回调
     * @param callbackUrl 回调URL
     * @param callbackBody 回调参数
     */
    public static void uploadFile(File file, String fileName, String bucketName, String folderName, boolean folderBoolean, boolean callbackBoolean, String callbackUrl, String callbackBody) {
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(ConstantUtils.getConstant("Endpoint"), ConstantUtils.getConstant("accessKeyId"), ConstantUtils.getConstant("accessKeySecret"));
        // 上传文件流
        InputStream inputStream;
        try {
            if (folderBoolean) {
                folderName = folderName + "/";
                ossClient.putObject(bucketName, folderName, new ByteArrayInputStream(new byte[0]));
                ossClient.getObject(bucketName, folderName);
            }
            if (callbackBoolean) {
                System.out.println("callbackBody--" + callbackBody);
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + fileName, file);
                Callback callback = new Callback();
                callback.setCallbackUrl(callbackUrl);
                callback.setCallbackHost("oss-cn-shanghai.aliyuncs.com");
                callback.setCallbackBody(callbackBody);
                callback.setCalbackBodyType(CalbackBodyType.JSON);
                
                putObjectRequest.setCallback(callback);
                
                PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
                byte[] buffer = new byte[1024];
                putObjectResult.getCallbackResponseBody().read(buffer);
                putObjectResult.getCallbackResponseBody().close();
                System.out.println(putObjectResult.toString());
            } else {
                inputStream = new FileInputStream(file);
                ossClient.putObject(bucketName, folderName + fileName, inputStream);
            }
            // 关闭client
            ossClient.shutdown();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    /**
     * 下载文件
     * @param fileName 下载文件名
     * @param filePath 保存路径
     * @param bucketName ossBucket名
     */
    public static void downloadFile(String fileName, String filePath, String bucketName) {
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(ConstantUtils.getConstant("Endpoint"), ConstantUtils.getConstant("accessKeyId"), ConstantUtils.getConstant("accessKeySecret"));
        ossClient.getObject(new GetObjectRequest(bucketName, fileName), new File(filePath));
        ossClient.shutdown();
    }
    
    /**
     * 根据文件路径删除OSS服务器上的文件
     * @param filePath 文件路径
     * @param bucketName ossBucket名
     */
    public static void deleteFile(String filePath, String bucketName) {
        OSSClient ossClient = new OSSClient(ConstantUtils.getConstant("Endpoint"), ConstantUtils.getConstant("accessKeyId"), ConstantUtils.getConstant("accessKeySecret"));
        ossClient.deleteObject(bucketName, filePath);
    }
    
    /**
     * 创建文件夹
     * @param filePath 文件夹名字
     * @param bucketName ossBucket名
     */
    public static void createFolder(String folderName, String bucketName) {
        OSSClient ossClient = new OSSClient(ConstantUtils.getConstant("Endpoint"), ConstantUtils.getConstant("accessKeyId"), ConstantUtils.getConstant("accessKeySecret"));
        ossClient.putObject(bucketName, folderName, new ByteArrayInputStream(new byte[0]));
        ossClient.getObject(bucketName, folderName);
        ossClient.shutdown();
    }
    
    // private static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn, String roleSessionName, String policy, ProtocolType protocolType) throws ClientException {
    // try {
    // // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
    // IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
    // DefaultAcsClient client = new DefaultAcsClient(profile);
    //
    // // 创建一个 AssumeRoleRequest 并设置请求参数
    // final AssumeRoleRequest request = new AssumeRoleRequest();
    // request.setVersion("2015-04-01");
    // request.setMethod(MethodType.POST);
    // request.setProtocol(protocolType);
    //
    // request.setRoleArn(roleArn);
    // request.setRoleSessionName(roleSessionName);
    // request.setPolicy(policy);
    //
    // // 发起请求，并得到response
    // final AssumeRoleResponse response = client.getAcsResponse(request);
    //
    // return response;
    // } catch (ClientException e) {
    // throw e;
    // }
    // }
    
    /**
     * 获取oss token
     * @param accessKeyID
     * @param accessKeySecret
     * @return
     */
    // public static String getToken(String accessKeyID, String accessKeySecret) {
    // // TODO Auto-generated method stub
    // // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
    // // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
    // // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
    // // RoleArn 需要在 RAM 控制台上获取
    // String roleArn = "acs:ram::1366492712588266:role/aliyunosstokengeneratorrole";
    //
    // // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
    // // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '.' '@' 字母和数字等字符
    // // 具体规则请参考API文档中的格式要求
    // String roleSessionName = "AliyunOSSTokenGeneratorRole";
    //
    // // {
    // // "Version": "1",
    // // "Statement": [
    // // {
    // // "Effect": "Allow",
    // // "Action": [
    // // "oss:List*",
    // // "oss:Get*"
    // // ],
    // // "Resource": [
    // // "acs:oss:*:*:obs-video",
    // // "acs:oss:*:*:obs-video/*",
    // // "acs:oss:*:*:obs-ceshi",
    // // "acs:oss:*:*:obs-ceshi/*"
    // // ]
    // // }
    // // ]
    // // }
    //
    // String policy = "{\n" + " \"Version\": \"1\", \n" + " \"Statement\": [\n" + " {\n" + " \"Action\": [\n" + " \"oss:List*\",\n"
    // + " \"oss:Get*\"\n" + " ], \n" + " \"Resource\": [\n" + " \"acs:oss:*:*:obs-video\", \n"
    // + " \"acs:oss:*:*:obs-video/* \", \n" + " \"acs:oss:*:*:obs-ceshi\", \n" + " \"acs:oss:*:*:obs-ceshi/* \", \n"
    // + " ], \n" + " \"Effect\": \"Allow\"\n" + " }\n" + " ]\n" + "}";
    //
    // // 此处必须为 HTTPS
    // ProtocolType protocolType = ProtocolType.HTTPS;
    // String token = "";
    // try {
    // final AssumeRoleResponse response = assumeRole(accessKeyID, accessKeySecret, roleArn, roleSessionName, policy, protocolType);
    //
    // System.out.println("Expiration: " + response.getCredentials().getExpiration());
    // System.out.println("Access Key Id: " + response.getCredentials().getAccessKeyId());
    // System.out.println("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
    // System.out.println("Security Token: " + response.getCredentials().getSecurityToken());
    // token = response.getCredentials().getSecurityToken();
    // } catch (ClientException e) {
    // System.out.println("Failed to get a token.");
    // System.out.println("Error code: " + e.getErrCode());
    // System.out.println("Error message: " + e.getErrMsg());
    // }
    // return token;
    // }
    
    /**
     * 上传文件
     * @param file 上传文件 (MultipartFile)
     * @param fileName 上传文件名
     * @param bucketName ossBucket名
     * @param folderName 文件夹名
     * @param folderBoolean 是否需要创建文件夹
     * @param callbackBoolean 是否回调
     * @param callbackUrl 回调URL
     * @param callbackBody 回调参数
     */
    public static String getOssFileUrl(String fileSrc) {
        OSSClient ossClient = new OSSClient(ConstantUtils.getConstant("Endpoint"), ConstantUtils.getConstant("ObsAccessKeyID"), ConstantUtils.getConstant("ObsAccessKeySecret"));
        // 上传文件流
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(ConstantUtils.getConstant("bucketName"), fileSrc, expiration);
        return url.toString();
    }
    
    public static void main(String[] args) {
        OssUtils o = new OssUtils();
        System.out.println(o.getOssFileUrl("18/drawing/016056bbfdeeef64e516bf59533f9c00(1).jpg"));;
    }
    
}
