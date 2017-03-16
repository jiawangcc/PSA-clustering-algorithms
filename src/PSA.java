import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PSA {

	// public static ArrayList distancelist;
	// public static ArrayList resultlist;
	public static int clusternum = 1;

	static ArrayList distancelist = new ArrayList();
	static ArrayList resultlist = new ArrayList();

	public PSA() {
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

	public static double MinX(ArrayList doublepoint) {
		double minpointx = 0;
		Point temp_point = (Point) doublepoint.get(0);
		minpointx = temp_point.getX();

		for (int i = 0; i < doublepoint.size(); i++) {
			Point eachpoint = (Point) doublepoint.get(i);
			if (eachpoint.getX() < minpointx) {
				minpointx = eachpoint.getX();
			}
		}

		return minpointx;
	}

	public static double MaxX(ArrayList doublepoint) {
		double maxpointx = 0;
		Point temp_point = (Point) doublepoint.get(0);
		maxpointx = temp_point.getX();

		for (int i = 0; i < doublepoint.size(); i++) {
			Point eachpoint = (Point) doublepoint.get(i);
			if (eachpoint.getX() > maxpointx) {
				maxpointx = eachpoint.getX();
			}
		}

		return maxpointx;
	}

	public static double MinY(ArrayList doublepoint) {
		double minpointy = 0;
		Point temp_point = (Point) doublepoint.get(0);
		minpointy = temp_point.getY();

		for (int i = 0; i < doublepoint.size(); i++) {
			Point eachpoint = (Point) doublepoint.get(i);
			if (eachpoint.getY() < minpointy) {
				minpointy = eachpoint.getY();
			}
		}

		return minpointy;
	}

	public static double MaxY(ArrayList doublepoint) {
		double maxpointy = 0;
		Point temp_point = (Point) doublepoint.get(0);
		maxpointy = temp_point.getY();

		for (int i = 0; i < doublepoint.size(); i++) {
			Point eachpoint = (Point) doublepoint.get(i);
			if (eachpoint.getY() > maxpointy) {
				maxpointy = eachpoint.getY();
			}
		}

		return maxpointy;
	}

	public static double distance_x(ArrayList doublepoint) {

		double minx = MinX(doublepoint);
		double maxx = MaxX(doublepoint);
		double distancex = maxx - minx;
		return distancex;

	}

	public static double distance_y(ArrayList doublepoint) {

		double miny = MinY(doublepoint);
		double maxy = MaxY(doublepoint);
		double distancey = maxy - miny;
		return distancey;

	}

	public static ArrayList maxdistance(ArrayList distancelist) {

		ArrayList result = new ArrayList();

		ArrayList storelist = new ArrayList();
		for (int k = 0; k < resultlist.size(); k++) {
			storelist.add(resultlist.get(k));
		}

		Distance temp = (Distance) distancelist.get(0);
		double max = temp.getxdis();

		for (int i = 0; i < distancelist.size(); i++) {

			Distance d = (Distance) distancelist.get(i);

			if (max < d.getxdis()) {

				max = d.getxdis();
			}

			if (max < d.getydis()) {

				max = d.getydis();
			}

		}

		double minimumx = 0;
		double minimumy = 0;
		double maximumx = 0;
		double maximumy = 0;

		for (int j = 0; j < distancelist.size(); j++) {

			Distance a = (Distance) distancelist.get(j);

			if ((a.getxdis() == max) || (a.getydis() == max)) {
				minimumx = a.getx1();
				minimumy = a.gety1();
				maximumx = a.getx2();
				maximumy = a.gety2();
				distancelist.remove(a);
				break;
			}
		}

		for (int n = 0; n < storelist.size(); n++) {

			Point p = (Point) storelist.get(n);

			if ((p.getX() >= minimumx) && (p.getX() <= maximumx)
					&& (p.getY() >= minimumy) && (p.getY() <= maximumy)) {

				result.add(p);
				resultlist.remove(p);

			}

		}

		return result;

	}

	public static ArrayList PSAmethod(ArrayList doublepoint, int counter) {

		if (doublepoint.size() == 0) {
			return resultlist;
		}

		ArrayList test1 = resultlist;

		ArrayList clusterA = new ArrayList();
		ArrayList clusterB = new ArrayList();

		double distancex = distance_x(doublepoint);
		double distancey = distance_y(doublepoint);

		if (distancex >= distancey) {

			for (int i = 0; i < doublepoint.size(); i++) {

				Point eachpoint = (Point) doublepoint.get(i);
				if ((eachpoint.getX()) <= (distancex / 2 + MinX(doublepoint))) {
					eachpoint.setId(eachpoint.getId());
					clusterA.add(eachpoint);
				} else {
					eachpoint.setId(clusternum + 1);
					clusterB.add(eachpoint);
				}
				resultlist.add(eachpoint);

			}

		} else {

			for (int i = 0; i < doublepoint.size(); i++) {

				Point eachpoint = (Point) doublepoint.get(i);
				if ((eachpoint.getY()) <= (distancey / 2 + MinY(doublepoint))) {
					eachpoint.setId(eachpoint.getId());
					clusterA.add(eachpoint);
				} else {
					eachpoint.setId(clusternum + 1);
					clusterB.add(eachpoint);
				}
				resultlist.add(eachpoint);

			}

		}

		clusternum = clusternum + 1;

		counter = counter - 1;

		if (counter == 1) {
			return resultlist;
		}

		Distance distanceA = new Distance();
		distanceA.setxdis(distance_x(clusterA));
		distanceA.setydis(distance_y(clusterA));
		distanceA.setx1(MinX(clusterA));
		distanceA.sety1(MinY(clusterA));
		distanceA.setx2(MaxX(clusterA));
		distanceA.sety2(MaxY(clusterA));
		distancelist.add(distanceA);

		Distance distanceB = new Distance();
		distanceB.setxdis(distance_x(clusterB));
		distanceB.setydis(distance_y(clusterB));
		distanceB.setx1(MinX(clusterB));
		distanceB.sety1(MinY(clusterB));
		distanceB.setx2(MaxX(clusterB));
		distanceB.sety2(MaxY(clusterB));
		distancelist.add(distanceB);

		ArrayList newlist = maxdistance(distancelist);

		PSAmethod(newlist, counter);

		return resultlist;

	}

	/***************************************************************************/

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
		
		//String s ="1,34,67,1,45,89,1,2,78,1,10,50,1,26,50,1,30,70,1,37,40,1,42,65,1,39,75,1,58,58"

		String s = sb.toString();
		String[] stringarray = s.split(",");

		for (int i = 0; i < stringarray.length; i = i + 2) {

			String id = "1";
			String x = stringarray[i];
			String y = stringarray[i + 1];

			String one = id + "," + x + "," + y;

			input.add(one);
		}

		return input;
	}

	private static void writeFile(String fileName, ArrayList result, ArrayList input) throws IOException {

		FileWriter fw = new FileWriter(fileName);
		//fw.write("inputpoint.size::" + (input.size()) + "\r\n");
		//fw.write("result.size::" + (result.size()) + "\r\n");
		//System.out.println(input.size());

		for (int m = 0; m < result.size(); m++) {

			Point tempp = (Point) result.get(m);
			//System.out.println("result---  x: " + tempp.getX() + "  y: " + tempp.getY() + "  cluster: " + tempp.getId());
			//fw.write("result---  x: " + tempp.getX() + "  y: " + tempp.getY() + "  cluster: " + tempp.getId() + "\r\n");
			fw.write(tempp.getX()+","+tempp.getY()+","+tempp.getId());
			fw.write(",");
		}

		fw.close();

	}

	public static void main(String[] args) throws IOException {
		
		long startTime=System.currentTimeMillis();
		
		//////////////////////////////////////////////////////////////////////////////////////
		String inputstring = "/Users/Jia/Desktop/dataset/r15_input.txt";             /////////
		String origin = "/Users/Jia/Desktop/dataset/r15_orign.txt";                   /////////
		String PSAresult = "/Users/Jia/Desktop/dataset/compound_PSAresult.txt"; 
		int clusternum = 6;                                                         //////////
        //////////////////////////////////////////////////////////////////////////////////////
		
		
		ArrayList input = new ArrayList();
		input = readFile(inputstring);

		ArrayList result = PSAmethod(stringtopoint(input), clusternum);

		writeFile(PSAresult, result, input);
		
		long endTime=System.currentTimeMillis();
		System.out.println("*****PSA Clustering Result*****");
		System.out.println();
		
	    ExternalEvaluation ee=new ExternalEvaluation();
	    ee.main(PSAresult,origin,clusternum);
	    //System.out.println();
	    InternalEvaluation ie=new InternalEvaluation();
	    ie.main(PSAresult,clusternum);
	    System.out.println("Running Time = "+(endTime-startTime)+" ms");

	}

	// class end!
}
