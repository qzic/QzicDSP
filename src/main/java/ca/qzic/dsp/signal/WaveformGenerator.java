/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.signal;

import ca.qzic.dsp.util.*;
import static java.lang.System.out;
import java.util.*;

/**
 *
 * @author Quentin
 */
public class WaveformGenerator implements Command {

    public enum WAVE_TYPE {
        SIN,
        SQUARE,
        SAW,
        PULSE
    }

    public enum CHANNELS {
        LEFT,
        RIGHT,
        BOTH,
        MONO
    }

    private final double TWO_PI = 2.0 * Math.PI;
    private volatile double amplitude;
    private WAVE_TYPE waveType;
    private long sampleCnt = 0;
    private final double AMPLITUDE_SCALING = Math.pow(10.0, 9.0 / 20.0);  // HiFiberry puts out +9dBv for a fullo scale sine wave
    private double samplesPerCycle;
    private double dutyCycle;
    private int dataStart, dataInc;
    private CHANNELS channels;
    private boolean enabled = false;
    private boolean bypassed = true;
    private double sampleRate;
    private volatile double frequency;
    private boolean pulseState = false;

    public WaveformGenerator(double sampleRate_, WAVE_TYPE waveType_, double frequency_, double amplitudedB, CHANNELS channel_) {
        sampleRate = sampleRate_;
        waveType = waveType_;
        frequency = frequency_;
        dutyCycle = 0.50;
        amplitude = Math.pow(10.0, amplitudedB / 20.0) / AMPLITUDE_SCALING;
        if (amplitude > 1.0) {
            amplitude = 1.0;
        }

        channels = channel_;
        switch (channels) {
            case LEFT:
            case BOTH:
                dataStart = 0;
                dataInc = 2;
                break;
            case RIGHT:
                dataStart = 1;
                dataInc = 2;
                break;
            case MONO:
                dataStart = 0;
                dataInc = 1;
        }
        samplesPerCycle = (int) Math.floor(sampleRate / frequency) * dataInc;

//        out.println("frequency = " + frequency + ", samplesPerCycle = " + samplesPerCycle);
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency_) {
        this.frequency = frequency_;
        samplesPerCycle = (int) Math.round(sampleRate / frequency) * dataInc;
        sampleCnt = 0;
//       out.println("frequency = " + frequency + ", samplesPerCycle = " + samplesPerCycle);
    }

    public CHANNELS getChannels() {
        return channels;
    }

    public void setChannels(CHANNELS channels) {
        this.channels = channels;
    }

    public boolean isBypassed() {
        return bypassed;
    }

    public void setBypassed(boolean bypassed) {
        this.bypassed = bypassed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAmplitude(double amplitudedB) {
        amplitude = Math.pow(10.0, amplitudedB / 20.0) / AMPLITUDE_SCALING;
        if (amplitude > 1.0) {
            amplitude = 1.0;
        }
    }

    public void setWaveType(WAVE_TYPE waveType) {
        this.waveType = waveType;
    }

    public WAVE_TYPE getWaveType() {
        return waveType;
    }

    public void setDutyCycle(double dutyCyclePercent) {
        this.dutyCycle = dutyCycle/100.0;
    }

    public double getDutyCycle() {
        return dutyCycle*100.0;
    }

    @Override
    public boolean execute(double[] data) {
        if (bypassed) {
            return true;
        } else if (!enabled) {
            Arrays.fill(data, 0.0);
            return false;
        }
        switch (waveType) {
            case SIN:
                for (int i = dataStart; i < data.length; i += dataInc) {
                    double inc = ((sampleCnt + i) % samplesPerCycle) / samplesPerCycle;
//                    out.println("inc = " + inc);
                    data[i] = Math.sin(TWO_PI * inc) * amplitude;
                    if (channels == CHANNELS.BOTH) {
                        data[i + 1] = data[i];
                    }
//                    out.println("data[" + i + "] = " + data[i]);
                }
                sampleCnt += data.length;
                break;

            case SQUARE:
                for (int i = dataStart; i < data.length; i += dataInc) {
                    if ((((sampleCnt + i) % (samplesPerCycle / 2) / samplesPerCycle / 2)) == 0) {
                        if (pulseState) {
                            pulseState = false;
                        } else {
                            pulseState = true;
                        }
                    }
                    data[i] = (pulseState ? 1.0 : -1.0) * amplitude;

//                    out.println("data[" + i + "] = " + data[i]);
                    if (channels == CHANNELS.BOTH) {
                        data[i + 1] = data[i];
                    }
                }
                break;

            case SAW:
                break;

            case PULSE:
                for (int i = dataStart; i < data.length; i += dataInc) {
                    if ((((sampleCnt + i) % (samplesPerCycle / 2) / samplesPerCycle / 2)) == 0) {
                        if (pulseState) {
                            pulseState = false;
                        } else {
                            pulseState = true;
                        }
                    }
                    data[i] = (pulseState ? 1.0 : 0.0) * amplitude;

//                    out.println("data[" + i + "] = " + data[i]);
                    if (channels == CHANNELS.BOTH) {
                        data[i + 1] = data[i];
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public boolean execute(int[] data) {
        if (bypassed) {
            return true;
        } else if (!enabled) {
            Arrays.fill(data, 0);
            return false;
        }
        return true;
    }

}
