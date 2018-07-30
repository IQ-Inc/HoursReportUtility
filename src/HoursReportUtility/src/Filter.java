import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import org.apache.poi.ss.usermodel.Cell;

public class Filter {
	Filter filter;
	JButton filterBut;
	JCheckBox checkBox;
	static JTextArea areaNames;

	public void filterGUI(List<Cell> names) {
		// Create instance of Class and all containers needed for the frame
		Filter filter = new Filter();
		JPanel containerPanel = new JPanel(new BorderLayout());
		JPanel midPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		areaNames = new JTextArea(10, 15);
		JScrollPane sPane = new JScrollPane(filter.fillPanel(names, areaNames));
		sPane.setPreferredSize(new Dimension(100, 150));
		sPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		midPanel.add(areaNames);
		bottomPanel.add(filter.createButton(70, 50, "Filter"));
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(sPane, BorderLayout.NORTH);
		containerPanel.add(midPanel, BorderLayout.CENTER);
		containerPanel.add(bottomPanel, BorderLayout.SOUTH);
		JFrame frame = new JFrame("Employee Filter");
		frame.add(containerPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public JButton createButton(int width, int height, String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(width, height));
		Filter filter = new Filter();
				// filter.filter(FileUtility.names, FileUtility.dates, FileUtility.duration,
				// FileUtility.projects,
				// FileUtility.status, filter.areaNames);
			
		
		
		return button;

	}

	public JPanel fillPanel(List<Cell> names, JTextArea areaNames) {

		FileWriter writer = new FileWriter();
		writer.removeDupes(names);

		// Removes "Name" from list "names"
		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).getStringCellValue().equals("Name")) {
				names.remove(i);
			}
		}
		int n = names.size();
		String[] empNames = new String[n];
		for (int i = 0; i < names.size(); i++) {
			empNames[i] = names.get(i).getStringCellValue();
		}
		Arrays.sort(empNames);
		JPanel panel = new JPanel();

		// Create checkBoxes for all names in the list and adds to the pane
		for (int i = 0; i < empNames.length; i++) {
			checkBox = new JCheckBox(empNames[i]);
			checkBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if(checkBox.isSelected()) {
						areaNames.setText(checkBox.getText());
					}
					
				}
				
			});
			panel.add(checkBox);
		}
	

		

		panel.setLayout(new GridLayout(0, 1));
		return panel;
	
	}
	

	// Methods to copy new contents from the arrays to output file

	public void filter(ArrayList<Cell> names, ArrayList<Cell> dates, ArrayList<Cell> duration, ArrayList<Cell> projects,
			ArrayList<Cell> status, JTextArea area) {
		ArrayList<Cell> fNames = new ArrayList<Cell>();
		ArrayList<Cell> fDates = new ArrayList<Cell>();
		ArrayList<Cell> fDuration = new ArrayList<Cell>();
		ArrayList<Cell> fStatus = new ArrayList<Cell>();
		ArrayList<Cell> fProjects = new ArrayList<Cell>();
		String[] empNames = area.getText().split("\n");
		ArrayList<String> areaNames = new ArrayList<String>(Arrays.asList(empNames));

		for (int i = 0; i < areaNames.size(); i++) {
			for (int j = 0; j < names.size(); j++) {
				if (areaNames.get(i).equals(names.get(j).getStringCellValue())) {
					fNames.add(names.get(j));
					fDates.add(dates.get(j));
					fDuration.add(duration.get(j));
					fStatus.add(status.get(j));
					fProjects.add(projects.get(j));
				}
			}
		}

		try {
			ExcelWriter.outputFile(fProjects, fDates, fNames, fDuration, fStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
