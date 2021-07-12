package utility;

import java.util.List;
import java.util.Objects;

public class Solution {
    public String name;
    public List<Integer> tour;
    public long cost;
    public double time;

    public Solution(String name ,List tour, long cost, double time) {
        this.name = name;
        this.tour = tour;
        this.cost = cost;
        this.time = time;
    }
    
    @Override
    public String toString () {
        return name + " " + tour + " " + cost + " " + time;
    }
    @Override
    public boolean equals (Object o){
            Solution s = (Solution)o;
            return this.name.equals(s.name);
        }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
