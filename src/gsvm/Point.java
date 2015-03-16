/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsvm;

/**
 *
 *
 */
public class Point {
    double suradnice[];

    public Point(double a[]) {
        suradnice = a.clone();
    }
    
    public Point(double x, double y, double z, int isPoint){
        suradnice = new double[]{x,y,z,isPoint};
    }

    @Override
    public String toString() {
        return suradnice[0] + " " + suradnice[1] + " " + suradnice[2] + " " + suradnice[3];
    }
    
    
    
}
