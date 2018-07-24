import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class FileUtility {

	static JFileChooser fileChooser;
	ArrayList<Cell> projects = new ArrayList<>();
	ArrayList<Cell> dates = new ArrayList<>();
	ArrayList<Cell> names = new ArrayList<>();
	ArrayList<Cell> duration = new ArrayList<>();
	ArrayList<Cell> status = new ArrayList<>();
	File lastPath;

	public static void readFile(File selected, String outFile, JTextArea area) throws Exception {
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

						utility.projects.add(cell);
					} else if (cell.getColumnIndex() == 5) {
						utility.dates.add(cell);
					} else if (cell.getColumnIndex() == 7) {
						utility.names.add(cell);
					} else if (cell.getColumnIndex() == 11) {
						utility.duration.add(cell);
					} else if (cell.getColumnIndex() == 9) {
						utility.status.add(cell);
					}
				}
			}

			utility.fillArray(utility.projects);

			ExcelWriter.outputFile(utility.projects, utility.dates, utility.names, utility.duration, utility.status);
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

			utility.names.clear();
			utility.dates.clear();
			utility.duration.clear();
			utility.projects.clear();
			utility.status.clear();

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
						utility.projects.add(cell);
					} else if (cell.getColumnIndex() == 1) {
						utility.names.add(cell);
					} else if (cell.getColumnIndex() == 2) {
						utility.dates.add(cell);
					} else if (cell.getColumnIndex() == 3) {
						utility.duration.add(cell);
					} else if (cell.getColumnIndex() == 4) {
						utility.status.add(cell);
					}

				}

			}
			wb.close();
			Filter filter = new Filter();
			filter.filter(utility.names, utility.dates, utility.duration, 
					utility.projects, utility.status, Filter.nameArea);

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
