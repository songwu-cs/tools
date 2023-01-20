package angle;


import java.util.Map;

//所有角度必须在[0, 360]之间
public class Angle {
    //角度差限定在[0, 180]
    public static double difference(double a1, double a2){
        double diff = Math.abs(a1 - a2);
        return diff <= 180 ? diff : (360 - diff);
    }

    //due north as 0 degrees
    public static double heading(double fromLon, double fromLat, double toLon, double toLat){
        if(fromLon == toLon && fromLat == toLat)
            return 0;
        double deltaX = toLat - fromLat;
        double deltaY = toLon - fromLon;

        if (deltaX == 0)
            return deltaY >= 0 ? 90 : 270;

        double theta = Math.atan(deltaY / deltaX);
        double thetaActual = theta;
        if (deltaX > 0 && theta < 0)
            thetaActual = 2 * Math.PI + theta;
        else if (deltaX < 0)
            thetaActual = Math.PI + theta;

        return thetaActual / Math.PI * 180;
    }

    public static void main(String[] args) {
//        System.out.println(difference(350, 20));

        System.out.println(heading(0,0,1,1));
        System.out.println(heading(0,0,-1,1));
        System.out.println(heading(0,0,-1,-1));
        System.out.println(heading(0,0,1,-1));
        System.out.println(heading(0,0,1,0));
        System.out.println(heading(0,0,-1,0));

    }
}
