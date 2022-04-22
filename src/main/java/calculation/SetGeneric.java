package calculation;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class SetGeneric {
    //必须保证A和B是不可变的
    public static <T> Set<T> copy(Set<T> source){
        if(source == null || source.size() == 0)
            return null;
        Set<T> answer = new HashSet<>();
        answer.addAll(source);
        return answer;
    }

    //会移除a中的元素
    public static <T> boolean equals(@NotNull Set<T> a, @NotNull Set<T> b){
        if(a.size() != b.size())
            return false;
        Set<T> aCOPY = copy(a);
        aCOPY.removeAll(b);
        return aCOPY.size() == 0;
    }

    public static void main(String[] args) {
        Set<String> a = new HashSet<>();
        a.add("0"); a.add("2");
        Set<String> b = new HashSet<>();
        b.add("0"); b.add("2");
        System.out.println(equals(a, b));
    }
}
