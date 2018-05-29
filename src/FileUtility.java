import java.io.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class FileUtility extends ReDoReport {
	
	 static JFileChooser fileChooser;
	 	static ArrayList<String> projects = new ArrayList<>();
		static ArrayList<Date> dates = new ArrayList<>();
		static ArrayList<String> names = new ArrayList<>();
		static ArrayList<Double> duration = new ArrayList<>();
	 public static void findFile() {
		fileChooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File","xlsx");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		//filter that will only show Excel files
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Select Input File");
		
		int status = fileChooser.showDialog(fileChooser, "Open");
		try { //options option to read file and executes if yes
			if(status == JFileChooser.APPROVE_OPTION) {
				 fileChooser.getSelectedFile();
				
				int n = JOptionPane.showConfirmDialog(fileChooser, "Read File?", "File Reader", 
						JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.OK_OPTION) { 
					readFile(fileChooser);
				}else if (n == JOptionPane.NO_OPTION) {
					findFile();
				}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void readFile(JFileChooser fileChooser) throws IOException{
		
		File selectedFile = fileChooser.getSelectedFile();
		FileInputStream fInput = new FileInputStream(selectedFile);
		XSSFWorkbook wb = new XSSFWorkbook(fInput);
		XSSFSheet sheet0 = wb.getSheetAt(1); // <-----change to 0 when finished with tool
		Iterator<Row> rowIt = sheet0.rowIterator();
		
		while(rowIt.hasNext()) { 
			XSSFRow row = (XSSFRow)rowIt.next();
			Iterator<Cell> cellIt = row.cellIterator();
		
		while(cellIt.hasNext()) {
			XSSFCell cell =(XSSFCell) cellIt.next();
			if(cell.getColumnIndex() == 0 &&
					cell.getCellType() != cell.CELL_TYPE_BLANK) {
				projects.add(cell);
			}else if(cell.getColumnIndex() == 4 &&
					cell.getCellType() != cell.CELL_TYPE_BLANK) { //<----block needs changed to work
				dates.add(cell);
			}else if(cell.getColumnIndex() == 6 &&
					cell.getCellType() != cell.CELL_TYPE_BLANK) {
				names.add(cell);
			}else if(cell.getColumnIndex() == 8 &&
					cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				duration.add(cell);
			}
		}
	}
		
		int n = JOptionPane.showConfirmDialog(fileChooser, "Select Date Range");
		if(n == JOptionPane.OK_OPTION) {
			ReDoReport report = new ReDoReport();
			report.datePaneGUI();
		} else if (n == JOptionPane.NO_OPTION) {
			// add something here 
			System.out.println("Chose not to change.");
		}
		
		ExcelWriter.outputFile(projects, dates, names, duration);
		wb.close();
}
	
	
	public static String getTimeStamp() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
			Date now = new Date();
			String strDate = df.format(now);
			return strDate;
		}
	} 
