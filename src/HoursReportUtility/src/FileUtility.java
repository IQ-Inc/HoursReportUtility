import java.io.*;
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
	File lastPath;

	public void readFile(File selected, String outFile, JTextArea area) throws Exception {
		// Creates the input stream, workbooks and worksheets
		FileUtility utility = new FileUtility();
		if (outFile == null || area == null) { // Block that reads the original file

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
					}
				}
			}

			utility.fillArray(FileUtility.projects);

			ExcelWriter.outputFile(FileUtility.projects, FileUtility.dates, FileUtility.names, FileUtility.duration, FileUtility.status);
			wb.close();

		} else // Else block that reads the created file
		{
			File output = new File(outFile);
			String[] fNames = area.getText().split("\n");
			ArrayList<String> filterNames = new ArrayList<>(Arrays.asList(fNames));
			for (int i = 0; i < filterNames.size(); i++) {
				if (filterNames.get(i).equals("")) {
					filterNames.remove(i);
					i--;
				}
			}

			

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
						FileUtility.names.add(cell);
					} else if (cell.getColumnIndex() == 2) {
						FileUtility.dates.add(cell);
					} else if (cell.getColumnIndex() == 3) {
						FileUtility.duration.add(cell);
					} else if (cell.getColumnIndex() == 4) {
						FileUtility.status.add(cell);
					}

				}

			}
			
			wb.close();
			//Filter filter = new Filter();
			//filter.filter(utility.names, utility.dates, utility.duration, 
					//utility.projects, utility.status, Filter.nameArea);

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

	// adds the date to the output file's name
	public static String getTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}
}
