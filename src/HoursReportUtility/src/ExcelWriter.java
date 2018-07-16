import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.LayoutManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilterColumn;

public class ExcelWriter {
	static JFileChooser fileChooser;
	public static final String OUTPUT_FILE = "Hours_" + FileUtility.getTimeStamp() + ".xlsx";

	public void filterGUI(List<Cell> list) {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		FileWriter writer = new FileWriter();
		writer.removeDupes(list);
		for (int i = 0; i < list.size(); i++) {
			listModel.addElement(list.get(i).getStringCellValue());
		}
		JList<String> employeeList = new JList(listModel);
		JFrame frame = new JFrame();
		JTextArea textArea = new JTextArea(15, 20);
		textArea.setEditable(false);
		JPanel topPane = new JPanel();
		JPanel bottomPane = new JPanel();
		topPane.add(new JScrollPane(employeeList));
		bottomPane.add(textArea);
		frame.add(bottomPane, BorderLayout.PAGE_END);
		frame.add(topPane, BorderLayout.PAGE_START);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Employee Filter");
		frame.setSize(200, 200);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		
		employeeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		employeeList.addListSelectionListener((new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					String employee = employeeList.getSelectedValue();
					textArea.append(employee + "\n");
				}
			}
		}));

		
	}

	public static void outputFile(List<Cell> projects, List<Cell> dates, List<Cell> names, List<Cell> duration,
			List<Cell> status) throws IOException {
		String[] header = { "Project", "Employee", "Date", "Hours", "Billing Status", "Cost", "Invoiced",
				"Profitability" };
		String[] dateHeader = { " ", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
				"Dec" };

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet0 = wb.createSheet("Profitability");
		XSSFSheet sheet1 = wb.createSheet("Utilization");
		// create new font for header
		Font font = wb.createFont();
		font.setBold(true);
		font.setColor(Font.COLOR_RED);
		font.setUnderline(Font.U_SINGLE);
		CellStyle headCellStyle = wb.createCellStyle();
		headCellStyle.setFont(font);
		DataFormat fmt = wb.createDataFormat();
		CellStyle textStyle = wb.createCellStyle();
		CellStyle numStyle = wb.createCellStyle();
		numStyle.setDataFormat(fmt.getFormat("#"));
		textStyle.setDataFormat(fmt.getFormat("@"));
		Row profitHeadRow = sheet0.createRow(0);
		Row utilHeadRow = sheet1.createRow(0);
		CreationHelper create = wb.getCreationHelper();

		// creates the profit sheet header
		for (int i = 0; i < header.length; i++) {
			Cell cell = profitHeadRow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(headCellStyle);
			sheet0.setDefaultColumnWidth(25);
		} // creates the date header
		for (int i = 1; i < dateHeader.length; i++) {
			Cell cell = utilHeadRow.createCell(i);
			cell.setCellValue(dateHeader[i] + "-" + year);
			cell.setCellStyle(headCellStyle);
		}

		// fills the sheets with the data read from Excel File
		for (int i = 1; i < projects.size(); i++) {
			Row row = sheet0.createRow(i);
			row.createCell(0).setCellValue(create.createRichTextString(projects.get(i).toString()));
			row.createCell(1).setCellValue(create.createRichTextString(names.get(i).toString()));
			row.createCell(2).setCellValue(create.createRichTextString(dates.get(i).toString()));
			Cell cell = row.createCell(3);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			Cell hoursCell = duration.get(i);
			hoursCell.setCellType(Cell.CELL_TYPE_NUMERIC);
			double value = hoursCell.getNumericCellValue();
			cell.setCellValue(value);
			row.createCell(4).setCellValue(create.createRichTextString(status.get(i).toString()));

		}
		sheet0.autoSizeColumn(0);
		sheet0.setAutoFilter(CellRangeAddress.valueOf("B1"));
		ExcelWriter writer = new ExcelWriter();
		writer.filterGUI(names);

		FileOutputStream out = new FileOutputStream(OUTPUT_FILE);
		wb.write(out);

	}

}
