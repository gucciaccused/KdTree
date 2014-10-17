package s3;


/****************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    
 *  Dependencies:
 *  Author:
 *  Date:
 *
 *  Data structure for maintaining a set of 2-D points, 
 *    including rectangle and nearest-neighbor queries
 *
 *************************************************************************/

import java.util.Arrays;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;


public class PointSET {
	
	SET<Point2D> set;
	
    // construct an empty set of points
    public PointSET() {
    	set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
    	if(!contains(p))
    		set.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
    	if(set.contains(p))
    		return true;
    	else
    		return false;
    	
    }

    // draw all of the points to standard draw
    public void draw() {
    	for(Point2D p : set){
    		p.draw();
    	}
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
    	if(set.isEmpty())
    		return null;
    	else{
    		
    		SET<Point2D> tmp = new SET<Point2D>();
        	for(Point2D p : set){
        		double cmpXmin = Double.compare(p.x(), rect.xmin());
        		double cmpXmax = Double.compare(p.x(), rect.xmax());
        		double cmpYmin = Double.compare(p.y(), rect.ymin());
        		double cmpYmax = Double.compare(p.y(), rect.ymax());
        		
        		
        		if(cmpXmin >= 0 && cmpXmax <= 0 && cmpYmin >= 0 && cmpYmax <= 0){
        			tmp.add(p);
        		}
        	}
        	
        	return tmp;
    	}
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if(set.isEmpty())
        	return null;
        else{
        		Point2D neighbor = p;	
        		double distance = 1.1;	//max distance a node can have
        		
        		for(Point2D n : set){
        			if(p.distanceSquaredTo(n) < distance){
        				distance = p.distanceSquaredTo(n);
        				neighbor = n;
        			}
        		}
        	return neighbor;
        }
    }

        public static Point2D randomPoint(){
        double x = StdRandom.uniform();
        double y = StdRandom.uniform();
        return new Point2D(x,y);
    }
    
    public static void main(String[] args) {
    
        In in100k = new In("/Users/bjornhalldor/Dropbox/HR_Tolvunarfraedi/Haustonn_2014/Reiknirit/Skilaverkefni_3/packet/SomeInputs/input100K.txt");
        In in1m = new In("/Users/bjornhalldor/Dropbox/HR_Tolvunarfraedi/Haustonn_2014/Reiknirit/Skilaverkefni_3/packet/SomeInputs/input1M.txt");
        Out out = new Out();
        
        // TESTING 100K
        int countCalculations_100k = 0;
        PointSET set100k = new PointSET();
        for (int i = 0; !in100k.isEmpty(); i++) {
            double x = in100k.readDouble(), y = in100k.readDouble();
            set100k.insert(new Point2D(x, y));
        }
        for (int i = 0; i < 5; i++){
            Stopwatch time100k = new Stopwatch();
            while(time100k.elapsedTime() < 100.0) {
                Point2D nearestTest = randomPoint();
                set100k.nearest(nearestTest);
                countCalculations_100k++;
            }
        }
        
        countCalculations_100k = countCalculations_100k/5;
        
        // TESTING 1 M
        PointSET set1m = new PointSET();
        int countCalculations_1m = 0;
        for (int i = 0; !in1m.isEmpty(); i++) {
            double x = in1m.readDouble(), y = in1m.readDouble();
            set1m.insert(new Point2D(x, y));
        }
        for (int i = 0; i < 5; i++){
            Stopwatch time1m = new Stopwatch();
            while(time1m.elapsedTime() < 100.0) {
                Point2D nearestTest = randomPoint();
                set1m.nearest(nearestTest);
                countCalculations_1m++;
            }
        }
        countCalculations_1m = countCalculations_1m/5;


        out.println("Number of calculations per sek where tree has 10K nodes: " + countCalculations_100k/100);
        out.println("Number of calculations per sek where tree has 1m nodes: " + countCalculations_1m/100);
    }
}
