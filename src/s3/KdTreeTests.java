package s3;

/*************************************************************************
 *************************************************************************/

import java.text.DecimalFormat;
import java.util.Arrays;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdRandom;

public class KdTreeTests {	
	private Node root;
    private int size;
	
	private class Node {
		// 
	  	private Point2D point;
	  	// Rectangle which node belongs to
	  	private RectHV rect;
	    // Links to nodes left and right of current node
	    private Node left, right;
	       
	    public Node(Point2D point, RectHV rect) {
	    	this.point = point;
	        this.rect = rect;
	    }
	}
	// construct an empty set of points
	public KdTreeTests() {
		root = null;
		size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
    	if (root == null) return true;
    	else return false;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
    	if (root == null){
    		RectHV rect = new RectHV(0,0,1,1);
    		root = new Node(p, rect);
    		size++;
    	} else {
    		insert(root, p, 1);
    	}
    };
    
    public void insert(Node node, Point2D p, int alignment){
    	// alignment is an even number which means the node being checked is Horizontal
    	if(alignment%2 == 0){
    		// cmp point being inserted with point in the node, cmp < 0 if p is less then node else cmp > 0 or equal if they are equal
    		//double cmp = Double.compare(p.y(), node.point.y());
    		// If the y value of inserted point is less then current node y value we go to the left
    		if (p.equals(node.point)){
    			return;
    		}
    		else if (p.y() < node.point.y()){
    			if (node.left != null){
        			insert(node.left, p, alignment + 1);	
    			} else {
    				// create a new rectangle with a ymax as the value of y from the parent point
    				RectHV rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.point.y());
    				// Create a new node with the point p and new rectangle
    				node.left = new Node(p, rect);
    				// Increment the size of the tree
    				size++;
    				return;
    			}
    		} 
    		// If the y value of the inserted point is more then current node y value we go to the right
    		else {
    			if (node.right != null){
    				// recursive call to the right
        			insert(node.right, p, alignment + 1);
    			} else {
    				// Create a new rectangle with y min as the value of y from the parent node
    				RectHV rect = new RectHV(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.rect.ymax());
    				// Create a new node with the point p and new rectangle
    				node.right = new Node(p, rect);
    				// Increment the size of the tree
    				size++;
    				return;
    			}
    		} 
    	} 
    	// Alignment is an Odd number so we check the x values of the nodes
    	else {
    		//double cmp = Double.compare(p.x(), node.point.x());
    		if (p.equals(node.point)){
    			return;
    		}
    		if (p.x() < node.point.x()){
    			// recursive call to the left
    			if (node.left != null){
    				insert(node.left, p, alignment + 1);	
    			} else {
    				// Create a new rectangle with x max as the value of x from the parent node
    				RectHV rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.point.x(), node.rect.ymax());
    				// Create a new node containing the point p and new rect
                    node.left = new Node(p, rect);
                    // increment the size
    				size++;
    				return;
    			}
    		} else {
                // recursive call to the right
    			if(node.right != null){
        			insert(node.right,p, alignment + 1);	
    			} else {
                    // Create a new rect with its x min value taken from parent node's x value
    				RectHV rect = new RectHV(node.point.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
    				node.right = new Node(p, rect);
    				size++;
    				return;
    			}
    		} 
    	}
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        if (root == null) return false;
        else return contains(root, p, 1);
    }
    
    public boolean contains(Node node, Point2D p, int alignment){
    	// If alignment is an even number we check the y values of nodes
        if (alignment%2 == 0){
            // Create a compare value
    		//double cmp = Double.compare(p.y(), node.point.y());
    		// if cmp is less then 0 we go to the left
        	if (p.equals(node.point)){
    			return true;	
			}
        	if (p.y() < node.point.y()) {
                // node to the left is null we return false
    			if (node.left == null){
    				return false;
    			} 
                // node to the left is not null and we do a recursive call to the left
                else {
    				return contains(node.left, p, alignment + 1);
    			}
    		} 
            // cmp is more then 0 so we go to the right
            else {
                // node to the right is null, we therefore return false
    			if(node.right == null){
    				return false;
    			} else {
    				return contains(node.right, p, alignment +1);
    			}
    		} 
    	} else {
    		//double cmp = Double.compare(p.x(), node.point.x());
        	if (p.equals(node.point)){
    			return true;	
			}
    		if (p.x() < node.point.x()){
    			if (node.left == null){
    				return false;
    			} else {
    				return contains(node.left, p, alignment + 1);
    			}
    		} else {
    			if (node.right == null){
    				return false;
    			} else {
    				return contains(node.right, p, alignment + 1);
    			} 
    		} 
    	}
    }

    // draw all of the points to standard draw
    public void draw() {

    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> points = new SET<Point2D>();
    	range(root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, SET<Point2D> points){
        if (node == null){
            return;
        }
        if (node.rect.intersects(rect)){
        	if(rect.contains(node.point)){
        		points.add(node.point);
       		}
       		range(node.left, rect, points);
       		range(node.right, rect, points);
        }
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if(isEmpty()) 
        	return null;
        else
        	return nearest(root, p, root.point);
    }
    
    private Point2D nearest(Node node, Point2D p, Point2D nearest){
    	if(node == null)
    		return nearest;
    	else{
    		if(node.point.distanceSquaredTo(p) < nearest.distanceSquaredTo(p))	//If the distance from the node point is smaller than nearest found
    			nearest = node.point;
    		if(node.left != null && node.left.rect.distanceSquaredTo(p) < node.point.distanceSquaredTo(p)){	//If the distance from the nodes left child rect is smaller
        		nearest = nearest(node.left, p, nearest);
        	}
        	if(node.right != null && node.right.rect.distanceSquaredTo(p) < node.point.distanceSquaredTo(p))	// if the distance from the nodes right child rect is smaller
        		nearest = nearest(node.right, p , nearest);
        	
        	return nearest;
    	}	
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
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
        KdTreeTests set10k = new KdTreeTests();
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
        KdTreeTests set100k = new KdTreeTests();
        for (int i = 0; !in100k.isEmpty(); i++) {
            double x = in100k.readDouble(), y = in100k.readDouble();
            set100k.insert(new Point2D(x, y));
        }
        
        Stopwatch time100k = new Stopwatch();
        for (int i = 0; i < numberOfNearestPointsTested; i++){
        	Point2D nearestTest = randomPoint();
        	set100k.nearest(nearestTest);
        	nearestTime100k += time100k.elapsedTime();
        }
        nearestTime100k = nearestTime100k/numberOfNearestPointsTested;
        
        // TESTING 1 M
        KdTreeTests set1m = new KdTreeTests();
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
}
