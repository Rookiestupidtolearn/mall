package com.platform.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.platform.service.QzRechargeRecordService;
import com.platform.service.UploadService;
import com.platform.utils.R;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {
   
	@Autowired
	private QzRechargeRecordService qzRechargeRecordService;
	
	@Override
	public R uploadRechargeExcel( MultipartFile file ){
		 //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        
        if(workbook != null){
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue; 
                }
                //获得当前sheet的开始行
                int firstRowNum  = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if(row == null){
                        continue;
                    }
                    Cell cell = row.getCell(0);
                    String mobile = cell.getStringCellValue();
                    Cell cell1 = row.getCell(1);
                    String amount = cell1.getStringCellValue();
                    
                    Map<String, Object> map = new HashMap<>(); 
                    map.put("mobiles",mobile);
                    map.put("amount",amount);
                    map.put("memo","Excel导入充值");
                    
                    qzRechargeRecordService.rechargeBatch(map);

                }
            }
     
      
        }
        
		return R.ok();
		
	}
	
	
	public static  Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith("xls")){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith("xlsx")){
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
           
        }
        return workbook;
    }
	
	
}
