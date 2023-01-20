package calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDouble {
    public static double[] toArray(List<Double> ds){
        double[] answer = new double[ds.size()];

        int pos = 0;
        for(double d : ds)
            answer[pos++] = d;

        return answer;
    }

    public static void main(String[] args) {
        List<Double> ds = new ArrayList<>();
        ds.add(1.1); ds.add(1.2);
        System.out.println(Arrays.toString(toArray(ds)));
    }
}
