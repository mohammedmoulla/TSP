package interfaces;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Introduction extends JFrame {
    JLabel image;
    JLabel L1,L2,L3,L4,L5,L6,L7;
    JButton B1,B2;
    public static JFrame ref,in;
    
    public Introduction (){
        setTitle("TSP");
        setSize(400,600);
        ref = this;
        setLayout(null); //no Layout --> setbounds in pixel for each component
        Container cp = getContentPane();
        image = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/Tishreen University.jpg"));
        image.setIcon(icon);
        image.setBounds(250, 2, 150, 150);
        cp.add(image);
        L1 = new JLabel("جامعة تشرين");
        L2 = new JLabel("كلية الهندسة المعلوماتية");
        L3 = new JLabel("هندسة البرمجيات ونظم المعلومات");
        L1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L1.setHorizontalAlignment(SwingConstants.RIGHT);
        L2.setHorizontalAlignment(SwingConstants.RIGHT);
        L3.setHorizontalAlignment(SwingConstants.RIGHT);
        L1.setBounds(25,25,200,20);
        L2.setBounds(25,50,200,20);
        L3.setBounds(25,75,200,20);
        cp.add(L1);
        cp.add(L2);
        cp.add(L3);
        
        L4 = new JLabel("Travelling Salesman Person (TSP)");
        L4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L4.setHorizontalAlignment(SwingConstants.CENTER);
        L4.setBounds(5,200,380,30);
        cp.add(L4);
        
        L5 = new JLabel("محمد هيثم معلا");
        L6 = new JLabel("عبد الرزاق عبد الفتاح المصطفى");
        L7 = new JLabel("رهف عباس بربهان");
        L5.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L6.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L7.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        L5.setHorizontalAlignment(SwingConstants.CENTER);
        L6.setHorizontalAlignment(SwingConstants.CENTER);
        L7.setHorizontalAlignment(SwingConstants.CENTER);
        L5.setBounds(5,250,380,30);
        L6.setBounds(5,275,380,30);
        L7.setBounds(5,300,380,30);
        cp.add(L5);
        cp.add(L6);
        cp.add(L7);
        
        B1 = new JButton("Start Project");
        B1.setBounds(137,375,126,40);
        B1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        B1.setFocusPainted(false);
        cp.add(B1);
        B1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (in == null)
                    in = new InputGraph();
                else{ 
                    in.setVisible(true);
                }
                if (DrawSolution.J != null)
                    DrawSolution.J.setVisible(true);
                ref.setVisible(false);
            }
        });
        
        
        
        B2 = new JButton("Exit");
        B2.setBounds(162,450,76,40);
        B2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cp.add(B2);
        B2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (in != null)
                    in.dispose();
                ref.dispose();
                
            }
        });
        
        cp.setBackground(Color.WHITE);
        setLocationRelativeTo(null); //put the interface in the middle of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}