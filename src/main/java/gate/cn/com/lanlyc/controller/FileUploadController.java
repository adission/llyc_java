package gate.cn.com.lanlyc.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mysql.fabric.xmlrpc.base.Array;

import cn.com.lanlyc.base.util.ConstantUtils;
import cn.com.lanlyc.base.util.DataUtils;
import cn.com.lanlyc.core.util.Response;

@Controller
@RequestMapping("/FileUpload")
public class FileUploadController extends BaseController {
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Response saveEmergency(MultipartFile file, HttpServletRequest request) {
		
		List<String> file_path=new ArrayList();
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		if (resolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				String fileName = iter.next();
				
		        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/HH");
		        /** 构建文件保存的目录* */
		        String logoPathDir = "/html/upload";
		        /** 得到文件保存目录的真实路径* */
		        String logoRealPathDir = request.getSession().getServletContext()
		                .getRealPath(logoPathDir);
		        /** 根据真实路径创建目录* */
		        File logoSaveFile = new File(logoRealPathDir);
		        if (!logoSaveFile.exists())
		            logoSaveFile.mkdirs();
		        // 取得上传文件
		        MultipartFile multipartFile = multiRequest.getFile(fileName);
		        /** 页面控件的文件流* */
		        
//		         multipartFile = multipartRequest.getFile("file");
		        /** 获取文件的后缀* */
		        String suffix = multipartFile.getOriginalFilename().substring(
		                multipartFile.getOriginalFilename().lastIndexOf("."));
		        /** 使用UUID生成文件名称* */
		        String logImageName =DataUtils.randomString(5)+"_"+ (new Date().getTime()) + suffix;// 构建文件名称
		        /** 拼成完整的文件保存路径加文件* */
		        String savefileName = logoRealPathDir + File.separator + logImageName;
		        File savefile = new File(savefileName);
		        try {
		        	multipartFile.transferTo(savefile);
		        } catch (IllegalStateException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        String output_path=logoPathDir+ "/" + logImageName;
		        /** 打印出上传到服务器的文件的绝对路径* */
		        System.out.println("****************"+output_path+"**************");
		        file_path.add(output_path);
				
//				String newfileName = DataUtils.randomString(5)+"_"+ (new Date().getTime());
			}
		}

//		File files = new File(request.getSession().getServletContext().getRealPath("/"));
//		files = files.getParentFile();
//		String path = files.getAbsolutePath() + File.separator + "ROOT" + File.separator;
//		String dataurl = "";

//		if (file != null) {
//			// 获取文件后缀
//			if (!DataUtils.isNullOrEmpty(file.getOriginalFilename())) {
//				String str[] = file.getOriginalFilename().split("\\.");
//				String fileType = str[str.length - 1];
//				String fileName = str[0];
//				String newfileName = DataUtils.randomString(5)+"_"+ (new Date().getTime());
//		
//                
////				String ossPath = ConstantUtils.getConstant("ossUrl") + admin.getId() + "/emergency/" + fileName + "."
////						+ fileType;
////				OssUtils.uploadFile(file, fileName + "." + fileType, ConstantUtils.getConstant("bucketName"),
////						admin.getId().toString() + "/emergency", true, false, null, null);
////				emergency.setcFileUrl(ossPath);
////				emergency.setcName(fileName + "." + fileType);
////				emergency.setcFileSize(OperationProcedureService.GetFileSize(file));
//			}
//		}
		
		return Response.set(file_path);
	}
}
