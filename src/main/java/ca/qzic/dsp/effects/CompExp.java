/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.effects;

import ca.qzic.dsp.filter.IirFilter1stOrder;

/* from DAFX book pg 102
 * M-file 5.3 (c0mpexp.m)
function y=compexp(x,comp,release,attack,a,Fs)
% Compressor/expander
% comp - compression: O>comp>-i, expansion: O<comp<i
% a - filter parameter <l
h=filter([(l-a)-2], [l.OOOO -2*a a-21 ,abs(x));
h=h/max (h) ;
h=h.-comp;
y=y*max(abs(x)>/max(abs(y)) ;
 */

/**
 *
 * @author Quentin
 */
public class CompExp {
    IirFilter1stOrder lpFilter;
    double comp;
    double h[];
    
    public CompExp(int blockSize, double comp, double release, double attack, double lpFilterCoef, double Fs){
        lpFilter = new IirFilter1stOrder(blockSize,lpFilterCoef);
        h = new double[blockSize];
    }
    
    public double[] process(double x[]) {
        h = lpFilter.process(x);
        
        
        return h;
    }
    
}
