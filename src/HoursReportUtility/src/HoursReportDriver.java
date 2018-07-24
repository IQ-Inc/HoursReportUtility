import java.io.File;
import javax.swing.*;

public class HoursReportDriver {

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
					DefaultListModel<File> model = report.getModel();
					for (int i = 0; i < model.getSize(); i++) {
						System.out.println(i);
						file = (File) model.getElementAt(i);
						System.out.println(model.size());
						

						try {
							FileUtility utility = new FileUtility();
							utility.readFile(file, null, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					 Filter filter = new Filter();
					 filter.filterGUI(FileUtility.names);
					
				}
				
			}
			
		});

	}
}
