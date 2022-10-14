package calculation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class UnitString {
    public static String subset(@NotNull String input, String splitter, int... indices){
        String[] parts = input.split(splitter);
        List<String> ls = new ArrayList<>();
        for(int i : indices)
            ls.add(parts[i]);
        return String.join(splitter, ls);
    }

    //n从1开始计数
    public static int nthIndexOf(@NotNull String s, String goal, int n){
        int counter = 0;
        for(int i = 0; i < s.length(); i++){
            if(s.substring(i, Math.min(s.length(), i + goal.length())).equals(goal)){
                if(++counter == n)
                    return i;
                i += goal.length() - 1;
            }
        }
        return -1;
    }

    //requires an even number of quotes
    public static String replaceCommaInQuote(String source, char oldChar, char newChar){
        char[] chars = source.toCharArray();
        boolean firstQuoteAppeared = false;
        for(int i = 0; i < chars.length; i++){
            if(firstQuoteAppeared && chars[i] == oldChar)
                chars[i] = newChar;
            else if (chars[i] == '"') {
                firstQuoteAppeared = !firstQuoteAppeared;
            }
        }
        return new String(chars);
    }

    public static void main(String[] args) {
//        System.out.println(nthIndexOf("", "ab", 4));
//        System.out.println(subset("A,B,C,D", ",", 0, 2,3));
    }
}
