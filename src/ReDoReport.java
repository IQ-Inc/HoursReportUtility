import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;;

public class ReDoReport extends Frame implements ActionListener {
	JPanel mPane0,mPane1, mDatePane0, mDatePane1, mDatePane2;
	JTextField mFileField, mStartField, mEndField;
	JButton mBrowse,mCancel,mFind, mNext, mBack;
	JLabel mFileLabel, mStartDate, mEndDate;
	JFrame mFrame, mDateFrame;
	
	@SuppressWarnings("static-access")
	public void createGUI() {
		// Create the GUI
		mPane0 = new JPanel();//top panel - borderLayout (TextField)
		mPane1 = new JPanel(); //bottom panel of the window - Flow (buttons)
		mFileField = new JTextField(25); // field where file will be entered
		//Buttons
		mBrowse = createButton(40,40, "Browse");
		mCancel = createButton(40,40, "Cancel");
		mFind = createButton(40,40, "Search");
		mBrowse.addActionListener(this);
		mCancel.addActionListener(this);
		mFind.addActionListener(this);	
		mFileLabel = new JLabel("Filename");
		// add components to the panel
		mPane0.add(mFileLabel);
		mPane0.add(mFileField);
		mPane0.add(mFind);
		mPane1.add(mBrowse);
		mPane1.add(mCancel);
		//create the frame
		mFrame = new JFrame("Hours Utility Tool");
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setDefaultLookAndFeelDecorated(true);
		mFrame.add(mPane0, BorderLayout.NORTH);
		mFrame.add(mPane1,BorderLayout.SOUTH);
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
		mStartDate = new JLabel("Start Date");
		mEndDate = new JLabel("End Date");
		mDatePane0 = new JPanel(new BorderLayout());
		mDatePane0.add(mStartDate, BorderLayout.WEST);
		mDatePane0.add(mEndDate, BorderLayout.EAST);
		mDatePane1 = new JPanel(new FlowLayout());
		mStartField = new JTextField("MM/DD/YYYY", 12);		
		mEndField = new JTextField("MM/DD/YYYY", 12);
		mDatePane1.add(mStartField);
		mDatePane1.add(mEndField);
		mDatePane2 = new JPanel(new FlowLayout());
		mNext = createButton(40,40,"Next");
		mNext.addActionListener(this);
		mBack = createButton(40,40,"Back");
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
		mDateFrame.setVisible(true);
		
	}
	//Add all Action Listeners
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Browse") {
			FileUtility file = new FileUtility();
			file.findFile();
			} else if(e.getActionCommand() == "Cancel") {
			System.exit(0);
		} else if(e.getActionCommand() == "Next") {
			System.out.println("Next");
		} else if(e.getActionCommand() == "Back") {
			System.out.println("Back");
		}
	}
}
