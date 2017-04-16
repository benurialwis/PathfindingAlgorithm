
/**
 *
 * @author Benuri Alwis
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {

    Node start;
    Node end;
    Node[][] grid;

    static PriorityQueue<Node> open; //priority queue

    static boolean closed[][];
    
    
    /* set the final  cost for visited nodes */
    static void updateCost(Node current, Node t, double cost){
        if(t == null || closed[t.x][t.y])return;
        double tFCost = t.heuristicCost+cost;
        
        boolean inOpen = open.contains(t);
        if(!inOpen || tFCost<t.fCost){
            t.fCost = tFCost; //setting up the final cost of the cell
            t.parent = current; //setting the parent node
            if(!inOpen)open.add(t);
        }
    }

    /* serach for possible path considering the lowest cost */
    public void tracePath(int Bi, int Bj, double hVCost, double dCost) {
        open.add(start);

        Node current;
        Node t;

        while (open.size() > 0) {
            current = open.poll(); //taking the first element in the queue
            if (current == null) { //breaking out if the start point is a blocked cell
                break;
            }
            closed[current.x][current.y] = true; 

            if (current.equals(grid[Bi][Bj])) { //returning if the start point and the end point is the same
                return;
            }
            //up
            if (current.x - 1 >= 0) {
                t = grid[current.x - 1][current.y];
                updateCost(current, t, current.fCost + hVCost);
                
                //upper left
                if (current.y - 1 >= 0) {
                    t = grid[current.x - 1][current.y - 1];
                    updateCost(current, t, current.fCost + dCost);
                }
                
                //upper right
                if (current.y + 1 < grid[0].length) {
                    t = grid[current.x - 1][current.y + 1];
                    updateCost(current, t, current.fCost + dCost);
                }
            }

            //left
            if (current.y - 1 >= 0) {
                t = grid[current.x][current.y - 1];
                updateCost(current, t, current.fCost + hVCost);
            }

            //right
            if (current.y + 1 < grid[0].length) {
                t = grid[current.x][current.y + 1];
                updateCost(current, t, current.fCost + hVCost);
            }

            //down
            if (current.x + 1 < grid.length) {
                t = grid[current.x + 1][current.y];
                updateCost(current, t, current.fCost + hVCost);

                //down left
                if (current.y - 1 >= 0) {
                    t = grid[current.x + 1][current.y - 1];
                    updateCost(current, t, current.fCost + dCost);
                }

                //down right
                if (current.y + 1 < grid[0].length) {
                    t = grid[current.x + 1][current.y + 1];
                    updateCost(current, t, current.fCost + dCost);
                }
            }
            
        }
    }
    
    /* tracing back the path*/
    public ArrayList<Node> getPath(boolean[][] a, int Ai, int Aj, int Bi, int Bj,double hVCost, double dCost, String method){
        int N = a.length;
        
        grid = new Node[N][N];
        closed = new boolean[N][N];
        
        open = new PriorityQueue<>((Object o1, Object o2) -> {
                Node c1 = (Node)o1;
                Node c2 = (Node)o2;

                return c1.fCost<c2.fCost?-1:
                        c1.fCost>c2.fCost?1:0;
            });
        
        start = new Node(Ai, Aj); //setting the start point
        end = new Node(Bi, Bj); //setting the end point
        
        //setting the heuristic value
        for(int i=0;i<N;++i){
              for(int j=0;j<N;++j){
                  grid[i][j] = new Node(i, j);
                  grid[i][j].heuristicCost = Math.abs(i-Bi)+Math.abs(j-Bj);
              }
           }
        
        //setting the blocked cells
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = new Node(i, j);
                if (a[i][j] == false) {
                    grid[i][j] = null;
                }
            }
        }
        
        
           start.fCost = 0; //initialising the final cost of the starting point as zero
           
           tracePath(Bi, Bj, hVCost,dCost);
           
           ArrayList<Node> path = new ArrayList<>();
           DecimalFormat f = new DecimalFormat("##.00");
           
           if(closed[Bi][Bj]){
               //Trace back the path 
                Node current = grid[Bi][Bj]; //starting from the end point
                path.add(current);
                System.out.print("Cost in " + method + ": " + f.format(current.fCost)); 
                while(current.parent!=null){
                    path.add(current);
                    current = current.parent;
                } 
                System.out.println();
           }else {
               System.out.println("No possible path");
           }
           
           return path;
        
    }
    

}
