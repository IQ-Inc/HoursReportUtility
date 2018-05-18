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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadTest {
	
	public static final String OUTPUT_FILE = "Hours_" + getCurrentTimeStamp() +
			".xlsx";
	static ArrayList<Object> fields = new ArrayList<>();
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		
		JFileChooser fileChooser = new JFileChooser();
		int status = fileChooser.showOpenDialog(null);
		try {
		if (status == JFileChooser.APPROVE_OPTION) {
			File fileToOpen = fileChooser.getSelectedFile();
			fileReader(fileChooser, fileToOpen, fields);
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
		
		
	}
	
	public static void fileReader(JFileChooser fileChooser,
			File fileToOpen, ArrayList<Object> fields) throws IOException {
		
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", 
				".xlsx");
		fileChooser.setFileFilter(filter);
		
		
		try {
			FileInputStream excelFile = new FileInputStream(
					new File(fileToOpen.getAbsolutePath()));
			
			XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			Iterator<Row> rowIterator = sheet1.iterator();
			
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
			
			
						
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static XSSFWorkbook createOutputFile(String OUTPUT_FILE,
			ArrayList<Object> fields)throws IOException {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet1 = workbook.createSheet("Profitability");
		XSSFSheet sheet2 = workbook.createSheet("Utilization");
		Object [] header = {"Month", "Customer", "Project", "Hours", "Cost", 
				"Invoiced" + "Profitability"};
		
		int rowNum = 0;
		
	
		for (Object head : header) {
			Row row = sheet1.createRow(rowNum);
		
			int colNum = 0;
			for (Object field : fields) {
				Cell cell = row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				}
				else if (field instanceof Double) {
					cell.setCellValue((double) field);
				}else if (field instanceof Date) {
					cell.setCellValue((Date) field);
				}
				
			}
		}
		
		
		return workbook;
		
				
		
	}
		
	
	
	public static XSSFWorkbook writeFile(XSSFWorkbook workbook, 
			String OUTPUT_FILE) throws IOException {
		
		try {
			FileOutputStream out = new FileOutputStream(new File(OUTPUT_FILE));
			workbook.write(out);
			out.close();
			workbook.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return workbook;
		
	}
		
	public static String getCurrentTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}
}
