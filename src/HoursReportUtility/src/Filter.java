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
	JComboBox<String> namesBox;
	static JTextArea nameArea;

	public void filterGUI() {
		Filter filter = new Filter();
		FileUtility utility = new FileUtility();
		namesBox = new JComboBox<String>(filter.fillCBox(utility.names)); // added to top pane
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
				Filter filter = new Filter();
				try {
					filter.readOutFile(ExcelWriter.OUTPUT_FILE, nameArea);
				} catch (IOException e1) {
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

	public void readOutFile(String outFile, JTextArea area) throws IOException {
		File output = new File(outFile);
		String[] fNames = area.getText().split("\n");
		ArrayList<String> filterNames = new ArrayList<>(Arrays.asList(names));
		for (int i = 0; i < filterNames.size(); i++) {
			if (filterNames.get(i).equals("")) {
				filterNames.remove(i);
				i--;
			}
		}
		FileUtility.names.clear();
		FileUtility.dates.clear();
		FileUtility.duration.clear();
		FileUtility.projects.clear();
		FileUtility.status.clear();

		FileInputStream fInput = new FileInputStream(output);
		XSSFWorkbook wb = new XSSFWorkbook(fInput);
		XSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> rowIT = sheet.rowIterator();
		while (rowIT.hasNext()) {
			XSSFRow row = (XSSFRow) rowIT.next();
			Iterator<Cell> cellIT = row.cellIterator();
			while (cellIT.hasNext()) {
				Cell cell = cellIT.next();
				if (cell.getColumnIndex() == 0) {
					FileUtility.projects.add(cell);
				} else if (cell.getColumnIndex() == 1) {
					FileUtility.names.add(cell);
				} else if (cell.getColumnIndex() == 2) {
					FileUtility.dates.add(cell);
				} else if (cell.getColumnIndex() == 3) {
					FileUtility.duration.add(cell);
				} else {
					FileUtility.status.add(cell);
				}
			}

		}
		
	}
	 // Implement methods to copy new contents from the arrays to output file
	// Need to compare TextArea to contents in the array so they can be filtered

}
