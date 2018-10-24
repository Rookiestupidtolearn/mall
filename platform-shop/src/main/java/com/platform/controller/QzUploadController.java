package com.platform.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.platform.service.UploadService;
import com.platform.utils.DateUtils;
import com.platform.utils.ResourceUtil;

@RestController
@RequestMapping("qzUpload")
public class QzUploadController {

	
	@Autowired
	private UploadService uploadService;
	
	/**
	  * 上传文件
	  */
	@RequestMapping("/upload")
	 public List<String> upload(@RequestParam("file") MultipartFile file,HttpServletRequest  request) {
		 List<String> error = new ArrayList<>();
	  if (file.isEmpty()) {
		  error.add("上传文件不能为空");
	     return error;
	  }
	  String fileName = file.getOriginalFilename();// 文件名
	  String fileExtension = fileName.substring(fileName.lastIndexOf("."));//扩展名
	  if((!StringUtils.endsWithIgnoreCase(fileName, ".xls") && (!StringUtils.endsWithIgnoreCase(fileName, ".xlsx") ))){
		  error.add("请上传Excel文件");
		     return error;  
      }
	  
	  String name = "";
	  String path = "";
       try {
           InputStream fileInputStream = file.getInputStream();
           //保存到服务器的路径
          String homePath = ResourceUtil.getConfigByName("file.upload.excelPath");
           String pathName = DateUtils.format(new Date(),"yyyyMMdd");
            path = homePath+pathName+"/";
   		  File fileNew =new File(path);    
   		//如果文件夹不存在则创建    
   		if  (!fileNew .exists()  && !fileNew .isDirectory())      
   		{       
   		    System.out.println("//文件夹不存在");  
   		   fileNew .mkdirs();    
   		}
            name = DateUtils.format(new Date(),"yyyyMMddHHmmssSSS");
           if(fileName.endsWith("xls")){
               //2003
        	   name +=".xls";
           }else if(fileName.endsWith("xlsx")){
               //2007 及2007以上
        	   name +=".xlsx";
           }
           FileOutputStream fs=new FileOutputStream( path + File.separator + name);
           byte[] buffer =new byte[1024*1024];
           int bytesum = 0;
           int byteread = 0;
           while ((byteread=fileInputStream.read(buffer))!=-1)
           {
               bytesum+=byteread;
               fs.write(buffer,0,byteread);
               fs.flush();
           }
           fs.close();
           fileInputStream.close();
           
           
       } catch (IOException e) {
           e.printStackTrace();
       } 
   
      List<String> iStrings = new ArrayList<>();
	try {
		iStrings = uploadService.uploadRechargeExcelByHave(path,name);
	} catch (IOException e) {
	
		e.printStackTrace();
	}
	   
	  return iStrings ;
	}
}
