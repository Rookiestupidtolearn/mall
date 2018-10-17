package com.platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.platform.service.UploadService;
import com.platform.utils.R;

@RestController
@RequestMapping("qzUpload")
public class QzUploadController {

	
	@Autowired
	private UploadService uploadService;
	
	/**
	  * 上传文件
	  */
	@RequestMapping("/upload")
	 public R upload(@RequestParam("file") MultipartFile file,HttpServletRequest  request) {
	  if (file.isEmpty()) {
	     return R.error(400,"上传文件不能为空");
	  }
	  String fileName = file.getOriginalFilename();// 文件名
	  String fileExtension = fileName.substring(fileName.lastIndexOf("."));//扩展名
	  if((!StringUtils.endsWithIgnoreCase(fileName, ".xls") && (!StringUtils.endsWithIgnoreCase(fileName, ".xlsx") ))){
		  return R.error(400,"请上传Excel文件");
      }
      
	 R  r = uploadService.uploadRechargeExcel(file);
	  
	  return r ;
	}
}
