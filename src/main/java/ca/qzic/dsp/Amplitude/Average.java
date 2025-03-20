/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qzic.dsp.Amplitude;

/**
 *
 * @author Quentin
 */
public class Average {

    public static double SimpleAvg(double[] data) {
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum /= data.length;
        return sum;
    }
    
    public static double[] SimpleAvgStereo(double[] data) {
        double[] sum = new double[2];
        sum[0] = 0;
        sum[1] = 0;
        for (int i = 0; i < data.length; i+=2) {
            sum[0] += data[i];
            sum[1] += data[i+1];
        }
        sum[0] /= data.length/2;
        sum[1] /= data.length/2;
        return sum;
    }

    public static double Abs(double[] data) {
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += Math.abs(data[i]);
        }
        sum /= data.length;
        return sum;
    }
    
        public static double[] AbsStereo(double data[]){
        double[] sum = new double[2];

        sum[0] = 0;
        sum[1] = 0;
        for(int i=0; i<data.length; i+=2){
            sum[0] += Math.abs(data[i]);
            sum[1] += Math.abs(data[i+1]);
        }
        sum[0] /= data.length;
        sum[1] /= data.length;
        
        return sum;
    }

    public static double RmsMono(double[] data) {
        double acc = 0;
        for (int i = 0; i < data.length; i ++) {
            acc += data[i] * data[i];
        }
        acc /= data.length;
        return Math.sqrt(acc);
    }
    
        public static double RmsMono(double[] data, int start, int end) {
        double acc = 0;
        int length = end-start;
        for (int i = 0; i < length; i ++) {
            acc += data[start+i] * data[start+i];
        }
        acc /= length;
        return Math.sqrt(acc);
    }
    

    public static double RmsMono(double[] x, double tAvg_ms, double sampleRate ) {
    double xrms = 0;
        double tav = 1.0 - Math.exp(-2.2/sampleRate/(tAvg_ms * 0.001));
        for (int n = 0; n<x.length; n++) {
            xrms = (1-tav)*xrms + tav*(x[n]*x[n]);
        }
        return xrms;
    }
    
    public static double[] RmsStereo(double[] data) {
        double[] acc = new double[2];
        acc[0] = 0;
        acc[1] = 0;
        for (int i = 0; i < data.length; i += 2) {
            acc[0] += data[i]   * data[i];
            acc[1] += data[i+1] * data[i+1];            
        }
        acc[0] /= (data.length/2.0);
        acc[1] /= (data.length/2.0);
        acc[0] = Math.sqrt(acc[0]);
        acc[1] = Math.sqrt(acc[1]);
        return acc;
    }
    
    static double[] xrms = new double[2];
    public static double[] RmsStereo(double[] x, double tAvg_ms, double sampleRate ) {
        double tav = 1.0 - Math.exp(-2.2/sampleRate/(tAvg_ms * 0.001));
        for (int n = 0; n<x.length; n+=2) {
            xrms[0] = (1-tav)*xrms[0] + tav*(x[n]*x[n]);
            xrms[1] = (1-tav)*xrms[1] + tav*(x[n+1]*x[n+1]);
        }
        return xrms;
    }
}
