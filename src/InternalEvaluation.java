import java.io.BufferedReader;
import java.lang.Math.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class InternalEvaluation {

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

	public static double Cohesion(ArrayList doublepoint, double clusternum) {

		double result = 0;
		double centerx = 0;
		double centery = 0;
		double center_num = 0;

		for (int i = 0; i < doublepoint.size(); i++) {

			Point temp = (Point) doublepoint.get(i);

			if (temp.getId() == clusternum) {

				centerx = centerx + temp.getX();
				centery = centery + temp.getY();
				center_num = center_num + 1;
			}

		}

		for (int i = 0; i < doublepoint.size(); i++) {

			Point temp = (Point) doublepoint.get(i);
			if (temp.getId() == clusternum) {

				result = result
						+ Math.pow((centerx / center_num - temp.getX()), 2);
				result = result
						+ Math.pow((centery / center_num - temp.getY()), 2);
			}

		}

		return result;

	}

	public static double Seperation(ArrayList doublepoint, double clusternum) {

		double result = 0;
		double centerx = 0;
		double centery = 0;
		double center_num = 0;
		double x = 0;
		double y = 0;

		for (int i = 0; i < doublepoint.size(); i++) {

			Point temp = (Point) doublepoint.get(i);

			if (temp.getId() == clusternum) {

				centerx = centerx + temp.getX();
				centery = centery + temp.getY();
				center_num = center_num + 1;
			}

		}

		for (int i = 0; i < doublepoint.size(); i++) {

			Point temp = (Point) doublepoint.get(i);

			x = x + temp.getX();
			y = y + temp.getY();
		}

		result = result
				+ center_num
				* (Math.pow((centerx / center_num - x / doublepoint.size()), 2));
		result = result
				+ center_num
				* (Math.pow((centery / center_num - y / doublepoint.size()), 2));

		return result;

	}

	public static double Silhouette (ArrayList doublepoint, double clusternum, int num){
	
	double result = 0;
	double ai = 0;
	double bi = 0;
	double min = 1000000000;
	int bisize = 0;
	
	ArrayList lista = new ArrayList();
	ArrayList listb = new ArrayList();
	
	for (int n =0; n< doublepoint.size(); n++){
		 Point temp = (Point) doublepoint.get(n);
		 listb.add(temp);
	}
	
	for ( int i = 0; i < doublepoint.size(); i++ ){
	
	     Point temp = (Point) doublepoint.get(i);
	
	     if (temp.getId() == clusternum){
	    	 
	    	 lista.add(temp);
	    	 listb.remove(temp);
	     }
	     
	}
	
	for (int i = 0; i < lista .size(); i++){
		
		ai = 0;
		
		Point p = (Point) lista.get(i);
	
	    for ( int j = 0; j < lista.size(); j++ ){
		
	        Point other = (Point) lista.get(j);
	    	 
	        ai = ai + Math.sqrt(Math.pow((p.getX()- other.getX()),2) + Math.pow((p.getY()- other.getY()),2));
	    	 	    		     
	    }
	    ai = ai/(lista.size() - 1);
	    min = 1000000000;
	    
		for (int m = 1; m <= num; m++){
			
			bi = 0;
			bisize = 0;
			
			if (m == p.getId())
				continue;
			
		    for (int k = 0; k < listb.size(); k++ ){
		    	Point b = (Point) listb.get(k);
		    	if ( b.getId() == m ){
		    		
		    		bi = bi + Math.sqrt(Math.pow((p.getX()- b.getX()),2) + Math.pow((p.getY()- b.getY()),2));
		    		bisize = bisize + 1;
		    	}		    	
		    }
		    bi = bi/bisize;
		    if ( min > bi ){
		    	min = bi;
		    }
		}
		
		if ( ai < min)
			result = result + 1 - ai/min;
		else
			result = result + min/ai - 1;
		
	}
	
	result = result/lista.size();
	    	 	
	return result;
	
}
	public static double Precision (ArrayList doublepoint, ArrayList orign) {
		
		double result = 0;
		double tp = 0;
		double fp = 0;
		
		for (int i = 0; i < doublepoint.size(); i++){
			Point p1 = (Point) doublepoint.get(i);
			for (int j = 0; j < orign.size() ; j++){
				Point p2 = (Point) orign.get(j);
				if (( p1.getX() == p2.getX()) && ( p1.getY() == p2.getY())){
					if (p1.getId() == p2.getId()){
						tp = tp + 1;
						break;
					}
					else
						fp = fp + 1;				
				}
			}
		}
		
		result = tp/(tp + fp);
		
		return result;
		
	}
	
public static double TP (ArrayList doublepoint, ArrayList orign) {
		
		double result = 0;
		double tp = 0;
		double fp = 0;
		
		for (int i = 0; i < doublepoint.size(); i++){
			Point p1 = (Point) doublepoint.get(i);
			for (int j = 0; j < orign.size() ; j++){
				Point p2 = (Point) orign.get(j);
				if (( p1.getX() == p2.getX()) && ( p1.getY() == p2.getY())){
					if (p1.getId() == p2.getId()){
						tp = tp + 1;
						break;
					}
					else
						fp = fp + 1;				
				}
			}
		}
		
		result = tp/(tp + fp);
		
		return result;
		
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

		// String s
		// ="1,34,67,1,45,89,1,2,78,1,10,50,1,26,50,1,30,70,1,37,40,1,42,65,1,39,75,1,58,58"

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
	
	private static void rules (ArrayList input, int clusternum){
		
		double result_sse = 0;
		for (int i = 1; i <= clusternum; i++) {

			result_sse = result_sse + Cohesion(stringtopoint(input), i);

		}
		java.text.DecimalFormat  df=new   java.text.DecimalFormat("#.##");
		java.text.DecimalFormat  df2=new   java.text.DecimalFormat("#.####");
	    System.out.println("Cohesion = " + df.format(result_sse));
		//System.out.println(df.format(result_sse));
		/**************************************************************/
		double result_bbs = 0;
		for (int i = 1; i <= clusternum; i++) {

			result_bbs = result_bbs + Seperation(stringtopoint(input), i);

		}

		System.out.println("Separation = " + result_bbs);
		//System.out.println(df.format(result_bbs));
        /**************************************************************/
		double result_silhouette = 0;
		for (int i = 1; i <= clusternum; i++) {

			result_silhouette = result_silhouette + Silhouette(stringtopoint(input), i, clusternum);
			//System.out.println("Cluster" + i + "_Silhouette =" + result_silhouette);			
		}	
		
		System.out.println("Silhouette = " + result_silhouette/clusternum);
		System.out.println();
		//System.out.println(df2.format(result_silhouette/clusternum));
	}

	public static void main(String inputfile,int clusternum) throws IOException {

		ArrayList input = new ArrayList();
		input = readFile(inputfile);
		rules(input,clusternum);


	}
}