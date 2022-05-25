package angle;


import java.util.Map;

//所有角度必须在[0, 360]之间
public class Angle {
    public static double difference(double a1, double a2){
        double diff = Math.abs(a1 - a2);
        return diff <= 180 ? diff : (360 - diff);
    }

    public static void main(String[] args) {
        System.out.println(difference(350, 20));
    }
}
