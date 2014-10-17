package s3;


/*************************************************************************
 *************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdDraw;


public class KdTree {
	private Node root;	//Root of KdTree
	private int size;	//Size of KdTree
	
	//Class for Node and a constructor
	private static class Node {
		private Point2D pt;				//The point
		private RectHV rect;			//The axis-aligned rectangle
		private Node left;		
		//Left/bottom subtree
		private Node right;				//Right/top subtree
		private Boolean alignment; 		//True for vertical, false for horizontal
		
		public Node(Point2D pt, RectHV rect, Boolean alignment){
			this.pt = pt;
			this.rect = rect;
			this.alignment = alignment;
		}
	}
	
    // construct an empty set of points
    public KdTree() {
    	root = null;
    	size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
    	if(isEmpty()){
    		RectHV rect = new RectHV(0, 0, 1, 1); //The root has the all of the space in the beginning
    		root = new Node(p, rect, true);
    		size++;
    	}
    	else
    		insert(root, p);	
    }
    
    private void insert(Node node, Point2D p) {
    	double cmpX = Double.compare(p.x(), node.pt.x());
    	double cmpY = Double.compare(p.y(), node.pt.y());
    	
    	if(cmpX == 0 && cmpY == 0) return;	//if there is a node with the same coordinates 
    	
    	if(node.alignment){	//If node is vertical
    		if(cmpX < 0 ){	//If it is supposed to go to the left
    			if(node.left == null){
    				RectHV rect = calcRect(node, true, false);
    				Node newnode = new Node(p, rect, false);
        			node.left = newnode;
        			size++;
    			}
    			else insert(node.left, p);
    		}
    		if(cmpX >= 0){	//If it is supposed to go to the right
    			if(node.right == null){
    				RectHV rect = calcRect(node, false, false);
    				Node newnode = new Node(p, rect, false);
    				node.right = newnode;
    				size++;
    			}
    			else insert(node.right, p);
    		}
    	}
    	else{	//If node is horizontal
    		if(cmpY < 0){	//Top
    			if(node.left == null){
    				RectHV rect = calcRect(node, false, true);
    				Node newnode = new Node(p, rect, true);
    				node.left = newnode;
    				size++;
    			}
    			else insert(node.left, p);
    		}
    		if(cmpY >= 0){	//Bottom
    			if(node.right == null){
    				RectHV rect = calcRect(node, false, false);
    				Node newnode = new Node(p, rect, true);
    				node.right = newnode;
    				size++;
    			}
    			else insert(node.right, p);
    		}
    	}
    }
    
    //Calculates the rectangular of a given node
    private RectHV calcRect(Node node, boolean left, boolean top ){
    	if(node.alignment){
    		if(left){
    			RectHV rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.pt.x(), node.rect.ymax());
    			return rect;
    		}
    		else{
    			RectHV rect = new RectHV(node.pt.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
    			return rect;
    		}
    	}
    	else{
    		if(top){
    			RectHV rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.pt.y());
    			return rect;
    		}
    		else{
    			RectHV rect = new RectHV(node.rect.xmin(), node.pt.y(), node.rect.xmax(), node.rect.ymax());
    			return rect;
    		}
    	}
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
    	if(isEmpty())
    		return false;
    	else 
    		return contains(root, p);
    }
    
    private boolean contains(Node node, Point2D p){
    	if(node != null){
    		double Xcmp = Double.compare(p.x(), node.pt.x());	//Compares x value of p to x value of current node
    		double Ycmp = Double.compare(p.y(), node.pt.y());	//compares y value of p to y value of current node
    		
    		if(Xcmp == 0 && Ycmp == 0)
    			return true;
    		if(node.alignment){	//If the node is vertical we compare the x values
    			if(Xcmp <= 0)
    				return contains(node.left, p);
    			else
    				return contains(node.right, p);
    		}
    		else{
    			if(Ycmp <= 0)	// If the node is not horizontal, we compare the y values
    				 return contains(node.left, p);
    			else
    				return contains(node.right, p);
    		}
    		
    	}
    	return false;	//if node is null
    }

    // draw all of the points to standard draw
    public void draw() {
    	StdDraw.line(0.0, 1, 0.0, 0.0);
    	StdDraw.line(0.0, 0.0, 1, 0.0);
    	StdDraw.line(1, 1, 0.0, 1);
    	StdDraw.line(1, 0.0, 1, 1);
    	
    	draw(root);
    }
    
    private void draw(Node node){
    	if(node != null){
    		double Xmin = node.rect.xmin();
    		double Ymin = node.rect.ymin();
    		double Xmax = node.rect.xmax();
    		double Ymax = node.rect.ymax();
    		
	    	if(node.alignment){
	    		StdDraw.setPenRadius(0.002);
	    		StdDraw.setPenColor(StdDraw.RED);
	    		StdDraw.line(node.pt.x(), Ymin, node.pt.x(), Ymax);
	    		StdDraw.setPenColor(StdDraw.BLACK);
	    		StdDraw.setPenRadius(0.005);
	    		node.pt.draw();
	    	}
	    	else{
	    		StdDraw.setPenRadius(0.002);
	    		StdDraw.setPenColor(StdDraw.BLUE);
	    		StdDraw.line(Xmin, node.pt.y(), Xmax, node.pt.y());
	    		StdDraw.setPenColor(StdDraw.BLACK);
	    		StdDraw.setPenRadius(0.005);
	    		node.pt.draw();
	    	}
	    	draw(node.right);
	    	draw(node.left);
    	}
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
    	if(isEmpty()) return null;
    	else{
    		SET<Point2D> set = new SET<Point2D>();
        	set = range(root, rect, set);
            return set;
    	}
    }
    
    private SET<Point2D> range(Node node, RectHV rect, SET<Point2D> set) {
    	if(node == null || !rect.intersects(node.rect))	//If the node is null or if it does not intersect we dont have to go further into the subtree
    		return set;
    	
    	double cmpXMin = Double.compare(node.pt.x(), rect.xmin());	//X value of the node has to be greater or equal to xmin to be in the rect
    	double cmpXMax = Double.compare(node.pt.x(), rect.xmax());	//X value of the node has to be smaller or equal to xmax to be in the rect
    	double cmpYMin = Double.compare(node.pt.y(), rect.ymin());	//Y value of the node has to be greater or equal to ymin to be in the rect
    	double cmpYMax = Double.compare(node.pt.y(), rect.ymax());	//Y value of the node has to be smaller or equal to ymax to be in the rect
    	
    	if(cmpXMin >= 0 && cmpXMax <= 0 && cmpYMin >= 0 && cmpYMax <= 0)	//If the node is within given rect
    		set.add(node.pt);
    	
    	if(node.alignment){	//If the node is vertical compare the x axis
    		if(cmpXMin >= 0)
    			set = range(node.left, rect, set);
    		if(cmpXMax <= 0)
    			set = range(node.right, rect, set);
    	}
    	else{	//The node is horizontal, compare the Y axis
    		if(cmpYMin >= 0)
    			set = range(node.left, rect, set);
    		if(cmpYMax <= 0)
    			set = range(node.right, rect, set);
    	}
    	return set;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if(isEmpty()) 
        	return null;
        else
        	return nearest(root, p, root.pt);
    }
    
    private Point2D nearest(Node node, Point2D p, Point2D nearest){
    	if(node == null)
    		return nearest;
    	else{
    		if(node.pt.distanceSquaredTo(p) < nearest.distanceSquaredTo(p))	//If the distance from the node point is smaller than nearest found
    			nearest = node.pt;
    		if(node.left != null && node.left.rect.distanceSquaredTo(p) < node.pt.distanceSquaredTo(p)){	//If the distance from the nodes left child rect is smaller
        		nearest = nearest(node.left, p, nearest);
        	}
        	if(node.right != null && node.right.rect.distanceSquaredTo(p) < node.pt.distanceSquaredTo(p))	// if the distance from the nodes right child rect is smaller
        		nearest = nearest(node.right, p , nearest);
        	
        	return nearest;
    	}
    	
    }
    

    public static Point2D randomPoint(){
        double x = StdRandom.uniform(0, 1);
        double y = StdRandom.uniform(0, 1);
        return new Point2D(x,y);
    }
    
    public static void main(String[] args) {
        In in10k = new In("/Users/bjornhalldor/Dropbox/HR_Tolvunarfraedi/Haustonn_2014/Reiknirit/Skilaverkefni_3/packet/SomeInputs/input10K.txt");
        In in100k = new In("/Users/bjornhalldor/Dropbox/HR_Tolvunarfraedi/Haustonn_2014/Reiknirit/Skilaverkefni_3/packet/SomeInputs/input100K.txt");
        In in1m = new In("/Users/bjornhalldor/Dropbox/HR_Tolvunarfraedi/Haustonn_2014/Reiknirit/Skilaverkefni_3/packet/SomeInputs/input1M.txt");
        Out out = new Out();
        
        int numberOfNearestPointsTested = 400;
        double nearestTime10k = 0;
        double nearestTime100k = 0;
        double nearestTime1m = 0;
        
        
        // TESTING 10K
        PointSET set10k = new PointSET();
        for (int i = 0; !in10k.isEmpty(); i++) {
            double x = in10k.readDouble(), y = in10k.readDouble();
            set10k.insert(new Point2D(x, y));
        }
        
        Stopwatch time10k = new Stopwatch();
        for (int i = 0; i < numberOfNearestPointsTested; i++){
            Point2D nearestTest = randomPoint();
            set10k.nearest(nearestTest);
            nearestTime10k += time10k.elapsedTime();
        }
        nearestTime10k = nearestTime10k/numberOfNearestPointsTested;
        
        // TESTING 100K
        PointSET set100k = new PointSET();
        for (int i = 0; !in100k.isEmpty(); i++) {
            double x = in100k.readDouble(), y = in100k.readDouble();
            set10k.insert(new Point2D(x, y));
        }
        
        Stopwatch time100k = new Stopwatch();
        for (int i = 0; i < numberOfNearestPointsTested; i++){
            Point2D nearestTest = randomPoint();
            set100k.nearest(nearestTest);
            nearestTime100k += time100k.elapsedTime();
        }
        nearestTime100k = nearestTime100k/numberOfNearestPointsTested;
        
        // TESTING 1 M
        PointSET set1m = new PointSET();
        for (int i = 0; !in1m.isEmpty(); i++) {
            double x = in1m.readDouble(), y = in1m.readDouble();
            set1m.insert(new Point2D(x, y));
        }
        
        Stopwatch time1m = new Stopwatch();
        for (int i = 0; i < numberOfNearestPointsTested; i++){
            Point2D nearestTest = randomPoint();
            set1m.nearest(nearestTest);
            nearestTime1m += time1m.elapsedTime();
        }
        nearestTime1m = nearestTime1m/numberOfNearestPointsTested;
        
        
        DecimalFormat numberFormat = new DecimalFormat("#0.00000000000000");
        out.println("Average time it takes to run nearest in a tree the size of 10K: " + numberFormat.format(nearestTime10k));
        out.println("Average time it takes to run nearest in a tree the size of 100K: " + numberFormat.format(nearestTime100k));
        out.println("Average time it takes to run nearestin a tree the size of 1M: " + numberFormat.format(nearestTime1m));
}
