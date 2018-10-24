package com.platform.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	List<String> uploadRechargeExcel(MultipartFile file);

}
