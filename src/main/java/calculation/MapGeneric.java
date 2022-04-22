package calculation;

import java.util.HashMap;
import java.util.Map;

public class MapGeneric {
    //必须保证A和B是不可变的
    public static <A,B> Map<A,B> copy(Map<A,B> source){
        if(source == null || source.size() == 0)
            return null;
        Map<A,B> answer = new HashMap<>();
        for(A a : source.keySet()){
            answer.put(a, source.get(a));
        }
        return answer;
    }

    public static void main(String[] args) {

    }
}
