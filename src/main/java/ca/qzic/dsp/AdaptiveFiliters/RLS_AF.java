package ca.qzic.dsp.AdaptiveFiliters;


import org.ejml.simple.*;
import static java.lang.System.out;

class RLS_AF {
    double filterCoefs[][];
    static SimpleMatrix Mat_W0, Mat_X, Mat_A, Mat_G, Mat_k, Mat_kL1;
    static double K[] = {1.5, 2.5, 0, 0, 1, 1};
    static double lambda,delta,alpha,xL1, eta, f, rf1, gammaL11, gammaL1;
    static double psif, rb1f, rb1s, psis, psierr, psi1, psi2, psi5;
    static double rf, rb, psi, den, rb1err, gamma1s, gammas, gamma1f, gamma1err, gamma1inv, gammaerr;
    static double b1, b2, epsilon;
    static int L,iteration;

    public RLS_AF(int firLength, double lambda, double delta) {
        
        L = firLength;
        filterCoefs = new double[1][L];
        
        Mat_W0 = new SimpleMatrix(L, 1); // coefficients
        Mat_X = new SimpleMatrix(L, 1);
        Mat_A = new SimpleMatrix(L, 1);
        Mat_G = new SimpleMatrix(L, 1);
        Mat_k = new SimpleMatrix(L, 1);
        Mat_kL1 = new SimpleMatrix(L + 1, 1);

        // Mat_W0.zero();
        Mat_X.zero();
        Mat_A.zero();
        Mat_G.zero();
        Mat_k.zero();
        Mat_k.zero();
        Mat_kL1.zero();
        
        this.lambda = lambda; 
        this.delta = delta;
        this.alpha = delta * Math.pow(lambda, L);
    }

    /*============================================================================================
    % Chapter 11, page 17
    function[W,dhat,e] = ftf1(lambda,delta,W0,x,d);
    % This function adapts the coefficients of an FIR filter
    % using the first version of the fast RLS transversal filter
    % given in Table 11.1.
    %
    % Input parameters:
    %
    % lambda = forgetting factor
    % delta = soft constrained initialization constant
    % W0 = initial values of coefficients (L x 1)
    % x = input signal (num_iter x 1)
    % d = desired response signal (num_iter x 1)
    %
    % Output of program:
    %
    % W = Evolution of coefficients (L x (num_iter + 1))
    % dhat = output of adaptive filter (num_iter x 1)
    % e = error of adaptive filter (num_iter x 1)
    %

    %
    ================================================================================================*/
    public void process(boolean converge,int start, int end, double feData[], double neData[], double err[]) {

//      init coef matrix from external coefficient array. This could be initialized by a LMS run
//      OR a previous RLS converge on a small area and we ant to filter a larger area with these coefficients
        for (int i = 0; i < L; i++) Mat_W0.set(i, 0, filterCoefs[iteration][L - 1 - i]);
       
        // Fill out X matrix with data to process
        for (int i = 0; i < L - 1; i++) Mat_X.set(L - 2 - i, 0, feData[start + i]);
        
//        if(converge) out.println("RLS AF converging from " + start + " to " + end);
        // Recursions
        for (int n = start; n < end; n++) {
            if (converge) { // update coefficients
//                out.println("start converge at  " + start);
                xL1 = Mat_X.get(L - 1, 0);
                eta = feData[n + L - 1] - Mat_A.transpose().dot(Mat_X);
                Mat_A.set(Mat_A.plus(eta, Mat_k));
                f = feData[n + L - 1] - Mat_A.transpose().dot(Mat_X);
            }
            // shift matrix X right and insert next sample.
            Mat_X.insertIntoThis(1, 0, Mat_X.extractMatrix(0, L - 1, 0, 1));  // N.B.extract: set the numRows & numCols you want + 1 
            Mat_X.set(0, 0, feData[n + L - 1]);
            if (converge) { // update coefficients
                alpha = lambda * alpha + eta * f;
                rf = f / alpha;
                Mat_kL1.set(0, 0, rf);
                Mat_kL1.insertIntoThis(1, 0, Mat_k.minus(Mat_A.scale(rf)));
                rb = Mat_kL1.get(L, 0);
                psi = xL1 - Mat_G.transpose().dot(Mat_X);
                den = 1 / (1 - psi * rb);
                Mat_k.set((Mat_kL1.extractMatrix(0, L, 0, 1).plus(rb, Mat_G)).scale(den));
                Mat_G.set(Mat_G.plus(psi, Mat_k));
                epsilon = neData[n] - Mat_W0.transpose().dot(Mat_X);
                Mat_W0.set(Mat_W0.plus(epsilon, Mat_k));
            }
            err[n] = neData[n] - Mat_W0.transpose().dot(Mat_X);
        }
        for (int n = 0; n < L; n++) filterCoefs[iteration][L - 1 - n] = Mat_W0.get(n, 0);
        
        // if we not converging then coeffs are not updated
        // also If we have converged and are processing other areas not converging, use the coeffs at index 0
        if(converge) iteration++;  
    }

    public void resetRLS(){
        this.iteration = 0;
    }
}
