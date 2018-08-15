import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.plaf.synth.SynthSpinnerUI;

import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class FileUtility {

	static JFileChooser fileChooser;
	static ArrayList<Cell> projects = new ArrayList<>();
	static ArrayList<Cell> dates = new ArrayList<>();
	static ArrayList<Cell> names = new ArrayList<>();
	static ArrayList<Cell> duration = new ArrayList<>();
	static ArrayList<Cell> status = new ArrayList<>();
	static ArrayList<Cell> notBillType = new ArrayList<>();

	public void readFile(File selected, String outFile, ArrayList<String> checkNames) throws Exception {
		// Creates the input stream, workbooks and worksheets
		FileUtility utility = new FileUtility();
		if (outFile == null || checkNames == null) { // Block that reads the original file

			FileInputStream fInput = new FileInputStream(selected);
			XSSFWorkbook wb = new XSSFWorkbook(fInput);
			XSSFSheet sheet0 = wb.getSheetAt(1); // <-----change to 0 when finished with tool
			Iterator<Row> rowIt = sheet0.rowIterator();
			while (rowIt.hasNext()) {
				XSSFRow row = (XSSFRow) rowIt.next();
				Iterator<Cell> cellIt = row.cellIterator();
				// loop that puts data into proper array
				while (cellIt.hasNext()) {
					Cell cell = cellIt.next();
					if (cell.getColumnIndex() == 1) {
						FileUtility.projects.add(cell);
					} else if (cell.getColumnIndex() == 5) {
						FileUtility.dates.add(cell);
					} else if (cell.getColumnIndex() == 7) {
						FileUtility.names.add(cell);
					} else if (cell.getColumnIndex() == 11) {
						FileUtility.duration.add(cell);
					} else if (cell.getColumnIndex() == 9) {
						FileUtility.status.add(cell);
					} else if (cell.getColumnIndex() == 2) {
						FileUtility.notBillType.add(cell);
					}
				}
			}

			utility.fillArray(FileUtility.projects);
			utility.fillArray(notBillType);
			utility.removeBlanks(projects, dates, names, duration, status, notBillType);
			utility.convertDates(dates, wb);
			ExcelWriter.outputFile(FileUtility.projects, FileUtility.dates, FileUtility.names, FileUtility.duration,
					FileUtility.status, FileUtility.notBillType);

			wb.close();

		} else // Else block that reads the created file
		{
			names.clear();
			projects.clear();
			dates.clear();
			duration.clear();
			status.clear();

			File output = new File(outFile);
			FileInputStream fInput = new FileInputStream(output);
			XSSFWorkbook wb = new XSSFWorkbook(fInput);
			XSSFSheet sheet = wb.getSheetAt(0);
			Iterator<Row> rowIT = sheet.rowIterator();
			while (rowIT.hasNext()) {
				XSSFRow row = (XSSFRow) rowIT.next();
				Iterator<Cell> cellIT = row.cellIterator();
				while (cellIT.hasNext()) {
					Cell cell = cellIT.next();
					if (cell.getColumnIndex() == 0) {
						FileUtility.projects.add(cell);
					} else if (cell.getColumnIndex() == 1) {
						FileUtility.notBillType.add(cell);
					} else if (cell.getColumnIndex() == 2) {
						FileUtility.names.add(cell);
					} else if (cell.getColumnIndex() == 3) {
						FileUtility.dates.add(cell);
					} else if (cell.getColumnIndex() == 4) {
						FileUtility.duration.add(cell);
					} else if (cell.getColumnIndex() == 5) {
						FileUtility.status.add(cell);
					}

				}
			}

			wb.close();
		}
	}

	// Fills in the blanks in the Array for a pivot table

	public ArrayList<Cell> fillArray(ArrayList<Cell> list) {
		String value = " ";
		for (int i = 0; i < list.size(); i++) {
			Cell cell = list.get(i);
			if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				value = cell.getStringCellValue();
			} else {
				cell.setCellValue(value);
			}

		}
		return list;
	}
	
	public void removeBlanks(ArrayList<Cell> projects, ArrayList<Cell> dates, ArrayList<Cell> names,
			ArrayList<Cell> duration, ArrayList<Cell> status, ArrayList<Cell> nBType) {
		// if employee column is blank - sets the row to blank
		for (int i = 0; i < projects.size(); i++) {
			if (names.get(i).getCellType() == Cell.CELL_TYPE_BLANK) {
				projects.get(i).setCellValue("");
				dates.get(i).setCellValue("");
				names.get(i).setCellValue("");
				duration.get(i).setCellValue(0);
				status.get(i).setCellValue("");
			}
		}
		// sets formula rows to 0 so doesn't interfere with pivot table 
		for (Cell time : duration) {
			if (time.getCellType() == Cell.CELL_TYPE_FORMULA) {
				time.setCellValue(0);
			}
		}
		// removes rows containing the total contract information
		for (Cell type : nBType) {
			if (type.getStringCellValue().contains("Total") || type.getStringCellValue().contains("Contract")) {
				type.setCellType(Cell.CELL_TYPE_BLANK);
			}
		}
	}

	// adds the date to the output file's name
	public static String getTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}

	// converts the date to MM/yyyy - Only works with one file - not sure how to get
	// it to
	// work with both
	public void convertDates(List<Cell> dates, XSSFWorkbook wb) {
		SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
		for (int i = 0; i < names.size(); i++) {
			if (dates.get(i).getCellType() == Cell.CELL_TYPE_NUMERIC) {

				Date cellDate = dates.get(i).getDateCellValue();
				String date = df.format(cellDate);
				dates.get(i).setCellValue(date);
			} else {
				continue;

			}

		}
	}
}
