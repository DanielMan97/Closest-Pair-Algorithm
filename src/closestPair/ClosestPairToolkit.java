package closestPair;

import java.util.*;
import java.io.*;

import javax.swing.JFrame;

/** The software toolkit for the exercise. */
public class ClosestPairToolkit {

    /** Check if the student's implementation produces correct outputs by hardcore
     *  tests on t point sets containing n points.
     *
     *  @param t    Number of point sets to test on.
     *  @param n    The size of point sets.
     *  @throws InvalidNumberOfTestsException   Number of tests is negative.
     *  @throws UnwritablePointSetException     Cannot save the point set
     */
    public static void closestPairCheck(int t, int n) throws
            InvalidNumberOfTestsException, UnwritablePointSetException {
        try{
            
            if (t <= 0)
                throw new InvalidNumberOfTestsException();

            boolean isSuccess = true;
    
            for (int i = 1; i <= t; i++) {
                System.out.print("Test " + i + ": ");
                PointSet p = PointSet.generatePoints(n);
                System.out.print("(Naive algorithm: ");
                int naiveResult = PointSet.naiveClosestPair(p);
                System.out.print("done) (Student algorithm: ");
                int studentResult = StudentCode.closestPair(p);
                System.out.print("done)");
                if (naiveResult == studentResult) {
                    System.out.println(" ... comparison successful");
                } else {
                    isSuccess = false;
                    System.out.println(" ... comparison unsuccessful");
                    System.out.println();
                    System.out.println("The result from the naive algorithm    : " + naiveResult);
                    System.out.println("The result from the student's algorithm: " + studentResult);
                    System.out.println();
                    System.out.print("Saving the test point set to \"points.txt\"... ");
                    p.resetIter();

                    Point point = p.nextPoint();

                    try {
                        File pointsFile = new File("points.txt");
                        pointsFile.createNewFile();
                        FileOutputStream fstream = new FileOutputStream(pointsFile, false);
                        DataOutputStream out = new DataOutputStream(fstream);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
                        while (point != null) {
                            bw.write(point.getX() + " " + point.getY() + "\n");
                            point = p.nextPoint();
                        }
                        bw.flush();
                        bw.close();
                    } catch (Exception e) {
                        throw new UnwritablePointSetException();
                    }

                    System.out.println("Done.");
                    break;
                }
            }
    
            if (isSuccess) {
                System.out.println();
                System.out.println("Congratulations! Your program has passed the heavy test.");
            } else {
                System.out.println();
                System.out.println("The program has terminated due to an unsuccessful test.");
            }

        } catch (UnknownSortOptionException e) {
            System.out.println("UnknownSortOptionException occurs.");
        } catch (TrivialClosestPairException e) {
            System.out.println("TrivialClosestPairException occurs.");
        }
    }

