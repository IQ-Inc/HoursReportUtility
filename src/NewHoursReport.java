import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class NewHoursReport extends JFrame  {
	  static JFrame frame;
	 JLabel label;
	 Date date;
	 JFrame dateFrame;
	 JButton homeBut;
	 JButton nextBut;
	 JButton prevBut;
	 JButton browse;
	 JButton exit;
	 JPanel panelContainer;
	 JFileChooser fileChooser;
	 static JTextField startDate;
	 static JTextField endDate;

	 public static final String OUTPUT_FILE = "Hours_" + getCurrentTimeStamp() +
				".xlsx";

	static String[] header = {"Month", "Customer", "Project", "Hours", "Cost", 
			"Invoiced", "Profitability"};
	static String[] dateHeader = {" ","Jan","Feb","Mar","Apr", "May","Jun","Jul",
			"Aug","Sep","Oct","Nov","Dec"};
	
			
			
	static ArrayList<Object> input = new ArrayList<>();
	static ArrayList<Object> projects = new ArrayList<>();
	static ArrayList<Object> dates = new ArrayList<>();
	static ArrayList<Object> billStatus = new ArrayList<>();
	static ArrayList<Object> names = new ArrayList<>();
	static ArrayList<Object> duration = new ArrayList<>();
	
	
		public static void main(String[] args) throws IOException {
			NewHoursReport report = new NewHoursReport();
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowFrame();
				}
			});
			
		}
		
		// next 4 methods set up the GUI
		public static JFrame createAndShowFrame() {
	 		
	 		frame = new JFrame();
	 		
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
	 		createJFrame(topPane, bottomPane, 300,150);
	 		frame.pack();
	 		
	 	return frame;
	 		
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
	 		JFrame dateFrame = new JFrame("Change Dates");
	 		dateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 		
	 		
	 		nextListenerClass listener3 = new nextListenerClass();
	 		backListenerClass listener4 = new backListenerClass();
	 		//Buttons
	 		JButton next = createButton(40,40,"Next");
	 		JButton back = createButton(40,40,"Back");
	 		next.addActionListener(listener3);
	 		back.addActionListener(listener4);
	 		
	 		//Create Labels
	 		JLabel start = new JLabel("Start Date");
	 		JLabel end = new JLabel("End Date");
	 		
	 		//TextFields
	 		 startDate = new JTextField("mm/dd/yyyy", 10);
	 		 endDate = new JTextField("mm/dd/yyyy", 10);
	 		
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
	 		dateFrame.add(bottomPane, BorderLayout.PAGE_END);
	 		dateFrame.add(leftPane, BorderLayout.WEST);
	 		dateFrame.add(Box.createHorizontalGlue());
	 		dateFrame.add(rightPane, BorderLayout.EAST);
	 		dateFrame.pack();
	 		dateFrame.setVisible(true);
	 		
	 		return dateFrame;
	 	}
	 	
	 	public static List<Date> setDate(String start, String end) 
	 			throws ParseException {
	 		
	 		
	 		List<Date> datesInRange = new ArrayList<>();
	 		
	 		try {
	 		start = startDate.getText();
	 		end = endDate.getText();
	 		Date sDate = new SimpleDateFormat("MM/dd/yyyy").parse(start);
	 		Date eDate = new SimpleDateFormat("MM/dd/yyyy").parse(end);
	 		
	 		
	 		Calendar calendar = new GregorianCalendar();
	 		Calendar endCalendar = new GregorianCalendar();
	 		endCalendar.setTime(eDate);
	 		calendar.setTime(sDate);
	 		
	 		while(calendar.before(endCalendar)) {
	 			Date result = calendar.getTime();
	 			datesInRange.add(result);
	 			calendar.add(Calendar.DATE, 1);
	 		}
	 } catch (ParseException e) {
	 		// Doesn't allow the user to move on until a valid date is entered	
		 	changeDatePane(); 
	 			
	 		}
	 		
	 		
	 		return datesInRange;
	 	}

	
		public static void readFile( JFileChooser fileChooser, ArrayList<Object> input) 
						throws IOException {
				
			createOutputFile();
			
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
							input.add(cell);
							
						}
							
					}
					System.out.println(input);
					workbook.close();
		
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), "File not found.");
			}
			
			
			// to switch the dates if ok
			int n = JOptionPane.showConfirmDialog(fileChooser, 
					"Overwrite Start and End Dates?");
					if (n == JOptionPane.OK_OPTION) {
					changeDatePane();
					}
					
					else if (n == JOptionPane.NO_OPTION) {
						System.out.println("Chose not to change");
					
					}
					
			 
	
			
}
		
		public static void findFile() throws IOException {
			
			input = new ArrayList<Object>();
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
					 fileChooser.getSelectedFile();
					
					int n = JOptionPane.showConfirmDialog(fileChooser, "Read File?", "File Reader", 
							JOptionPane.YES_NO_OPTION);
				
					if(n == JOptionPane.OK_OPTION) { 
						readFile(fileChooser, input);
						
					}else if (n == JOptionPane.NO_OPTION) {
						findFile();
					}
			}
			
			
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), "File not found.");
			}
			}
		
			//Need to redo this 
		public static void createOutputFile() throws IOException {
				Calendar now = Calendar.getInstance();
				int year = now.get(Calendar.YEAR);
				XSSFWorkbook wb = new XSSFWorkbook();
				
				XSSFSheet sheet1 = wb.createSheet("Profitability");
				XSSFSheet sheet2 = wb.createSheet("Utilization");
				
			
				// Creates the output file with the header needed
				CellStyle headerCellStyle = wb.createCellStyle();
				
				
				Row headerRow = sheet1.createRow(0);
				Row dateRow = sheet2.createRow(0);
				
				//Populates the headers for both sheets
				for (int i = 0; i < header.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(header[i]);
					cell.setCellStyle(headerCellStyle);
				}
				
				
				for (int i = 1; i < dateHeader.length; i++) {
					Cell cell = dateRow.createCell(i);
					cell.setCellValue(dateHeader[i] + "-" + year );
					
				}
				
				FileOutputStream out = new FileOutputStream("Hours_" + 
						getCurrentTimeStamp() + ".xlsx");
				
				wb.write(out);
				wb.close();
				out.close();
				
		}
	
		// method that returns date for save path 
		public static String getCurrentTimeStamp() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HHmmss");
			Date now = new Date();
			String strDate = df.format(now);
			return strDate;
		}
		
		//Listener classes
		
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
			public void actionPerformed(ActionEvent e) {	
			String command = e.getActionCommand();
			
			if(command.equals("Back")) {
				changeDatePane();
			}
				
	}
			
		}
		
		public static class nextListenerClass implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				//select an option is parent dialog
				
				try {
					setDate(startDate.getText(), endDate.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		
		}
				
}
		
	
	
	
	
	
