import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.swing.JFileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class ExcelWriter {
	static JFileChooser fileChooser;
	public static final String OUTPUT_FILE = "Hours_" + FileUtility.getTimeStamp() + ".xlsx";

	public static void outputFile(List<Cell> projects, List<Cell> dates, List<Cell> names, List<Cell> duration,
			List<Cell> status, List<Cell> nBType) throws IOException {
		String[] header = { "Project", "Non-Billable Type", "Employee", "Dates", "Hours", "Billing Status", "Cost",
				"Invoiced", "Profitability"};
		String[] dateHeader = { " ", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
				"Dec" };

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet0 = wb.createSheet("Profitability");
		XSSFSheet sheet1 = wb.createSheet("Utilization");
		// create new font for header
		Font font = wb.createFont();
		font.setBold(true);
		font.setColor(Font.COLOR_RED);
		font.setUnderline(Font.U_SINGLE);
		CellStyle headCellStyle = wb.createCellStyle();
		headCellStyle.setFont(font);
		DataFormat fmt = wb.createDataFormat();
		CellStyle textStyle = wb.createCellStyle();
		CellStyle numStyle = wb.createCellStyle();
		numStyle.setDataFormat(fmt.getFormat("#"));
		textStyle.setDataFormat(fmt.getFormat("@"));
		Row profitHeadRow = sheet0.createRow(0);
		Row utilHeadRow = sheet1.createRow(0);
		CreationHelper create = wb.getCreationHelper();

		// creates the profit sheet header
		for (int i = 0; i < header.length; i++) {
			Cell cell = profitHeadRow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(headCellStyle);
			sheet0.setDefaultColumnWidth(25);
		} // creates the date header
		for (int i = 1; i < dateHeader.length; i++) {
			Cell cell = utilHeadRow.createCell(i);
			cell.setCellValue(dateHeader[i] + "-" + year);
			cell.setCellStyle(headCellStyle);
		}
		

		// fills the sheets with the data read from Excel File
		
		for (int i = 0; i < projects.size(); i++) {
			Row row = sheet0.createRow(i + 1);
			row.createCell(0).setCellValue(create.createRichTextString(projects.get(i).toString()));
			row.createCell(1).setCellValue(create.createRichTextString(nBType.get(i).toString()));
			row.createCell(2).setCellValue(create.createRichTextString(names.get(i).toString()));
			row.createCell(3).setCellValue(create.createRichTextString(dates.get(i).toString()));
			Cell cell = row.createCell(4);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			Cell hoursCell = duration.get(i);
			hoursCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			double value = hoursCell.getNumericCellValue();
			cell.setCellValue(value);
			row.createCell(5).setCellValue(create.createRichTextString(status.get(i).toString()));

		}
		
		
		sheet0.autoSizeColumn(0);
		FileOutputStream out = new FileOutputStream(OUTPUT_FILE);
		wb.write(out);
		wb.close();
		
		

		

	}

}
