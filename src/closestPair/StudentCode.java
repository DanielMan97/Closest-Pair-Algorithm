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
  
        if(P.size()  < 4){
            return PointSet.naiveClosestPair(P);
        }
        //Calls recursive method with the sorted X and Y axis Points of the PointSet
        
        Point[] Px = P.sort('x');
        Point[] Py = P.sort('y');
        return closestPairAux(Px,Py);
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
        if(X.length < 4){
            return PointSet.naiveClosestPair(new PointSet(X));
        }
        Point[] XL = new Point[(X.length)/2];
        Point[] XR = new Point[(X.length)-(XL.length)];
        
        int i = 0;
        while(i < X.length / 2){
            XL[i] = X[i];

        	i++;
        }
        int j = 0;
        while(j < X.length - XL.length){
            XR[j] = X[(X.length/2) + j];
        	j++;
        }
      
        
        Point[] YR = new Point[(XR.length)];

        Point[] YL = new Point[(XL.length)];
        Point point = XL[XL.length - 1];
        
        splitY(point,Y,YL,YR);
        
        int deltaRight = closestPairAux(XR,YR);
        int deltaLeft = closestPairAux(XL,YL);
        int delta = Math.min(deltaLeft, deltaRight);
       
		ArrayList<Point> my = new ArrayList<Point>();
		for (int m=0; m<Y.length; m++){

			if(Math.abs(Y[m].getX()-point.getX()) <= delta){
				
				my.add(Y[m]);
			}
		}

		for (int outer=0; outer<my.size(); outer++) {
			for (int inner=outer+1; inner < outer+5; inner++) {
				if(inner < my.size()){
					int distance = my.get(outer).sqrDist(my.get(inner));
					if(distance < delta){
						delta = distance;
					}
				}
			}
		}
        
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