    /** Get the runtime of the naive algorithm.
     *
     *  @param p    A set of points in which a closest pair is to be found
     *  @return     Execution duration
     */
    public static long getNaiveRuntime(PointSet p) {
        try {
            long start = System.nanoTime();
            int naiveResult = PointSet.naiveClosestPair(p);
            long stop = System.nanoTime();
            return (stop - start) / 1000;
        } catch (TrivialClosestPairException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /** Get the runtime of the student's implementation of Shamos's Algorithm.
     *
     *  @param p    A set of points in which a closest pair is to be found
     *  @return     Execution duration
     */
    public static long getStudentRuntime(PointSet p) {
        try {
            long start = System.nanoTime();
            int studentResult = StudentCode.closestPair(p);
            long stop = System.nanoTime();
            return (stop - start) / 1000;
        } catch (UnknownSortOptionException e) {
            e.printStackTrace();
            return -1;
        } catch (TrivialClosestPairException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /** Measure worst-case runtimes of the naive algorithm and the student's implementation by hardcore tests
     * (on p point sets for various sizes starting from 10 to 10t points) and save it into a text file.
     *
     *  @param p    A set of points in which a closest pair is to be found
     *  @param t    Number of point sets to test on.
     *  @param f    Output file path
     */
    public static void getRuntimes(int p, int t, String f) {
        try {
            BufferedWriter ofhdl = new BufferedWriter(new FileWriter(f));
            ofhdl.write("//  Input-size\t    Naive-RT\t    Student-RT\n");
            ofhdl.flush();

            for (int i = 1; i <= t; i++) {
                int size = 10 * i;
                System.out.print(">> Testing with " + p + " sets of " + size + " points ");
                System.out.flush();
                PointSet pointset = PointSet.generatePoints(size);
                long worstNaiveRuntime = 0;
                long worstStudentRuntime = 0;
    
                for (int j = 0; j < p; j++) {
                    System.out.print(".");
                    System.out.flush();
                    long naiveRuntime = getNaiveRuntime(pointset);
                    long studentRuntime = getStudentRuntime(pointset);
                    if (naiveRuntime > worstNaiveRuntime)
                        worstNaiveRuntime = naiveRuntime;
                    if (studentRuntime > worstStudentRuntime)
                        worstStudentRuntime = studentRuntime;
                }

                System.out.println(" done");
                System.out.flush();

                ofhdl.write(String.format( "%14d\t%12d\t%14d\n", size, worstNaiveRuntime, worstStudentRuntime));
                ofhdl.flush();
            }

            System.out.println(">> Complete!");

            ofhdl.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Get the ratios of worst-case runtimes of the student's implementation of Shamos's Algorithm divided by n lg n
     *  and save them into a text file.
     *
     *  @param p            A set of points in which a closest pair is to be found
     *  @param t            Number of point sets to test on
     *  @param threshold    Index of the last ratio in the array of sorted ratios to include in calculation of the
     *                      average and maximum
     *  @param f            Output file path
     *  @return             An array, where first element contains the truncated maximum ratio, and the second - the
     *                      truncated average ratio
     */
    public static double[] getRatios(int p, int t, int threshold, String f) {
        double[] aggregatedRatios = {0.0, 0.0};
        try{

            ArrayList<Double> ratios = new ArrayList<Double>();

            BufferedWriter ofhdl = new BufferedWriter(new FileWriter(f));
            ofhdl.write("//  Input-size\t         Ratio\n");
            ofhdl.flush();

            for (int i = 1; i <= t; i++) {
                int size = 10 * i;
                System.out.print(">> Testing with " + p + " sets of " + size + " points ");
                System.out.flush();
                PointSet pointset = PointSet.generatePoints(size);
                long worstStudentRuntime = 0;

                for (int j = 0; j < p; j++) {
                    System.out.print(".");
                    System.out.flush();
                    long studentRuntime = getStudentRuntime(pointset);
                    if (studentRuntime > worstStudentRuntime)
                        worstStudentRuntime = studentRuntime;
                }

                double ratio = 1.0 * worstStudentRuntime / (size * Math.log(size));
                ratios.add(ratio);

                System.out.println(" done");
                System.out.flush();

                ofhdl.write(String.format(
                        "%14d\t%14.4f\n",
                        size, ratio
                ));
                ofhdl.flush();
            }

            double[] sortedRatios = new double[ratios.size()];
            for (int i = 0; i < ratios.size(); i++)
                sortedRatios[i] = ratios.get(i);
            Arrays.sort(sortedRatios);

            double maxRatio = sortedRatios[threshold];
            aggregatedRatios[0] = maxRatio;
            double avgRatio = 0.0;
            for (int i = 0; i <= threshold; i++)
                avgRatio += sortedRatios[i];
            avgRatio /= (threshold + 1);
            aggregatedRatios[1] = avgRatio;

            ofhdl.write("Sorted ratios are:");
            for (int i = 0; i < sortedRatios.length; i++) {
                if (i % 5 == 0) ofhdl.write("\n    ");
                ofhdl.write(String.format("%.4f", sortedRatios[i]));
                if (i != sortedRatios.length - 1) ofhdl.write(", ");
            }
            ofhdl.write("\n");
            ofhdl.write(String.format("Truncated maximum ratio is: %14.4f\n", maxRatio));
            ofhdl.write(String.format("Truncated average ratio is: %14.4f\n", avgRatio));
            ofhdl.write(String.format("Ratio index threshold is: %13d\n", threshold));

            System.out.println(">> Complete!");

            ofhdl.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return aggregatedRatios;
    }

    /** Graphically plot the worst-case runtimes read from the text file and also plot the asymptotic lines for the
     *  maximum and average ratios.
     *  @param maxRatio             Maximum ratio of runtime to n log(n) across varying point set sizes
     *  @param avgRatio             Average ratio of runtime to n log(n) across varying point set sizes
     *  @param inputRuntimesPath    Path to the file containing ratios
     */
	public static void plotRuntimes(double maxRatio, double avgRatio, String inputRuntimesPath) {
        try {
            BufferedReader ifhdl = new BufferedReader(new FileReader(inputRuntimesPath));
            Hashtable<Integer, Integer> naiveTbl = new Hashtable<Integer, Integer>();
            Hashtable<Integer, Integer> studentTbl = new Hashtable<Integer, Integer>();
            while (true) {
                String line = ifhdl.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.startsWith("//")) continue;
                String[] tokens = line.split("[ \t]+");
                if (tokens.length == 0) continue;
                if (tokens.length != 3) throw new IllegalArgumentException();
                int inputSize = Integer.parseInt(tokens[0]);
                int naiveRT = Integer.parseInt(tokens[1]);
                int studentRT = Integer.parseInt(tokens[2]);

                naiveTbl.put(inputSize, naiveRT);
                studentTbl.put(inputSize, studentRT);
            }

			double[][] data = new double[naiveTbl.size()+1][3];

            data[0][0] = 0.0;
            data[0][1] = 0.0;
            data[0][2] = 0.0;

			Integer[] keys = new Integer[naiveTbl.size()];
			keys = naiveTbl.keySet().toArray(keys);
			Arrays.sort(keys);
			int inputSize = 0;
            int naiveRT = 0;
			int studentRT = 0; 
			for (int i = 0; i < keys.length; i++) {
				inputSize = keys[i];
				naiveRT = naiveTbl.get(inputSize);
				studentRT = studentTbl.get(inputSize);
				data[i+1][0] = inputSize;
				data[i+1][1] = ((double) naiveRT) / 1000;
				data[i+1][2] = ((double) studentRT) / 1000;
			}

			JFrame f = new JFrame();
		    f.setTitle("Runtime Plot (INF2B Coursework 1)");
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    GraphingData gd = new GraphingData(1.0, data, 1, 10, maxRatio, avgRatio, "plot.jpg");
		    f.add(gd);
		    f.setSize(800,800);
		    f.setLocation(200,200);
		    f.setVisible(true);
		    gd.save();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println(">> Illegal file format: Graph plotter has terminated unexpectedly.");
		}
	}
}
