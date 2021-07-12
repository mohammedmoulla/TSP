package drawings;

import java.awt.*;
import javax.swing.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.util.*;

public class Randomize extends JPanel {
    public static Graph <Integer,DefaultWeightedEdge> graph ;
    public static int n = 0;
    
    public Randomize (int number) {
        n = number;
        graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (int i=1; i<=n; i++)
            graph.addVertex(i);
        Random rnd = new Random();
        for (int i=1; i<n; i++)
            for (int j = i+1; j<=n; j++) {
                graph.addEdge(i, j);
                graph.setEdgeWeight(i, j, rnd.nextInt(200)+1);
            }
        setSize(900, 700);
        repaint();
    }
    
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            setBackground(Color.WHITE);
            
            if (n == 0)
                return ;
            
            int a = 450;
            int b = 350;
            int radius = 300;
            double theta = 0;
            
            int angles = 360/n; //increase of the theta
            Map <Integer,String> m = new HashMap<>();
            
            //generate vertices locations
            for (int node : graph.vertexSet()){
                int x =(int) (a + Math.cos(Math.toRadians(theta))*radius);
                int y =(int) (b + Math.sin(Math.toRadians(theta))*radius);
                m.put(node,x+","+y);
                theta+=angles;
            }//end of genrating vertices
            
            //draw edges
            g.setColor(Color.BLACK);
            for (DefaultWeightedEdge e : graph.edgeSet()){
                int source = graph.getEdgeSource(e);
                int target = graph.getEdgeTarget(e);
                String source_location = m.get(source);
                String target_location = m.get(target);
                int X1 = Integer.parseInt(source_location.split(",")[0]);
                int Y1 = Integer.parseInt(source_location.split(",")[1]);
                int X2 = Integer.parseInt(target_location.split(",")[0]);
                int Y2 = Integer.parseInt(target_location.split(",")[1]);
                int weight = (int)graph.getEdgeWeight(e);
                g.drawLine(X1 , Y1 , X2 , Y2);
                g.drawString(""+weight, (X1+X2)/2, (Y1+Y2)/2);
            }//end of drawing edges 
            
            //drawing vertices
            for (int node : graph.vertexSet()){
                String node_location = m.get(node);
                int X = Integer.parseInt(node_location.split(",")[0])-15;
                int Y = Integer.parseInt(node_location.split(",")[1])-15;
                g.setColor(Color.BLACK);
                g.fillOval(X, Y, 30, 30);
                g.setColor(Color.CYAN);
                g.fillOval(X+2, Y+2, 26, 26);
                g.setColor(Color.RED);
                g.drawString(""+node, X, Y);
            }//end of drawing vertices 
        }
    }