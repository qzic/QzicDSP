package ca.qzic.dsp.Misc;

import static ca.qzic.dsp.Misc.VuMeter.METER_MAX;
import static ca.qzic.dsp.Misc.VuMeter.METER_OFFSET;
import static ca.qzic.dsp.Misc.VuMeter.maxAngle;
import java.awt.image.*;
import java.io.*;
import static java.lang.Math.abs;
import static java.lang.Math.log10;
import java.util.logging.*;
import javax.imageio.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class VU_Meter extends Canvas {

    public enum meterMode {
        GR, VU
    }
    static Composite p;
    static Image image;

    static int needleX;
    static int needleY;
    static int needleL;
    double r = 1.1, r1;
    static double maxGR = 2.4, minGR = 1.1, minVU = 2.4, maxVU = 0.65;
    meterMode mode;

    static String iFileVu = "vu_meter.jpg";
    static int maxAngle = 41;
    double angle = -maxAngle;
    double lastAngle = angle;
    float responseTime = 50;       // time to rise or fall full scale
//    float responseTime = 300;       // time to rise or fall full scale
    float responseDistance = 60;    // angle from -20 to 0 i.e. full scale

    public VU_Meter(Composite parent, int style, meterMode m) {
        super(parent, style);
        p = parent;
        mode = m;
        if (m == meterMode.GR) {
            r = minGR;
            r1 = minGR;
        } else {
            r = minVU;
            r1 = minVU;
        }

        try {
            BufferedImage vuImage = ImageIO.read(this.getClass().getResource("/Resources/" + iFileVu));
            ImageIO.write(vuImage, "jpg", new File(iFileVu));
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        Image vu = resize(new Image(p.getDisplay(), "vu_meter.jpg"), 2);
//        Image vu = new Image(p.getDisplay(), "vu_meter.jpg");

        needleX = vu.getBounds().width / 2 - 2;
        needleY = vu.getBounds().height - 10;
        needleL = (int) Math.floor(vu.getBounds().height * 0.6);
        // System.out.println(image.getBounds());
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                GC g = e.gc;
                g.drawImage(vu, 0, 0);
                int deltaY = needleY - (int) Math.floor(needleL * Math.sin(r));
                int deltaX = needleX + (int) Math.floor(needleL * Math.cos(r));
                // System.out.println(needleX + ", " +needleY+ ", " + deltaX + ", " + deltaY);
                g.drawLine(needleX, needleY, deltaX, deltaY);
                // image.dispose();
            }
        });
    }

    public void setPointer(VU_Meter vu,byte value) {
        double angle = mapdB(value);
        double delta = angle - lastAngle;
        lastAngle = angle;
        long dur = (long) (responseTime * abs(delta) / responseDistance);
//        Log.i("VU ", "angle = "+ angle + ", delta = " + delta + ", dur = " + dur + ", value = " + value);
        this.getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
//                p.animate().rotation((float) angle).withEndAction(this).setDuration(dur).setInterpolator(new LinearInterpolator()).start();
            vu.redraw();
            }
        });

    }

    public void setLevel(double dB) {
        if (dB > 20) {
            dB = 20;
        }
        if (dB < 0) {
            dB = 0;
        }
        r = ((maxGR - minGR) * dB / 20) + minGR;
    }

    public void setSigLevel(double dB) {
        double ki = 0.1, a1 = 1.0 - ki;
        switch (mode) {
            case GR:
                if (dB > 20) {
                    dB = 20;
                }
                if (dB < 0) {
                    dB = 0;
                }
                r = ki * (((maxGR - minGR) * dB / 20) + minGR) + a1 * r1;
                break;
            case VU:
                if (dB < -20) {
                    dB = -20;
                }
                if (dB > 3) {
                    dB = 3;
                }
                r = ki * (((maxVU - minVU) * (dB + 20) / 23) + minVU) + a1 * r1;
                break;
        }
        r1 = r;
        // System.out.println(dB + ", " + r);

    }

    public static void main(String[] args) {
        Display display = Display.getDefault();
        Shell aShell = new Shell(display);
        VU_Meter gr = new VU_Meter(aShell, 0, meterMode.GR);
        aShell.open();
        gr.setBounds(0, 0, 116, 64);
        aShell.pack();
        while (!aShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private Image resize(Image image, double scale) {
        int newWidth = (int) (image.getBounds().width / scale);
        int newHeight = (int) (image.getBounds().height / scale);

        Image scaled = new Image(Display.getDefault(), newWidth, newHeight);
        GC gc = new GC(scaled);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height,
                0, 0, newWidth, newHeight);
        gc.dispose();
        image.dispose(); // don't forget about me!
        return scaled;
    }

    int[] dbMap = {-30, -20, -10, -6, -3, 0, 3, 5, 10};
    int[] angleMap = {-35, -30, -20, -9, 3, 15, 31, 37, maxAngle};

    float mapdB(byte input) {
        double val = (double) input / METER_MAX; // make unsigned and convert to fraction.
        float retVal = angleMap[1];
        float dB = (float) (20 * log10(val) + METER_OFFSET);  // scaling for lower levels i.e. -10dBm
        for (int i = 0; i < dbMap.length; i++) {
            if (dB < dbMap[i]) {
                if (i == 0) {
                    retVal = angleMap[0];
                } else {
                    retVal = (float) linInterp(dbMap[i - 1], angleMap[i - 1], dbMap[i], angleMap[i], dB);
                }
//                retV = angleMap[i];
//                Log.i(" VU ", "i="+ i + ", dB = " + dB + " dbMap = " + dbMap[i] + ". angle = " + retV );
                break;
            }
        }
        return retVal;
    }

    double linInterp(double x0, double y0, double x1, double y1, double x) {
        return (y0 * (x1 - x) + y1 * (x - x0)) / (x1 - x0);
    }

}
