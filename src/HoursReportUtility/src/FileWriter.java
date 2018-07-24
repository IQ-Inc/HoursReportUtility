import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileWriter {

	public void writeToTxt(ArrayList<Cell> list) throws IOException {
		String employee = null;
		File file = new File("C://temp//employees.txt");
		// creates the stream to write and creates a new file
		DataFormatter format = new DataFormatter();
		FileOutputStream out = new FileOutputStream(file);
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));

		if (file.isFile() && !file.isDirectory()) {
			for (Cell cell : list) {
				employee = format.formatCellValue(cell);
				writer.println(employee);

			}
			System.out.println("exists");
		} else if (!file.isFile() && !file.isDirectory()) {
			file.createNewFile();
			for (Cell cell : list) {
				employee = format.formatCellValue(cell);
				writer.println(employee);
			}
			System.out.println("Created");
		}
		writer.flush();
		writer.close();

	}

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
