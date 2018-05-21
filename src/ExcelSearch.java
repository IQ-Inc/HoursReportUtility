import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelSearch extends NewHoursReport {
	
	public static void main(String[] args) {
		
	}
	
	
	public static ArrayList<Row> searchSheet(String searchText, XSSFSheet sheet, 
		NewHoursReport report ) throws FileNotFoundException{
		
		
		
		
		Double doubleValue = null;
		Boolean booleanValue = null;
		ArrayList<Row> filteredRows = new ArrayList<Row>();
		
		try {
			doubleValue = Double.parseDouble(searchText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			booleanValue = Boolean.parseBoolean(searchText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
			
			XSSFRow row = sheet.getRow(j);
			
			for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
				XSSFCell cell = row.getCell(i);
				
				switch (cell.getCellType()) {
				
				case XSSFCell.CELL_TYPE_NUMERIC:
					if (doubleValue != null && doubleValue.doubleValue() ==
					cell.getNumericCellValue()) {
						filteredRows.add(row);
					}
					break;
				
				case XSSFCell.CELL_TYPE_STRING:
					if(searchText != null && searchText.equals(cell.getStringCellValue())) {
						filteredRows.add(row);
					}
					break;
					
				case XSSFCell.CELL_TYPE_BOOLEAN:
					if (booleanValue != null && booleanValue.booleanValue() == 
					cell.getBooleanCellValue()) {
						filteredRows.add(row);
					}
					break;
				
				default:
					if(searchText != null && searchText.equals(cell.getStringCellValue())){
						filteredRows.add(row);
					}
					break;
				}
			}
		}
		return filteredRows;
	}
}
