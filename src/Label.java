import java.io.BufferedReader;
import java.lang.Math.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Label {
	
	public static ArrayList correctLabel(ArrayList inputlist) {
		
		for (int i = 0; i < inputlist.size(); i++) {
			
			Point temp = (Point) inputlist.get(i);
			
			switch ((int)temp.getId()) {	
			case 1: temp.setId(8);break;
			case 2: temp.setId(9);break;
			case 3: temp.setId(10);break;
			case 4: temp.setId(11);break;
			case 5: temp.setId(12);break;
			case 6: temp.setId(13);break;
			case 7: temp.setId(14);break;	
			}	
		}
		
		for (int i = 0; i < inputlist.size(); i++) {
			
			Point temp = (Point) inputlist.get(i);
			
			switch ((int)temp.getId()) {	
			case 8: temp.setId(4);break;
			case 9: temp.setId(5);break;
			case 10: temp.setId(2);break;
			case 11: temp.setId(1);break;
			case 12: temp.setId(3);break;
			case 13: temp.setId(7);break;
			case 14: temp.setId(6);break;	
			}	
		}
		
		
		return inputlist;
		
	}

	public static ArrayList stringtopoint(ArrayList inputlist) {

		ArrayList doublepoint = new ArrayList();
		for (int i = 0; i < inputlist.size(); i++) {
			String inputstring = inputlist.get(i).toString();
			String[] inputpoint = inputstring.split(",");
			String id = inputpoint[0];
			String x = inputpoint[1];
			String y = inputpoint[2];

			Point point = new Point();
			double id0 = Double.parseDouble(id);
			point.setId(id0);

			double x0 = Double.parseDouble(x);
			point.setX(x0);
			double y0 = Double.parseDouble(y);
			point.setY(y0);

			doublepoint.add(point);

		}
		return doublepoint;
	}

	
	private static ArrayList readFile(String fileName) throws IOException {

		ArrayList input = new ArrayList();

		File file = new File(fileName);

		BufferedReader bf = new BufferedReader(new FileReader(file));

		String content = "";
		StringBuilder sb = new StringBuilder();

		while (content != null) {
			content = bf.readLine();

			if (content == null) {
				break;
			}

			sb.append(content.trim());
		}

		bf.close();

		String s = sb.toString();
		String[] stringarray = s.split(",");

		for (int i = 0; i < stringarray.length; i = i + 3) {

			String x = stringarray[i];
			String y = stringarray[i + 1];
			String id = stringarray[i + 2];
			;

			String one = id + "," + x + "," + y;

			input.add(one);
		}

		return input;
	}
	
	private static void writeFile(String fileName, ArrayList result, ArrayList input) throws IOException {

		FileWriter fw = new FileWriter(fileName);

		for (int m = 0; m < result.size(); m++) {

			Point tempp = (Point) result.get(m);
			fw.write(tempp.getX()+","+tempp.getY()+","+tempp.getId());
			fw.write(",");
		}

		fw.close();

	}

	public static void main(String[] ffff) throws IOException {

		ArrayList input = new ArrayList();
		input = readFile("D:\\aggr_result7.txt");

		ArrayList result = correctLabel(stringtopoint(input));

		writeFile("D:\\aggr_result7_label.txt", result, input);
		
	}
}