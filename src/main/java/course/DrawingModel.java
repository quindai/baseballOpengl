package course;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class DrawingModel {

    private List<List<Double>> myPolygons;    
    private List<Double> myWorkingPoly;
    private double[] myMousePoint;
    private float myCol[]  = {0.0F,0.0F,0.0F,1};
    private float myBgCol[] = {1,1,1,1};
    
    public DrawingModel() {        
        myPolygons = new ArrayList<List<Double>>();
        myWorkingPoly = new ArrayList<Double>();
        myMousePoint = new double[2];
    }
    
    public void setColor(Color c){
    	myCol[0] = c.getRed()/255.0f;
    	myCol[1] = c.getGreen()/255.0f;
    	myCol[2] = c.getBlue()/255.0f;    	
    }
    
    public void setBackgroundColor(Color c){
    	myBgCol[0] = c.getRed()/255.0f;
    	myBgCol[1] = c.getGreen()/255.0f;
    	myBgCol[2] = c.getBlue()/255.0f;    	
    }
    
    public float[] getBgColor(){
    	return myBgCol;
    }
    
    public float[] getColor(){
    	return myCol;
    }
    
    public List<List<Double>> getPolygons() {
        return myPolygons;
    }
    
    public List<Double> getWorkingPolygon() {
        return myWorkingPoly;
    }    
    
    public double[] getMousePoint() {
        return myMousePoint;
    }
    
    public void setMousePoint(double x, double y) {
        myMousePoint[0] = x;
        myMousePoint[1] = y;
    }
    
    public void addPoint(double x, double y) {
        myWorkingPoly.add(x);
        myWorkingPoly.add(y);
    }
    
    public void addPolygon() {
        myPolygons.add(myWorkingPoly);
        myWorkingPoly = new ArrayList<Double>();
    }
}