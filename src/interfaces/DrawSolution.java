package interfaces;

import java.awt.*;
import javax.swing.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.util.*;
import java.awt.event.*;
import java.util.List;
import static java.lang.Math.*;
import utility.Solution;

public class DrawSolution extends JFrame{
    public static JFrame ref = null;
    public static JDialog J = null;
    public static Details D = null;
    public static JButton B1,B2,B3;
    private Graph <Integer,DefaultWeightedEdge> graph ;
    static Set <Solution> solution_set;
    String solution_name;
    List <Integer> tour;
    int [] tour_array;
    String tour_text;
    double time;
    long tour_cost ;
    JPan JP;
    public int n = 0;
    
    public DrawSolution (String s , Graph g , Solution sol) {
        setTitle(s);
        setSize(900, 720);
        
        graph = g;
        solution_name = sol.name;
        tour = sol.tour;
        tour_cost = sol.cost;
        time = sol.time;
        
        if (J == null)
            solution_set = new HashSet();
        
        n = graph.vertexSet().size();
        ref = this;
        tour_array = new int [tour.size()];
        int i = 0;
        for (int x : tour)
            tour_array[i++] = x;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        Container cp = getContentPane();
        JP = new JPan();
        JP.setSize(900, 720);
        cp.add(JP);
        setVisible(false);
        show_details(sol);
        Point p = J.getLocation();
        setLocation(p.x+410,p.y);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e){
                graph = null;
                n = 0;
                solution_set.remove(sol);
                if (solution_set.isEmpty() && J != null){
                    J.dispose();
                    J = null;
                    return ;
                }
                J.repaint();
            }
        });
    }
    private class JPan extends JPanel {
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            setBackground(Color.WHITE);
            
            g.setColor(Color.red);
            g.setFont(new Font("MonoSpaced",Font.BOLD,24));
            g.drawString(solution_name+" Solution",10,20);
            
            if (n == 0)
                return ;
            
            int a = 450;
            int b = 350;
            int radius = 300;
            double theta = 0;

            int angles = 360 / n;
            Map<Integer, String> m = new HashMap<>();
            
            //generate vertices
            for (int node : graph.vertexSet()){
                int x =(int) (a + Math.cos(Math.toRadians(theta))*radius);
                int y =(int) (b + Math.sin(Math.toRadians(theta))*radius);
                m.put(node,x+","+y);
                theta+=angles;
            }//end of genrating vertices
            
            Graphics2D g2 = (Graphics2D) g;
            
            //drawing tour (edge --> directed edge)
            for (int i=0; i<tour_array.length-1; i++){
                int source = tour_array[i];
                int target = tour_array[i+1];
                String source_location = m.get(source);
                String target_location = m.get(target);
                int X1 = Integer.parseInt(source_location.split(",")[0]);
                int Y1 = Integer.parseInt(source_location.split(",")[1]);
                int X2 = Integer.parseInt(target_location.split(",")[0]);
                int Y2 = Integer.parseInt(target_location.split(",")[1]);
                int weight = (int)graph.getEdgeWeight(graph.getEdge(source, target));
                g2.setStroke(new BasicStroke(2));
                g.setColor(Color.CYAN);
                g.drawLine(X1 , Y1 , X2 , Y2);
                g.setColor(Color.BLACK);
                
                //put the weight randomly on the edge
                int Tx = (X1+X2)/2;
                int Ty = (Y1+Y2)/2;
                Random offset = new Random ();
                Ty += offset.nextInt(25);
                Tx += offset.nextInt(25);
                g.drawString(""+weight,Tx,Ty); 
                
                //drawing the arrow
                double M = (double)(Y2-Y1)/(X2-X1);
                double angle = atan(M);
                if (X2>X1 && Y2<Y1){
                    X2 =(int) (X2 - 15*cos(-angle));
                    Y2 =(int) (Y2 + 15*sin(-angle));
                } else if (X2<X1 && Y2<Y1){
                    X2 =(int) (X2 + 15*cos(angle));
                    Y2 =(int) (Y2 + 15*sin(angle));
                } else if (X2<X1 && Y2>Y1){
                    X2 =(int) (X2 + 15*cos(-angle));
                    Y2 =(int) (Y2 - 15*sin(-angle));
                } else if (X2>X1 && Y2>Y1) {
                    X2 =(int) (X2 - 15*cos(angle));
                    Y2 =(int) (Y2 - 15*sin(angle));
                } else if (X2>X1 && Y2 == Y1){
                    X2 =(int) (X2 - 15);
                    Y2 =(int) (Y2);
                } else if (X2 == X1 && Y2<Y1){
                    X2 =(int) (X2);
                    Y2 =(int) (Y2+15);
                } else if (X2<X1 && Y2 ==Y1){
                    X2 =(int) (X2+15);
                    Y2 =(int) (Y2);
                } else if (X2 == X1 && Y2>Y1){
                    X2 =(int) (X2);
                    Y2 =(int) (Y2-15);
                }
                int height = 30;
                int width = 8;
                int dx = X2 - X1;
                int dy = Y2 - Y1;
                double D = Math.sqrt(dx*dx + dy*dy);
                double xm = D - height, xn = xm, ym = width, yn = -width;
                double sin = dy / D, cos = dx / D;
                double t;
                t = xm*cos - ym*sin + X1;
                ym = xm*sin + ym*cos + Y1;
                xm = t;
                t = xn*cos - yn*sin + X1;
                yn = xn*sin + yn*cos + Y1;
                xn = t;
                g.setColor(Color.BLUE);
                g.fillPolygon(new int [] {X2, (int) xm, (int) xn},new int [] {Y2, (int) ym, (int) yn}, 3);
            }
            
            //drawing vertices
            for (int node : graph.vertexSet()){
                String node_location = m.get(node);
                int X = Integer.parseInt(node_location.split(",")[0])-15;
                int Y = Integer.parseInt(node_location.split(",")[1])-15;
                g.setColor(Color.BLACK);
                g.fillOval(X, Y, 30, 30);
                g.setColor(Color.MAGENTA);
                g.fillOval(X+2, Y+2, 26, 26);
                g.setColor(Color.RED);
                g.drawString(""+node, X, Y);
            }//end of drawing vertices
            
        }
    }
    
    public  void show_details(Solution sol){
        
        if (J == null || (J != null && ! J.isVisible())){
            J = new JDialog();
            J.setTitle("Solution Details");
            J.setSize(400,340);
            J.setResizable(false);
            J.setAlwaysOnTop(true);
            J.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            B1 = new JButton("View/Hide");
            B1.setBounds(260, 60, 100, 25);
            B1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            B1.setVisible(false);
            B1.addActionListener((ActionEvent e) -> {
                InputGraph.HH.setVisible(!InputGraph.HH.isVisible()); 
                InputGraph.HH.repaint();
                if (InputGraph.AA != null)
                    InputGraph.AA.setVisible(false); 
                if (InputGraph.NN != null)
                    InputGraph.NN.setVisible(false); 
            });
            
            B2 = new JButton("View/Hide");
            B2.setBounds(260, 150, 100, 25);
            B2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            B2.setVisible(false);
            B2.addActionListener((ActionEvent e) -> {
                InputGraph.AA.setVisible(!InputGraph.AA.isVisible());
                InputGraph.AA.repaint();
                if (InputGraph.HH != null)
                    InputGraph.HH.setVisible(false); 
                if (InputGraph.NN != null)
                    InputGraph.NN.setVisible(false); 
            });
            
            B3 = new JButton("View/Hide");
            B3.setBounds(260, 240, 100, 25);
            B3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            B3.setVisible(false);
            B3.addActionListener((ActionEvent e) -> {
                InputGraph.NN.setVisible(!InputGraph.NN.isVisible());
                InputGraph.NN.repaint();
                if (InputGraph.HH != null)
                    InputGraph.HH.setVisible(false); 
                if (InputGraph.AA != null)
                    InputGraph.AA.setVisible(false); 
            });
            
            
            D = new Details ();
            D.setPreferredSize(new Dimension(360,270));
            D.setBorder(BorderFactory.createLineBorder(Color.BLACK,4));
            D.setLayout(null);
            
            D.add(B1);
            D.add(B2);
            D.add(B3);
            
            JScrollPane JS = new JScrollPane(D);
            JS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            JS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            J.setContentPane(JS);
            
            J.setLocation(InputGraph.ref.getLocation());
            J.setVisible(true);
        }
        //if we solve the problem with the same algorithm
        if (solution_set.contains(sol))
            solution_set.remove(sol);
        solution_set.add(sol);
        J.repaint();
        J.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e){
                clean();
                J = null;
            }
        });
    }
   public static void clean() {
       solution_set = new HashSet<>();
                if (InputGraph.HH != null){
                    InputGraph.HH.dispose();
                    InputGraph.HH = null;
                }
                if (InputGraph.AA != null){
                    InputGraph.AA.dispose();
                    InputGraph.AA = null;
                }
                if (InputGraph.NN != null){
                    InputGraph.NN.dispose();
                    InputGraph.NN = null;
                }
   }
    
    public static class Details extends JPanel {
        @Override
        public void paintComponent (Graphics g){
            super.paintComponent(g);
            super.setBackground(Color.WHITE);
            
            int y = 0;
            boolean t1=false,t2=false,t3=false;
            
            for (Solution sol : solution_set){
                if (sol.name.equals("Held-Karp")){
                   y=0;
                   t1 = true;
                } else if (sol.name.equals("Ant-Colony")){
                    y = 90;
                    t2 = true;
                } else if (sol.name.equals("Nearest-Neighbour")){
                    y = 180;
                    t3 = true;
                }
                String tour_text = "tour : ";
                for (int city : sol.tour)
                    tour_text += city+"-";
                tour_text = tour_text.substring(0,tour_text.length()-1);
                int len = tour_text.length();
                if (len > 30)
                    D.setPreferredSize(new Dimension(380+(len-30)*10,270));
                g.setColor(Color.red);
                g.setFont(new Font("MonoSpaced",Font.BOLD,24));
                g.drawString(sol.name+" Solution",10,20+y);
                g.setColor(Color.BLACK);
                g.setFont(new Font("MonoSpaced",Font.BOLD,18));
                g.drawString(tour_text,10,40+y);
                g.drawString("Tour cost = "+sol.cost,10,60+y);
                g.drawString("Time : "+sol.time+" ms",10,80+y);
            }//end of for (sol)
            
            //drawing lines
            g.setColor(Color.red);
            int width = D.getWidth();
            g.drawLine(0, 90, width, 90);
            g.drawLine(0, 180, width, 180);
            
            B1.setVisible(t1);
            B2.setVisible(t2);
            B3.setVisible(t3);
        }
    }
}