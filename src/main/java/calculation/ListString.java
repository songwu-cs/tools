package calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class ListString {
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
    }
}
