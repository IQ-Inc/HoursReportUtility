import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.swing.JFileChooser;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.*;

@SuppressWarnings("serial")
public class ExcelWriter extends HoursReport {
	static JFileChooser fileChooser;
	public static final String OUTPUT_FILE = "Hours_" + FileUtility.getTimeStamp() + ".xlsx";

	public static void outputFile(List<Cell> projects, List<Cell> dates, List<Cell> names, List<Cell> duration)
			throws IOException {
		String[] header = { "Month", "Employee", "Project", "Hours", "Cost", "Invoiced", "Profitability" };
		String[] dateHeader = { " ", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
				"Dec" };
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet0 = wb.createSheet("Profitability");
		XSSFSheet sheet1 = wb.createSheet("Utilization");
		CellStyle headCellStyle = wb.createCellStyle();
		Row profitHeadRow = sheet0.createRow(0);
		Row utilHeadRow = sheet1.createRow(0);
		DataFormatter formatter = new DataFormatter();

		for (int i = 0; i < header.length; i++) {
			Cell cell = profitHeadRow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(headCellStyle);
			sheet0.setDefaultColumnWidth(25);
		}
		for (int i = 1; i < dateHeader.length; i++) {
			Cell cell = utilHeadRow.createCell(i);
			cell.setCellValue(dateHeader[i] + "-" + year);
			cell.setCellStyle(headCellStyle);
		}
		for (int i = 1; i < duration.size(); i++) {
			Row row = sheet0.createRow(i);
			Cell timeCell = row.createCell(3);
			String time = formatter.formatCellValue(duration.get(i));
			timeCell.setCellValue(time);
			Cell empCell = row.createCell(1);
			String employee = formatter.formatCellValue(names.get(i));
			empCell.setCellValue(employee);
			Cell dateCell = row.createCell(0);
			if (i < dates.size()) {
			String date = formatter.formatCellValue(dates.get(i));
			dateCell.setCellValue(date);
		}
			Cell projCell = row.createCell(2);
			if (i < projects.size()) {
				String project = formatter.formatCellValue(projects.get(i));
				projCell.setCellValue(project);
				sheet0.autoSizeColumn(2);
		} 
		}
		try {
			FileOutputStream out = new FileOutputStream(OUTPUT_FILE);
			wb.write(out);
			wb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
