import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.swing.JFileChooser;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

@SuppressWarnings("serial")
public class ExcelWriter extends HoursReport {
	static JFileChooser fileChooser;
	public static final String OUTPUT_FILE = "Hours_" + FileUtility.getTimeStamp() + ".xlsx";

	public static void outputFile(List<Cell> projects, List<Cell> dates, List<Cell> names, List<Cell> duration,
			List<Cell> status) throws IOException {
		String[] header = { "Project", "Employee", "Date", "Hours", "Billing Status", "Cost", "Invoiced",
				"Profitability" };
		String[] dateHeader = { " ", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
				"Dec" };
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet0 = wb.createSheet("Profitability");
		XSSFSheet sheet1 = wb.createSheet("Utilization");
		CellStyle headCellStyle = wb.createCellStyle();
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
		for (int i = 1; i < projects.size(); i++) {
			Row row = sheet0.createRow(i);
			row.createCell(0).setCellValue(create.createRichTextString(projects.get(i).toString()));
			row.createCell(1).setCellValue(create.createRichTextString(names.get(i).toString()));
			row.createCell(2).setCellValue(create.createRichTextString(dates.get(i).toString()));
			row.createCell(3).setCellValue(create.createRichTextString(duration.get(i).toString()));
			row.createCell(4).setCellValue(create.createRichTextString(status.get(i).toString()));
		}

		sheet0.autoSizeColumn(0);

		try { // Writer to the file
			FileOutputStream out = new FileOutputStream(OUTPUT_FILE);
			wb.write(out);
			wb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Desktop dt = Desktop.getDesktop();
		File file = new File(OUTPUT_FILE);
		dt.open(file);

	}
}
