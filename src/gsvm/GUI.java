/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsvm;

import java.awt.BorderLayout;
import java.awt.Canvas;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * 
 */
public class GUI implements Runnable{
    
    
    /**
     *  Classa je skopirovana z https://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html
     * a pozmenena mnou
     * 
     */
    class CyclingSpinnerNumberModel extends SpinnerNumberModel {
        Object firstValue, lastValue;

        public CyclingSpinnerNumberModel(int val, int min, int max, int step) {
            super(val, min, max, step);
            firstValue = min;
            lastValue = max;
        }

        @Override
        public Object getNextValue() {
            Object value = super.getNextValue();
            if (value == null) {
                value = firstValue;
            }
            return value;
        }

        @Override
        public Object getPreviousValue() {
            Object value = super.getPreviousValue();
            if (value == null) {
                value = lastValue;
            }
            return value;
        }
    }
    
    
    class GraphicPanel extends JPanel{
        Matrix scaleMatrix, transMatrix, rotateXMatrix, rotateYMatrix, rotateZMatrix, rotateMatrix, comboMatrix;
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
            rotateXMatrix = new Matrix(new double[][]{
                                                    {1, 0, 0, 0},
                                                    {0, 1, 0, 0},
                                                    {0, 0, 1, 0},
                                                    {0, 0, 0 ,1}
                                                   });
            rotateYMatrix = new Matrix(new double[][]{
                                                    {1, 0, 0, 0},
                                                    {0, 1, 0, 0},
                                                    {0, 0, 1, 0},
                                                    {0, 0, 0 ,1}
                                                   });
            rotateZMatrix = new Matrix(new double[][]{
                                                    {1, 0, 0, 0},
                                                    {0, 1, 0, 0},
                                                    {0, 0, 1, 0},
                                                    {0, 0, 0 ,1}
                                                   });
            rotateMatrix = new Matrix(new double[][]{
                                                    {1, 0, 0, 0},
                                                    {0, 1, 0, 0},
                                                    {0, 0, 1, 0},
                                                    {0, 0, 0 ,1}
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
            ArrayList<Point> tmp2Body = new ArrayList<Point>();
            rotateMatrix = Matrix.multiply(rotateXMatrix, Matrix.multiply(rotateYMatrix, rotateZMatrix));
            for(Point p : body){
                tmpBody.add(Matrix.multiply(p, rotateMatrix));
            }
            
            for(Point p : tmpBody){
                tmp2Body.add(Matrix.multiply(p, comboMatrix));
            }
            
            for(Point p : tmp2Body){
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
                    g.drawLine((int)tmp2Body.get(order.get(i)).suradnice[0],
                               (int)tmp2Body.get(order.get(i)).suradnice[1],
                               (int)tmp2Body.get(order.get((i+1)%order.size())).suradnice[0],
                               (int)tmp2Body.get(order.get((i+1)%order.size())).suradnice[1]);
                    
                    
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
        
        // ******* panely na manipulaciu modelu **************
        
        JPanel scalePanel = new JPanel();
        
        scalePanel.setLayout(new BoxLayout(scalePanel, BoxLayout.Y_AXIS));
        JSpinner scaleX = new JSpinner(new SpinnerNumberModel(100, 0, 1000, 10));
        scaleX.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                // ocividne moj netbeans nedokaze castovat Integer do Double priamo aj ked na to ma funkcie
                canvas.scaleMatrix.matrix[0][0] = ((Integer) scaleX.getValue()).doubleValue();
                canvas.repaint();
            }
        });
        scalePanel.add(scaleX);
        JSpinner scaleY = new JSpinner(new SpinnerNumberModel(100, 0, 1000, 10));
        scaleY.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.scaleMatrix.matrix[1][1] = ((Integer) scaleY.getValue()).doubleValue();;
                canvas.repaint();
            }
        });
        scalePanel.add(scaleY);
        JSpinner scaleZ = new JSpinner(new SpinnerNumberModel(100, 0, 1000, 10));
        scaleZ.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.scaleMatrix.matrix[2][2] = ((Integer) scaleZ.getValue()).doubleValue();
                canvas.repaint();
            }
        });
        scalePanel.add(scaleZ);
        scalePanel.setBorder(BorderFactory.createTitledBorder("Scale"));
        bottomPanel.add(scalePanel);
        
        JPanel rotatePanel = new JPanel();
        
        rotatePanel.setLayout(new BoxLayout(rotatePanel, BoxLayout.Y_AXIS));
        JSpinner rotateX = new JSpinner(new CyclingSpinnerNumberModel(0, 0, 360, 5));
        rotateX.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                // ocividne moj netbeans nedokaze castovat Integer do Double priamo aj ked na to ma funkcie
                double value = ((Integer) rotateX.getValue()).doubleValue();
                canvas.rotateXMatrix.matrix[1][1] = Math.cos((value/180)*Math.PI);
                canvas.rotateXMatrix.matrix[1][2] = -Math.sin((value/180)*Math.PI);
                canvas.rotateXMatrix.matrix[2][1] = Math.sin((value/180)*Math.PI);
                canvas.rotateXMatrix.matrix[2][2] = Math.cos((value/180)*Math.PI);
                
                canvas.repaint();
            }
        });
        rotatePanel.add(rotateX);
        JSpinner rotateY = new JSpinner(new CyclingSpinnerNumberModel(0, 0, 360, 5));
        rotateY.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((Integer) rotateY.getValue()).doubleValue();
                canvas.rotateYMatrix.matrix[0][0] = Math.cos((value/180)*Math.PI);
                canvas.rotateYMatrix.matrix[0][2] = Math.sin((value/180)*Math.PI);
                canvas.rotateYMatrix.matrix[2][0] = -Math.sin((value/180)*Math.PI);
                canvas.rotateYMatrix.matrix[2][2] = Math.cos((value/180)*Math.PI);
                
                canvas.repaint();
            }
        });
        rotatePanel.add(rotateY);
        JSpinner rotateZ = new JSpinner(new CyclingSpinnerNumberModel(0, 0, 360, 5));
        rotateZ.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double value = ((Integer) rotateZ.getValue()).doubleValue();
                canvas.rotateZMatrix.matrix[0][0] = Math.cos((value/180)*Math.PI);
                canvas.rotateZMatrix.matrix[0][1] = -Math.sin((value/180)*Math.PI);
                canvas.rotateZMatrix.matrix[1][0] = Math.sin((value/180)*Math.PI);
                canvas.rotateZMatrix.matrix[1][1] = Math.cos((value/180)*Math.PI);
                
                canvas.repaint();
            }
        });
        rotatePanel.add(rotateZ);
        rotatePanel.setBorder(BorderFactory.createTitledBorder("rotate"));
        bottomPanel.add(rotatePanel);
        
        JPanel transPanel = new JPanel();
        
        transPanel.setLayout(new BoxLayout(transPanel, BoxLayout.Y_AXIS));
        JSpinner transX = new JSpinner(new SpinnerNumberModel(0, -500, 500, 10));
        transX.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                // ocividne moj netbeans nedokaze castovat Integer do Double priamo aj ked na to ma funkcie
                canvas.transMatrix.matrix[3][0] = 300 + ((Integer) transX.getValue()).doubleValue();
                canvas.repaint();
            }
        });
        transPanel.add(transX);
        JSpinner transY = new JSpinner(new SpinnerNumberModel(0, -500, 500, 10));
        transY.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.transMatrix.matrix[3][1] = 200 + ((Integer) transY.getValue()).doubleValue();;
                canvas.repaint();
            }
        });
        transPanel.add(transY);
        JSpinner transZ = new JSpinner(new SpinnerNumberModel(0, -500, 500, 10));
        transZ.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.transMatrix.matrix[3][2] =  0 + ((Integer) transZ.getValue()).doubleValue();
                canvas.repaint();
            }
        });
        transPanel.add(transZ);
        transPanel.setBorder(BorderFactory.createTitledBorder("Transformation"));
        bottomPanel.add(transPanel);
        
//****************************************************
        
        frame.add(bottomPanel,BorderLayout.SOUTH);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    
}
