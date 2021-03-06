import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.*;
import java.util.prefs.Preferences;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

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

	public static void findFile() throws IOException {
		fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
		javax.swing.filechooser.FileFilter xlsxFilter = new FileTypeFilter(".xlsx", "Microsoft Excel Documents");
		fileChooser.addChoosableFileFilter(xlsxFilter);
		// filter that will only show Excel files
		fileChooser.setDialogTitle("Select Input File");
		int status = fileChooser.showDialog(fileChooser, "Open");
		try { // options option to read file and executes if yes
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				fileChooser.setCurrentDirectory(file);
				mImport.setEnabled(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFile(JFileChooser fileChooser) throws Exception {
		File selectedFile;
		selectedFile = fileChooser.getSelectedFile();
		fileChooser.setCurrentDirectory(selectedFile);
		FileInputStream fInput = new FileInputStream(selectedFile);
		XSSFWorkbook wb = new XSSFWorkbook(fInput);
		XSSFSheet sheet0 = wb.getSheetAt(1); // <-----change to 0 when finished with tool
		Iterator<Row> rowIt = sheet0.rowIterator();
		while (rowIt.hasNext()) {
			XSSFRow row = (XSSFRow) rowIt.next();
			Iterator<Cell> cellIt = row.cellIterator();

			while (cellIt.hasNext()) {
				Cell cell = cellIt.next();
				if ((cell.getColumnIndex() == 0)) {
					projects.add(cell);
				} else if ((cell.getColumnIndex() == 4)) {
					dates.add(cell);
				} else if ((cell.getColumnIndex() == 6)) {
					names.add(cell);
				} else if ((cell.getColumnIndex() == 8)) {
					duration.add(cell);
				} else if ((cell.getColumnIndex() == 7)) {
					status.add(cell);
				}
			}
		}
		projects.remove(0);
		dates.remove(0);
		names.remove(0);
		duration.remove(0);
		status.remove(0);

		// Fills in the blanks in the Array for a pivot table
		FileUtility utility = new FileUtility();
		utility.fillArray(dates);
		utility.fillArray(duration);
		utility.fillArray(names);
		utility.fillArray(projects);
		utility.fillArray(status);

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

	public ArrayList<Cell> fillArray(ArrayList<Cell> list) {
		DataFormatter formatter = new DataFormatter();
		Iterator<Cell> cellIT = list.iterator();
		int index = 0;
		while (cellIT.hasNext()) {
			Cell cell = cellIT.next();
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				String format = formatter.formatCellValue(list.get(index));
				cell.setCellValue(format);
				index++;
			}

		}
		System.out.println(index);
		return list;
	}

	public static String getTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}
}
