package ml;

import calculation.ListGeneric;
import calculation.ListString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrajSegmentation {
    //要求ground truth和prediction每个segment的编号不能相同

    public static double purity(List<String> groundTruth, List<String> prediction, boolean denominatorIsTruth){
        List<List<String>> segmentsPred = ListGeneric.groupString(prediction, e -> (String)e);
        double sum = 0;
        int index = 0;
        for(List<String> ls : segmentsPred){
            List<List<String>> segmentsTruth = ListGeneric.groupString(groundTruth.subList(index, index + ls.size()), e -> (String)e);
            int majority = 0;
            String truth = "";
            for(List<String> segmentTruth : segmentsTruth){
                truth = segmentTruth.size() > majority ? segmentTruth.get(0) : truth;
                majority = segmentTruth.size() > majority ? segmentTruth.size() : majority;
            }
            int denominator = denominatorIsTruth ? (groundTruth.lastIndexOf(truth) - groundTruth.indexOf(truth) + 1) : ls.size();
            sum += 1.0 * majority / denominator;
            index += ls.size();
        }
        return sum / segmentsPred.size();
    }

    public static double coverageFromTruth(List<String> groundTruth, List<String> prediction){
        return purity(prediction, groundTruth, false);
    }

    public static double coverageFromPred(List<String> groundTruth, List<String> prediction){
        return purity(groundTruth, prediction, true);
    }

    public static void main(String[] args) {
        List<String> truth = new ArrayList<>();
        truth.addAll(Arrays.asList("g1","g1","g1","g1","g2","g2","g2","g3","g3","g3"));
        List<String> pred = new ArrayList<>();
        pred.addAll(Arrays.asList("r1","r1","r1","r1","r1","r1","r2","r2","r2","r2"));

        System.out.println(purity(truth, pred, false));
    }
}
