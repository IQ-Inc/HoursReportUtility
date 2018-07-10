import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;

public class GetDates extends HoursReport {
	
	public Date getFirstDay(Date date) throws Exception {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	return calendar.getTime();
	
	}
	
	public Date getLastDay(Date date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(
				Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	public String convertDateToString(Date inDate) {
		String dateString = null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		try {
			dateString = format.format(inDate);
		} catch (Exception e) {
			System.out.println("Error");
		}
		return dateString;
	}
}
