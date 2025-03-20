//import java.util.Arrays;
//import java.util.Random;
//
//// For echo cancellation noise input is the NE signal
////public class NoiseGeneration {
//public class FT_RLS {
//    public static void main(String[] args) {
//        // NOISE GENERATION
//        int M = 25;
//        int N = 1000;
//        int Delay = 3;
//        double lambda = 1.0;
//        double[] noise = new double[N];
//        double[] fnoise = new double[N];
//        double[][] weights = new double[M][N + 1];
//        double[] d = new double[N];
//        double epsilon = 0.00001;
//        double[][] w = new double[M][N];
//        double[] uvec = new double[M];
//        double[] e = new double[N];
//        double[][] wf = new double[M][1];
//        double[][] wb = new double[M][1];
//        double[] phi = new double[M];
//        double[] gamma = new double[1];
//        double[] epb = new double[M];
//        double[] epf = new double[M];
//        double[] efa = new double[N];
//        double[] ef = new double[N];
//        double[] eba = new double[N];
//        double[] eb = new double[N];
//        double NUM, DEM;
//
//
////		fnoise=filter([zeros(1 ,Delay) 1],1 ,noise);
////		[Num,Oen]=bulter(10,O.5);
////		fnoise =filter( Num, De n, foise );
////		fnoise =fnoiseistd (fnoise )*0.5;
////		weights=zeros(M,length(fnoise )+1);
////		d=signal+fnoise;
//
//        Random rand = new Random();
//        for (int i = 0; i < N; i++) {
//            noise[i] = rand.nextGaussian();
//        }
//
//        // Assuming filter function is defined elsewhere
//        fnoise = filter(new double[Delay + 1], noise);
//        fnoise = filter(new double[]{0, 1}, fnoise);
//        fnoise = Arrays.stream(fnoise).map(x -> x / std(fnoise) * 0.5).toArray();
//
//        // INITIALIZATION
//        Arrays.fill(w, 0);
//        Arrays.fill(uvec, 0);
//        Arrays.fill(e, 0);
//        Arrays.fill(wf[0], 0);
//        Arrays.fill(wb[0], 0);
//        Arrays.fill(phi, 0);
//        gamma[0] = 1;
//        Arrays.fill(epb, epsilon);
//        Arrays.fill(epf, epsilon);
//
//        // FILTERING ALGORITHM
//        for (int i = 1; i < fnoise.length; i++) {
//            // Forward A Priori Prediction Error
//            efa[i] = noise[i] - dotProduct(uvec, wf[i - 1]);
//            // Forward A Posterior Prediction Error
//            ef[i] = efa[i] * gamma[i - 1];
//            // MWLS Forward Error
//            epf[i] = lambda * epf[i - 1] + efa[i] * ef[i];
//            // Forward Weight Update
//            wf[i] = add(wf[i - 1], multiply(phi, ef[i]));
//            // phi1 update
//            phi = add(new double[]{0}, add(phi, divide(efa[i], lambda * epf[i - 1])));
//            // M+1 Conversion Factor
//            gamma[i] = gamma[i - 1] * (lambda * epf[i - 1] / epf[i]);
//            // Backward A Priori Prediction Error
//            eba[i] = lambda * epb[i - 1] * phi[M - 1];
//            // M Conversion Factor
//            double gammainv = 1 / gamma[i] - phi[M - 1] * eba[i];
//            gamma[i] = 1 / gammainv;
//            // Backward A Posterior Prediction Error
//            eb[i] = eba[i] * gamma[i];
//            // MWLS Backward Error
//            epb[i] = lambda * epb[i - 1] + eb[i] * eba[i];
//            // M Conversion Weight
//            double[] newvec = subtract(phi, multiply(phi[M - 1], new double[]{wb[i - 1]}));
//            phi = Arrays.copyOf(newvec, M);
//            // Backward Weight Update
//            wb[i] = add(wb[i - 1], multiply(phi, eb[i]));
//            // Update with New Sample of Input Data
//            uvec = prepend(noise[i], uvec);
//            // A Priori Joint-Estimation Error
//            e[i] = d[i] - dotProduct(uvec, w[i - 1]);
//            // A Posterior Joint-Estimation Error
//            e[i] = e[i] * gamma[i];
//            // Joint-Estimation Weight Update
//            w[i] = add(w[i - 1], multiply(e[i], phi));
//        }
//    }
//
//    // Helper methods for vector operations
//    private static double[] filter(double[] b, double[] a) {
//        // Implement filter logic here
//        return new double[a.length]; // Placeholder
//    }
//
//    private static double std(double[] array) {
//        // Implement standard deviation calculation here
//        return 1.0; // Placeholder
//    }
//
//    private static double dotProduct(double[] a, double[] b) {
//        double sum = 0;
//        for (int i = 0; i < a.length; i++) {
//            sum += a[i] * b[i];
//        }
//        return sum;
//    }
//
//    private static double[] add(double[] a, double[] b) {
//        double[] result = new double[a.length];
//        for (int i = 0; i < a.length; i++) {
//            result[i] = a[i] + b[i];
//        }
//        return result;
//    }
//
//    private static double[] subtract(double[] a, double[] b) {
//        double[] result = new double[a.length];
//        for (int i = 0; i < a.length; i++) {
//            result[i] = a[i] - b[i];
//        }
//        return result;
//    }
//
//    private static double[] multiply(double[] a, double b) {
//        double[] result = new double[a.length];
//        for (int i = 0; i < a.length; i++) {
//            result[i] = a[i] * b;
//        }
//        return result;
//    }
//
//    private static double[] prepend(double value, double[] array) {
//        double[] result = new double[array.length + 1];
//        result[0] = value;
//        System.arraycopy(array, 0, result, 1, array.length);
//        return result;
//    }
//
//    private static double[] divide(double a, double b) {
//        return new double[]{a / b};
//    }
//}
//
