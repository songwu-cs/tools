package calculation;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Array1DBoolean {
    public static void test(){
    }

    //空数组或者null返回null
    public static List<Integer> lastTrueIndices(boolean[] bools){
        if(bools == null || bools.length == 0)
            return null;

        List<Integer> lastTrue = new ArrayList<>();
        boolean previousAnchor = false;
        for(int i = 0; i < bools.length; i++){
            if((! bools[i]) && previousAnchor){
                lastTrue.add(i - 1);
            }
            previousAnchor = bools[i];
        }
        if(previousAnchor)
            lastTrue.add(bools.length - 1);

        return lastTrue;
    }

    //空数组或者null返回null
    public static List<Integer> firstTrueIndices(boolean[] bools){
        if(bools == null || bools.length == 0)
            return null;

        List<Integer> firstIndices = new ArrayList<>();
        boolean previousTrue = false;
        for(int i = 0; i < bools.length; i++){
            if(bools[i] && (!previousTrue)){
                firstIndices.add(i);
            }
            previousTrue = bools[i];
        }

        return firstIndices;
    }

    //空数组或者null返回null
    //非in-place修改
    //中间不超过tolerance个false将会变为true
    public static boolean[] false2trueWithTolerance(boolean[] bools, int tolerance){
        if(bools == null || bools.length == 0)
            return null;

        boolean[] labels = bools.clone();
        List<Integer> firstIndices = Array1DBoolean.firstTrueIndices(labels);
        List<Integer> lastIndices = Array1DBoolean.lastTrueIndices(labels);
        for(int i = 0; i < firstIndices.size() - 1; i++){
            int end = lastIndices.get(i);
            int start = firstIndices.get(i + 1);
            if(start - end - 1 <= tolerance){
                for(int _ = end + 1; _ < start; _++)
                    labels[_] = true;
            }
        }
        return labels;
    }

    //空数组或者null返回null
    public static int[] falseTo0TrueIncrease(boolean[] bools){
        if(bools == null || bools.length == 0)
            return null;

        int[] answer = new int[bools.length];
        int counter = 1;
        boolean previousTrue = false;
        for(int i = 0; i < answer.length; i++){
            if(bools[i]) {
                answer[i] = counter;
            }else{
               answer[i] = 0;
               if(previousTrue){
                   counter++;
               }
            }
            previousTrue = bools[i];
        }
        return answer;
    }

    //空数组或者null返回null
    public static int[] falseNegativeTruePositive(boolean[] booleans){
        if(booleans == null || booleans.length == 0)
            return null;

        int[] answer = new int[booleans.length];
        List<Integer> firstTrue = firstTrueIndices(booleans);
        List<Integer> lastTrue = lastTrueIndices(booleans);
        for(int i = 0; i < firstTrue.size(); i++){
            for(int j = firstTrue.get(i); j <= lastTrue.get(i); j++){
                answer[j] = i + 1;
            }
        }
        if(firstTrue.size() == 0)
            Arrays.fill(answer, -1);
        for(int i = 0; i < firstTrue.size() + 1; i++){
            int start = i == 0 ? 0 : lastTrue.get(i - 1) + 1;
            int end = i == firstTrue.size() ? booleans.length - 1 : firstTrue.get(i) - 1;
            for(int j = start; j <= end; j++)
                answer[j] = -(i + 1);
        }
        return answer;
    }

    public static List<Integer> runLengthEncoding(@NotNull boolean[] bools){
        List<Integer> answer = new ArrayList<>();
        int status = bools[0] ? 1 : -1;
        for(int i = 1; i < bools.length; i++){
            if(bools[i] && status > 0){
                status++;
            }else if (bools[i] && status < 0){
                answer.add(status);
                status = 1;
            }else if ((!bools[i]) && status > 0){
                answer.add(status);
                status = -1;
            }else {
                status--;
            }
        }
        answer.add(status);
        return answer;
    }

    public static void main(String[] args) {
//        System.out.println(Arrays.toString(falseTo0TrueIncrease(new boolean[]{false,true,true,false,true})));
//        System.out.println(Arrays.toString(falseNegativeTruePositive(new boolean[]{false,true,true,false,false,true})));
        System.out.println(runLengthEncoding(new boolean[]{false,true,true,false,false,true}));
    }
}
