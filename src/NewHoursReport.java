import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.NumberFormat.Field;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class NewHoursReport extends JPanel  {
	JFrame frame;
	JLabel label;
	Date date;
	
	
	public static void main(String[] args) {
		
		
		NewHoursReport report = new NewHoursReport();
		
		
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
	
	
	public static void readFile( JFileChooser fileChooser) {
		
		
		try {
			
				File selectedFile = fileChooser.getSelectedFile();
				FileInputStream fileInput = new FileInputStream(selectedFile);
				XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
				XSSFSheet sheet = workbook.getSheetAt(0);
				
				//Iterator to scan through sheets
				Iterator<Row> rows = sheet.rowIterator();
				
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow)rows.next();
					
					Iterator<Cell> cells = row.cellIterator();
					
					while (cells.hasNext()) {
						XSSFCell cell = (XSSFCell) cells.next();
							switch(cell.getCellType()) {
							case XSSFCell.CELL_TYPE_NUMERIC:
								System.out.println(cell.getNumericCellValue());
								break;
							case XSSFCell.CELL_TYPE_STRING:
								System.out.println(cell.getStringCellValue());
								break;
						}
				
					}
				}
	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "File not found.");
		}
		JOptionPane.showConfirmDialog(fileChooser, "Overwrite Start and End Dates?");
		int result = JOptionPane.OK_OPTION;
		
				if (result != JOptionPane.OK_OPTION) { // <-----add here after creating pane to hold start end dates
			System.exit(0);
		} else {
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
				JOptionPane.showConfirmDialog(fileChooser, "Read File?", "File Reader", 
						JOptionPane.YES_NO_OPTION);
			String result = JOptionPane.showInputDialog(null);
				if(result.equals(JOptionPane.OK_OPTION)){
					readFile(fileChooser);
					
				}else if (result.equals(JOptionPane.NO_OPTION)) {
					findFile();
				}
		}
		
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "File not found.");
		}
		}
	
	//Over-write method here <-------------
	
		 
	
	
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
	
	
	
