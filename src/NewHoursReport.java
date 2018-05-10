import java.awt.BorderLayout;
import java.awt.Desktop;
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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class NewHoursReport extends JPanel  {
	JFrame frame;
	JLabel label;
	Date date;
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		NewHoursReport report = new NewHoursReport(frame);
		
		
	}

	
		public NewHoursReport(JFrame frame) {
		super(new BorderLayout());
		this.frame = frame;
		
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
	
	public JButton createButton(int width, int height, String name) {
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
	
	public static JFrame changeDatePane(JFrame frame, JPanel topPane, JPanel bottomPane) {
		// Note to self - Use box layout here to make labels and text fields even
		// Not finished yet
		topPane = new JPanel();
		bottomPane = new JPanel();
		JLabel start = new JLabel("Start Date");
		JLabel end = new JLabel("End Date");
		
		frame = createJFrame(topPane, bottomPane,300, 100);
		
		return frame;
	}
	
	
	public static void readFile(JFileChooser fileChooser, int result) {
		
		
		try {
			
				File selectedFile = fileChooser.getSelectedFile();
				FileInputStream fileInput = new FileInputStream(selectedFile);
				XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
				XSSFSheet sheet = workbook.getSheetAt(0);
				
				//Iterator to scan through sheets
				Iterator<Row> rowIterator = sheet.iterator();
				
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							// Method to write to file instead of console (for both case)
							System.out.print(cell.getNumericCellValue() + "\t");
							
							break;
						case Cell.CELL_TYPE_STRING:
							System.out.print(cell.getStringCellValue() + "\t");
						}
					}
				}
	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "File not found.");
		}
		JOptionPane.showConfirmDialog(fileChooser, "Overwrite Start and End Dates?");
		if (true) { // <-----add here after creating pane to hold start end dates
			
			
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
					readFile(fileChooser, result);
				}
		}
		
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "File not found.");
		}
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
}
	
	
	
