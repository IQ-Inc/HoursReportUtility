import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.*;

public class HoursReportDriver implements Runnable {
	


	public static void main(String[] args) {
		(new Thread(new HoursReportDriver())).start();
	}
	//Figure this out - Preferences
	public void run() {
		//Sets up the path for the preferences
		Preferences pref = Preferences.userRoot();
		String path = pref.get("DEFAULT_PATH", "");
		File file;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(path));
		HoursReport report = new HoursReport(chooser);
		chooser.setAccessory(report);
		// reads the file if 'ok' selected
		int open = chooser.showOpenDialog(chooser);
		if (open == JFileChooser.APPROVE_OPTION) {
			
			DefaultListModel<File> model = report.getModel();
			for (int i = 0; i < model.getSize(); i++) {
				
				//saves opened file in preferences so the program starts at the last directory
				file = (File) model.getElementAt(i);
				chooser.setCurrentDirectory(file);
				pref.put("DEFAULT_PATH", file.getAbsolutePath());
				
				
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

}
