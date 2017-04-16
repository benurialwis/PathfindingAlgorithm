import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

/*************************************************************************
 *  Author: Dr E Kapetanios
 *  Edited By: Benuri Alwis
 *  Last update: 02-04-2017
 *
 *************************************************************************/

public class PathFindingOnSquaredGrid {
    static Scanner sc = new Scanner(System.in);

    // given an N-by-N matrix of open cells, return an N-by-N matrix
    // of cells reachable from the top
    public static boolean[][] flow(boolean[][] open) {
        int N = open.length;
    
        boolean[][] full = new boolean[N][N];
        for (int j = 0; j < N; j++) {
            flow(open, full, 0, j);
        }
    	
        return full;
    }
    
    // determine set of open/blocked cells using depth first search
    public static void flow(boolean[][] open, boolean[][] full, int i, int j) {
        int N = open.length;

        // base cases
        if (i < 0 || i >= N) return;    // invalid row
        if (j < 0 || j >= N) return;    // invalid column
        if (!open[i][j]) return;        // not an open cell
        if (full[i][j]) return;         // already marked as open

        full[i][j] = true;

        flow(open, full, i+1, j);   // down
        flow(open, full, i, j+1);   // right
        flow(open, full, i, j-1);   // left
        flow(open, full, i-1, j);   // up
    }

    // does the system percolate?
    public static boolean percolates(boolean[][] open) {
        int N = open.length;
    	
        boolean[][] full = flow(open);
        for (int j = 0; j < N; j++) {
            if (full[N-1][j]) return true;
        }
    	
        return false;
    }
    
 // does the system percolate vertically in a direct way?
    public static boolean percolatesDirect(boolean[][] open) {
        int N = open.length;
    	
        boolean[][] full = flow(open);
        int directPerc = 0;
        for (int j = 0; j < N; j++) {
        	if (full[N-1][j]) {
        		// StdOut.println("Hello");
        		directPerc = 1;
        		int rowabove = N-2;
        		for (int i = rowabove; i >= 0; i--) {
        			if (full[i][j]) {
        				// StdOut.println("i: " + i + " j: " + j + " " + full[i][j]);
        				directPerc++;
        			}
        			else break;
        		}
        	}
        }
    	
        // StdOut.println("Direct Percolation is: " + directPerc);
        if (directPerc == N) return true; 
        else return false;
    }
    
    // draw the N-by-N boolean matrix to standard draw
    public static void show(boolean[][] a, boolean which) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                	StdDraw.square(j, N-i-1, .5);
                else StdDraw.filledSquare(j, N-i-1, .5);
    }

    // draw the N-by-N boolean matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle
    public static void show(boolean[][] a, boolean which, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                	if ((i == x1 && j == y1) ||(i == x2 && j == y2)) {
                		StdDraw.circle(j, N-i-1, .5);
                	}
                	else StdDraw.square(j, N-i-1, .5);
                else StdDraw.filledSquare(j, N-i-1, .5);
    }
    
    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static boolean[][] random(int N, double p) {
        boolean[][] a = new boolean[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = StdRandom.bernoulli(p);
        return a;
    }
    
    //draw the path form A (x1,y1) to B (x2,y2)
    public static void drawPath(boolean[][] a, int Ai, int Aj, int Bi, int Bj, double hVCost, double dCost, String method){
        Stopwatch timerFlow = new Stopwatch(); //start timer
        int N = a.length;
        ArrayList<Node> path = new AStar().getPath(a, Ai, Aj, Bi, Bj, hVCost, dCost, method);
        
        StdDraw.setPenColor(Color.BLUE);
        
        //coloring the path
        StdDraw.filledSquare(Aj, N-Ai-1, .5);
        for(Node node : path){
            StdDraw.filledSquare(node.y, N - node.x - 1, .5);
        }
        
        //drawing the line
        for(int i = 0 ; i < path.size() -1; i++){
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(path.get(i).y,N-path.get(i).x-1,path.get(i+1).y, N-path.get(i+1).x-1);
        }
        StdDraw.line(path.get(path.size()-1).y,N-path.get(path.size()-1).x-1,Aj, N-Ai-1);
        
        StdOut.println("Elapsed time = " + timerFlow.elapsedTime()); //print the elapsed time
    }
    
    
    public static void findPath(boolean[][] a, int Ai, int Aj, int Bi, int Bj, double hVCost, double dCost){
        System.out.println("Press E/M/C: "); //prompt user to decide the metric
        String input = sc.next();
        StdDraw.clear(); //clear the canvas
        show(a, true, Ai, Aj, Bi, Bj);
        
        switch(input.toUpperCase()){
            case "E":
                dCost = 1.4;
                drawPath(a,Ai,Aj,Bi,Bj,hVCost,dCost,"Euclidean");
                findPath(a, Ai, Aj, Bi, Bj, hVCost, dCost);
                break;
            case "M":
                dCost = 2.0;
                drawPath(a,Ai,Aj,Bi,Bj,hVCost,dCost,"Manhattan");
                findPath(a, Ai, Aj, Bi, Bj, hVCost, dCost);
                break;
            case "C":
                dCost = 1.0;
                drawPath(a,Ai,Aj,Bi,Bj,hVCost,dCost,"Chebyshev");
                findPath(a, Ai, Aj, Bi, Bj, hVCost, dCost);
                break;
            default:
                System.out.println("invalid Input!");
                findPath(a, Ai, Aj, Bi, Bj, hVCost, dCost);
                
    }}
    

    // test client
    public static void main(String[] args) {
        double hVCost = 1.0;
        double dCost = 0;
        int N = 10;

    	// The following will generate a NxN squared grid with relatively few obstacles in it
    	// The lower the second parameter, the more obstacles (black cells) are generated
    	boolean[][] randomlyGenMatrix = random(N, 0.5);
    	
    	StdArrayIO.print(randomlyGenMatrix);
    	show(randomlyGenMatrix, true);
    	
    	System.out.println();
    	System.out.println("The system percolates: " + percolates(randomlyGenMatrix));
    	
    	System.out.println();
    	System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
    	System.out.println();
    	
    	Scanner in = new Scanner(System.in); //prompting user to enter the x,y coordinates of the start and end pointsS
        System.out.println("Enter i for A > ");
        int Ai = in.nextInt();
        
        System.out.println("Enter j for A > ");
        int Aj = in.nextInt();
        
        System.out.println("Enter i for B > ");
        int Bi = in.nextInt();
        
        System.out.println("Enter j for B > ");
        int Bj = in.nextInt();
        
        show(randomlyGenMatrix, true, Ai, Aj, Bi, Bj);

        findPath(randomlyGenMatrix, Ai, Aj, Bi, Bj, hVCost, dCost);
        }
        
    }





