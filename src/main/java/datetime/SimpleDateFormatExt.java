package datetime;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatExt extends SimpleDateFormat{
    public final static String UNIXSTAMP = "_";
    private String pattern;

    public SimpleDateFormatExt(String pattern) {
        super(pattern);
        this.pattern = pattern;
    }

    @Override
    public Date parse(String text) throws ParseException {
        if(pattern.equals(UNIXSTAMP))
            return new Date((long) (Integer.parseInt(text)) * 1000);
        return super.parse(text);
    }

    public String formatExt(Date date){
        if(pattern.equals(UNIXSTAMP))
            return (int)(date.getTime() / 1000) + "";
        return super.format(date);
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormatExt simpleUnix = new SimpleDateFormatExt(SimpleDateFormatExt.UNIXSTAMP);
        System.out.println(OneTimestamp.formatter1.formatExt(simpleUnix.parse("1651356000")));
    }
}
