import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.poi.ss.usermodel.Cell;



@SuppressWarnings("serial")
public class HoursReport extends JComponent implements PropertyChangeListener {
	private File file = null; 
	private DefaultListModel<File> model;
	private JList<File> list;
	private JButton removeItem;
	
	
	
	public HoursReport(JFileChooser chooser) {
		chooser.addPropertyChangeListener(this);
		
		model = new DefaultListModel<File>();
		list = new JList<File>(model);
		JScrollPane pane = new JScrollPane(list);
		pane.setPreferredSize(new Dimension(200,250));
		
		removeItem = createRemoveItemButton();
		
		setBorder(new EmptyBorder(10,10,10,10));
		setLayout(new BorderLayout());
		add(pane);
		add(removeItem, BorderLayout.SOUTH);
	}
	

	private JButton createRemoveItemButton() {
		JButton button = new JButton("Remove");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed (ActionEvent e) {
				removeFileFromList();
			}
		});
		return button;
	}
	
	private void removeFileFromList() {
		if (list.getSelectedIndex() != -1) {
			model.remove(list.getSelectedIndex());
		}
	}
	
	public DefaultListModel<File> getModel(){
		return model;
	}
	private void addFileToList() {
		model.addElement(file);
	}
	
	@Override 
	public void propertyChange(PropertyChangeEvent e) {
		boolean update = false;
		String prop = e.getPropertyName();
		
		//If directory is changed, don't do anything
		if(JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
			file = null;
			update = true;
			//If a file became selected, find out which one.
		} else if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
			file = (File) e.getNewValue();
			update = true;
		}
		
		if(update && file != null) {
			addFileToList();
		}
	}
	
	
	
	
	

	
}