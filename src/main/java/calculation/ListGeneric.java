package calculation;

import function.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ListGeneric {

    //必须保证T是不可变的
    public static <T> List<T> copy(List<T> source){
        if(source == null || source.size() == 0)
            return null;
        List<T> answer = new ArrayList<>();
        answer.addAll(source);
        return answer;
    }

    public static <T> List<T> booleanSelect(@NotNull List<T> ts, @NotNull boolean[] boolArr){
        List<T> answer = new ArrayList<>();
        for(int i = 0; i < boolArr.length; i++){
            if(boolArr[i])
                answer.add(ts.get(i));
        }
        return answer;
    }

    //return all elements which holds true for "predicate"
    //don't modify "ts"
    public static <T> List<T> filter(List<T> ts, Predicate<T> predicate){
        if(ts == null || ts.size() == 0)
            return null;

        List<T> answer = new ArrayList<>();
        for(T t : ts){
            if(predicate.test(t))
                answer.add(t);
        }

        return answer;
    }

    public static <T> List<List<T>> groupPredicate(List<T> ts, Predicate<T> predicate){
        if(ts == null || ts.size() == 0)
            return null;

        List<List<T>> answer = new ArrayList<>();
        answer.add(new ArrayList<>());
        answer.get(answer.size() - 1).add(ts.get(0));
        boolean previous = predicate.test(ts.get(0));
        for(int i = 1; i < ts.size(); i++){
            boolean now = predicate.test(ts.get(i));
            if(now != previous)
                answer.add(new ArrayList<>());
            answer.get(answer.size() - 1).add(ts.get(i));
            previous = now;
        }
        return answer;
    }

    public static <T> List<List<T>> groupString(List<T> ts, ToString<T> toString){
        if(ts == null || ts.size() == 0)
            return null;

        List<List<T>> answer = new ArrayList<>();
        answer.add(new ArrayList<>());
        answer.get(answer.size() - 1).add(ts.get(0));
        String  previous = toString.applyAsString(ts.get(0));
        for(int i = 1; i < ts.size(); i++){
            String now = toString.applyAsString(ts.get(i));
            if(! now.equals(previous))
                answer.add(new ArrayList<>());
            answer.get(answer.size() - 1).add(ts.get(i));
            previous = now;
        }
        return answer;
    }

    public static <T> int firstIndex(List<T> ts, Predicate<T> predicate){
        for(int i = 0; i < ts.size(); i++){
            if(predicate.test(ts.get(i)))
                return i;
        }
        return -1;
    }

    public static <T> int lastIndex(List<T> ts, Predicate<T> predicate){
        for(int i = ts.size() - 1; i >= 0; i--){
            if(predicate.test(ts.get(i)))
                return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        List<String> ss = new ArrayList<>();
        ss.add("qq"); ss.add("qqq"); ss.add("ww"); ss.add("4565");
//        System.out.println(filter(ss, e->e.length()==2));
//        System.out.println(lastIndex(ss, e -> e.length() == 3));

        for(List<String> ls : groupString(ss, e -> e.substring(0,1))){
            System.out.println(ls);
        }

    }
}
