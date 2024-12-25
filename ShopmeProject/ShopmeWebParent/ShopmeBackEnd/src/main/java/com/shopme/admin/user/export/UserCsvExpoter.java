package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractFileExpoter;
import com.shopme.common.entities.User;

public class UserCsvExpoter extends AbstractFileExpoter {

	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", "csv","USER_");
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "User ID", "E-mail", "FirstName", "LastName", "Roles", "Enabled" };
		String[] csvfieldMapping = { "id", "email", "firstname", "lastname", "roles", "enabled" };
		beanWriter.writeHeader(csvHeader);
		for (User user : listUser) {
			beanWriter.write(user, csvfieldMapping);
		}
		beanWriter.close();
	}
}
