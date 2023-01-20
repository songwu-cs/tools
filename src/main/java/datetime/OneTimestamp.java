package datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OneTimestamp {
    public static final SimpleDateFormatExt formatter1 = new SimpleDateFormatExt("yyyy-MM-dd HH:mm:ss");

    public static String add(String date, int hour, int minute, int second, SimpleDateFormatExt format){
        try {
            long add = 0;
            add += hour * (long)3600;
            add += minute * (long)60;
            add += second;
            add *= 1000;
            return format.formatExt(new Date(format.parse(date).getTime() + add));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(add("1950-01-01 00:00:00", 634032, 0, 0, formatter1));
//        System.out.println(formatter1.parse("1950-01-01 00:00:00").getTime());
        System.out.println((int) 1.5);

    }
}
