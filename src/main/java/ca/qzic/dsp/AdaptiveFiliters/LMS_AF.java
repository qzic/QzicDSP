package ca.qzic.dsp.AdaptiveFiliters;

import static java.lang.System.out;
import java.util.Arrays;
// import ca.qzic.dsp.*;

class LMS_AF {
    double filterCoefs[][];
    double filterData[];
    static int L;
    static double err;
    double dotP;

    // =================================================================================================
    // =================================================================================================
    public LMS_AF(int FIR_Length) {
        L = FIR_Length;
        filterCoefs =  new double[1][L];
        Arrays.fill(filterData, 0);
        dotP = 0;
    }

    // =================================================================================================
    // =================================================================================================
    public void process(double lambda, boolean converge, int start, int length, double feData[], double neData[],
        double errData[]) {
        double delta;

        if (start + length - 1 >= feData.length || start + length - 1 >= neData.length) {
            out.printf("LMS pointer out of range: start %d, end %d, feDatalen %d, neDatalen %d\n", start, start
                    + length, feData.length, neData.length);
            return;
        }
        // out.printf("converge %b, lambda %1.8f, start %d, end %d, feDatalen %d\n",converge,lambda,start,start+length,feData.length);

        if (converge) {
            for (int cnt = start; cnt < start + length; cnt++) {
                // Add new input data to the filter
                System.arraycopy(filterData, 0, filterData, 1, L - 1);
                filterData[0] = feData[cnt];
                errData[cnt] = neData[cnt] - dotProduct(filterCoefs[0], filterData);

                // Update the LMS filter coefficients
                final double max = 0.015625; // pow(2,-6)
                delta = errData[cnt] * lambda;
                if (delta > max) {
                    delta = max;
                }
                if (delta < -max) {
                    delta = -max;
                }
                for (int i = 0; i < L; i++) {
                    filterCoefs[0][i] += filterData[i] * delta;
                }
            }
        } else {
            for (int cnt = start; cnt < start + length; cnt++) {
                // Shift and then add new input data to the filter
                System.arraycopy(filterData, 0, filterData, 1, L - 1);
                filterData[0] = feData[cnt];
                errData[cnt] = neData[cnt] - dotProduct(filterCoefs[0], filterData);
            }
        }
    }

    // =================================================================================================
    // =================================================================================================
    public void process2(double lambda, boolean converge, int start, int length, double feData[], double neDataIir[],
            double neData[], double errData[]) {
        double delta;
        double dotP;
        if (start + length - 1 >= feData.length || start + length - 1 >= neData.length) {
            out.printf("LMS pointer out of range: start %d, end %d, feDatalen %d, neDatalen %d\n", start, start
                    + length, feData.length, neData.length);
            return;
        }
        // out.printf("converge %b, lambda %1.8f, start %d, end %d, feDatalen %d\n",converge,lambda,start,start+length,feData.length);

        if (converge) {
            for (int cnt = start; cnt < start + length; cnt++) {
                // Add new input data to the filter
                System.arraycopy(filterData, 0, filterData, 1, L - 1);
                filterData[0] = feData[cnt];
                dotP = dotProduct(filterCoefs[0], filterData);
                errData[cnt] = neData[cnt] - dotP;

                // Update the LMS filter coefficients
                final double max = 0.015625; // pow(2,-6)
                delta = (neDataIir[cnt] - dotP) * lambda;
                if (delta > max) {
                    delta = max;
                }
                if (delta < -max) {
                    delta = -max;
                }
                for (int i = 0; i < L; i++) {
                    filterCoefs[0][i] += filterData[i] * delta;
                }
            }
        } else {
            for (int cnt = start; cnt < start + length; cnt++) {
                // Shift and then add new input data to the filter
                System.arraycopy(filterData, 0, filterData, 1, L - 1);
                filterData[0] = feData[cnt];
                errData[cnt] = neData[cnt] - dotProduct(filterCoefs[0], filterData);
            }
        }
    }


    // =================================================================================================
    // =================================================================================================
    static double dotProduct(double[] v1, double[] v2) {
        double result = 0;

        if (v1.length <= v2.length) {
            for (int i = 0; i < v1.length; i++) {
                result += v1[i] * v2[i];
            }
            return result;
        } else {
            for (int i = 0; i < v2.length; i++) {
                result += v1[i] * v2[i];
            }
            return result;
        }
    }
}
