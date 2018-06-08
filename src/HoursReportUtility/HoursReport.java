import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import java.io.*;

@SuppressWarnings("serial")
public class HoursReport extends Frame implements ActionListener {
	JPanel mPane0, mPane1, mDatePane0, mDatePane1, mDatePane2;
	JTextField  mStartField, mEndField;
	static JButton mBrowse;
	JButton mCancel;
	JButton mNext;
	JButton mBack;
	Object sDate;
	Object eDate;
	static JButton mImport;
	JLabel mFileLabel, mStartDate, mEndDate;
	JFrame mFrame, mDateFrame;
	static File inputFile;
	@SuppressWarnings("static-access")
	public void createGUI() {
		// Create the GUI
		mPane0 = new JPanel();// top panel - borderLayout (TextField)
		mPane1 = new JPanel(); // bottom panel of the window - Flow (buttons)
		// Buttons
		mBrowse = createButton(40, 40, "Browse");
		mCancel = createButton(40, 40, "Cancel");
		mImport = createButton(40, 40, "Import");
		mImport.setEnabled(false);
		mBrowse.addActionListener(this);
		mCancel.addActionListener(this);
		mImport.addActionListener(this);
		mFileLabel = new JLabel("Input Filename");
		Font labelFont = new Font("SansSerif", Font.BOLD, 15);
		mFileLabel.setFont(labelFont);
		// add components to the panel
		mPane0.add(mFileLabel);
		mPane1.add(mBrowse);
		mPane1.add(mCancel);
		mPane1.add(mImport);
		// create the frame
		mFrame = new JFrame("Hours Utility Tool");
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setDefaultLookAndFeelDecorated(true);
		mFrame.add(mPane0, BorderLayout.NORTH);
		mFrame.add(mPane1, BorderLayout.SOUTH);
		mFrame.pack();
		mFrame.setLocationRelativeTo(null);
		mFrame.setVisible(true);
	}

	public static JButton createButton(int width, int height, String name) {
		JButton button = new JButton();
		button.setSize(width, height);
		button.setText(name);
		return button;
	}

	public void datePaneGUI() {
		DataFormatter formatter = new DataFormatter();
		String sDate = formatter.formatCellValue((Cell) FileUtility.dates.get(1));
		String eDate = formatter.formatCellValue(
				(Cell) FileUtility.dates.get(FileUtility.dates.size()-1));
		mStartDate = new JLabel("Start Date");
		mEndDate = new JLabel("End Date");
		mDatePane0 = new JPanel(new BorderLayout());
		mDatePane0.add(mStartDate, BorderLayout.WEST);
		mDatePane0.add(mEndDate, BorderLayout.EAST);
		mDatePane1 = new JPanel(new FlowLayout());
		mStartField = new JTextField(sDate, 12);
		mEndField = new JTextField(eDate, 12);
		mDatePane1.add(mStartField);
		mDatePane1.add(mEndField);
		mDatePane2 = new JPanel(new FlowLayout());
		mNext = createButton(40, 40, "Next");
		mNext.addActionListener(this);
		mBack = createButton(40, 40, "Back");
		mBack.addActionListener(this);
		mDatePane2.add(mBack);
		mDatePane2.add(mNext);
		mDateFrame = new JFrame("Change Dates");
		mDateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		mDateFrame.add(mDatePane0, BorderLayout.PAGE_START);
		mDateFrame.add(mDatePane1, BorderLayout.CENTER);
		mDateFrame.add(mDatePane2, BorderLayout.PAGE_END);
		mDateFrame.pack();
		mDateFrame.setLocationRelativeTo(mFrame);
		mDateFrame.setVisible(true);

	}

	// Add all Action Listeners
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Browse") {
			try {
				FileUtility.findFile();
			} catch (Exception e1) {
				e1.printStackTrace();}
			} else if (e.getActionCommand() == "Import") {
				try {
					FileUtility.readFile(FileUtility.fileChooser);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getActionCommand() == "Cancel") {
					System.exit(0);
		} else if (e.getActionCommand() == "Next") {
					Cell sDate = FileUtility.dates.get(1);
					Cell eDate = FileUtility.dates.get(FileUtility.dates.size() - 1);
					mImport.setEnabled(true);
					System.out.println(sDate + " and " + eDate);
		} else if (e.getActionCommand() == "Back") {
					mDateFrame.setVisible(false);
					FileUtility.fileChooser.setVisible(false);
			}
	} 
}