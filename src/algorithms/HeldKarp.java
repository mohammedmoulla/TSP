package algorithms;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.jgrapht.*;
import utility.Solution;

public class HeldKarp {
    public static Solution solve (Graph graph,int starting_vertex){
        //calculate time
        StopWatch timer = new StopWatch();
        timer.start();
       
        int n = graph.vertexSet().size();
        int v = starting_vertex; //starting vertex 
        
        
        //add all vertices to nodes
        Set <Integer> nodes = new HashSet<>(); //empty set
        for (Object w : graph.vertexSet())
            nodes.add(Integer.parseInt(w.toString()));
        
        //remove the starting vertex
        //nodes = all nodes - starting_vertex
        nodes.remove(v);
        
        //c[i][j] --> distance between node i & j
        int [] [] c = make_distance_matrix(graph);
        
        //D = g(c,S) --> c=vertex , S=subset
        Map <KEY,Long> D = new HashMap<>();
        //P = P(c,S) return the city (parent) which gave min g(c,S)
        Map <KEY,Integer> P = new HashMap<>();
        //S = all the subsets fai , {2} , {3} , {4} , {2,3} , ..... ,{2,3,..,N}
        Set <Set<Integer>> S = generate_all_subsets(nodes);
       
        //initialize with infinity
        for (int cw : nodes){
            for (Set <Integer> subset : S){
                if (subset.contains(cw))
                    continue;
                KEY k = new KEY(cw,subset);
                D.put(k,(long)Integer.MAX_VALUE); //infinity
                P.put(k,-1);
            }
        }
        
        Set <Integer> phi = new HashSet(); //empty set
        
        //step 1
        //initialize with distance from starting to each vertex
        //g(c,phi)
        for (int w : nodes){
            KEY k = new KEY(w,phi);
            D.replace(k,(long)c[w][v]); //update the value
            P.replace(k, v);    //update the parent
        }
        
        //step 2
        //build the solution
        for (int k=1; k<=n-1; k++){
            for (Set <Integer> subset : generate_defined_subsets(S,k)){
                for (int w : remaining_elements(nodes, subset)){
                    KEY key = new KEY (w,subset);
                    //find the min 
                    for (int x : subset){
                        long z = c[w][x] + D.get(new KEY(x,subtract(subset,x)));
                        if (z < D.get(key) || D.get(key)==Integer.MAX_VALUE){
                            D.replace(key,z); //update the value
                            P.replace(key,x); //update the parent
                        }
                    }
                }
            }
        }
        
        //step 3
        //add the starting vertex to D & P
        //last = (1,{2,3,...,N})
        KEY last = new KEY (v,nodes);
        //we seach for minimum value --> opt = infinty 
        D.put(last,(long)Integer.MAX_VALUE);
        P.put(last,-1);
        
        
        //find the optimal solution g(1,{2,3,...,N})
        for (int x : nodes){
            long z = c[v][x] + D.get(new KEY(x,subtract(nodes,x)));
            if (z < D.get(last) || D.get(last)==Integer.MAX_VALUE){
                D.replace(last,z); //update the value
                P.replace(last,x); //update the parent
            }
        }
        
        
        
        //building the tour
        Set <Integer> temp = new HashSet();
        temp.addAll(nodes);
        List<Integer> tour = new LinkedList();
        
        //add the starting_vertex to beginning of the tour
        int parent = v;
        tour.add(parent);
        
        for (int k=1; k<=n; k++){
            parent = P.get(new KEY(parent,temp));
            tour.add(parent);
            temp = subtract(temp, parent);
        }
        
        //get the tour cost
        long optimal_value = D.get(new KEY(v,nodes));
        
        timer.stop();
        double time = (double)timer.getTime(TimeUnit.MICROSECONDS)/1000;
        
        Solution sol = new Solution("Held-Karp",tour,optimal_value,time);
        return sol;
    }
    static int [] set_to_array (Set<Integer> S){
        Object [] T = S.toArray() ;
        int [] mat = new int [S.size()];
        for (int i=0;i<S.size(); i++)
            mat[i]=Integer.parseInt((T[i].toString()));
        return mat;
    }
    
    static Set <Set<Integer>> generate_defined_subsets (Set<Set<Integer>> S,int k){
        Set <Set<Integer>> result = new HashSet();
        for (Set<Integer> subset : S){
            if (subset.size() == k)
                result.add(subset);
        }
        return result;
    }
    
    static Set <Set<Integer>> generate_all_subsets (Set<Integer> S){
        int n = S.size();
        Set <Set<Integer>> result = new HashSet<> () ;
        int [] mat = set_to_array(S);
        Set<Integer> temp;
        for (long i=0; i<(1<<n); i++){
             temp= new HashSet<>(); //initialize with empty
            for (int j=0; j<n; j++){
                if ((i & (1<<j) ) > 0)
                    temp.add(mat[j]);
            }
            result.add(temp);
        }
        return result;
    }
    
    public static Set<Integer> remaining_elements (Set<Integer> nodes , Set<Integer> subset){
        Set <Integer> result = new HashSet();
        for (int w : nodes)
            if (! subset.contains(w))
                result.add(w);
        return result;
    }
    
    public static Set<Integer> subtract (Set <Integer> origin , int element){
        Set<Integer> result = new HashSet();
        result.addAll(origin);
        result.remove(element);
        return result;
    }
    
    public static int [] []  make_distance_matrix (Graph graph) {
        int n = graph.vertexSet().size();
        //we need to start from index 1 because nodes in graph start with 1
        int [] [] distance = new int [n+1] [n+1];
        for (int i=1 ; i<=n; i++){
            for (int j=1; j<=n; j++){
                if (i==j)
                    distance[i][j] =0;
                else 
                    distance[i][j] = (int)graph.getEdgeWeight(graph.getEdge(i,j));
            }
        }
        return distance;
    }
    
    static private class KEY {
        int vertex ;
        Set<Integer> set;
        KEY (int vertex , Set<Integer> set){
            this.vertex = vertex ;
            this.set = set;
        }
        @Override
        public boolean equals (Object o){
            KEY k = (KEY)o;
            return ((vertex == k.vertex) && (set.equals(k.set)));
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 41 * hash + this.vertex;
            hash = 41 * hash + Objects.hashCode(this.set);
            return hash;
        }
        public String toString(){
            return "{"+vertex+","+set+"}";
        }
    }
}