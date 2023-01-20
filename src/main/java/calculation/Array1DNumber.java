package calculation;

import java.util.Arrays;

public class Array1DNumber {
    private static double BadDouble = -1;

    //不接受0或者负数
    //空数组或者null返回BadDouble
    public static double entropy(int[] frequency){
        if(frequency == null || frequency.length == 0)
            return BadDouble;
        int sum = 0;
        for(int fre : frequency)
            sum += fre;

        double[] probs = new double[frequency.length];
        for(int i = 0; i < probs.length; i++)
            probs[i] = 1.0 * frequency[i] / sum;

        return entropy(probs);
    }

    public static int closest(double[] ds, double key){
        if(ds == null || ds.length == 0)
            return (int)BadDouble;

        int latPos = Arrays.binarySearch(ds, key);
        if(latPos < 0){
            latPos = -latPos - 1;
            int latPosRight = latPos;
            int latPosLeft = latPos - 1;
            if (latPosRight >= ds.length)
                latPos = ds.length - 1;
            else if (latPosLeft < 0) {
                latPos = 0;
            }else {
                double diffRight = ds[latPosRight] - key;
                double diffLeft = key - ds[latPosLeft];
                latPos = diffRight >= diffLeft ? latPosLeft : latPosRight;
            }
        }
        return latPos;
    }

    //不接受0或者负数
    //空数组或者null返回BadDouble
    public static double entropy(double[] probabilities){
        if(probabilities == null || probabilities.length == 0)
            return BadDouble;

        double answer = 0;
        double log2 = Math.log(2);
        for(double prob : probabilities)
            answer += -prob * Math.log(prob) / log2;
        return answer;
    }

    //null和空数组都返回0
    public static int sum(int[] integers){
        if(integers == null)
            return 0;
        int sum = 0;
        for(int integer : integers)
            sum += integer;
        return sum;
    }

    //null和空数组都返回0
    public static double sum(double[] doubles){
        if(doubles == null)
            return 0;
        double sum = 0;
        for(double d : doubles)
            sum += d;
        return sum;
    }



    public static void main(String[] args) {
        for(int i = 10; i <= 20; i++){
            System.out.println(i + "," + entropy(new int[]{2,i,9,8}));
        }
        for(int i = 10; i <= 20; i++){
            System.out.println(i + "," + entropy(new int[]{2,20,i,8}));
        }

    }
}
