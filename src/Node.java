
/**
 *
 * @author Benuri Alwis
 */
public class Node {

    double heuristicCost = 0; //heuristic cost
    double fCost = 0; //final cost 
    int x, y;
    Node parent;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
