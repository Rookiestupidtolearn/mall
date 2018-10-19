package com.platform.service;

import org.springframework.web.multipart.MultipartFile;

import com.platform.utils.R;

public interface UploadService {

	R uploadRechargeExcel(MultipartFile file);

}
