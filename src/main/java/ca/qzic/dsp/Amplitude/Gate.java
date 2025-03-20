/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.Amplitude;

import ca.qzic.dsp.util.*;

/**
 *
 * @author Quentin
 */
public class Gate implements Command {

    private double sampleRate;
    private double frequency;
    private double dutyCycle;
    private int samplesPerCycle;
    private long sampleCnt = 0;
    private boolean enabled = false;

    public Gate(double sampleRate_, double frequency_, double dutyCycle_, boolean zeroCross) {
        sampleRate = sampleRate_;
        frequency = frequency_;
        dutyCycle = dutyCycle_;
        samplesPerCycle = (int) Math.floor(sampleRate / frequency);
    }

    @Override
    public boolean execute(double[] data) {
        if (!enabled) {
            return false;
        }
        for (int i = 0; i < data.length; i++) {
            data[i] *= (((sampleCnt + i) % samplesPerCycle) < (samplesPerCycle * dutyCycle) ? 1.0 : 0);
//            out.println("data[" + i + "] = " + data[i]);
        }
        sampleCnt += data.length;
        return true;
    }

    @Override
    public boolean execute(int[] data) {
        if (!enabled) {
            return false;
        }
        return true;
    }

}
