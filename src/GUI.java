import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GUI implements ItemListener {
	public JPanel cards;
	public JButton homeBut;
	public JButton nextBut;
	public JButton prevBut;
	public JButton browse;
	public JButton exit;
	public JPanel panelContainer;
	public JFileChooser fileChooser;
	
	public void initComponents() {
		
		prevBut = new JButton();
		nextBut = new JButton();
		homeBut = new JButton();
		panelContainer = new JPanel();
		JLabel label1 = new JLabel("Select Input File");
		
		// create the cards
		JPanel card1 = new JPanel();
		card1.add(browse, BorderLayout.PAGE_END);
		card1.add(exit, BorderLayout.PAGE_END);
		card1.add(label1, BorderLayout.PAGE_START);
		
		
		
}
	
	public class Home implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
			cardLayout.first(panelContainer);
			
		}
		
	}
	
	public class Next implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
			cardLayout.next(panelContainer);
			
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		CardLayout c1 = (CardLayout)(cards.getLayout());
		c1.show(cards, (String) evt.getItem());
		
	}
}


