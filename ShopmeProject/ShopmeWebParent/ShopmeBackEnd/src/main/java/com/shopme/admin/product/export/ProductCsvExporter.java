package com.shopme.admin.product.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractFileExpoter;
import com.shopme.common.entities.Product;

public class ProductCsvExporter extends AbstractFileExpoter {
	public void export(List<Product> listProducts, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", "csv","PRODUCT_");
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Product ID", "Product Name","Product Categories","Product Brand" };
		String[] csvfieldMapping = { "id", "name","category","brand"};
		beanWriter.writeHeader(csvHeader);
		for (Product product : listProducts) {
			beanWriter.write(product, csvfieldMapping);
		}
		beanWriter.close();
	}
}
