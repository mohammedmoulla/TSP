package interfaces;

import algorithms.AntColony;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jgrapht.Graph;
import utility.Solution;
import static interfaces.InputGraph.*;
public class InsertParameters extends JFrame {

        JTextField TF1,TF2,TF3,TF4,TF5,TF6,TF7;
        JLabel L1,L2,L3,L4,L5,L6,L7;
        JLabel LL1,LL2,LL3,LL4,LL5,LL6,LL7;
        JButton B;
        
        int n ;
                
        //⍺ β ⍴ ∈  α τ  ρ
        double alpha;       //1
        double beta;        //2
        double rho;         //3
        double Q;           //4
        int pheromone;      //5
        int ants;           //6
        int rounds;         //7
        
        public InsertParameters(Graph graph,String s, int W, int L) {
            setTitle(s);
            setSize(W, L);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setResizable(false);
            setLayout(null);
            n = graph.vertexSet().size();
            alpha = 1.0;
            beta = 6.0;
            rho = 0.6;
            Q = 1;
            pheromone = 1;
            ants = n/3 + 1;
            rounds = n/2;
            
            Container cp = getContentPane();
            Font font = new Font("MonoSpaced",Font.PLAIN,16);
            
            L1 = new JLabel("alpha(⍺)");       L1.setBounds(50,  55, 150, 20);     cp.add(L1);   L1.setFont(font);
            L2 = new JLabel("beta(β)");        L2.setBounds(50, 105, 150, 20);     cp.add(L2);   L2.setFont(font);
            L3 = new JLabel("rho(⍴)");         L3.setBounds(50, 155, 150, 20);     cp.add(L3);   L3.setFont(font);
            L4 = new JLabel("constant(Q)");    L4.setBounds(50, 205, 150, 20);     cp.add(L4);   L4.setFont(font);
            L5 = new JLabel("pheromone(τ)");   L5.setBounds(50, 255, 150, 20);     cp.add(L5);   L5.setFont(font);
            L6 = new JLabel("ants number");    L6.setBounds(50, 305, 150, 20);     cp.add(L6);   L6.setFont(font);
            L7 = new JLabel("rounds number");  L7.setBounds(50, 355, 150, 20);     cp.add(L7);   L7.setFont(font);
            
            TF1 = new JTextField(""+alpha);         TF1.setBounds(200,  50, 60, 30);    cp.add(TF1);    TF1.setFont(font);
            TF2 = new JTextField(""+beta);          TF2.setBounds(200, 100, 60, 30);    cp.add(TF2);    TF2.setFont(font);
            TF3 = new JTextField(""+rho);           TF3.setBounds(200, 150, 60, 30);    cp.add(TF3);    TF3.setFont(font);
            TF4 = new JTextField(""+Q);             TF4.setBounds(200, 200, 60, 30);    cp.add(TF4);    TF4.setFont(font);
            TF5 = new JTextField(""+pheromone);     TF5.setBounds(200, 250, 60, 30);    cp.add(TF5);    TF5.setFont(font);
            TF6 = new JTextField(""+ants);          TF6.setBounds(200, 300, 60, 30);    cp.add(TF6);    TF6.setFont(font);
            TF7 = new JTextField(""+rounds);        TF7.setBounds(200, 350, 60, 30);    cp.add(TF7);    TF7.setFont(font);
            
            font = new Font("MonoSpaced",Font.PLAIN,14);
            
            LL1 = new JLabel("⍺ ∈ ]0,15[");        LL1.setBounds(270,  55, 150, 20);     cp.add(LL1);   LL1.setFont(font);  LL1.setForeground(Color.red);
            LL2 = new JLabel("β ∈ ]0,15[");        LL2.setBounds(270, 105, 150, 20);     cp.add(LL2);   LL2.setFont(font);  LL2.setForeground(Color.red);
            LL3 = new JLabel("⍴ ∈ ]0,1[");         LL3.setBounds(270, 155, 150, 20);     cp.add(LL3);   LL3.setFont(font);  LL3.setForeground(Color.red);
            LL4 = new JLabel("Q ∈ ]0,15[");        LL4.setBounds(270, 205, 150, 20);     cp.add(LL4);   LL4.setFont(font);  LL4.setForeground(Color.red);
            LL5 = new JLabel("τ ∈ ]0,15[");        LL5.setBounds(270, 255, 150, 20);     cp.add(LL5);   LL5.setFont(font);  LL5.setForeground(Color.red);
            LL6 = new JLabel("ants ∈ [1,"+n+"]");  LL6.setBounds(270, 305, 150, 20);     cp.add(LL6);   LL6.setFont(font);  LL6.setForeground(Color.red);
            LL7 = new JLabel("rounds ∈ [1,100]");  LL7.setBounds(270, 355, 150, 20);     cp.add(LL7);   LL7.setFont(font);  LL7.setForeground(Color.red);
            
            B = new JButton("OK");          B.setBounds(170, 405, 75, 30);     cp.add(B);
            
            B.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (! test_parameters())
                        return ;
                    Solution sol = AntColony.solve(graph, ants, rounds, alpha, beta, rho, Q, pheromone);
                     if (sol.cost >= Integer.MAX_VALUE)
                    JOptionPane.showMessageDialog(rootPane, "There is no solution", "Oohps !!!", JOptionPane.ERROR_MESSAGE);
                     else{
                    if (AA != null)
                        AA.dispose();
                    AA = new DrawSolution("Ant-Colony",graph,sol);
                     }
                    dispose();
                }
            });
            
            setVisible(true);
            setLocationRelativeTo(null);
        }//end of constructor
        
        boolean test_parameters (){
            int counter = 1;
            
            try { 
                alpha = Double.parseDouble(TF1.getText());       //1
                if (alpha<=0 || alpha>=15)
                    throw new Exception();
                
                counter=2;
                beta = Double.parseDouble(TF2.getText());        //2
                if (beta<=0 || beta>=15)
                    throw new Exception();
                
                counter=3;
                rho = Double.parseDouble(TF3.getText());         //3
                if (rho<=0 || rho>=1)
                    throw new Exception();
                
                counter=4;
                Q = Double.parseDouble(TF4.getText());           //4
                if (Q<=0 || Q>=15)
                    throw new Exception();
                
                counter=5;
                pheromone = Integer.parseInt(TF5.getText());      //5
                if (pheromone<=0 || pheromone>=15)
                    throw new Exception();
                
                counter=6;
                ants = Integer.parseInt(TF6.getText());           //6
                if (ants<=0 || ants>n)
                    throw new Exception();
                
                counter=7;
                rounds = Integer.parseInt(TF7.getText());         //7
                if (rounds<=0 || rounds>100)
                    throw new Exception();
                
            } catch (Exception e){
                switch (counter){
                    case 1 : JOptionPane.showMessageDialog(rootPane,"alpha should be in ]0,15[","error",JOptionPane.ERROR_MESSAGE); break;
                    case 2 : JOptionPane.showMessageDialog(rootPane,"beta should be in ]0,15[","error",JOptionPane.ERROR_MESSAGE); break;
                    case 3 : JOptionPane.showMessageDialog(rootPane,"rho should be in ]0,1[","error",JOptionPane.ERROR_MESSAGE); break;
                    case 4 : JOptionPane.showMessageDialog(rootPane,"Q should be in ]0,15[","error",JOptionPane.ERROR_MESSAGE); break;
                    case 5 : JOptionPane.showMessageDialog(rootPane,"pheromone should be in ]0,15[","error",JOptionPane.ERROR_MESSAGE); break;
                    case 6 : JOptionPane.showMessageDialog(rootPane,"ants should be in [1,"+n+"]","error",JOptionPane.ERROR_MESSAGE); break;
                    case 7 : JOptionPane.showMessageDialog(rootPane,"rounds should be in [1,100]","error",JOptionPane.ERROR_MESSAGE); break;    
                }
                
                return false;
            }
            return true;
        }//end of test_parameters
    }//end of private class InsertParameters