import java.io.BufferedReader;
import java.lang.Math.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class ExternalEvaluation {
	
	static double tp = 0;
	static double tn = 0;
	static double fp = 0;
	static double fn = 0;

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
	
	public static ArrayList Combine(ArrayList doublepoint, ArrayList orign) {
		
		ArrayList result = new ArrayList();
		
		for (int i = 0; i < doublepoint.size(); i++){
			
			Point newpoint = (Point)doublepoint.get(i);
			
			for (int j = 0; j < doublepoint.size(); j++){
				
				Point oldpoint = (Point)orign.get(j);
				
				if ((newpoint.getX() == oldpoint.getX()) && (newpoint.getY()==oldpoint.getY())){
					Clusteredpoint cp = new Clusteredpoint();
					cp.setX(newpoint.getX());
					cp.setY(newpoint.getY());
					cp.setOriginalid(oldpoint.getId());
					cp.setNewid(newpoint.getId());
					result.add(cp);
					break;
				}
				
			}
		}
		
		return result;
		
	}
	
    public static double TP (ArrayList combinedlist, int num) {
				
		ArrayList clusterA = new ArrayList();
		
		for (int i = 0; i < combinedlist.size(); i++){
			Clusteredpoint p = (Clusteredpoint) combinedlist.get(i);
			if (p.getNewid() == num){
				clusterA.add(p);
			}
		}
		
		while ( clusterA.size() > 1){
			Clusteredpoint p1 = (Clusteredpoint) clusterA.get(0);
			for (int j=1; j < clusterA.size(); j++){
				Clusteredpoint p2 = (Clusteredpoint) clusterA.get(j);
				if ( p1.getOriginalid() == p2.getOriginalid() ) {
					tp = tp + 1;
				}
			}
			clusterA.remove(p1);
		}
		
		num = num - 1; 
		
		if (num == 0){
			return tp;
		}
		
		TP (combinedlist, num);
				
		return tp;
		
	}
    
    public static double FP (ArrayList combinedlist, int num) {
		
		ArrayList clusterA = new ArrayList();
		
		for (int i = 0; i < combinedlist.size(); i++){
			Clusteredpoint p = (Clusteredpoint) combinedlist.get(i);
			if (p.getNewid() == num){
				clusterA.add(p);
			}
		}
		
		while ( clusterA.size() > 1){
			Clusteredpoint p1 = (Clusteredpoint) clusterA.get(0);
			for (int j=1; j < clusterA.size(); j++){
				Clusteredpoint p2 = (Clusteredpoint) clusterA.get(j);
				if ( p1.getOriginalid() != p2.getOriginalid() ) {
					fp = fp + 1;
				}
			}
			clusterA.remove(p1);
		}
		
		num = num - 1; 
		
		if (num == 0){
			return fp;
		}
		
		FP (combinedlist, num);
				
		return fp;
		
	}
    
public static double TN (ArrayList combinedlist, int num) {
		
		ArrayList clusterA = new ArrayList();
		
		for (int i = combinedlist.size() - 1; i >= 0; i--){
			Clusteredpoint p = (Clusteredpoint) combinedlist.get(i);
			if (p.getNewid() == num){
				clusterA.add(p);
				combinedlist.remove(p);
			}
			
		}
		
		for (int i = 0; i < clusterA.size(); i++){
			Clusteredpoint p1 = (Clusteredpoint) clusterA.get(i);
			for (int j = 0; j < combinedlist.size(); j++){
				Clusteredpoint p2 = (Clusteredpoint) combinedlist.get(j);
				if ( p1.getOriginalid() != p2.getOriginalid() ) {
					tn = tn + 1;
				}
			}
		}				
		
		
		num = num - 1; 
		
		if (num == 1){
			return tn;
		}
		
		TN (combinedlist, num);
				
		return tn;
		
	}
    
public static double FN (ArrayList combinedlist, int num) {
		
		ArrayList clusterA = new ArrayList();
		
		for (int i = combinedlist.size() - 1; i >= 0; i--){
			Clusteredpoint p = (Clusteredpoint) combinedlist.get(i);
			if (p.getNewid() == num){
				clusterA.add(p);
				combinedlist.remove(p);
			}
		}
		
		for (int i = 0; i < clusterA.size(); i++){
			Clusteredpoint p1 = (Clusteredpoint) clusterA.get(i);
			for (int j = 0; j < combinedlist.size(); j++){
				Clusteredpoint p2 = (Clusteredpoint) combinedlist.get(j);
				if ( p1.getOriginalid() == p2.getOriginalid() ) {
					fn = fn + 1;
				}
			}
		}				
		
		num = num - 1; 
		
		if (num == 1){
			return fn;
		}
		
		FN (combinedlist, num);
				
		return fn;
		
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
	
	private static void rules(ArrayList input, ArrayList orign, int clusternum){
		
		double tp = TP(Combine(stringtopoint(input),stringtopoint(orign)),clusternum);

		//System.out.println("TP = " + tp);
        /********************************************************************************/
		double fp = FP(Combine(stringtopoint(input),stringtopoint(orign)),clusternum);

		//System.out.println("FP = " + fp);
        /********************************************************************************/
		double fn = FN(Combine(stringtopoint(input),stringtopoint(orign)),clusternum);

		//System.out.println("FN = " + fn);
		
        /********************************************************************************/
		double tn = TN(Combine(stringtopoint(input),stringtopoint(orign)),clusternum);

		//System.out.println("TN = " + tn);
        /********************************************************************************/
		
		java.text.DecimalFormat  df=new   java.text.DecimalFormat("#.####");

		double precision = tp/(tp+fp);
		System.out.println("Precision = " + precision);
		//System.out.println(df.format(precision));
		
		double recall = tp/(tp+fn);
		System.out.println("Recall = " + recall);
		//System.out.println(df.format(recall));
		
		double RI = (tp+tn)/(tp+fp+fn+tn);
		System.out.println("Rand Index = " + RI);
		//System.out.println(df.format(RI));
		
		double Fmeasure = (2*precision*recall)/(precision+recall);
		System.out.println("F-measure = " + Fmeasure);
		System.out.println();
		//System.out.println(df.format(Fmeasure));
		
	}

	public static void main(String inputfile,String orignfile,int clusternum) throws IOException {

		ArrayList input = new ArrayList();
		ArrayList orign = new ArrayList();
		input = readFile(inputfile);
		orign = readFile(orignfile);
        rules(input,orign,clusternum);
			
	}	
}