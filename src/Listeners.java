import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Listeners extends NewHoursReport {
	
	public static class browseListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				findFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

}
	public static class cancelListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			
			
		}
		
	}

	public static class backListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//add something here 
			
			
		}
		
	}
	
	public static class nextListenerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//select an option is parent dialog
			
			
			
		}
	
	}
}
