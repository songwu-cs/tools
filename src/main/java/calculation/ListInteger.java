package calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListInteger {
    //元素不等于0
    //有正的，并且正的总是>=两个负的邻居
    public static boolean trueWin(List<Integer> runLength){
        if(runLength.get(0) > 0){
            int cur = 0;
            while (cur < runLength.size() - 1) {
                if(Math.abs(runLength.get(cur)) < Math.abs(runLength.get(cur+1)))
                    return false;
                if(cur + 2 < runLength.size()){
                    if(Math.abs(runLength.get(cur+2)) < Math.abs(runLength.get(cur+1)))
                        return false;
                    else
                        cur += 2;
                }else break;
            }
            return true;
        }else {
            if(runLength.size() == 1)
                return false;
            int cur = 0;
            while (cur < runLength.size() - 1) {
                if(Math.abs(runLength.get(cur)) > Math.abs(runLength.get(cur+1)))
                    return false;
                if(cur + 2 < runLength.size()){
                    if(Math.abs(runLength.get(cur+2)) > Math.abs(runLength.get(cur+1)))
                        return false;
                    else
                        cur += 2;
                }else break;
            }
            return true;
        }
    }

    public static int sum(List<Integer> ints){
        int answer = 0;
        for(Integer i : ints)
            answer += i;
        return answer;
    }

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

    public static int[] toArray(List<Integer> ds){
        int[] answer = new int[ds.size()];

        int pos = 0;
        for(int d : ds)
            answer[pos++] = d;

        return answer;
    }

    public static void main(String[] args) {
        List<Integer> is = new ArrayList<>();
        is.add(0); is.add(1); is.add(5);
        System.out.println(splitInterval(is, 10));

//        System.out.println(trueWin(Arrays.asList(-2,3,-2,1)));
    }
}
