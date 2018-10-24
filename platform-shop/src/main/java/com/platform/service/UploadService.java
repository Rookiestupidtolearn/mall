package com.platform.service;

import java.io.IOException;
import java.util.List;

public interface UploadService {

	List<String> uploadRechargeExcelByHave(String filepath, String fileName) throws IOException;

}
