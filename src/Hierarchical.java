
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hierarchical {
	
   public List<Cluster> startAnalysis(List<DataPoint> dataPoints,int ClusterNum){
      List<Cluster> finalClusters=new ArrayList<Cluster>();
    
      List<Cluster> originalClusters=initialCluster(dataPoints);
      finalClusters=originalClusters;
      while(finalClusters.size()>ClusterNum){
          double min=Double.MAX_VALUE;
          double centerAx = 0;
          double centerAy = 0;
          double centerBx = 0;
          double centerBy = 0;
          int mergeIndexA=0;
          int mergeIndexB=0;
          for(int i=0;i<finalClusters.size();i++){
              for(int j=0;j<finalClusters.size();j++){
                  if(i!=j){
                      Cluster clusterA=finalClusters.get(i);
                      Cluster clusterB=finalClusters.get(j);
                      List<DataPoint> dataPointsA=clusterA.getDataPoints();
                      List<DataPoint> dataPointsB=clusterB.getDataPoints();
                      
                      for(int m=0;m<dataPointsA.size();m++){
                    	  centerAx = dataPointsA.get(m).getX() + centerAx;
                    	  centerAy = dataPointsA.get(m).getY() + centerAy;
                      }
                      centerAx = centerAx /dataPointsA.size();
                      centerAy = centerAy /dataPointsA.size();
                      
                      for(int n=0;n<dataPointsB.size();n++){
                    	  
                    	  centerBx = dataPointsB.get(n).getX() + centerBx;
                    	  centerBy = dataPointsB.get(n).getY() + centerBy;
                      }  
                      centerBx = centerBx /dataPointsB.size();
                      centerBy = centerBy /dataPointsB.size();
                      
                      double tempDis=getDistance(centerAx,centerAy,centerBx,centerBy);
                      if(tempDis<min){
                           min=tempDis;
                           mergeIndexA=i;
                           mergeIndexB=j;
                      }
                      centerAx=0;
                      centerAy=0;
                      centerBx=0;
                      centerBy=0;
                      
                  }
              } //end for j
          }// end for i
          //merge cluster[mergeIndexA] and cluster[mergeIndexB]
          finalClusters=mergeCluster(finalClusters,mergeIndexA,mergeIndexB);
      }//end while
      return finalClusters;
   }
   private List<Cluster> mergeCluster(List<Cluster> clusters,int mergeIndexA,int mergeIndexB){
       if (mergeIndexA != mergeIndexB) {
           // put the data points of cluster[mergeIndexB] into cluster[mergeIndexA]
           Cluster clusterA = clusters.get(mergeIndexA);
           Cluster clusterB = clusters.get(mergeIndexB);
           List<DataPoint> dpA = clusterA.getDataPoints();
           List<DataPoint> dpB = clusterB.getDataPoints();
           for (DataPoint dp : dpB) {
               DataPoint tempDp = new DataPoint();
               //tempDp.setDataPointName(dp.getDataPointName());
               tempDp.setDimension(dp.getDimension());
               //tempDp.setCluster(clusterA);
               dpA.add(tempDp);
           }
           //clusterA.setDataPoints(dpA);
           clusters.remove(mergeIndexB);
       }
       return clusters;
  }
 
  private List<Cluster> initialCluster(List<DataPoint> dataPoints){
      List<Cluster> originalClusters=new ArrayList<Cluster>();
      
      for(int i=0;i<dataPoints.size();i++){
    	  
          DataPoint tempDataPoint=dataPoints.get(i);
          List<DataPoint> tempDataPoints=new ArrayList<DataPoint>();
          tempDataPoints.add(tempDataPoint);
          Cluster tempCluster=new Cluster();
          tempCluster.setClusterName("Cluster "+String.valueOf(i));
          tempCluster.setDataPoints(tempDataPoints);
          originalClusters.add(tempCluster);
      }
      return originalClusters;
  }

  private double getDistance(DataPoint dpA,DataPoint dpB){
       double distance=0;
       double[] dimA = dpA.getDimension();
       double[] dimB = dpB.getDimension();
       if (dimA.length == dimB.length) {
           for (int i = 0; i < dimA.length; i++) {
                double temp=Math.pow((dimA[i]-dimB[i]),2);
                distance=distance+temp;
           }
       }
      return distance;
  }
  
  private double getDistance(double centerAx,double centerAy,double centerBx,double centerBy){
	  
     double distance=0;
     distance = Math.pow((centerAx-centerBx), 2) + Math.pow((centerAy-centerBy), 2);

     return distance;
 }
  
	private static ArrayList<DataPoint> readFile(String fileName) throws IOException {

		ArrayList<DataPoint> input = new ArrayList<DataPoint>();

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

		for (int i = 0; i < stringarray.length; i = i + 2) {

			double[] point={0,0};
			point[0]=Double.parseDouble(stringarray[i]);
			point[1]=Double.parseDouble(stringarray[i+1]);
	

			input.add(new DataPoint(point));
		}

		return input;
	}
	
  
  public static void main(String[] args) throws IOException{
	  
	  long startTime = System.currentTimeMillis();
      ArrayList<DataPoint> dpoints = new ArrayList<DataPoint>();

      ///////////////////////////////////////////////////////////////////////////
      String input = "/Users/Jia/Desktop/dataset/compound_input.txt";           /////////////
      String origin = "/Users/Jia/Desktop/dataset/compound_orign.txt";                 /////////////
      String hierresult = "/Users/Jia/Desktop/dataset/compound_hierresult.txt"; /////////////
      int clusternum = 6;                                           /////////////
      ///////////////////////////////////////////////////////////////////////////
      dpoints = readFile(input);
    
      FileWriter fw = new FileWriter(hierresult);
      
      int num =1;
      Hierarchical ca=new Hierarchical();
      
      List<Cluster> clusters=ca.startAnalysis(dpoints, clusternum);
      
      for(Cluster cl:clusters){
          //System.out.println("------"+cl.getClusterName()+"------");
          List<DataPoint> tempDps=cl.getDataPoints();
          for(DataPoint tempdp:tempDps){
        	  
        	  //System.out.println(tempdp.getX()+","+tempdp.getY()+","+num+",");
        	  fw.write(tempdp.getX()+","+tempdp.getY()+","+num+",");
              
          }
          num = num +1;
      }
      fw.close();
      
      long endTime = System.currentTimeMillis();
      System.out.println("*****Agglomerative Hierarchical Clustering Result*****");
      System.out.println();
      ExternalEvaluation ee=new ExternalEvaluation();
      ee.main(hierresult,origin,clusternum);
      //System.out.println();
      InternalEvaluation ie=new InternalEvaluation();
      ie.main(hierresult,clusternum);
      System.out.println("Running Time = " + (endTime - startTime) + " ms");
  }
}