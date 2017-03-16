public class DataPoint {

    private double dimension[]; 
    public DataPoint(){
    }
    public DataPoint(double[] dimension){
         this.dimension=dimension;
    }
    public double[] getDimension() {
        return dimension;
    }
    public void setDimension(double[] dimension) {
        this.dimension = dimension;
    }
    public double getX(){
    	return this.dimension[0];
    }
    public double getY(){
    	return this.dimension[1];
    }
}