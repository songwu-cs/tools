package calculation;

import java.util.Arrays;

public class Array1DString {
    public static String firstNotNull(String... ss){
        for(String s : ss){
            if(s != null && (!s.equals("")))
                return s;
        }
        return null;
    }

    public static String firstNotNullProxy(int[] pos, String[] parts){
        String[] tmp = new String[pos.length];
        for(int i = 0; i < pos.length; i++){
            tmp[i] = parts[pos[i]];
        }
        return firstNotNull(tmp);
    }

    public static void main(String[] args) {
        String[] ss = new String[]{"", "", null, "a"};
        System.out.println(firstNotNull(ss));

        System.out.println(Arrays.toString(",,,".split(",", -1)));
    }
}
