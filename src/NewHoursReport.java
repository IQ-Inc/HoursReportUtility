import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class NewHoursReport extends JPanel  {
	 JFrame frame;
	 JLabel label;
	 Date date;
	 JPanel cards;
	 JButton homeBut;
	 JButton nextBut;
	 JButton prevBut;
	 JButton browse;
	 JButton exit;
	 JPanel panelContainer;
	 JFileChooser fileChooser;

	static String[] header = {"Month", "Customer", "Project", "Hours", "Cost", 
			"Invoiced" + "Profitability"};
	
	static ArrayList<String> projects = new ArrayList<>();
	static ArrayList<Date> dates = new ArrayList<>();
	static ArrayList<String>billingStatus = new ArrayList<>();
	static ArrayList<Double>duration = new ArrayList<>();
	static ArrayList<String> names = new ArrayList<>();
	
	
	
		public static void main(String[] args) throws IOException {
			NewHoursReport report = new NewHoursReport();
			createOutputFile(projects, dates, billingStatus, duration, names);
			
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
	
		public static void readFile( JFileChooser fileChooser, ArrayList projects, 
				ArrayList dates, ArrayList status, ArrayList duration, 
				ArrayList names) {
				
			
			
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
			
			
			int n = JOptionPane.showConfirmDialog(fileChooser, 
					"Overwrite Start and End Dates?");
					if (n == JOptionPane.OK_OPTION) { 
					changeDatePane();
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
					
					int n = JOptionPane.showConfirmDialog(fileChooser, "Read File?", "File Reader", 
							JOptionPane.YES_NO_OPTION);
				
					if(n == JOptionPane.OK_OPTION) { 
						readFile(fileChooser, projects, dates, billingStatus, duration, names);
						
					}else if (n == JOptionPane.NO_OPTION) {
						findFile();
					}
			}
			
			
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), "File not found.");
			}
			}
		
			//Need to redo this 
		public static void createOutputFile(ArrayList projects, ArrayList dates,
					ArrayList billingStatus, ArrayList duration, ArrayList names)
							throws IOException {
				XSSFWorkbook wb = new XSSFWorkbook();
				CreationHelper create = wb.getCreationHelper();
				XSSFSheet sheet1 = wb.createSheet("Profitability");
				XSSFSheet sheet2 = wb.createSheet("Utilization");
				
			
				
				CellStyle headerCellStyle = wb.createCellStyle();
				
				
				Row headerRow = sheet1.createRow(0);
				
				for (int i = 0; i < header.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(header[i]);
					cell.setCellStyle(headerCellStyle);
				}
				
			
				
			
				
				
				for (int i = 0; i < header.length; i++) {
					sheet1.autoSizeColumn(i);
				}
				int firstRow = sheet1.getFirstRowNum();
				int lastRow = sheet1.getLastRowNum();
				int firstCol = sheet1.getRow(0).getFirstCellNum();
				int lastCol = sheet1.getRow(0).getLastCellNum();
				
				CellReference topLeft = new CellReference(firstRow, firstCol);
				CellReference botRight = new CellReference(lastRow, lastCol - 1);
				
				AreaReference aref = new AreaReference(topLeft, botRight);
				CellReference pos = new CellReference(firstRow + 4, lastCol + 1);
				
				XSSFPivotTable pivotTable = sheet1.createPivotTable(aref, pos);
				
				
				pivotTable.addRowLabel(0);
				pivotTable.addRowLabel(1);
				
						
				FileOutputStream out = new FileOutputStream("Hours_" + 
						getCurrentTimeStamp() + ".xlsx");
				
				wb.write(out);
				out.close();
				
			
					
				
			}
		
		public static String getCurrentTimeStamp() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
			Date now = new Date();
			String strDate = df.format(now);
			return strDate;
		}
		
		public static class browseListenerClass implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					findFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
	
	
	
	
