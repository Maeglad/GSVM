/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsvm;

import java.util.ArrayList;

/**
 *
 * 
 */
public class Stena {
    private final ArrayList<Integer> points;
    private int vyska;

    public Stena() {
        points = new ArrayList<Integer>();
        points.clear();
    }
    
    public boolean add(int p){
        return points.add(p);
    }
    
    public ArrayList<Integer> getPoints(){
        return points;
    }
    
    public int getHeight(){
        return vyska;
    }
    
    
}
