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

public class FileUtility extends HoursReport {

	static JFileChooser fileChooser;
	static ArrayList<Cell> projects = new ArrayList<>();
	static ArrayList<Cell> dates = new ArrayList<>();
	static ArrayList<Cell> names = new ArrayList<>();
	static ArrayList<Cell> duration = new ArrayList<>();
	static ArrayList<Cell> status = new ArrayList<>();
	static File lastPath;

	public static void findFile() throws IOException {
		fileChooser = new JFileChooser(lastPath);
		javax.swing.filechooser.FileFilter xlsxFilter = new FileTypeFilter(".xlsx", "Microsoft Excel Documents");
		// filter that will only show Excel files
		fileChooser.addChoosableFileFilter(xlsxFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("Select Input File");
		int status = fileChooser.showDialog(fileChooser, "Open");
		try { // options option to read file and executes if yes
			if (status == JFileChooser.APPROVE_OPTION) {
				lastPath = fileChooser.getSelectedFile();
				fileChooser.setCurrentDirectory(lastPath);
				mImport.setEnabled(true);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "File not Found");
		}
	}

	public static void readFile(JFileChooser fileChooser) throws Exception {
		File selectedFile;
		selectedFile = fileChooser.getSelectedFile();
		FileInputStream fInput = new FileInputStream(selectedFile);
		XSSFWorkbook wb = new XSSFWorkbook(fInput);
		XSSFSheet sheet0 = wb.getSheetAt(1); // <-----change to 0 when finished with tool
		XSSFRow headRow = sheet0.getRow(0);
		int projIndex = 0;
		int dateIndex = 0;
		int nameIndex = 0;
		int timeIndex = 0;
		int statusIndex = 0;
		for (int i = 0; i < headRow.getLastCellNum(); i++) {
			Cell cell = headRow.getCell(i);
			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				//empty cell
				continue;
			}
			if (cell != null && cell.getStringCellValue().contains("Project")) {
				 projIndex = cell.getColumnIndex();
			} else if (cell != null && cell.getStringCellValue().contains("Date")) {
				dateIndex = cell.getColumnIndex();
			} else if(cell != null && cell.getStringCellValue().contains("Name")) {
				nameIndex = cell.getColumnIndex();
			} else if (cell != null && cell.getStringCellValue().contains("Billing Status")) {
				statusIndex = cell.getColumnIndex();
			} else if (cell != null && cell.getStringCellValue().contains("Duration")) {
				timeIndex = cell.getColumnIndex();
			}
		}
		System.out.println(projIndex);
		Iterator<Row> rowIt = sheet0.rowIterator();
		while (rowIt.hasNext()) {
			XSSFRow row = (XSSFRow) rowIt.next();
			Iterator<Cell> cellIt = row.cellIterator();
			// loop that puts data into proper array
			while (cellIt.hasNext()) {
				Cell cell = cellIt.next();
				if (cell.getColumnIndex() == projIndex) {
					projects.add(cell);
				} else if (cell.getColumnIndex() == dateIndex) {
					dates.add(cell);
				} else if (cell.getColumnIndex() == nameIndex) {
					names.add(cell);
				} else if (cell.getColumnIndex() == timeIndex) {
					duration.add(cell);
				}else if (cell.getColumnIndex() == statusIndex) {
					status.add(cell);
				}
			}
		}

		System.out.println(duration);
		FileUtility utility = new FileUtility();
		utility.fillArray(projects);

		int n = JOptionPane.showConfirmDialog(fileChooser, "Select Date Range");
		if (n == JOptionPane.OK_OPTION) {
			HoursReport report = new HoursReport();
			report.datePaneGUI();
		} else if (n == JOptionPane.NO_OPTION) {
			mImport.setEnabled(false);
		}

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
