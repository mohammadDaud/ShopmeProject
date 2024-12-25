package com.shopme.admin.category.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractFileExpoter;
import com.shopme.common.entities.Category;

public class CategoryCsvExpoter extends AbstractFileExpoter {

	public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", "csv","CATEGORY_");
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Category ID", "Category Name" };
		String[] csvfieldMapping = { "id", "name"};
		beanWriter.writeHeader(csvHeader);
		for (Category category : listCategory) {
			category.setName(category.getName().replace("--", "  "));
			beanWriter.write(category, csvfieldMapping);
		}
		beanWriter.close();
	}
}
