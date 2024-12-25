package com.shopme.admin.brand.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractFileExpoter;
import com.shopme.common.entities.Brand;

public class BrandCsvExpoter extends AbstractFileExpoter {

	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", "csv","BRAND_");
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Brand ID", "Brand Name" };
		String[] csvfieldMapping = { "id", "name"};
		beanWriter.writeHeader(csvHeader);
		for (Brand brand : listBrands) {
			beanWriter.write(brand, csvfieldMapping);
		}
		beanWriter.close();
	}
}
