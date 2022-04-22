package calculation;

public class ArrayDoubleTwo {
    public static double euclidean(double[] vec1, double[] vec2){
        double sum = 0;
        for(int i = 0; i < vec1.length; i++){
            sum += Math.pow(vec1[i] - vec2[i], 2);
        }
        return Math.sqrt(sum);
    }

    public static void main(String[] args) {
        System.out.println(euclidean(new double[]{0,0}, new double[]{3,4}));
    }
}
