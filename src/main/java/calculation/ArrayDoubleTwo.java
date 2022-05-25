package calculation;

public class ArrayDoubleTwo {
    public static double euclidean(double[] vec1, double[] vec2){
        double sum = 0;
        for(int i = 0; i < vec1.length; i++){
            sum += Math.pow(vec1[i] - vec2[i], 2);
        }
        return Math.sqrt(sum);
    }

    //正右方向为0，沿着逆时针角度增加
    //返回值在[0, 360]
    //输入为[x, y]
    public static double heading(double[] from, double[] to){
        if(from[0] == to[0]){
            if(from[1] < to[1])
                return 90;
            else if (from[1] > to[1])
                return 270;
            throw new ArithmeticException();
        }
        double ratio = (to[1] - from[1]) / (to[0] - from[0]);
        double angle = Math.atan(ratio) / Math.PI * 180;
        if(from[0] > to[0])
            angle += 180;
        return angle < 0 ? angle + 360 : angle;
    }

    public static void main(String[] args) {
        System.out.println(euclidean(new double[]{3601447.5352882487,3658770.8400572}, new double[]{3601385.353686755,3659028.8287786576}));

//        double[] from = {0,0};
//        double[] to = {0,1};
//        System.out.println(heading(from, to));
    }
}
