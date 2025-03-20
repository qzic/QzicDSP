/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.Amplitude;

import static java.lang.Math.abs;

/**
 *
 * @author Quentin
 */
public class Peak {
        public static double SimplePeak(double[] data) {
        double peak = 0;
        for (int i = 0; i < data.length; i++) {
            peak = abs(data[i]) > abs(peak) ? data[i] : peak;
        }
        return peak;
    }
    
}
