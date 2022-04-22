package datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TwoTimestamp {
    public static final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static double diffInSeconds(String t1, String t2, SimpleDateFormat formatter){
        long l1 = 0;
        long l2 = 0;
        try {
            l1 = formatter.parse(t1).getTime();
            l2 = formatter.parse(t2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (l1 - l2) / 1000.0;
    }

    public static double diffInSeconds(Date d1, Date d2){
        return (d1.getTime() - d2.getTime()) / 1000.0;
    }

    public static void main(String[] args) throws ParseException {
    }
}
