package com.shopme.admin.user.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.AbstractFileExpoter;
import com.shopme.common.entities.User;

public class UserPDFExpoter extends AbstractFileExpoter {

	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", "pdf","USER_");
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC);
		font.setColor(Color.DARK_GRAY);
		font.setSize(18);
		Paragraph paragraph = new Paragraph("SHOPME PVT.LTD.", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		writeTableHeader(table);
		writeTableData(table,listUser);
		document.add(table);
		document.close();
	}

	private void writeTableData(PdfPTable table, List<User> listUser) {
		for(User user:listUser) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getFirstname());
			table.addCell(user.getLastname());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnabled()));
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.gray);
		cell.setPadding(4);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setBorderColor(Color.gray);
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setColor(Color.WHITE);
		font.setSize(12);
		
		cell.setPhrase(new Phrase("User ID", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("E-mail", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("FirstName", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("LastName", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Roles", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
	}
}
