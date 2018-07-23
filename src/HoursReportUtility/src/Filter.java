import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Filter {

	static JTextArea nameArea;
	JComboBox<String> namesBox;

	public void filterGUI(List<Cell> names) {
		Filter filter = new Filter();
		namesBox = new JComboBox<String>(filter.fillCBox(names));
		nameArea = new JTextArea(10, 15); // will go to middle pane
		nameArea.setEditable(false);
		namesBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedName = (String) namesBox.getSelectedItem();
				nameArea.append(selectedName + '\n');
			}

		});
		JButton filterButton = createButton(100, 50, "Filter"); // added to bottom pane
		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					FileUtility.readFile(null, ExcelWriter.OUTPUT_FILE, nameArea);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton removeButton = createButton(100, 50, "Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameArea.replaceSelection("");
			}
		});
		JPanel topPane = new JPanel(new BorderLayout(10, 10));
		JPanel midPane = new JPanel(new BorderLayout(10, 10));
		JPanel bottomPane = new JPanel(new FlowLayout());
		topPane.add(namesBox);
		midPane.add(nameArea);
		bottomPane.add(filterButton);
		bottomPane.add(removeButton);
		JFrame frame = new JFrame("Employee Filter");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.add(topPane, BorderLayout.PAGE_START);
		frame.add(midPane, BorderLayout.CENTER);
		frame.add(bottomPane, BorderLayout.PAGE_END);
		frame.pack();
		frame.setVisible(true);

	}

	public static JButton createButton(int width, int height, String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(width, height));

		return button;

	}

	public String[] fillCBox(List<Cell> list) {
		FileWriter writer = new FileWriter();
		writer.removeDupes(list);
		int n = list.size();
		String[] names = new String[n];

		for (int i = 0; i < n; i++) {
			names[i] = (String) list.get(i).getStringCellValue();
		}
		Arrays.sort(names);
		return names;
	}

	// Implement methods to copy new contents from the arrays to output file
	// Need to compare TextArea to contents in the array so they can be filtered
	public void filter(ArrayList<Cell> names, ArrayList<Cell> dates, ArrayList<Cell> duration, ArrayList<Cell> projects,
			ArrayList<Cell> status, JTextArea area) {
		ArrayList<Cell> fNames = new ArrayList<Cell>();
		ArrayList<Cell> fDates = new ArrayList<Cell>();
		ArrayList<Cell> fDuration = new ArrayList<Cell>();
		ArrayList<Cell> fStatus = new ArrayList<Cell>();
		ArrayList<Cell> fProjects = new ArrayList<Cell>();
		String[] empNames = area.getText().split("\n");
		ArrayList<String> areaNames = new ArrayList<String>(Arrays.asList(empNames));
		System.out.println(areaNames);
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
