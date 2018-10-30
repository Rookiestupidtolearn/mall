package com.platform.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.platform.entity.UserEntity;
import com.platform.service.QzRechargeRecordService;
import com.platform.service.UploadService;
import com.platform.service.UserService;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {
	private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
	
   @Autowired
	private QzRechargeRecordService qzRechargeRecordService;
	@Autowired
	private UserService userService;

	private List<String> uploadRechargeExcel(Workbook workbook, String filepath, String fileName) throws IOException {
		List<String> info = new ArrayList<>();

		if (workbook != null) {
			FileOutputStream out = new FileOutputStream(filepath + fileName); // 向d://test.xls中写数据

			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				// 获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if (sheet == null) {
					continue;
				}
				// 获得当前sheet的开始行
				int firstRowNum = sheet.getFirstRowNum();
				// 获得当前sheet的结束行
				int lastRowNum = sheet.getLastRowNum();
				// 循环除了第一行的所有行
				for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
					// 获得当前行
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}
					Cell cell = row.getCell(0);
					String mobile = cell.getStringCellValue();
					Cell cell1 = row.getCell(1);
					String amount = cell1.getStringCellValue();

					// 校验手机号，金额参数

					String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
					if (mobile.length() != 11) {
						info.add(mobile + ":手机号长度错误，应该是11位!");
						row.createCell(2).setCellValue("手机号长度错误，应该是11位");
						continue;
					}
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(mobile);
					boolean isMatch = m.matches();

					if (!isMatch) {
						info.add(mobile + ":请填入正确的手机号!");
						row.createCell(2).setCellValue("手机号格式不对!");
						continue;
					}

					UserEntity entity = userService.queryEntityByMobile(mobile);
					if (entity == null) {
						info.add("手机号【" + mobile + "】不是会员!");
						row.createCell(2).setCellValue("手机号不是会员!");
						continue;
					}

					Map<String, Object> map1 = new HashMap<>();
					map1.put("mobile", mobile);
					List<UserEntity> uEntities = userService.queryList(map1);
					if (uEntities.size() > 1) {
						info.add("手机号【" + mobile + "】不能绑定两个会员!");
						row.createCell(2).setCellValue("手机号不能绑定两个会员");
						continue;
					}

					if (amount.equals("")) {
						info.add("手机号【" + mobile + "】转账金额不能为空!");
						row.createCell(2).setCellValue("转账金额不能为空!");
						continue;
					}

					Pattern pattern = Pattern.compile("\\d\\.\\d*|[1-9]\\d*|\\d*\\.\\d*|\\d");
					Matcher match = pattern.matcher(amount);
					if (match.matches() == false) {
						info.add("手机号【" + mobile + "】转账金额不合法，请检查!");
						row.createCell(2).setCellValue("转账金额不合法，请检查!");
						continue;
					}

					Double checkAmount = Double.valueOf(amount);
					if (checkAmount <= 0) {
						row.createCell(2).setCellValue("转账金额应大于0元!");
						info.add("手机号【" + mobile + "】转账金额应大于0元!");
						continue;
					}

					Double bigAmount = Double.valueOf(amount);
					if (bigAmount > 90000000) {
						info.add("手机号【" + mobile + "】转账金额不能大于9千万元!");
						row.createCell(2).setCellValue("转账金额不能大于9千万元!");
						continue;
					}

					Pattern pattern2 = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
					Matcher match2 = pattern2.matcher(amount);
					if (match2.matches() == false) {
						info.add("手机号【" + mobile + "】转账金额小数点后保留2位");
						row.createCell(2).setCellValue("转账金额小数点后保留2位");
						continue;
					}

					Map<String, Object> map = new HashMap<>();
					map.put("mobiles", mobile);
					map.put("amount", amount);
					map.put("memo", "uploadRechargeExcel");
					log.info("Excel上传开始执行充值操作，手机号:"+mobile+",充值金额："+amount);
					qzRechargeRecordService.rechargeBatch(map);

				}
			}

			out.flush();
			workbook.write(out);
			out.close();

		}
		if (CollectionUtils.isEmpty(info)) {
			info.add("上传成功");
		}

		return info;

	}

	@Override
	public List<String> uploadRechargeExcelByHave(String filepath, String fileName) throws IOException {
		List<String> info = new ArrayList<>();
		Workbook workbook = null;
		FileInputStream fs = new FileInputStream(filepath + fileName);
		if (fileName.endsWith("xls")) {
			// 2003
			workbook = new HSSFWorkbook(fs);
		} else if (fileName.endsWith("xlsx")) {
			// 2007 及2007以上
			workbook = new XSSFWorkbook(fs);
		}
		info = uploadRechargeExcel(workbook, filepath, fileName);
		return info;
	}

}
