/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.Delay;

/**
 *
 * @author Quentin
 */
public class Delay {
    double buffer[];
    int maxDelay;
    int writePos, readPos;
    
    public Delay(int maxDelay, int delay){
        buffer = new double[maxDelay];
        writePos = 0;
        readPos = delay;
     }
    
    // by block ToDo
    public double[] process(double[] x) {
        return x;
    }
    
    // by sample
    public double process(double x){
        buffer[writePos] = x;
        writePos = ((writePos += 1) >= buffer.length) ? 0 : writePos;
        
        double output = buffer[readPos];
        readPos = ((readPos += 1) >= buffer.length) ? 0 : readPos;
        
        return output;
    }
    
}
