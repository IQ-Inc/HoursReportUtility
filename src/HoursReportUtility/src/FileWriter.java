import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class FileWriter {

	public void writeToTxt(List<Cell> names) throws IOException {
		String employee = null;
		File file = new File("C://temp//employees.txt");
		// creates the stream to write and creates a new file
		DataFormatter format = new DataFormatter();
		FileOutputStream out = new FileOutputStream(file);
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));

		if (file.isFile() && !file.isDirectory()) {
			for (Cell cell : names) {
				employee = format.formatCellValue(cell);
				writer.println(employee);

			}
			
		} else if (!file.isFile() && !file.isDirectory()) {
			file.createNewFile();
			for (Cell cell : names) {
				employee = format.formatCellValue(cell);
				writer.println(employee);
			}
			
		}
		writer.flush();
		writer.close();

	}
	// removes the duplicate names for the text file
	public void removeDupes(List<Cell> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i).getStringCellValue().equals(list.get(j).getStringCellValue())) {
					list.remove(list.get(j));
					j--;
				}
			}
		}

	}
	
	
}
