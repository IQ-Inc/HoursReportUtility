import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.*;
import java.util.prefs.Preferences;

import javax.swing.filechooser.FileNameExtensionFilter;
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
		fileChooser = new JFileChooser();
		fileChooser
				.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File", "xlsx");
		// filter that will only show Excel files
		fileChooser.setFileFilter(filter);
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
		FileInputStream fInput = new FileInputStream(selectedFile);
		XSSFWorkbook wb = new XSSFWorkbook(fInput);
		XSSFSheet sheet0 = wb.getSheetAt(1); // <-----change to 0 when finished with tool
		Iterator<Row> rowIt = sheet0.rowIterator();
		DataFormatter formatter = new DataFormatter();
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
		// Fills in the blanks in the Array for a pivot table
		Iterator<Cell> arrayIt = projects.iterator();
		int index = 0;
		while (arrayIt.hasNext()) {
			Cell cell = arrayIt.next();
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				String project = formatter.formatCellValue(projects.get(index));
				cell.setCellValue(project);
				index++;
			}
			
			
		}
		System.out.println(projects + " " + projects.size());

		int n = JOptionPane.showConfirmDialog(fileChooser, "Select Date Range");
		if (n == JOptionPane.OK_OPTION) {
			HoursReport report = new HoursReport();
			report.datePaneGUI();
		} else if (n == JOptionPane.NO_OPTION) {
			// add something here
			mImport.setEnabled(false);
		}

		ExcelWriter.outputFile(projects, dates, names, duration, status);
		wb.close();
	}

	public static String getTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}
}
