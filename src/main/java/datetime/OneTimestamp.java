package datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OneTimestamp {
    public static final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String add(String date, int hour, int minute, int second, SimpleDateFormat format){
        try {
            return format.format(new Date(format.parse(date).getTime() + 1000 * (hour * 3600 + minute * 60 + second)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(add("2022-04-02 20:00:20", 2, 10, 20, formatter1));
    }
}
