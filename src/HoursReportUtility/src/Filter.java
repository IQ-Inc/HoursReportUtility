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
	JFrame frame;
	JFileChooser chooser;
	Filter filter;
	JButton filterBut;
	JCheckBox checkBox;
	static JTextArea areaNames;
	List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
	static ArrayList<String> boxText;

	public void filterGUI(List<Cell> names) {
		// Creates the GUI
		Filter filter = new Filter();
		JPanel containerPanel = new JPanel(new BorderLayout());

		JPanel bottomPanel = new JPanel();
		areaNames = new JTextArea(10, 15);
		JScrollPane sPane = new JScrollPane(filter.fillPanel(names));
		sPane.setPreferredSize(new Dimension(200, 200));
		sPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sPane.getVerticalScrollBar().setUnitIncrement(20);
		sPane.setWheelScrollingEnabled(true);

		JButton filterBut = filter.createButton(70, 50, "Filter");
		filterBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Filter filter = new Filter();
				FileUtility utility = new FileUtility();
				try {
					utility.readFile(null, ExcelWriter.OUTPUT_FILE, boxText);
				} catch (Exception e1) {

					e1.printStackTrace();
				}
				filter.filter(FileUtility.names, FileUtility.dates, FileUtility.duration, FileUtility.projects,
						FileUtility.status, Filter.boxText, FileUtility.notBillType);

			}
		});

		JButton backButton = filter.createButton(70, 50, "Back");
		backButton.addActionListener(new ActionListener() {
			// Starts a new Thread when "Back" is Pressed
			public void actionPerformed(ActionEvent e) {
				FileUtility.names.clear();
				FileUtility.dates.clear();
				FileUtility.duration.clear();
				FileUtility.notBillType.clear();
				FileUtility.projects.clear();
				FileUtility.status.clear();
				frame.setVisible(false);
				(new Thread(new HoursReportDriver())).start();

			}
		});
		bottomPanel.add(backButton);
		bottomPanel.add(filterBut);
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(sPane, BorderLayout.NORTH);

		containerPanel.add(bottomPanel, BorderLayout.SOUTH);
		frame = new JFrame("Employee Filter");
		frame.add(containerPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
	// creates new buttons
	public JButton createButton(int width, int height, String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(width, height));

		return button;

	}
	// fills the Scroll Pane with empployee name
	public JPanel fillPanel(List<Cell> names) {

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
			if (i == 0) {
				// Action Listener for select all - selects or deselects all
				JCheckBox selectAll = new JCheckBox("Select All");
				selectAll.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (selectAll.isSelected()) {
							Filter filter = new Filter();
							filter.checkAll(panel);
						} else if (!selectAll.isSelected()) {
							Filter filter = new Filter();
							filter.deselectAll(panel);
						}
					}
				});
				panel.add(selectAll);
			}
			if (empNames[i].equals("")) {
				continue;
			}
			checkBox = new JCheckBox(empNames[i]);
			checkBoxes.add(checkBox);
			checkBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					boxText = new ArrayList<String>();
					for (JCheckBox box : checkBoxes) {
						if (box.isSelected()) {
							boxText.add(box.getText());
						}
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
			ArrayList<Cell> status, ArrayList<String> checked, ArrayList<Cell> type) {
		ArrayList<Cell> fNames = new ArrayList<Cell>();
		ArrayList<Cell> fDates = new ArrayList<Cell>();
		ArrayList<Cell> fDuration = new ArrayList<Cell>();
		ArrayList<Cell> fStatus = new ArrayList<Cell>();
		ArrayList<Cell> fProjects = new ArrayList<Cell>();
		ArrayList<Cell> fType = new ArrayList<Cell>();
// loop creates filtered arrays 
		for (int i = 0; i < checked.size(); i++) {

			for (int j = 1; j < names.size(); j++) {
				if (checked.get(i).equals(names.get(j).getStringCellValue())) {
					fNames.add(names.get(j));
					fDates.add(dates.get(j));
					fDuration.add(duration.get(j));
					fStatus.add(status.get(j));
					fProjects.add(projects.get(j));
					fType.add(type.get(j));
				}
			}
		}

		try {
			ExcelWriter.outputFile(fProjects, fDates, fNames, fDuration, fStatus, fType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// methods for "Select All" checkbox
	public void checkAll(JPanel scroll) {
		ArrayList<Component> components = new ArrayList<>();
		for (int i = 0; i < scroll.getComponentCount(); i++) {
			if (scroll.getComponent(i) instanceof JCheckBox) {
				components.add(scroll.getComponent(i));
			}
		}
		for (Component component : components) {
			if (component instanceof JCheckBox) {
				((JCheckBox) component).setSelected(true);
			}
		}
	}

	public void deselectAll(JPanel scroll) {
		ArrayList<Component> components = new ArrayList<>();
		for (int i = 0; i < scroll.getComponentCount(); i++) {
			if (scroll.getComponent(i) instanceof JCheckBox) {
				components.add(scroll.getComponent(i));
			}
		}
		for (Component component : components) {
			if (component instanceof JCheckBox) {
				((JCheckBox) component).setSelected(false);
			}
		}

	}
}
