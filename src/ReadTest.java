import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadTest {
	
	public static final String OUTPUT_FILE = "Hours_" + getCurrentTimeStamp() +
			".xlsx";
	 public static ArrayList<Object> fields = new ArrayList<>();
	
	
	public static void main(String[] args) throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		
		int status = fileChooser.showOpenDialog(null);
		
		if (status == fileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			fileReader(selectedFile, fields, fileChooser);
		}
		
		
		
		
		
		
		
}
	
	
	
	public static void fileReader(File selectedFile, ArrayList fields, JFileChooser
			fileChooser) throws IOException {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", 
				"xlsx");
		fileChooser.setFileFilter(filter);
		
		
		try {
			FileInputStream excelFile = new FileInputStream(
					new File(selectedFile.getAbsolutePath()));
			XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
			XSSFSheet dataTypeSheet = workbook.getSheetAt(1);
			Iterator<Row> rowIterator = dataTypeSheet.iterator();
			
			while(rowIterator.hasNext()) {
				Row currentRow = rowIterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					
					if (currentCell.getCellTypeEnum() == CellType.STRING) {
						
						fields.add(currentCell);
					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
						
						fields.add(currentCell);
					}
				}
				
			}
			
			createOutputFile(OUTPUT_FILE, selectedFile, fields);
							
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}
	
	public static void createOutputFile(String OUTPUT_FILE, File excelFile, 
			ArrayList fields) throws IOException {
		
		
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet1 = workbook.createSheet("Profitability");
		XSSFSheet sheet2 = workbook.createSheet("Utilization");
		String[] header = {"Month", "Customer", "Project", "Hours", "Cost", 
				"Invoiced" + "Profitability"};
		
		int rowNum = 0;
		
		for (Object head : header) {
			Row row = sheet1.createRow(rowNum++);
			int colNum = 0;
			for (Object field : fields) {
				Cell cell= row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				} else if (field instanceof Date){
					cell.setCellValue((Date) field);
				}
				
				
				
				
			}
		}
		writeFile(OUTPUT_FILE, workbook);
		
	}
	
	public static XSSFWorkbook writeFile(String OUTPUT_FILE, XSSFWorkbook workbook) 
			throws IOException {
		FileOutputStream output = new FileOutputStream(new File(OUTPUT_FILE));
		workbook.write(output);
		output.close();
		workbook.close();
		return workbook;
		
	}
		
	public static String getCurrentTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}
}
