import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GUI extends JPanel {
	JFrame frame;
	JLabel label;
	
	
	public static void main(String[] args) {
		// Just used for testing 
		JFrame frame = new JFrame();
		GUI gui = new GUI(frame);
		
		
	}

	
		public GUI(JFrame frame) {
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
	
	public static JFrame createJFrame(JPanel topPane, JPanel bottomPane, int width, int height) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(topPane, BorderLayout.PAGE_START);
		frame.add(bottomPane, BorderLayout.PAGE_END);
		frame.setSize(width, height);
		frame.setVisible(true);
		return frame;
	}
	
	public static class browseListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				NewHoursReport.findFile();
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
		
}
	public static class cancelListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			// exit app
			
		}
		
	}
	
	
	
	
	
	
}
