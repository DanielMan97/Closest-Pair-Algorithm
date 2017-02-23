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
    	if(P.size() < 2){
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
    private static int naive(Point[] X, Point[] Y){
    	
    	return 0;
    }
    
    
    public static int closestPairAux(Point[] X, Point[] Y)
            throws TrivialClosestPairException, UnknownSortOptionException {
    	
    	
    	
    	if(X.length <=3){
    		return naive(X,Y);
    	}
		//Make and fill Xl, Xr
		Point[] ptsXL = new Point[(X.length)/2];
		Point[] ptsXR = new Point[(X.length)-(ptsXL.length)];
		for (int i=0; i<(X.length/2); i++){
			ptsXL[i] = X[i];
		}
		for (int i=0; i<((X.length)-(ptsXL.length)); i++){
			ptsXR[i] = X[(X.length/2) + i];
		}

		
		//Make and fill Yl, Yr
		Point[] ptsYL = new Point[(ptsXL.length)];
		Point[] ptsYR = new Point[(ptsXR.length)];
		Point median = ptsXL[ptsXL.length - 1];
		int mid = ptsXL[ptsXL.length-1].getX();
		
		splitY(median,Y,ptsYL,ptsYR);
		/*
		int li = 0;
		int ri = 0;
		for (int i=0; i<Y.length; i++){
			if(Y[i].getX() <= mid && li<ptsXL.length){
				ptsYL[li] = Y[i];
				li++;
			}
			else{
				ptsYR[ri] = Y[i];
				ri++;
			}
		}*/

    		/*
    	//Make and fill Xl, Xr
		Point[] XL = new Point[(X.length)/2];
		Point[] XR = new Point[(X.length)-(XL.length)];
		for (int i=0; i<(X.length/2); i++){
			XL[i] = X[i];
		}
		for (int i=0; i<((X.length)-(XL.length)); i++){
			XR[i] = X[(X.length/2) + i];
		}
    	
    	
    	Point[] YL = new Point[XL.length];
    		Point[] YR = new Point[XR.length];
    	Point median = XL[XL.length - 1];

    	splitY(median,Y,YL,YR);
    	
    	
    	*/

    	int deltaLeft = closestPairAux(ptsXL,ptsYL);
    	int deltaRight = closestPairAux(ptsXR,ptsYR);

    	int delta = Math.min(deltaLeft, deltaRight);
    	
    	
    	return delta;
    }

    /** Create arrays YL and YR that contain points of Y to the left and to the
     *  right of testPoint respectively.
     *
     * @param testPoint     The deciding point (XL, YL)
     * @param Y             All points at the current level of recursion
     * @param YL            An output parameter for the YL array
     * @param YR            An output parameter for the YR array
    */
    public static void splitY(Point mid, Point [] Y, Point [] ptsYL, Point [] ptsYR) {
    	int li = 0;
		int ri = 0;
		
		for (int i=0; i<Y.length; i++){
			System.out.println("-----------------");

			System.out.println("i -> " + i + " and Y.length -> " + Y.length);
			System.out.println("li -> " + li + " and YL.length -> " + ptsYL.length);
			System.out.println("ri -> " + ri + " and YR.length -> " + ptsYR.length);

			if(Y[i].getX() == mid.getX()){
				if(Y[i].getY() <= mid.getY()){
					ptsYL[li++] = Y[i];
				}
				else{
					ptsYR[ri++] = Y[i];
				}
			}
			else if(Y[i].getX() < mid.getX()){
				ptsYL[li++] = Y[i];
			}
			else{
				ptsYR[ri++] = Y[i];
				
			}
		}
    }

}
