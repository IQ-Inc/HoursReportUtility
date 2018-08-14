import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

public class myTest {
	private static final String LAST_SAVE_KEY = "last-save-dir";
	private static final Preferences PREF = Preferences.userNodeForPackage(myTest.class);
	
	public static void main(String[] args) {
		String saveDir = PREF.get(LAST_SAVE_KEY, System.getProperty("user.home"));
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(
				new File(System.getProperty("user.home")));
		chooser.setSelectedFile(new File(saveDir));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			PREF.put(LAST_SAVE_KEY, chooser.getSelectedFile().toString());
			System.out.println(PREF.get(LAST_SAVE_KEY, saveDir));
		}
	}
}
