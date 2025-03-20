/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.filter;

/**
 *
 * @author Quentin
 */
public class IirFilter1stOrder {
    
    double filterMem[];
    double ki,a1;
    
    public IirFilter1stOrder(int blockSize, double a1){
        filterMem = new double[blockSize];
        this.a1 = a1;
        this.ki = 1-a1;
    }
       
    public double[] process(double x[]){
        for (int i=0; i< x.length; i++) {
            filterMem[i] = x[i] * ki + filterMem[i] * a1;
        }
        return filterMem;
    }
    
}
