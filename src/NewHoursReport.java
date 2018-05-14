import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class NewHoursReport extends JPanel  {
	JFrame frame;
	JLabel label;
	Date date;
	static String [] header = {"Project, Date, Name, Billing Status, Duration"};
	static ArrayList<String> projects = new ArrayList<>();
	static ArrayList<Date> dates = new ArrayList<>();
	static ArrayList<String>billingStatus = new ArrayList<>();
	static ArrayList<Double>duration = new ArrayList<>();
	static ArrayList<String> names = new ArrayList<>();
	
	
	public static void main(String[] args) throws IOException {
		
		NewHoursReport report = new NewHoursReport();
		createOutputBook();
		
		 
	}
		
		
		
		
	

	
		public NewHoursReport() {
		super(new BorderLayout());
		
		this.frame = new JFrame();
		
		JLabel label = new JLabel("Please select a file to open");
		
		//create the panel to add it to
		JPanel topPane = new JPanel();
		JPanel bottomPane = new JPanel();
		
		//create 2 buttons Browse and Cancel
		JButton browse = createButton(40,40,"Browse");
		JButton cancel = createButton(40,40,"Cancel");
		browseListenerClass listener1 = new browseListenerClass();
		cancelListenerClass listener2 = new cancelListenerClass();
		browse.addActionListener(listener1);
		cancel.addActionListener(listener2);
		
		
		
		// Add all to the panes
		bottomPane.add(browse);
		bottomPane.add(cancel);
		topPane.add(label);
		createJFrame(topPane, bottomPane, 300,100);
		frame.pack();
		
	
		
	}
	
	public static JButton createButton(int width, int height, String name) {
		JButton button = new JButton();
		button.setSize(width, height);
		button.setText(name);
		return button;
	}
	
	public static JFrame createJFrame(JPanel topPane, JPanel bottomPane, 
			int width, int height) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(topPane, BorderLayout.PAGE_START);
		frame.add(bottomPane, BorderLayout.PAGE_END);
		frame.setSize(width, height);
		frame.setVisible(true);
		return frame;
	}
	
	public static JFrame changeDatePane() {
		JFrame frame = new JFrame("Change Dates");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Buttons
		JButton next = createButton(40,40,"Next");
		JButton back = createButton(40,40,"Back");
		
		//Create Labels
		JLabel start = new JLabel("Start Date");
		JLabel end = new JLabel("End Date");
		
		//TextFields
		JTextField startDate = new JTextField("MM/DD/YYYY");
		JTextField endDate = new JTextField("MM/DD/YYYY");
		
		//Create Panes and Layouts
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
		JPanel rightPane = new JPanel();
		JPanel bottomPane = new JPanel();
		bottomPane.add(back);
		bottomPane.add(Box.createHorizontalGlue());
		bottomPane.add(next);
		
		//add components to pane
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		leftPane.add(start);
		leftPane.add(Box.createVerticalGlue());
		leftPane.add(startDate);
		
		rightPane.add(end);
		rightPane.add(Box.createVerticalGlue());
		rightPane.add(endDate);
		
		
		//add everything to pane
		frame.add(bottomPane, BorderLayout.PAGE_END);
		frame.add(leftPane, BorderLayout.WEST);
		frame.add(Box.createHorizontalGlue());
		frame.add(rightPane, BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
		
		return frame;
		}
	
	
	public static void readFile( JFileChooser fileChooser, List projects, List dates,
			List status, List duration, List names) {
		
		
		try {
			
				File selectedFile = fileChooser.getSelectedFile();
				FileInputStream fileInput = new FileInputStream(selectedFile);
				XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
				XSSFSheet sheet = workbook.getSheetAt(1);
				
				//Iterator to scan through sheets
				Iterator<Row> rows = sheet.rowIterator();
				
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow)rows.next();
					
					Iterator<Cell> cells = row.cellIterator();
					
					while (cells.hasNext()) { 
						XSSFCell cell = (XSSFCell) cells.next();
						// If statements to fill proper arrays with data 
						// to append to output arrays
					if (cell.getColumnIndex() == 1) {
						projects.add(cell);
					} else if (cell.getColumnIndex() == 4) {
						dates.add(cell);
					} else if (cell.getColumnIndex() == 6) {
						names.add(cell);
					} else if (cell.getColumnIndex() == 7) {
						status.add(cell);
					} else if (cell.getColumnIndex() == 8) {
						duration.add(cell);
					}
				
					}
				}
				
	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "File not found.");
		}
		
		
		int n = JOptionPane.showConfirmDialog(fileChooser, "Overwrite Start and End Dates?");
				if (n == JOptionPane.OK_OPTION) { 
				changeDatePane();
		} else {
			System.exit(0);
		}
		
		
	}
	
	public static void findFile() throws IOException {
		
		//Creates file chooser for browsing and selection
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setSize(400,400);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		//filter that will only show Excel files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files" , "xlsx");
		fileChooser.setFileFilter(filter);
		fileChooser.setApproveButtonText("Next");
		
		
		int status = fileChooser.showOpenDialog(null);
		
		try { //options option to read file and executes if yes
			if(status == JFileChooser.APPROVE_OPTION) {
				File fileToOpen = fileChooser.getSelectedFile();
				JOptionPane.showConfirmDialog(fileChooser, "Read File?", "File Reader", 
						JOptionPane.YES_NO_OPTION);
			int result = JOptionPane.OK_OPTION;
				if(result == JOptionPane.OK_OPTION) { 
					readFile(fileChooser, projects, dates, billingStatus, duration, names);
					
				}else if (result == JOptionPane.NO_OPTION) {
					findFile();
				}
		}
		
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "File not found.");
		}
		}
	
	public static XSSFWorkbook createOutputBook () throws IOException {
	
		
		
		//Create new workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		
		// Create Sheets
		Sheet sheet0 = workbook.createSheet("Profitability");
		Sheet sheet1 = workbook.createSheet("Utilization");
		
		//Format
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)8);
		headerFont.setColor(IndexedColors.BLUE.getIndex());
		
		//Create a cell style using the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		
		//Create Row
		Row headerRow = sheet0.createRow(0);
		
		//Create Cells
		for (int i = 0; i < header.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(headerCellStyle);
			
		//Create Date Format cells
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM-dd-yyyy"));
		}
	
		
		for (int i = 0; i < projects.size(); i++) {
			Row row = sheet0.createRow(i);
			row.createCell(0).setCellValue(projects.get(i));
			System.out.println(row.getCell(i));
			row.createCell(1).setCellValue(dates.get(i));
			row.createCell(2).setCellValue(names.get(i));
			row.createCell(3).setCellValue(billingStatus.get(i));
			row.createCell(4).setCellValue(duration.get(i));
			
			FileOutputStream fileOut = new FileOutputStream("C://temp//Hours_" + 
			getCurrentTimeStamp() + ".xlsx");
			fileOut.write(i);
			fileOut.close();
			workbook.close();
		
}
		return workbook;
	}
	
	public static String getCurrentTimeStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
		Date now = new Date();
		String strDate = df.format(now);
		return strDate;
	}
	
	
	
	
//Listener classes
	
	public static class browseListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				findFile();
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
		
}
	public static class cancelListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			
			
		}
		
	}
	
	public static class backListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//add something here 
			
		}
		
	}
}
	
	
	
