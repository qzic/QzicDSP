/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.filter;

import ca.qzic.dsp.util.*;
//import static java.lang.System.out;

/**
 *
 * @author Quentin
 */
public class ShelvingFilterStereo implements Command {

    public enum HS_LS {
        HS, LS
    };
    double hl_S;
    double dBgain, lastDBgain, Wc;
    double V0, H0, c;
    double[] y;
    double xx;
    double xh_new, ap_y;
    boolean firstRun;
    double xh_L, xh_R;

    public double getdBgain() {
        return dBgain;
    }

    public void setdBgain(double dBgain) {
        this.dBgain = dBgain;
        V0 = Math.pow(10,dBgain / 20);
        H0 = V0 - 1;
        if (dBgain >= 0.0) {
            c = (Math.tan(Math.PI * Wc) - 1) / (Math.tan(Math.PI * Wc) + 1);     // % boost
        } else {
            c = (Math.tan(Math.PI * Wc) - V0) / (Math.tan(Math.PI * Wc) + V0);   // % cut
        }
//        out.println("gain = " + dBgain + ", VO = " + V0 + ", c = " + c);
    }

    public ShelvingFilterStereo(float sampleRate, int cornerFreq, HS_LS hl_S, int dBgain) {
        this.dBgain = lastDBgain = dBgain;
        Wc = cornerFreq / sampleRate;
        V0 = Math.pow(10, dBgain / 20);
        H0 = V0 - 1.0;
        xh_L = 0;
        xh_R = 0;

        if (hl_S == HS_LS.LS) {
            this.hl_S = 1;
        } else {
            this.hl_S = -1;
        }
        if (dBgain >= 0.0) {
            c = (Math.tan(Math.PI * Wc) - 1) / (Math.tan(Math.PI * Wc) + 1);     // % boost
        } else {
            c = (Math.tan(Math.PI * Wc) - V0) / (Math.tan(Math.PI * Wc) + V0);   // % cut
        }
//        out.println("> gain = " + dBgain + ", VO = " + V0 + ", c = " + c);
    }

    @Override
    public boolean execute(double[] x) {
        // Left channel
        for (int n = 0; n < x.length; n += 2) {
            xh_new = x[n] - c * xh_L;
            ap_y = c * xh_new + xh_L;
            xh_L = xh_new;
            x[n] += 0.5 * H0 * (x[n] + hl_S * ap_y);     // % hl_S = -1.0 for HS, 1.0 for LS
        }
        // Right Channel
        for (int n = 1; n < x.length; n += 2) {
            xh_new = x[n] - c * xh_R;
            ap_y = c * xh_new + xh_R;
            xh_R = xh_new;
            x[n] += 0.5 * H0 * (x[n] + hl_S * ap_y);     // % hl_S = -1.0 for HS, 1.0 for LS
        }
        return true;
    }

    @Override
    public boolean execute(int[] x) {
        // Left chsnnel
        for (int n = 0; n < x.length; n += 2) {
            xx = (double) x[n] / Integer.MAX_VALUE;
            xh_new = xx - c * xh_L;
            ap_y = c * xh_new + xh_L;
            xh_L = xh_new;
            x[n] = (int) ((0.5 * H0 * (xx + hl_S * ap_y) + xx) * Integer.MAX_VALUE);
        }
        // Right Channel
        for (int n = 1; n < x.length; n += 2) {
            xx = (double) x[n] / Integer.MAX_VALUE;
            xh_new = xx - c * xh_R;
            ap_y = c * xh_new + xh_R;
            xh_R = xh_new;
            x[n] = (int) ((0.5 * H0 * (xx + hl_S * ap_y) + xx) * Integer.MAX_VALUE);
        }
        return true;
    }

}

/*
 * function y = lowshelving (x, Wc, G) % y = lowshelving (x, Wc, G) % Author: M. Holters % Applies a low-frequency shelving filter to the input signal x. % Wc
 * is the normalized cut-off frequency 0<Wc<1, i.e. 2*fc/fS % G is the gain in dB % %--------------------------------------------------------------------------
 * % This source code is provided without any warranties as published in % DAFX book 2nd edition, copyright Wiley & Sons 2011, available at %
 * http://www.dafx.de. It may be used for educational purposes and not % for commercial applications without further permission.
 * %--------------------------------------------------------------------------
 *
 * V0 = 10^(G/20); H0 = V0 - 1; if G >= 0 c = (tan(pi*Wc/2)-1) / (tan(pi*Wc/2)+1); % boost else c = (tan(pi*Wc/2)-V0) / (tan(pi*Wc/2)+V0); % cut end; xh = 0;
 * for n=1:length(x) xh_new = x(n) - c*xh; ap_y = c * xh_new + xh; xh = xh_new; y(n) = 0.5 * H0 * (x(n) + ap_y) + x(n); // change to (x(n) - ap_y) for HS;
 *
 */
