package calculation;

import java.util.ArrayList;
import java.util.List;

public class ListInteger {
    public static List<Integer> splitInterval(List<Integer> is, int size){
        List<Integer> answer = new ArrayList<>();
        if(is.size() == 0){
            answer.add(0); answer.add(size - 1);
            return answer;
        }
        int start = 0;
        for(Integer index : is){
            if(index - 1 >= start){
                answer.add(start); answer.add(index - 1);
            }
            start = index + 1;
        }
        if(start <= size - 1){
            answer.add(start); answer.add(size - 1);
        }
        return answer;
    }

    public static boolean[] toBoolean(List<Integer> is, int size){
        boolean[] answer = new boolean[size];
        for(int i : is)
            answer[i] = true;
        return answer;
    }

    public static void main(String[] args) {
        List<Integer> is = new ArrayList<>();
        is.add(0); is.add(1);
        System.out.println(splitInterval(is, 10));
    }
}
