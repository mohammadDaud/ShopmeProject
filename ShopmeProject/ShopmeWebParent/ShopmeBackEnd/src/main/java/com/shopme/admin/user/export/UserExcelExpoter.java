package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.admin.AbstractFileExpoter;
import com.shopme.common.entities.User;

public class UserExcelExpoter extends AbstractFileExpoter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public UserExcelExpoter() {
		workbook = new XSSFWorkbook();
	}

	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", "xlsx","USER_");
		wirteOrgnizationName("SHOPME PVT.LTD");
		wirteHeaderLine();
		wirteDataLine(listUser);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void wirteDataLine(List<User> listUser) {
		int indexRow = 2;
		int indexColumn = 0;
		for (User user : listUser) {
			XSSFRow row = sheet.createRow(indexRow);
			Map<Integer, String> map = new LinkedHashMap<>();
			map.put(indexColumn++, user. getId().toString());
			map.put(indexColumn++, user. getEmail().toString());
			map.put(indexColumn++, user. getFirstname().toString());
			map.put(indexColumn++, user. getLastname().toString());
			map.put(indexColumn++, user. getRoles().toString());
			map.put(indexColumn++, String.valueOf(user.isEnabled()));
			setDataCellValues(row,map);
	   	 indexRow++;
	   	 indexColumn=0;
	   	 map=null;
		}
	}
	private void setDataCellValues(XSSFRow row, Map<Integer, String> map) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(false);
		font.setFontHeight(14);
		cellStyle.setFont(font);
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			createCell(row, entry.getKey(), entry.getValue(), cellStyle);
		}
	}

	private void wirteOrgnizationName(String orgnizationName) {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		sheet.autoSizeColumn(0);
		Map<Integer, String> map = new LinkedHashMap<>();
		map.put(0, orgnizationName);
		setHeaderCellValues(row, map);
	}
	
	private void wirteHeaderLine() {
		XSSFRow row = sheet.createRow(1);
		sheet.autoSizeColumn(1);
		Map<Integer, String> map = new LinkedHashMap<>();
		map.put(0, "User ID");
		map.put(1, "E-mail");
		map.put(2, "FirstName");
		map.put(3, "LastName");
		map.put(4, "Roles");
		map.put(5, "Enabled");
		setHeaderCellValues(row, map);
	}

	private void setHeaderCellValues(XSSFRow row, Map<Integer, String> map) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			createCell(row, entry.getKey(), entry.getValue(), cellStyle);
		}
	}

	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}
}
