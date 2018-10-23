package com.platform.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.platform.service.UploadService;

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
	   List<String>  iStrings = uploadService.uploadRechargeExcel(file);
	  return iStrings ;
	}
}
