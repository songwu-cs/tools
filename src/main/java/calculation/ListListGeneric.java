package calculation;

import java.util.ArrayList;
import java.util.List;

public class ListListGeneric{
    //左闭右开
    public static <T> List<T> flat(List<List<T>> lists, int start, int end){
        if(lists == null || lists.size() == 0)
            return null;

        List<T> answer = new ArrayList<>();
        for(List<T> list : lists.subList(start, end)){
            answer.addAll(list);
        }
        return answer;
    }

    public static <T> List<T> flat(List<T>... lts){
        if(lts.length == 0)
            return null;
        List<T> answer = new ArrayList<>();
        for(List<T> list : lts){
            answer.addAll(list);
        }
        return answer;
    }

    public static void main(String[] args) {
        List<String> cities = new ArrayList<>();
        cities.add("Beijing"); cities.add("Dalian");
        System.out.println(flat(cities, cities));
    }
}
