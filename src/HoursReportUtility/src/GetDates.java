import java.text.SimpleDateFormat;
import java.util.*;

public class GetDates {
	
	public String getFirstDay(Date d) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date date = calendar.getTime();
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
		return sdf1.format(date);
	}
	
	public String getLastDay(Date d) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date date = calendar.getTime();
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
		return sdf1.format(date);
	}
}
