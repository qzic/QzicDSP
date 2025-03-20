/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qzic.dsp.util;

import java.nio.*;

/**
 *
 * @author Quentin
 */
public class ConvertData {

    final static int SHORT_BYTES = Short.SIZE / 8;
    final static int INTEGER_BYTES = Integer.SIZE / 8;
    static int intSample;
    static short shortSample;

    //--------------------------------------------------------------------------------------------------------------------------    
    // Converts a buffer of 32bit signed little endian PCM audio bytes to a AUDIO buufer of fractional double values between +/- 1.0
    //--------------------------------------------------------------------------------------------------------------------------
    public static void int32BytesToAudioArray(byte[] source, double[] dest) {
        intSample = 0;
        for (int cnt = 0; cnt < dest.length; cnt++) {
            intSample = (source[cnt * INTEGER_BYTES + 3]) << 24;
            intSample |= (source[cnt * INTEGER_BYTES + 2]) << 16 & 0xFF0000;
            intSample |= (source[cnt * INTEGER_BYTES + 1]) << 8 & 0xFF00;
            intSample |= (source[cnt * INTEGER_BYTES + 0]) & 0xFF;
            dest[cnt] = (double) intSample / Integer.MAX_VALUE;
        }
    }

    public static void short16BytesToAudioArray(byte[] source, double[] dest) {
        shortSample = 0;
        int cnt;
        try {
            for (cnt = 0; cnt < dest.length; cnt++) {
                shortSample |= (source[cnt * SHORT_BYTES + 1]) << 8;
                shortSample |= (source[cnt * SHORT_BYTES + 0]) & 0xFF;
                dest[cnt] = (double) shortSample / Short.MAX_VALUE;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // Converts an AUDIO buffer of fractional double values between +/- 1.0 to a buffer of 32bit signed PCM little ednian audio bytes. 
    //--------------------------------------------------------------------------------------------------------------------------
    static public void audioToInt32ByteArray(double[] source, byte[] dest) {
        int intSample;
        for (int cnt = 0; cnt < source.length; cnt++) {
            intSample = (int) (source[cnt] * Integer.MAX_VALUE);
            dest[cnt * INTEGER_BYTES + 0] = (byte) (intSample & 0xFF);
            dest[cnt * INTEGER_BYTES + 1] = (byte) ((intSample >> 8) & 0xFF);
            dest[cnt * INTEGER_BYTES + 2] = (byte) ((intSample >> 16) & 0xFF);
            dest[cnt * INTEGER_BYTES + 3] = (byte) ((intSample >> 24) & 0xFF);
        }
    }

    static public void audioToShort16ByteArray(double[] source, byte[] dest) {
        short shortSample;
        for (int cnt = 0; cnt < source.length; cnt++) {
            shortSample = (short) (source[cnt] * Short.MAX_VALUE);
            dest[cnt * SHORT_BYTES + 0] = (byte) (shortSample & 0xFF);
            dest[cnt * SHORT_BYTES + 1] = (byte) ((shortSample >> 8) & 0xFF);

        }
    }

    public static double[] int32ToAudioArray(int[] source) {
        double[] dest = new double[source.length];
        for (int i = 0; i < source.length; i++) {
            dest[i] = (double) source[i] / Integer.MAX_VALUE;
        }
        return dest;
    }

    public static int[] audioToInt32Array(double[] fractionalSource) {
        int[] dest = new int[fractionalSource.length];
        for (int i = 0; i < fractionalSource.length; i++) {
            dest[i] = (int) (fractionalSource[i] * Integer.MAX_VALUE);
        }
        return dest;
    }

    public static byte[] int32ToByteArray(int[] intArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length * INTEGER_BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(intArray);
        byte[] array = byteBuffer.array();
        return array;
    }

    public static int[] byteToInt32Array(byte[] byteArray) {
        IntBuffer intBuf = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return array;
    }

//    
//    //--------------------------------------------------------------------------------------------------------------------------    
//    // Converts a buffer of 32bit signed little endian PCM audio bytes to a buufer of fractional double values between +/- 1.0
//    //--------------------------------------------------------------------------------------------------------------------------
    public static double[] ByteIntArraytoFractionalDoubleArray(byte[] source) {
        IntBuffer intBuf = ByteBuffer.wrap(source).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        double[] dest = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = (double) array[i] / Integer.MAX_VALUE;
        }
        return dest;
    }

    public static int[] toIntArray(double[] fractionalSource) {
        int[] dest = new int[fractionalSource.length];
        for (int i = 0; i < fractionalSource.length; i++) {
            dest[i] = (int) (fractionalSource[i] * Integer.MAX_VALUE);
        }
        return dest;
    }

    public static byte[] toByteArray(int[] intArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length * INTEGER_BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(intArray);
        byte[] array = byteBuffer.array();
        return array;
    }

    public static byte[] toByteArray(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
//    
//    
//    //--------------------------------------------------------------------------------------------------------------------------    
//    // Converts a buffer of 16bit signed little endian PCM audio bytes to a buffer of fractional double values between +/- 1.0
//    //--------------------------------------------------------------------------------------------------------------------------

    public static double[] ByteShortArraytoFractionalDoubleArray(byte[] source) {
        ShortBuffer shortBuf = ByteBuffer.wrap(source).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] array = new short[shortBuf.remaining()];
        shortBuf.get(array);
        double[] dest = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = (double) array[i] / Short.MAX_VALUE;
        }
        return dest;
    }

    public static Short[] toShortArray(double[] fractionalSource) {
        Short[] dest = new Short[fractionalSource.length];
        for (int i = 0; i < fractionalSource.length; i++) {
            dest[i] = (short) (fractionalSource[i] * Short.MAX_VALUE);
        }
        return dest;
    }

    public static byte[] toByteArray(short[] shortArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(shortArray.length * SHORT_BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(shortArray);
        byte[] array = byteBuffer.array();
        return array;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // Converts a buffer of fractional double values between +/- 1.0 to a buffer of 32bit signed PCM little ednian audio bytes. 
    //--------------------------------------------------------------------------------------------------------------------------
    public static byte[] DoubleArraytoIntegerByteArray(double[] fractionalSource) {
        int[] array = new int[fractionalSource.length];
        for (int i = 0; i < fractionalSource.length; i++) {
            array[i] = (short) (fractionalSource[i] * Integer.MAX_VALUE);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * INTEGER_BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(array);
        byte[] dest = byteBuffer.array();
        return dest;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // Converts a buffer of fractional double values between +/- 1.0 to a buffer of 16bit signed PCM little ednian audio bytes. 
    //--------------------------------------------------------------------------------------------------------------------------
    public static byte[] DoubleArraytoShortByteArray(double[] fractionalSource) {
        short[] array = new short[fractionalSource.length];
        for (int i = 0; i < fractionalSource.length; i++) {
            array[i] = (short) (fractionalSource[i] * Short.MAX_VALUE);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * SHORT_BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(array);
        byte[] dest = byteBuffer.array();
        return dest;
    }

}
