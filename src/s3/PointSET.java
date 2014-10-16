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

    public static void main(String[] args) {
        In in = new In();
        Out out = new Out();
        int nrOfRecangles = in.readInt();
        int nrOfPointsCont = in.readInt();
        int nrOfPointsNear = in.readInt();
        RectHV[] rectangles = new RectHV[nrOfRecangles];
        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
        for (int i = 0; i < nrOfRecangles; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsCont; i++) {
            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsNear; i++) {
            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        PointSET set = new PointSET();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble(), y = in.readDouble();
            set.insert(new Point2D(x, y));
        }
        for (int i = 0; i < nrOfRecangles; i++) {
            // Query on rectangle i, sort the result, and print
            Iterable<Point2D> ptset = set.range(rectangles[i]);
            int ptcount = 0;
            for (Point2D p : ptset)
                ptcount++;
            Point2D[] ptarr = new Point2D[ptcount];
            int j = 0;
            for (Point2D p : ptset) {
                ptarr[j] = p;
                j++;
            }
            Arrays.sort(ptarr);
            out.println("Inside rectangle " + (i + 1) + ":");
            for (j = 0; j < ptcount; j++)
                out.println(ptarr[j]);
        }
        out.println("Contain test:");
        for (int i = 0; i < nrOfPointsCont; i++) {
            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
        }

        out.println("Nearest test:");
        for (int i = 0; i < nrOfPointsNear; i++) {
            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
        }

        out.println();
    }

}
