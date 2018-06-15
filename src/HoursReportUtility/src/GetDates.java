import java.text.SimpleDateFormat;
import java.util.*;

public class GetDates {
	
	public Date getFirstDay(Date date) throws Exception {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	return calendar.getTime();
	
	}
	
	public Date getLastDay(Date date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	public String convertDateToString(Date inDate) {
		String dateString = null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		try {
			dateString = format.format(inDate);
		} catch (Exception e) {
			System.out.println("Oooopppssss");
		}
		return dateString;
	}
	
	
}
