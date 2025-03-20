/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qzic.dsp.filter;

import static java.lang.Math.abs;

/**
 *
 * @author Quentin
 */
public class IirFilterOrder1 {
    double filterMem;
    double a1 = 0.1;
    
    public IirFilterOrder1(){
        filterMem=0;
    }

    public IirFilterOrder1(double coef){
        a1=coef;
        filterMem=0;
    }
        
    public void setCoef(double coef){
        a1 = coef;
    }
    
    public double process(double in) {
        double ki = 1-a1;
        filterMem = in * ki + filterMem * a1;
        return filterMem;
    }
    
    public double process(double[] in) {
        double ki = 1-a1;
        for(int i=0; i<in.length; i++) {
            filterMem = in[i] * ki + filterMem * a1;
        
        }
//        filterMem = filterMem > 1.0 ? 1.0 : filterMem;
//        filterMem = filterMem < -1.0 ? -1.0 : filterMem;    
        return filterMem;
    }
    

}
