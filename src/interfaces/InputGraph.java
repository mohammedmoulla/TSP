package interfaces;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jgrapht.Graph;
import algorithms.*;
import drawings.*;
import utility.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import static utility.Utility.*;

public class InputGraph extends JFrame {
    JButton B;
    JPanel JP_Right,JP_Left;
    JLabel L1,L2,L3;
    JRadioButton JR1,JR2,JR3;
    ButtonGroup BG;
    JButton B1,B2,B3,B4,B5;
    JTextField TF;
    public static  Graph <Integer,DefaultWeightedEdge> graph ;
    
    public static JFrame HH,AA,II,NN;
    public static JFrame ref;
    
    public InputGraph (){
        setTitle("Input Graph");
        setSize(1300,720);
        
        setResizable(false);
        setLayout(null);
        ref = this;
        graph = null;
        Container cp = getContentPane();
        cp.setBackground(Color.WHITE);
        //JPanel for input the graph
        JP_Right = new JPanel();
        JP_Right.setBackground(Color.GRAY);
        JP_Right.setSize(900, 700);
        JP_Right.setLocation(400, 0);
        cp.add(JP_Right);
        
        //JPanel for left side
        JP_Left = new JPanel();
        JP_Left.setBackground(Color.CYAN);
        JP_Left.setSize(400, 700);
        JP_Left.setLocation(0,0);
        JP_Left.setLayout(null);
        cp.add(JP_Left);
        
        B = new JButton("<<Back");
        B.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        B.setBounds(20, 10, 100, 25);
        B.setFocusPainted(false);
        B.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JP_Left.add(B);
        B.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ref.setVisible(false);
                Introduction.ref.setVisible(true);
                if (DrawSolution.J != null)
                    DrawSolution.J.setVisible(false);
                if (HH !=null)
                    HH.setVisible(false);
                if (AA !=null)
                    AA.setVisible(false);
                if (NN !=null)
                    NN.setVisible(false);
            }
        });
        
        L1 = new JLabel("Select input method :");
        L1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        L1.setBounds(20,50,220,30);;
        JP_Left.add(L1);
        
        BG = new ButtonGroup();
        JR1 = new JRadioButton("Draw Graph");
        JR2 = new JRadioButton("Insert edge weights Manually");
        JR3 = new JRadioButton("Generate Random Graph");
        JR1.setFocusPainted(false);
        JR2.setFocusPainted(false);
        JR3.setFocusPainted(false);
        JR1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        JR2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        JR3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        JR1.setBackground(JP_Left.getBackground());
        JR2.setBackground(JP_Left.getBackground());
        JR3.setBackground(JP_Left.getBackground());
        JR1.setBounds(40, 100, 200, 25);
        JR2.setBounds(40, 140, 300, 25);
        JR3.setBounds(40, 180, 300, 25);
        
        BG.add(JR1);
        BG.add(JR2);
        BG.add(JR3);
        
        JP_Left.add(JR1);
        JP_Left.add(JR2);
        JP_Left.add(JR3);
        
        JR1.addActionListener((ActionEvent e) -> {
            //Draw Graph
            if (JP_Right != null)
                ref.remove(JP_Right);
            JP_Right = new Draw();
            JP_Right.setLocation(400,0);
            ref.getContentPane().add(JP_Right);
            if (DrawSolution.J != null){
                DrawSolution.J.dispose();
                DrawSolution.J = null;
                DrawSolution.clean();
            }
            B1.setVisible(true);
            L2.setVisible(false);
            TF.setVisible(false);
            B2.setVisible(false);
            
            L3.setVisible(true);
            B3.setVisible(true);
            B4.setVisible(true);
            B5.setVisible(true);
            graph = Draw.graph;
            ref.repaint();
        });
        
        JR2.addActionListener((ActionEvent e) -> {
            //Manual Graph code
            if (JP_Right != null)
                ref.remove(JP_Right);
            JP_Right = null;
            if (DrawSolution.J != null){
                DrawSolution.J.dispose();
                DrawSolution.J = null;
                DrawSolution.clean();
            }
            B1.setVisible(false);
            L2.setVisible(true);
            TF.setVisible(true);
            B2.setVisible(true);
            
            L3.setVisible(false);
            B3.setVisible(false);
            B4.setVisible(false);
            B5.setVisible(false);
            TF.setText("");
            ref.repaint();
        });
        
        JR3.addActionListener((ActionEvent e) -> {
            //Random Graph code
            if (JP_Right != null)
                ref.remove(JP_Right);
            JP_Right = null;
            if (DrawSolution.J != null){
                DrawSolution.J.dispose();
                DrawSolution.J = null;
                DrawSolution.clean();
            }
            B1.setVisible(false);
            L2.setVisible(true);
            TF.setVisible(true);
            B2.setVisible(true);
            
            L3.setVisible(false);
            B3.setVisible(false);
            B4.setVisible(false);
            B5.setVisible(false);
            TF.setText("");
            ref.repaint();
        });
        
        B1 = new JButton("Random complete");
        B1.setBounds(100, 250, 150, 40);
        B1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        B1.setVisible(false);
        JP_Left.add(B1);
        B1.addActionListener((ActionEvent e) -> {
            if (Draw.n == 0){
                JOptionPane.showMessageDialog(rootPane, "There are no vertices !","ok",JOptionPane.INFORMATION_MESSAGE);
                ref.repaint();
                return ;
            }
            if (test_complete(Draw.graph)){
                JOptionPane.showMessageDialog(rootPane, "The graph is complete","ok",JOptionPane.INFORMATION_MESSAGE);
                ref.repaint();
            } else {
                complete_graph();
                ref.repaint();
            }
        });
       
        L2 = new JLabel("Insert n");
        L2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L2.setBounds(30, 250, 100, 20);
        L2.setVisible(false);
        JP_Left.add(L2);
        
        TF = new JTextField("");
        TF.setHorizontalAlignment(JTextField.CENTER);
        TF.setBounds(110,250,60,30);
        TF.setVisible(false);
        JP_Left.add(TF);
        
        TF.addKeyListener(new KeyAdapter() {
             @Override
             public void keyPressed(KeyEvent e) {
                 if (e.getKeyCode() == KeyEvent.VK_ENTER){
                     B2.doClick();
                 }
             }
        });
        
        B2 = new JButton("OK");
        B2.setBounds(250,250,60,30);
        B2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        B2.setVisible(false);
        JP_Left.add(B2);
        
        B2.addActionListener((ActionEvent e) -> {
            try { 
                int n = Integer.parseInt(TF.getText());
                if (n<=0)
                    throw new Exception();
                if (JR2.isSelected()){
                    //Manual Graph     
                    JP_Right = new Manual(n);
                    JP_Right.setLocation(400, 0);
                    ref.getContentPane().add(JP_Right);
                    ref.repaint();
                    Manual.generate_edges();
                    L3.setVisible(true);
                    B3.setVisible(true);
                    B4.setVisible(true);
                    B5.setVisible(true);
                    graph = Manual.graph;
                    ref.repaint();
                } else if (JR3.isSelected()){
                    //Random Graph
                    JP_Right = new Randomize(n);
                    JP_Right.setLocation(400, 0);
                    ref.getContentPane().add(JP_Right);
                    L3.setVisible(true);
                    B3.setVisible(true);
                    B4.setVisible(true);
                    B5.setVisible(true);
                    graph = Randomize.graph;
                    ref.repaint();
                }
            }catch (Exception ex) {
                JOptionPane.showMessageDialog(rootPane,"Error number","Oohps !!!",JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        /////////////////////////////***SOLVING***//////////////////////////////
        
        L3 = new JLabel("Select algorithm to solve :");
        L3.setBounds(20,350,300,30);;
        L3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        L3.setVisible(false);
        JP_Left.add(L3);
        
        
        B3 = new JButton("Held-Karp");
        B3.setBounds(100,400,175,50);
        B3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        B3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        B3.setVisible(false);
        JP_Left.add(B3);
        
        B4 = new JButton("Ant-Colony");
        B4.setBounds(100,500,175,50);
        B4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        B4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        B4.setVisible(false);
        JP_Left.add(B4);
        
        B5 = new JButton("Nearest-Neighbour");
        B5.setBounds(100,600,175,50);
        B5.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        B5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        B5.setVisible(false);
        JP_Left.add(B5);
        
        B3.addActionListener((ActionEvent e) -> {
            //Held-Karp Solution
            if (graph.vertexSet().isEmpty()){
                    JOptionPane.showMessageDialog(rootPane, "There are no vertices !","Oohps !!!",JOptionPane.INFORMATION_MESSAGE);
                    return ;
                }
                if (graph.vertexSet().size() == 1){
                    JOptionPane.showMessageDialog(rootPane, "There is no tour with one vertex","Oohps !!!",JOptionPane.INFORMATION_MESSAGE);
                    return ;
                }
            if (! good_order(graph)){
                    JOptionPane.showMessageDialog(rootPane,"Bad vertices index order","oohps !!!",JOptionPane.ERROR_MESSAGE);
                    return ;
                }
            int starting_vertex = get_starting_vertex(graph.vertexSet());
               
                if (starting_vertex == -1)
                    return;
                Graph <Integer,DefaultWeightedEdge> completed = Utility.generate_completed_graph(graph);
                Solution sol = HeldKarp.solve(completed,starting_vertex);
                if (sol.cost > Integer.MAX_VALUE)
                    JOptionPane.showMessageDialog(rootPane, "There is no solution", "Oohps !!!", JOptionPane.ERROR_MESSAGE);
                else {if (HH != null)
                    HH.dispose();
                HH = new DrawSolution("Held-Karp",graph,sol);
                }
        });
        
        B4.addActionListener((ActionEvent e) -> {
            //Ant-Colony Solution
            if (graph.vertexSet().isEmpty()){
                    JOptionPane.showMessageDialog(rootPane, "There are no vertices !","Oohps !!!",JOptionPane.INFORMATION_MESSAGE);
                    return ;
                }
                if (graph.vertexSet().size() == 1){
                    JOptionPane.showMessageDialog(rootPane, "There is no tour with one vertex","Oohps !!!",JOptionPane.INFORMATION_MESSAGE);
                    return ;
                }
            if (! good_order(graph)){
                    JOptionPane.showMessageDialog(rootPane,"Bad vertices index order","oohps !!!",JOptionPane.ERROR_MESSAGE);
                    return ;
                }
            int n = graph.vertexSet().size();
                
                //default values for Ant-Colony algorithm
                double alpha = 1.0;     //1
                double beta = 6.0;      //2
                double rho = 0.6;       //3
                double Q = 1.0;         //4
                int pheromone = 1;      //5
                int ants = n/3 + 1;     //6
                int rounds = n/2;       //7
                
                String [] options = new String [] {"Default","Manual"};
                int selected_option = JOptionPane.showOptionDialog(rootPane,"Which parameters do you want ?","Ant-Colony parameters",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null, options, options[0]);
                Graph <Integer,DefaultWeightedEdge> completed = Utility.generate_completed_graph(graph);
                // 0 --> Default , 1 --> Manual , -1 --> closed
                if (selected_option == 0){
                    //same values
                    Solution sol = AntColony.solve(completed, ants, rounds, alpha, beta, rho, Q, pheromone);
                    
                    if (sol.cost >= Integer.MAX_VALUE)
                    JOptionPane.showMessageDialog(rootPane, "There is no solution", "Oohps !!!", JOptionPane.ERROR_MESSAGE);
                    else {if (AA != null)
                        AA.dispose();
                    AA = new DrawSolution("Ant-Colony",graph,sol);
                    }
                } else if (selected_option == 1){
                    //manual values give the control to InsertParameters class
                    if (II != null)
                        II.dispose();
                    II = new InsertParameters(completed,"Ant-Colony Parameters",425,500);
                } else if (selected_option == -1){
                    //nothing to do
                    return ;
                }
        });
        
        B5.addActionListener((ActionEvent e) -> {
            //Nearest-Neighbour Solution
            if (graph.vertexSet().isEmpty()){
                    JOptionPane.showMessageDialog(rootPane, "There are no vertices !","Oohps !!!",JOptionPane.INFORMATION_MESSAGE);
                    return ;
                }
                if (graph.vertexSet().size() == 1){
                    JOptionPane.showMessageDialog(rootPane, "There is no tour with one vertex","Oohps !!!",JOptionPane.INFORMATION_MESSAGE);
                    return ;
                }
            if (! good_order(graph)){
                    JOptionPane.showMessageDialog(rootPane,"Bad vertices index order","Oohps !!!",JOptionPane.ERROR_MESSAGE);
                    return ;
                }
            int starting_vertex = get_starting_vertex(graph.vertexSet());
                if (starting_vertex == -1)
                    return;
                Graph <Integer,DefaultWeightedEdge> completed = Utility.generate_completed_graph(graph);
                Solution sol = NearestNeighbour.solve(completed,starting_vertex);
                if (sol.cost >= Integer.MAX_VALUE)
                    JOptionPane.showMessageDialog(rootPane, "There is no solution", "Oohps !!!", JOptionPane.ERROR_MESSAGE);
                else {if (NN != null)
                    NN.dispose();
                NN = new DrawSolution("Nearest-Neighbour",graph,sol);
                }
        });
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e){
                if (DrawSolution.J != null){
                    DrawSolution.J.dispose();
                    DrawSolution.J = null;
                }
                InputGraph.ref.dispose();
                InputGraph.ref = null;
                Introduction.in.dispose();
                Introduction.in = null;
                DrawSolution.clean();
                Introduction.ref.setVisible(true);
            }
        });
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);        
    }
    
}