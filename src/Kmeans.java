import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Kmeans {

	private int ClassCount;
	private int InstanceNumber;
	final static int FieldCount = 2;
	final static double t = 2.0;
	private float[][] data;
	private float[][] orign;
	private float[][] classData;
	private ArrayList<Integer> noises;
	private ArrayList<ArrayList<Integer>> result;


	public void initial(int ClassCount,int InstanceNumber) {
		this.ClassCount=ClassCount;
		this.InstanceNumber=InstanceNumber;
		this.data = new float[InstanceNumber][FieldCount + 1];
		this.orign = new float[InstanceNumber][FieldCount + 1];
		this.classData = new float[ClassCount][FieldCount];
		this.result = new ArrayList<ArrayList<Integer>>(ClassCount);
		this.noises = new ArrayList<Integer>();

	}
	
	private int getsize(String inputfile) throws IOException{

		File file = new File(inputfile);

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

		return stringarray.length/2;		

	}

	public void readData(String trainingFileName) {
		try {
			FileReader fr = new FileReader(trainingFileName);
			BufferedReader br = new BufferedReader(fr);
			String lineData = null;
			String[] splitData = null;
			int line = 0;

			while (br.ready()) {

				lineData = br.readLine();
				splitData = lineData.split(",");

				if (splitData.length > 1) {
					for (int i = 0; i < splitData.length; i=i+2) {
							data[line][0] = Float.parseFloat(splitData[i]);
							orign[line][0] = Float.parseFloat(splitData[i]);
							data[line][1] = Float.parseFloat(splitData[i+1]);				
							orign[line][1] = Float.parseFloat(splitData[i+1]);
							line = line + 1;
					}
				}
			}
			//System.out.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cluster() {

		normalize();

		boolean needUpdataInitials = true;


		int times = 1;

		while (needUpdataInitials) {
			
			needUpdataInitials = false;
			result.clear();
			//System.out.println("Find Initials Iteration" + (times++)+ "time(s)");

			findInitials();
			firstClassify();

			for (int i = 0; i < result.size(); i++) {
				if (result.get(i).size() < InstanceNumber
						/ Math.pow(ClassCount, t)) {
					needUpdataInitials = true;
					noises.addAll(result.get(i));
				}
			}
		}

		Adjust();
	}

	public void normalize() {
		float[] max = new float[FieldCount];
		for (int i = 0; i < InstanceNumber; i++) {
			for (int j = 0; j < FieldCount; j++) {
				if (data[i][j] > max[j])
					max[j] = data[i][j];
			}
		}

		for (int i = 0; i < InstanceNumber; i++) {
			for (int j = 0; j < FieldCount; j++) {
				data[i][j] = data[i][j] / max[j];
			}
		}
	}

	public void findInitials() {
		int i, j, a, b;
		i = j = a = b = 0;
		float maxDis = 0;
		int alreadyCls = 2;
		ArrayList<Integer> initials = new ArrayList<Integer>();

		for (; i < InstanceNumber; i++) {

			if (noises.contains(i))
				continue;
			j = i + 1;
			for (; j < InstanceNumber; j++) {
				if (noises.contains(j))
					continue;
				float newDis = calDis(data[i], data[j]);
				if (maxDis < newDis) {
					a = i;
					b = j;
					maxDis = newDis;
				}
			}
		}

		initials.add(a);
		initials.add(b);
		classData[0] = data[a];
		classData[1] = data[b];

		ArrayList<Integer> resultOne = new ArrayList<Integer>();
		ArrayList<Integer> resultTwo = new ArrayList<Integer>();
		resultOne.add(a);
		resultTwo.add(b);
		result.add(resultOne);
		result.add(resultTwo);

		while (alreadyCls < ClassCount) {
			i = j = 0;
			float maxMin = 0;
			int newClass = -1;
			for (; i < InstanceNumber; i++) {
				float min = 0;
				float newMin = 0;
				if (initials.contains(i))
					continue;
				if (noises.contains(i))
					continue;
				for (j = 0; j < alreadyCls; j++) {
					newMin = calDis(data[i], classData[j]);
					if (min == 0 || newMin < min)
						min = newMin;
				}

				if (min > maxMin) {
					maxMin = min;
					newClass = i;
				}
			}
			initials.add(newClass);
			classData[alreadyCls++] = data[newClass];
			ArrayList<Integer> rslt = new ArrayList<Integer>();
			rslt.add(newClass);
			result.add(rslt);
		}
	}

	public void firstClassify() {
		
		for (int i = 0; i < InstanceNumber; i++) {
			float min = 0f;
			int clsId = -1;
			for (int j = 0; j < classData.length; j++) {
				float newMin = calDis(classData[j], data[i]);
				if (clsId == -1 || newMin < min) {
					clsId = j;
					min = newMin;
				}
			}
			if (!result.get(clsId).contains(i))
				result.get(clsId).add(i);
		}
	}

	public void Adjust() {

		boolean change = true;
		int times = 1;
		while (change) {
			change = false;
			//System.out.println("Adjust Iteration" + (times++) + "time(s)");

			for (int i = 0; i < ClassCount; i++) {
				ArrayList<Integer> cls = result.get(i);
				float[] newMean = new float[FieldCount];
				for (Integer index : cls) {
					for (int j = 0; j < FieldCount; j++)
						newMean[j] += data[index][j];
				}
				for (int j = 0; j < FieldCount; j++)
					newMean[j] /= cls.size();
				if (!compareMean(newMean, classData[i])) {
					classData[i] = newMean;
					change = true;
				}
			}

			for (ArrayList<Integer> cls : result)
				cls.clear();
			for (int i = 0; i < InstanceNumber; i++) {
				float min = 0f;
				int clsId = -1;
				for (int j = 0; j < classData.length; j++) {
					float newMin = calDis(classData[j], data[i]);
					if (clsId == -1 || newMin < min) {
						clsId = j;
						min = newMin;
					}
				}
				data[i][FieldCount] = clsId;
				result.get(clsId).add(i);
			}
		}

	}


	private float calDis(float[] aVector, float[] bVector) {
		double dis = 0;
		int i = 0;
		for (; i < aVector.length; i++)
			dis += Math.pow(bVector[i] - aVector[i], 2);
		dis = Math.pow(dis, 0.5);
		return (float) dis;
	}

	private boolean compareMean(float[] a, float[] b) {
		if (a.length != b.length)
			return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > 0 && b[i] > 0 && a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	public void printResult(String fileName) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			for (int i = 0; i < InstanceNumber; i++) {
				bw.write(String.valueOf(orign[i][0])+",");
				bw.write(String.valueOf(orign[i][1])+",");
				bw.write(String.valueOf(data[i][FieldCount]+1)+",");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		
	    ///////////////////////////////////////////////////////////////////////////////
	    String input = "/Users/Jia/Desktop/dataset/r15_input.txt";                 ///////////
	    String origin = "/Users/Jia/Desktop/dataset/r15_orign.txt";                       ///////////
	    String kmeansresult = "/Users/Jia/Desktop/dataset/r15_kmeansresult.txt";   ///////////
	    int clusternum = 15;                                                 ///////////
	    ///////////////////////////////////////////////////////////////////////////////
		
		
		Kmeans cluster = new Kmeans();
		int size = cluster.getsize(input);
		cluster.initial(clusternum,size);

		cluster.readData(input);

		cluster.cluster();
	
		cluster.printResult(kmeansresult);
		
		long endTime = System.currentTimeMillis();
		System.out.println("*****K-Means Clustering Result*****");
	    System.out.println();
	    ExternalEvaluation ee=new ExternalEvaluation();
	    ee.main(kmeansresult,origin,clusternum);
	    //System.out.println();
	    InternalEvaluation ie=new InternalEvaluation();
	    ie.main(kmeansresult,clusternum);
	    System.out.println("Running Time = "+(endTime - startTime) + " ms");
	}
}