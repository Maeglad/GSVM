/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsvm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * 
 */
public class GUI implements Runnable{

    
    
    class GraphicPanel extends JPanel{
        Matrix scaleMatrix, transMatrix, comboMatrix;
        ArrayList<Stena> steny;
        ArrayList<Point> body;
        public GraphicPanel() {
            scaleMatrix = new Matrix(new double[][]{
                                                    {100, 0, 0, 0},
                                                    {0, 100, 0, 0},
                                                    {0, 0, 100, 0},
                                                    {0, 0, 0, 1}
                                                   });
            transMatrix = new Matrix(new double[][]{
                                                    {1, 0, 0, 0},
                                                    {0, 1, 0, 0},
                                                    {0, 0, 1, 0},
                                                    {300, 200, 0 ,1}
                                                   });
            comboMatrix = new Matrix(new double[4][4]);
            steny = new ArrayList<Stena>();
            body = new ArrayList<Point>();
            clear();
        }

        public void clear(){
            steny.clear();
            body.clear();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            comboMatrix = Matrix.multiply(scaleMatrix, transMatrix);
            ArrayList<Point> tmpBody = new ArrayList<Point>();
            for(Point p : body){
                tmpBody.add(Matrix.multiply(p, comboMatrix));
            }
            
            for(Point p : tmpBody){
                //System.err.println(p);
                p.suradnice[1] = 400 - p.suradnice[1];
                //System.err.println(p);
            }
            
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
            g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
            for(Stena s : steny){
                ArrayList<Integer> order = s.getPoints();
                for(int i = 0; i < order.size(); ++i){
                    g.drawLine((int)tmpBody.get(order.get(i)).suradnice[0],
                               (int)tmpBody.get(order.get(i)).suradnice[1],
                               (int)tmpBody.get(order.get((i+1)%order.size())).suradnice[0],
                               (int)tmpBody.get(order.get((i+1)%order.size())).suradnice[1]);
                    
                    
                }
            }
        }
        
        
    }
    
    @Override
    public void run() {
        JFrame frame = new JFrame("GSVM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        JFileChooser fc = new JFileChooser(".");
        
        GraphicPanel canvas = new GraphicPanel();
        canvas.setPreferredSize(new Dimension(600, 400));
        frame.add(canvas);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JButton loadButton = new JButton("Load object");
        loadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                int val = fc.showOpenDialog(frame);
                if(val == JFileChooser.APPROVE_OPTION){
                     canvas.clear();
                //  open file
                    File file = fc.getSelectedFile();
                    try {
                        BufferedReader in = new BufferedReader(new FileReader(file));
                        String s;
                        try {
                            while((s = in.readLine()) != null){
                                switch(s.charAt(0)){
                                    
                                    case 'v':
                                        String hodnoty[] = s.substring(1).trim().split(" ");
                                        Point p = new Point(Double.parseDouble(hodnoty[0]),
                                                            Double.parseDouble(hodnoty[1]),
                                                            Double.parseDouble(hodnoty[2]), 
                                                            1);
                                        canvas.body.add(p);
                                        break;
                                    case 'f':
                                        String body[] = s.substring(1).trim().split(" ");
                                        Stena f = new Stena();
                                        for(int i = 0; i < body.length; ++i){
                                            f.add(Integer.parseInt(body[i])-1);
                                        }
                                        canvas.steny.add(f);
                                        break;
                                }
                            }
                        } catch (IOException ex) {
                            
                        }
                    } catch (FileNotFoundException ex) {
                        System.err.println("File "+ file.toString() +" not found");
                    }
                }
                //System.out.println("test vypis load object");
                canvas.repaint();
            }
        });
        // pomocna vec na vypisanie bodov debug
        JButton drawButton = new JButton("vypis body");
        drawButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for(Point p : canvas.body){
                    System.out.println(p);
                }
                
                
                
                canvas.repaint();
            }
        });
        
        
        buttonPanel.add(loadButton);
        buttonPanel.add(drawButton);
        
        bottomPanel.add(buttonPanel);
        frame.add(bottomPanel,BorderLayout.SOUTH);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    
}
