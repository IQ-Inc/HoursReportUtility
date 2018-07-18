import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.*;
import java.util.prefs.Preferences;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow.CellIterator;
import org.apache.poi.xssf.usermodel.*;

public class FileUtility {

	static JFileChooser fileChooser;
	static ArrayList<Cell> projects = new ArrayList<>();
	static ArrayList<Cell> dates = new ArrayList<>();
	static ArrayList<Cell> names = new ArrayList<>();
	static ArrayList<Cell> duration = new ArrayList<>();
	static ArrayList<Cell> status = new ArrayList<>();
	static File lastPath;

	public static void readFile(File selected) throws Exception {
		// Creates the input stream, workbooks and worksheets
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
					projects.add(cell);
				} else if (cell.getColumnIndex() == 5) {
					dates.add(cell);
				} else if (cell.getColumnIndex() == 7) {
					names.add(cell);
				} else if (cell.getColumnIndex() == 11) {
					duration.add(cell);
				} else if (cell.getColumnIndex() == 9) {
					status.add(cell);
				}
			}
		}

		FileUtility utility = new FileUtility();
		utility.fillArray(projects);

		ExcelWriter.outputFile(projects, dates, names, duration, status);
		wb.close();

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

	// adds the date to the output file's name
	public static String getTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}
}
