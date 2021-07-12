package algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.jgrapht.Graph;
import utility.Solution;

public class NearestNeighbour {
    public static Solution solve (Graph graph , int starting_vertex){
        StopWatch timer = new StopWatch();
        timer.start();
        
        int n = graph.vertexSet().size();
        int v = starting_vertex; //starting vertex 
        
        //nodes start from index 1
        int [] distance = new int [n+1];
        for (int i=1;i<=n; i++)
            distance[i] = Integer.MAX_VALUE;
        
        Set <Integer> nodes = new HashSet();
        for (Object w : graph.vertexSet())
            nodes.add(Integer.parseInt(w.toString()));
        nodes.remove(v);//set without starting vertex

        //building solution
        List<Integer> tour = new LinkedList();
        tour.add(v);
        //we are in the node 1 (starting_vertex)
        int k = 1;
        //min = node which give us the min value(distance)
        int min = -1;
        
        for (int i=1; i<n; i++){
            for (int w : nodes){
                int z = (int) graph.getEdgeWeight(graph.getEdge(v,w));
                if (z<distance[k]|| distance[k]==Integer.MAX_VALUE){
                    distance[k] = z;
                    min = w;
                }
            }
            tour.add(min);
            nodes.remove(min);
            v = min;
            k++;
        }
        
        tour.add(starting_vertex);
        distance[k] = (int) graph.getEdgeWeight(graph.getEdge(v,starting_vertex));
        
        //calculate cost of the tour
        long sum = 0;
        for (int x : distance)
            sum+=x;
        
        long tour_cost = sum;
        
        timer.stop();
        double time = (double)timer.getTime(TimeUnit.MICROSECONDS)/1000;
        
        Solution sol = new Solution("Nearest-Neighbour",tour,tour_cost,time);
        return sol;
    }
}