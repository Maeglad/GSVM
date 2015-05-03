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
    private Point center, normal;
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
    
    public void computeCenter(ArrayList<Point> model){
        int x , y , z;
        x = 0;
        y = 0;
        z = 0;
        for(int id : points){
            Point p = model.get(id);
            x += p.suradnice[0];
            y += p.suradnice[1];
            z += p.suradnice[2];
        }
        x = x/points.size();
        y = y/points.size();
        z = z/points.size();
        center = new Point(x, y, z, 1);
    }
    
    // cross product computed from determinant normal = (x1,y1,z1)
    // | xn yn zn | 
    // | x1 y1 z1 | = xn*( y1*z2 - z1*y2) - yn*(x1*z2 - z1*x2) + zn*(x1*y2 - y1*x2)
    // | x2 y2 z2 |
    public void computeNormal(ArrayList<Point> model){
        Point v1, v2;
        Point p1,p2,p3;
        p1 = model.get(points.get(0));
        p2 = model.get(points.get(1));
        p3 = model.get(points.get(2));
        v1 = new Point(p2.suradnice[0] - p1.suradnice[0], 
                       p2.suradnice[1] - p1.suradnice[1], 
                       p2.suradnice[2] - p1.suradnice[2], 0);
        v2 = new Point(p3.suradnice[0] - p2.suradnice[0], 
                       p3.suradnice[1] - p2.suradnice[1], 
                       p3.suradnice[2] - p2.suradnice[2], 0);
        double xn,yn,zn;
        xn = (v1.suradnice[1]*v2.suradnice[2]) - (v1.suradnice[2]*v2.suradnice[1]);
        yn =-(v1.suradnice[0]*v2.suradnice[2]) - (v1.suradnice[2]*v2.suradnice[0]);
        zn = (v1.suradnice[0]*v2.suradnice[1]) - (v1.suradnice[1]*v2.suradnice[0]);
        normal = new Point(xn,yn,zn,0);
    }
    
    public static double getDotProduct(Point vector1, Point vector2){
        double res = 0;
        for(int i = 0; i < 3; ++i){
            res = res + vector1.suradnice[i] * vector2.suradnice[i];
        }
        return res;
    }
    
    public Point getNormal(){
        return normal;
    }
    
    public Point getCenter(){
        return center;
    }
    
    
}
