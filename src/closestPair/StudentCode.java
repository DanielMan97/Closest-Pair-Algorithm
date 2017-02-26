package closestPair;

import java.util.*;

/** The student's implementation of Shamos's Algorithm. */
public class StudentCode {

    /** Find the square distance of the closest pairs in the point set.
     *  This static function is the preparation step for the recursive part of
     *  the algorithm defined in the method closestPairAux.
     *
     *  @param P    A set of points in which a closest pair is to be found
     *  @return     The closest distance between two points in set P
     *  @throws TrivialClosestPairException     Number of points is less than 2
     *  @throws UnknownSortOptionException      The sorting criteria is unknown
     */
    public static int closestPair(PointSet P)
            throws TrivialClosestPairException, UnknownSortOptionException {
        if(P.size() < 2){//This case was stated in the coursework that this should throw an exception
            throw new TrivialClosestPairException();
        }
        if(P.size() <=3){
            return PointSet.naiveClosestPair(P);
        }
        //Calls recursive method with the sorted X and Y axis Points of the PointSet
        return closestPairAux(P.sort('x'),P.sort('y'));
    }

    /** The recursive part of Shamos's Algorithm. The parameter X is an array of
     *  points sorted by the X axis and the parameter Y is the same array of
     *  points sorted by the Y axis. The burden of work is going on here.
     *  Good luck!
     *
     *  @param X     An array of points sorted by X, breaking ties using Y
     *  @param Y     An array of points sorted by Y, breaking ties using X
     *  @return      The closest distance between two points in set X
     *  @throws TrivialClosestPairException     Number of points is less than 2
     *  @throws UnknownSortOptionException      The sorting criteria is unknown
     */
   
    
    
    public static int closestPairAux(Point[] X, Point[] Y)
            throws TrivialClosestPairException, UnknownSortOptionException {  
    	
    	//Perform Naive Algorithm
        if(X.length <=3){
        	PointSet set = new PointSet(X);
            return PointSet.naiveClosestPair(set);
        }
        //Split X into 2 arrays XL,XR which is evenly disjoint sets.
        Point[] XL = new Point[(X.length)/2];
        Point[] XR = new Point[(X.length)-(XL.length)];
       
        //Split XL and XR into the their respective arrays from the sorted X array.
        partitionX(X,XL,XR);
        
        //Split Y into YL and YR
        Point[] YL = new Point[(XL.length)];
        Point[] YR = new Point[(XR.length)];
        Point median = XL[XL.length - 1];
        
        splitY(median,Y,YL,YR);
        
        //Recurse throughout left and right arrays finding deltaLeft and deltaRight 
        int deltaLeft = closestPairAux(XL,YL);
        int deltaRight = closestPairAux(XR,YR);
        int delta = Math.min(deltaLeft, deltaRight);//The minimum of the 2 sides.
       
		ArrayList<Point> stripList = new ArrayList<Point>();
		for (int i=0; i<Y.length; i++){
			if(Math.abs(Y[i].getX()-median.getX()) <= 2 * delta){ // <= 2 * delta to cover each side of the strip

				stripList.add(Y[i]);
			}
		}    
        return strip(stripList,delta);
    }
private static int strip(List<Point> stripList, int delta){
	for (int i=0; i<stripList.size(); i++) {
		for (int j=i+1; j<(i+5); j++) {
			if(j < stripList.size()){
				if(stripList.get(i).sqrDist(stripList.get(j)) < delta){
					delta = stripList.get(i).sqrDist(stripList.get(j));
				}
			}
		}
	}
	return delta;
}
private static void partitionX(Point[] X, Point[] XL, Point[] XR){
	 for (int i=0; i<(X.length/2); i++){
         XL[i] = X[i];
     }
     for (int i=0; i<((X.length)-(XL.length)); i++){
         XR[i] = X[(X.length/2) + i];
     }
}
    /** Create arrays YL and YR that contain points of Y to the left and to the
     *  right of testPoint respectively.
     *
     * @param testPoint     The deciding point (XL, YL)
     * @param Y             All points at the current level of recursion
     * @param YL            An output parameter for the YL array
     * @param YR            An output parameter for the YR array
    */
    public static void splitY(Point mid, Point [] Y, Point [] YL, Point [] YR) {
        int l = 0;
        int r = 0;
        
        for (int i=0; i<Y.length; i++){
            
            if(Y[i].getX() == mid.getX()){
                if(Y[i].getY() <= mid.getY()){
                    YL[l++] = Y[i];
                }
                else{
                    YR[r++] = Y[i];
                }
            }
            else if(Y[i].getX() < mid.getX()){
                YL[l++] = Y[i];
            }
            else{
                YR[r++] = Y[i];
                
            }
        }
    }

}