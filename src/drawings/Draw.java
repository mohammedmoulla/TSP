package drawings;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.util.*;

public class Draw extends JPanel {
        int number = 1;
        int x,y;
        int x1 , y1;
        int x2 , y2;
        int line_x1,line_y1,line_x2,line_y2;
        boolean draw;
        
        public static  Graph <Integer,DefaultWeightedEdge> graph ;
        Map <Integer,String> vertices_locations ;
        public static int n;
        
        public Draw() {
            n = 0;
            setSize(900, 700);
            graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
            vertices_locations = new HashMap<>();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e){
                    line_x1 = 0; line_y1 = 0;
                    line_x2 = 0; line_y2 = 0;
                    if (e.getButton() == MouseEvent.BUTTON1){
                        touch_node(e);
                    }else if (e.getButton() == MouseEvent.BUTTON3){
                        delete_node(e);
                    } 
                }
                @Override
                public void mouseReleased(MouseEvent ee){
                    if (! draw)
                        return;
                    //released while you drawing
                    x2 = ee.getX();
                    y2 = ee.getY();
                    int node1 = get_node_number(x1, y1);
                    int node2 = get_node_number(x2, y2);
                    if (node1 != node2 && node1 !=-1 && node2 != -1){
                        make_edge(node1,node2);   
                        draw = false;
                    } else {
                        draw = false;
                        repaint();
                    }
                }
            });
            addMouseMotionListener(new MouseMotionAdapter(){ 
                @Override
                public void mouseDragged (MouseEvent e){
                    if (draw){
                        line_x2 = e.getX();
                        line_y2 = e.getY();
                        repaint();
                    }
                }
            });
        }
        String in_node (int x,int y){
            for (int node: graph.vertexSet()){
                String location = vertices_locations.get(node);
                int X = Integer.parseInt(location.split(",")[0]);
                int Y = Integer.parseInt(location.split(",")[1]);
                X+=18; Y+=18;
                if (x-22<=X && X<=x+22 && y-22<=Y && Y<=y+22)
                    return "IN";
                if (x-70<=X && X<=x+70 && y-70<=Y && Y<=y+70)
                    return "BESIDE";
            }
            return "OUT";
        }
        int get_node_number(int x,int y){
            for (int node: graph.vertexSet()){
                String location = vertices_locations.get(node);
                int X = Integer.parseInt(location.split(",")[0]);
                int Y = Integer.parseInt(location.split(",")[1]);
                X+=18; Y+=18;
                if (x-22<=X && X<=x+22 && y-22<=Y && Y<=y+22)
                    return node;
            }
            return -1;
        }
        void make_node (int x , int y) {
            for (int i=1 ; i<200; i++)
                if (!graph.containsVertex(i)){
                    number = i;
                    break;
                }
            graph.addVertex(number);
            vertices_locations.put(number,x+","+y);
            repaint();
        }
        void touch_node (MouseEvent e){
            x = e.getX();
            y = e.getY();
            String click_location = in_node(x, y);
            switch (click_location) {
                case "BESIDE":
                    draw = false;
                    break;
                case "OUT":
                    draw = false;
                    x -= 18;
                    y -= 18;
                    make_node(x, y);
                    break;
                case "IN":
                    draw = true;
                    int current_node = get_node_number(x, y);
                    String starting_location = vertices_locations.get(current_node);
                    line_x1 = Integer.parseInt(starting_location.split(",")[0]) + 18;
                    line_y1 = Integer.parseInt(starting_location.split(",")[1]) + 18;
                    x1 = x;
                    y1 = y;
                    break;
            }
        }
        void delete_node (MouseEvent e){
            x = e.getX(); 
            y = e.getY(); 
            String click_location = in_node(x,y);
            if (click_location.equals("IN")) {
                int node = get_node_number(x, y);
                graph.removeVertex(node);
                vertices_locations.remove(node);
                repaint();
            }
        }
       
        void make_edge(int node1 ,int node2){
            if (! graph.containsEdge(node1, node2)){
                try {
                int weight = Integer.parseInt(JOptionPane.showInputDialog("Insert weight"));
                if (weight <=0)
                    throw new Exception();
                graph.addEdge(node1, node2);
                graph.setEdgeWeight(node1, node2, weight);
                repaint();
                } catch (Exception e){ 
                    JOptionPane.showMessageDialog(null,"Edge weight is not correct","Oohps !!!",JOptionPane.ERROR_MESSAGE);
                    repaint();
                }
            } else {
                  JOptionPane.showMessageDialog(null,"Edge exist");
                  repaint();
            }
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            setBackground(Color.WHITE);
            
            g.setFont(new Font("MonoSpaced",Font.BOLD,18));            
            g.drawString("Left click --> Make new node",10,20);
            g.drawString("Right click --> Delete node",10,40);
            g.drawString("Drag mouse --> Make new edge",10,60);
            
            n = graph.vertexSet().size();
            g.setFont(new Font("MonoSpaced",Font.BOLD,20));
            
            //draw the edge while dragging the mouse
            g.setColor(Color.BLUE);
            if (draw) 
                g.drawLine(line_x1, line_y1, line_x2, line_y2);
            
            
            //drawing edges
            for (DefaultWeightedEdge e : graph.edgeSet()){
                int source = graph.getEdgeSource(e);
                int target = graph.getEdgeTarget(e);
                int weight = (int) graph.getEdgeWeight(e);
                
                String source_location = vertices_locations.get(source);
                String target_location = vertices_locations.get(target);
                
                int X1 = Integer.parseInt(source_location.split(",")[0])+18;
                int Y1 = Integer.parseInt(source_location.split(",")[1])+18;
                int X2 = Integer.parseInt(target_location.split(",")[0])+18;
                int Y2 = Integer.parseInt(target_location.split(",")[1])+18;
                
                g.setColor(Color.BLACK);
                g.drawLine(X1, Y1, X2, Y2);
                g.setColor(Color.BLUE);
                g.drawString(""+weight, (X1+X2)/2, (Y1+Y2)/2);
            }//end of drawing edges
            
            //draw vertices
            for (int node : graph.vertexSet()){
                String location = vertices_locations.get(node);
                int X = Integer.parseInt(location.split(",")[0]);
                int Y = Integer.parseInt(location.split(",")[1]);
                g.setColor(Color.BLACK);
                g.fillOval(X, Y, 36, 36);
                g.setColor(Color.LIGHT_GRAY);
                g.fillOval(X+2, Y+2, 32, 32);
                g.setColor(Color.RED);
                g.drawString(""+node,X, Y);
            }// end of drawing vertices
        }
    }
