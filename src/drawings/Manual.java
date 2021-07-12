package drawings;

import interfaces.InputGraph;
import java.awt.*;
import javax.swing.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.util.*;

public class Manual extends JPanel {
    public static Graph <Integer,DefaultWeightedEdge> graph ;
    public static int n ;
    public static JPanel ref ;
    public Manual (int number) {
        setSize(900, 700);
        ref = this;
        n = number;
        graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (int i=1; i<=n; i++)
            graph.addVertex(i);
        repaint();
    }
    public static void generate_edges() {
        for (int i=1 ; i<=n; i++){
            for (int j = i+1; j<=n; j++){
                String s = JOptionPane.showInputDialog("Insert edge weight between node("+i+") and node ("+j+")");
                //cancel option
                if (s == null){
                    int selection = JOptionPane.showConfirmDialog(null, "Are you finish inserting edges ?","Oohps !!!",JOptionPane.YES_NO_OPTION);
                    if (selection == JOptionPane.YES_OPTION){
                        return;
                    } else {
                        j--; 
                        continue;
                    }
                }
                //OK option
                try {
                int x = Integer.parseInt(s); //maybe character 'A'
                if (x<=0) //maybe negative weight
                    throw new Exception();
                graph.addEdge(i, j);
                graph.setEdgeWeight(i, j, x);
                ref.repaint();
                InputGraph.ref.repaint();
                } catch (Exception e){
                    //ok with nothing
                    if (s.equals("")){
                        JOptionPane.showMessageDialog(null, "No value --> no edge between ("+i+","+j+")");
                    }
                    else  {
                        JOptionPane.showMessageDialog(null,"Edge weight is not correct","Oohps !!!",JOptionPane.ERROR_MESSAGE);
                        j--; 
                    }
                }
            }// end of for (j)
    }// end of for (i)
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
            
            int angles = 360/n;
            Map <Integer,String> m = new HashMap<>();
            
            //generate vertices
            for (int node : graph.vertexSet()){
                int x =(int) (a + Math.cos(Math.toRadians(theta))*radius);
                int y =(int) (b + Math.sin(Math.toRadians(theta))*radius);
                m.put(node,x+","+y);
                theta+=angles;
            }//end of genrating vertices
            
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
             
            //drawing edges
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
                g.setColor(Color.ORANGE);
                g.fillOval(X+2, Y+2, 26, 26);
                g.setColor(Color.RED);
                g.drawString(""+node, X, Y);
            }//end of drawing vertices
        }
    }