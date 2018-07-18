import java.io.File;
import javax.swing.*;

public class HoursReportDriver extends ExcelWriter {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				File file;
				JFileChooser chooser = new JFileChooser();
				HoursReport report = new HoursReport(chooser);
				chooser.setAccessory(report);
				// reads the file if 'ok' selected
				int open = chooser.showOpenDialog(chooser);
				if (open == JFileChooser.APPROVE_OPTION) {
					DefaultListModel model = report.getModel();
					for (int i = 0; i < model.getSize(); i++) {
						file = (File) model.getElementAt(i);

						try {

							FileUtility.readFile(file);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					  
					
				}
				
			}
			
		});

	}
}
