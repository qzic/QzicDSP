/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.qzic.dsp.Misc;


import static java.lang.Math.abs;
import static java.lang.Math.log10;
import org.eclipse.swt.graphics.*;

public class VuMeter {
    public static final byte METER_MAX = 127;
    public static final double METER_OFFSET = 10.0;     // Vu meter sensitivity increase .... 0VU = -METER_OFFSET dBFS 
    static int maxAngle = 41;
    Image p, vu;
    double angle = -maxAngle;
    double lastAngle = angle;
    float responseTime = 50;       // time to rise or fall full scale
//    float responseTime = 300;       // time to rise or fall full scale
    float responseDistance = 60;    // angle from -20 to 0 i.e. full scale

    public VuMeter() {
//        p = main.findViewById(R.id.pointer);
//        vu = main.findViewById(R.id.Vu);
//        p.setRotation(-maxAngle);
    }
//
//    public void init() {
//        float vuBot = vu.getY() + vu.getHeight();
//        Log.i("VU ", "VuMeter: Y top = " + vu.getY() + ", y bottom = " + vuBot +
//                ", pointer Y = " + p.getY() + ", pointer H = " + p.getHeight());
//        main.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                float h1 = vu.getHeight();
//                float h2 = p.getHeight()/2.0f;
//                p.setTranslationY(h1- h2);
//            }
//        });
//    }
//
//    public void setPointer(byte value) {
//        angle = mapdB(value);
//        double delta = angle - lastAngle;
//        lastAngle = angle;
//        long dur = (long) (responseTime * abs(delta) / responseDistance);
////        Log.i("VU ", "angle = "+ angle + ", delta = " + delta + ", dur = " + dur + ", value = " + value);
//        main.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                p.animate().rotation((float) angle).withEndAction(this).setDuration(dur).setInterpolator(new LinearInterpolator()).start();
//            }
//        });
//    }
//
//    int[] dbMap =    {-30, -20,  -10,  -6, -3, 0,   3, 5, 10};
//    int[] angleMap = {-35, -30,  -20,  -9,  3, 15, 31, 37, maxAngle};
//    float mapdB(byte input) {
//        double val = (double) input / METER_MAX; // make unsigned and convert to fraction.
//        float retVal = angleMap[1];
//        float dB =(float) (20 * log10(val) + METER_OFFSET);  // scaling for lower levels i.e. -10dBm
//        for (int i = 0; i < dbMap.length; i++) {
//            if(dB < dbMap[i]) {
//                if (i==0)
//                    retVal = angleMap[0];
//                else
//                    retVal = (float) linInterp(dbMap[i-1],angleMap[i-1], dbMap[i],angleMap[i],dB);
////                retV = angleMap[i];
////                Log.i(" VU ", "i="+ i + ", dB = " + dB + " dbMap = " + dbMap[i] + ". angle = " + retV );
//                break;
//            }
//        }
//        return retVal;
//    }
//
//    double linInterp(double x0, double y0, double x1, double y1, double x) {
//        return (y0*(x1-x) + y1*(x-x0))/(x1-x0);
//    }

}

