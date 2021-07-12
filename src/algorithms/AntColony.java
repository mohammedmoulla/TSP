package algorithms;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.jgrapht.Graph;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import utility.Solution;

public class AntColony {
    //⍺ β ⍴ ∈  α τ  ρ 
    static int[] ant_location; //where is the ant[i]
    static int[][] c;           //distance (edge weights)
    static double[][] tao;      //t(i,j) total for e(i,j)
    static double[][] delta_tao;//dτ(i,j) total for e(i,j) 
    static double[][] eta;      //eta(i,j)
    static double[][][] d_tao;  //[k][i][j] --> for ant k
    static double[][][] p;      //[k][i][j] --> for ant k
    static Set<Integer> nodes;
    static List<Integer> [] visited;
    static Set<Integer> [] allowed;
    static long [] tour_cost;   //tour cost for each ant cost[i] = cost of all the tour for ant i
    static long shortest_tour_cost;
    static List<Integer> shortest_tour;
    static Graph graph;
    static int n,L,rounds,pheromone;
    static double alpha,beta,rho,Q;
    
    public static Solution solve (Graph g , int ants , int rounds_ ,
                                  double alpha_ , double beta_ , double rho_ , double Q_ , int pheromone_ ){
        StopWatch timer = new StopWatch();
        timer.start();
        
        if (ants == 0 || rounds_ ==0)
            return null;
        
        graph = g;
        n = graph.vertexSet().size()+1;     //V = {1,2,3,.....,n}
        L = ants+1;                         //ants = {1,2,3,....,L}
        rounds = rounds_;
        alpha = alpha_;
        beta = beta_;
        rho = rho_;
        Q = Q_;
        pheromone = pheromone_;
        
        initialize();
        
        //repeat the steps to optimize the solution
        for (int r=1; r<=rounds; r++){
            
            //starting the journy
            Random rnd = new Random();
            for (int k=1; k<L; k++){
                //n-1 --> last city index is n-1
                //+1 --> to avoid city with zero index
                //+1 --> because n-1 exclusive
                int selected_city = rnd.nextInt(n-1)+1;
                ant_location[k] = selected_city;
                visited[k].add(selected_city);
                allowed[k].remove(selected_city);
            }
            //step 2
            //visiting all vertices
            //we visited first city --> (n-1) - 1 = n-2
            for (int s=1; s<n-1; s++){
                for (int k=1; k<L; k++){
                    //roulette wheel
                    //if (allowed[k].isEmpty())
                        //continue;
                    int allowed_size = allowed[k].size()+1;
                    City [] city = new City[allowed_size];
                    city[0] = new City(0,-0.001,-0.001);
                    int ite = 1;
                    for (int allowed_city : allowed[k]){
                        //prob(i,jjj,k)
                        double prob = probabilty(ant_location[k],allowed_city, k);
                        city[ite] = new City(allowed_city,prob,prob);
                        ite++;
                    }

                    Arrays.sort(city,1,allowed_size,Collections.reverseOrder()); //decsending order
                    
                    //calculate the cumulative sum
                    for (int i=1; i<allowed_size-1;i++)
                        for(int j=i+1; j<allowed_size;j++)
                            city[i].cumulative += city[j].prob;
                    
                    Arrays.sort(city); //ascending order
                    
                    //select the next city randomly according to probability (cumulative)
                    int selected_city = -1;
                    double rand = rnd.nextDouble();
                    for (int i=1; i<allowed_size; i++)
                        if (city[i-1].cumulative < rand && rand <= city[i].cumulative){
                            selected_city = city[i].city;
                            break;
                        }
                    
                    //move the ant(k) to the selected city
                    ant_location[k] = selected_city;
                    visited[k].add(selected_city);
                    allowed[k].remove(selected_city);
                    
                } //end of for loop (k)
                
            }//end of for loop (s)
            
            //step 3
            //calculate the length of the journy for each ant
            for (int k=1; k<L; k++){
                //move the ant(k) from visited[k](n) to visited[k](1) to complete the tour
                int starting_city = visited[k].get(0);
                visited[k].add(starting_city);
                
                tour_cost[k] = compute_tour_cost(visited[k]);
                //update the minimum tour
                if (tour_cost[k] < shortest_tour_cost){
                    shortest_tour.clear();
                    shortest_tour.addAll(visited[k]);
                    shortest_tour_cost = tour_cost[k];
                }
                
            }//end of for loop (k)
            
            //step 4
            //update the pheromone
            
            //Q is constant 
            //double Q = 1;
            
            //rho is constant where rho = evaporation rate
            //double rho = 0.6;
            
            //for each edge e = (i,j)
            for (int i=1; i<n; i++){
                for (int j=1; j<n; j++){
                    
                    //calculate the increase of phermone
                    for (int k=1; k<L; k++){
                        //if ant walks on the edge --> increase the phermone
                        if (ant_travels_on_edge(k,i,j)){
                            d_tao[k][i][j] = Q/tour_cost[k];
                        } else {
                            d_tao[k][i][j] = 0;
                        }
                    }
                    //calculate the total increase of phermone on the edge (i,j)
                    for (int k=1; k<L; k++){
                        delta_tao[i][j] += d_tao[k][i][j];
                    }
                    
                    tao[i][j] = rho*tao[i][j] + delta_tao[i][j];
                }
            }
            //step 5
            //refresh the memory of ants and restart the search
            empty_visited();
            
        }//end of for loop (r)
        
        timer.stop();
        double time = (double)timer.getTime(TimeUnit.MICROSECONDS)/1000;
        
        Solution sol = new Solution("Ant-Colony",shortest_tour,shortest_tour_cost,time);
        return sol;
    }//end of solve method

