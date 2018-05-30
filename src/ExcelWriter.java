import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.swing.JFileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;


public class ExcelWriter extends HoursReport {
	static JFileChooser fileChooser;
	public static final String OUTPUT_FILE = "Hours_" + FileUtility.getTimeStamp() + ".xlsx";

	public static void outputFile(List<String> projects, List<Date> dates, List<String> names, List<Double> duration)
			throws IOException {
		String[] header = { "Month", "Customer", "Project", "Hours", "Cost", "Invoiced", "Profitability" };
		String[] dateHeader = { " ", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
				"Dec" };
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet0 = wb.createSheet("Profitability");
		XSSFSheet sheet1 = wb.createSheet("Utilization");
		XSSFSheet sheet2 = wb.createSheet("Original File");
		CellStyle headCellStyle = wb.createCellStyle();
		Row pHeadRow = sheet0.createRow(0);
		Row client = sheet0.createRow(1);
		Row uHeadRow = sheet1.createRow(0);
		Row initHeadRow = sheet2.createRow(0);

		for (int i = 0; i < header.length; i++) {
			Cell cell = pHeadRow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(headCellStyle);
		}
		for (int i = 1; i < dateHeader.length; i++) {
			Cell cell = uHeadRow.createCell(i);
			cell.setCellValue(dateHeader[i] + "-" + year);
			cell.setCellStyle(headCellStyle);
		}
		for (int row = 1; row < duration.size(); row++) {
			Cell cell = client.createCell(row);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(projects.get(row));
			cell.setCellStyle(headCellStyle);
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
