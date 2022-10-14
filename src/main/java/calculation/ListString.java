package calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public class ListString {
    public static String mode(List<String> ls){
        if(ls == null || ls.size() == 0)
            return null;
        Map<String, Integer> freq = new HashMap<>();
        for(String s : ls){
            if(freq.containsKey(s))
                freq.put(s, freq.get(s) + 1);
            else
                freq.put(s, 1);
        }

        int countMax = 0;
        String mode = "";
        for(String s : freq.keySet()){
            if(freq.get(s) >= countMax){
                countMax = freq.get(s);
                mode = s;
            }
        }
        return mode;
    }

    public static int indexStartWith(List<String> ss, String prefix){
        for(int i = 0; i < ss.size(); i++){
            if(ss.get(i).startsWith(prefix))
                return i;
        }
        return -1;
    }

    public static int indexStartWithFromRight(List<String> ss, String prefix){
        for(int i = ss.size() - 1; i >= 0; i--){
            if(ss.get(i).startsWith(prefix))
                return i;
        }
        return -1;
    }

    public static double average(List<String> ss, ToDoubleFunction<String> toDouble){
        double total = 0;
        for(String s : ss)
            total += toDouble.applyAsDouble(s);
        return total / ss.size();
    }

    public static void main(String[] args) {
        List<String> ss = new ArrayList<>();
        ss.add("a"); ss.add("b"); ss.add("a");
        System.out.println(indexStartWith(ss, "a"));
        System.out.println(indexStartWithFromRight(ss, "a"));
        System.out.println(mode(ss));
    }
}