    public static void initialize () {
        
        //allocation for the arrays
        ant_location = new int [L];
        c = make_distance_matrix(graph);
        tao = new double [n][n];
        delta_tao = new double [n][n];
        eta = new double [n][n];
        
        d_tao = new double [L][n][n];
        p = new double [L][n][n];
        
        nodes = new HashSet();
        visited = new LinkedList [L];   //array of LinkedList's
        allowed = new HashSet [L];      //array of HashSet's
        
        tour_cost = new long [L];
        shortest_tour_cost = Integer.MAX_VALUE; //value of opt solution
        shortest_tour = new LinkedList();       //opt solution itself
        
        for (Object x : graph.vertexSet())
            nodes.add(Integer.parseInt(x.toString()));
        
        //visited array for each ant
        for (int k=1; k<L; k++){
            visited[k] = new LinkedList();  //empty list
            allowed[k] = new HashSet();     //empty set 
            allowed[k].addAll(nodes); //all nodes in the graph
        }
        
        //initialize
        for (int i=1; i<n; i++){
            for (int j=1; j<n; j++){
                tao[i][j]=pheromone; //pheromone value
                delta_tao[i][j]=0; //increase of pheromone value
                eta[i][j]=(i==j?Double.POSITIVE_INFINITY:((double)1/c[i][j])); //visibility of city j from city i
            }
        }
    }
    //p(i,j,k)
    public static double probabilty (int i,int j,int k){
        if (allowed[k].contains(j)){
            double sum = 0;
            //baset
            double d = pow(tao[i][j],alpha)*pow(eta[i][j],beta);
            //makam
            for (int s : allowed[k])
                sum += pow(tao[i][s],alpha)*pow(eta[i][s],beta);
            return d/sum;
        } else {
            return 0;
        }
    }
    
    public static long compute_tour_cost (List <Integer> tour){
        long sum = 0 ;
        for (int i=0; i<tour.size()-1; i++)
            sum += c[tour.get(i)][tour.get(i+1)];
        return sum;
    }
    
    public static boolean ant_travels_on_edge(int k , int i , int j){
        boolean t = false;
        for (int index=0; index<visited[k].size()-1; index++){
            if(i == visited[k].get(index) && j == visited[k].get(index+1)){
                t = true;
                break;
            }
        }
        return t;
    }
    
    public static void empty_visited () {
        for (int k=1; k<L; k++){
            visited[k] = new LinkedList(); //empty set
            allowed[k] = new HashSet();
            allowed[k].addAll(nodes); //all nodes in the graph
        }
    }
    
    public static int [] []  make_distance_matrix (Graph graph) {
        int n = graph.vertexSet().size();
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
    
    static private class City implements Comparable<City>{
        int city;
        double prob;
        double cumulative;
        City (int city,double prob,double cumulative){
            this.city = city;
            this.prob = prob;
            this.cumulative = cumulative;
        }

        @Override
        public int compareTo(City o) {
            if (this.cumulative > o.cumulative)
                return 1;
            else if (this.cumulative == o.cumulative)
                return 0;
            else 
                return -1;
        }
        
    }
}