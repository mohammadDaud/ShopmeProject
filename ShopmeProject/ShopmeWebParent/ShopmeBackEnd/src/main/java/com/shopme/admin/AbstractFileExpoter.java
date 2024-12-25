package com.shopme.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractFileExpoter {

	public void setResponseHeader(HttpServletResponse response,String contentType,String fileExtension,String prefix) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
		String timestamp = dateFormat.format(new Date());
		String fileName = prefix + timestamp.toUpperCase() +"."+ fileExtension;
		response.setContentType(contentType);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
}
