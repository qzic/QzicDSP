/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qzic.dsp.effects;

import static ca.qzic.dsp.Amplitude.Average.RmsMono;
import ca.qzic.dsp.Delay.Delay;
import ca.qzic.dsp.util.*; 
import static java.lang.System.out;


/**
 *
 * @author Quentin
 */
public class Compression implements Command {
    final static double tAverage = 10;      // ms
    double sampleRate;
    int delay;
    double tav,at,rt,yrms,gr;
    double ratio,CS;
    double compGain = 0;
    double threshold = 0;
    int numChannels;
    double y[];
    double coeff;
    
    static Delay dla;
    boolean firstRun = true;
    double Y,G,f;
    
    public Compression(double ratio,double attackTime_ms, double releaseTime_ms, double threshold, double sampleRate, int numChannels){
        this.ratio = ratio;
        this.numChannels = numChannels;
        this.sampleRate = sampleRate;
        gr = 1.0;
        yrms = 0;
        tav = 1.0 - Math.exp(-2.2/sampleRate/(tAverage * 0.001));
        at = 1.0 - Math.exp(-2.2/sampleRate/(attackTime_ms * 0.001));
        rt = 1.0 - Math.exp(-2.2/sampleRate/(releaseTime_ms * 0.001));
        delay = (int) (sampleRate/1000*tAverage);            // delay by tAverage in samples
        dla = new Delay(delay*2 + 128,delay * 2);            // * 2 incase x[n] is stareo + 128 for safety 
        CS = 1-1/ratio;
//        System.out.println("at = " + at + ", rt = " + rt + ", tav = " + tav);
    }
    
    public void setCompGain(double compGain){
        this.compGain = compGain;
    }
    
    public double getCompGain(){
        return compGain;
    }
    
    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        double th = threshold < 0.01 ? 0.01 : threshold;    // min -40dB FS
//        this.threshold = 20.0 * Math.log10(th);
        this.threshold = -40.0 + threshold*40;
//        System.out.println("compression threshold = " + this.threshold + ", xrms = " + Y);
    }
    
    public double getGr() {
        return gr;
    }

    public void setGr(double gr) {
        this.gr = gr;
    }
    
    public double getRmsLevel(){
        return yrms;
    }    
    
    @Override
    public boolean execute(int x[]){
        if(firstRun) {
            y = new double[x.length];
        }

        for (int n = 0; n<x.length; n++) {
            Y = 20*Math.log10(yrms);
            G = Math.min(0, CS*(threshold-Y));
            f = Math.pow(10,(G/20));
            if (f < gr) {
                coeff = at;
            } else {
                coeff = rt;
            }
            gr = (1-coeff) * gr + coeff * f;
            gr = gr > 1.0 ? 1.0 : gr;
            y[n] = gr * x[n];  // dla.process(x[n]);            
        }  
        yrms = RmsMono(y,tAverage,sampleRate);
        System.arraycopy(y,0, x, 0, x.length);
        return true;
    }
    
    @Override
    public boolean execute(double x[]){
        if(firstRun) {
            y = new double[x.length];
        }

        for (int n = 0; n<x.length; n++) {
            Y = 20*Math.log10(yrms);
            G = Math.min(0, CS*(threshold-Y));
            f = Math.pow(10,(G/20));
            if (f < gr) {
                coeff = at;
            } else {
                coeff = rt;
            }
            gr = (1-coeff) * gr + coeff * f;
            gr = gr > 1.0 ? 1.0 : gr;
            y[n] = gr * x[n];  // dla.process(x[n]);            
        }  
        yrms = RmsMono(y,tAverage,sampleRate);
        System.arraycopy(y,0, x, 0, x.length);
        return true;
    }
}
