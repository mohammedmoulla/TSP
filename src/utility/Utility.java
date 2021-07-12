package utility;

import drawings.Draw;
import interfaces.InputGraph;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Utility {
  
    public static boolean test_complete (Graph <Integer,DefaultWeightedEdge> graph) {
        int n = graph.vertexSet().size();
         if (!graph.vertexSet().stream().noneMatch((node) -> (graph.inDegreeOf(node) != n-1)))
             return false;
        return true;
    }
    
    public static boolean good_order (Graph <Integer,DefaultWeightedEdge> graph){
        int n = graph.vertexSet().size();
        Set <Integer> nodes = graph.vertexSet();
        Set <Integer> correct_order = new HashSet();
        for (int i=1; i<=n; i++)
            correct_order.add(i);
         return nodes.equals(correct_order);
    }
    
    public static void complete_graph () {
        int n = Draw.n;
        Random rnd = new Random();
        for (int i : Draw.graph.vertexSet())
            for (int j : Draw.graph.vertexSet()) {
                if(i==j)
                    continue;
                
                //if edge exists
                if (Draw.graph.containsEdge(i,j))
                    continue;
                
                Draw.graph.addEdge(i, j);
                Draw.graph.setEdgeWeight(i, j, rnd.nextInt(200)+1);
                InputGraph.ref.repaint();
            }
    }
    
    public static int get_starting_vertex (Set <Integer> nodes){
                int starting_vertex =-1;
                while (! nodes.contains(starting_vertex)){
                    String s = JOptionPane.showInputDialog(null,"insert starting vertex");
                    if (s == null)
                        return -1;
                    try { 
                        starting_vertex = Integer.parseInt(s);
                        if (! nodes.contains(starting_vertex))
                            throw new Exception();
                    } catch (Exception ee){
                        JOptionPane.showMessageDialog(null, "insert correct vertex", "oohps !!!", JOptionPane.ERROR_MESSAGE);
                    }
                }//end of while 
                
              return starting_vertex;  
    }//end of function
    
    public static Graph <Integer,DefaultWeightedEdge> generate_completed_graph (Graph <Integer,DefaultWeightedEdge> graph){
        
        Graph <Integer,DefaultWeightedEdge> completed = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (int v : graph.vertexSet())
            completed.addVertex(v);
        for (DefaultWeightedEdge e : graph.edgeSet()){   
            int u = graph.getEdgeSource(e);
            int v = graph.getEdgeTarget(e);
            double w = graph.getEdgeWeight(e);
            completed.addEdge(u,v );
            completed.setEdgeWeight(u,v,w);
        }
        int n = graph.vertexSet().size();
        
        Random rnd = new Random();
        for (int i : graph.vertexSet())
            for (int j : graph.vertexSet()) {
                if(i==j)
                    continue;
                
                //if edge exists
                if (completed.containsEdge(i,j))
                    continue;
                
                completed.addEdge(i, j);
                completed.setEdgeWeight(i, j, Integer.MAX_VALUE);
                
            }
        return completed;
    }
}