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
public class Matrix {
    double matrix[][];

    public Matrix(double a[][]) {
        matrix = a.clone();
    }
    
    static Point multiply(Point m1, Matrix m2){
        double m3[] = new double[4];
        
       
        for(int i = 0; i < 4; ++i){
            m3[i] = 0;
        }
        
        
        for(int i = 0; i < 4; ++i){
            for(int k = 0; k < 4; ++k){
                m3[i] += m1.suradnice[k]*m2.matrix[k][i];  
            }
        }
        
        return new Point(m3);
    }
    
    static Matrix multiply(Matrix m1, Matrix m2){
        double m3[][] = new double[4][4];
        
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                m3[i][j] = 0;
            }
        }
        
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                for(int k = 0; k < 4; ++k){
                    m3[i][j] += m1.matrix[i][k]*m2.matrix[k][j];
                }
            }
        }
        
        return new Matrix(m3);
    }
    
}
