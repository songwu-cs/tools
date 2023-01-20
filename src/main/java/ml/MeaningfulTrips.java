package ml;

import calculation.Array1DBoolean;
import calculation.ListGeneric;
import calculation.ListInteger;
import datetime.TwoTimestamp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MeaningfulTrips {
    public int minpts;
    public int radius;
    public int timeThreshold;
    public int toleranceMax;
    public double distanceThreshold;

    public String[] timestamps;
    public double[] xs;
    public double[] ys;
    public double[] distances;
    public boolean[] distancesWithinShore;
    public int length;

    public MeaningfulTrips(int minpts, int radius, int timeThreshold, int tolerance, double distanceThreshold) {
        this.minpts = minpts;
        this.radius = radius;
        this.timeThreshold = timeThreshold;
        this.toleranceMax = tolerance;
        this.distanceThreshold = distanceThreshold;
    }

    public String[] go(String[] timestamps, double[] x, double[] y, double[] distanceToShore) throws SQLException {
        this.timestamps = timestamps;
        this.xs = x;
        this.ys = y;
        this.distances = distanceToShore;
        this.distancesWithinShore = null;
        this.length = timestamps.length;

        return activities();
    }

    public String[] go(String[] timestamps, double[] x, double[] y, boolean[] withinShore) throws SQLException {
        this.timestamps = timestamps;
        this.xs = x;
        this.ys = y;
        this.distancesWithinShore = withinShore;
        this.distances = null;
        this.length = timestamps.length;

        return activities();
    }


    public String[] activities() throws SQLException {
        boolean[] lnt = labelNoiseTrueFalse();
        int[] clusters = Array1DBoolean.falseNegativeTruePositive(lnt);
        String[] labels = new String[length];
        for(int i = 0; i < length; i++){
            labels[i] = (clusters[i] > 0 ? "anchor-" : "sailing-") + Math.abs(clusters[i]);
        }

        List<Integer> trueAnchorage = new ArrayList<>();
        List<String> labelsList = Arrays.asList(labels);

        List<List<String>> lls = ListGeneric.groupString(labelsList, e -> e);
        int historySize = 0;
        for(int i = 0; i < lls.size(); i++){
            List<String> ls2 = lls.get(i);
            if(ls2.get(0).startsWith("anchor")){
                String startTime = timestamps[historySize];
                String endTime = timestamps[historySize + ls2.size() - 1];
                if(TwoTimestamp.diffInSeconds(endTime, startTime, TwoTimestamp.formatter1) >= timeThreshold){
                    if(distances != null){
                        List<Double> distancesArr = new ArrayList<>();
                        for(int ii = historySize; ii < historySize + ls2.size(); ii++){
                            distancesArr.add(distances[ii]);
                        }
                        distancesArr.sort(Comparator.naturalOrder());
                        if (distancesArr.get(distancesArr.size() / 2) <= distanceThreshold)
                            trueAnchorage.add(i);
                    } else if (distancesWithinShore != null) {
                        if (Array1DBoolean.mode(distancesWithinShore))
                            trueAnchorage.add(i);
                    }
                }
            }
            historySize += ls2.size();
        }

        int[] startPointer = new int[lls.size()+1];
        for(int ii = 0; ii < startPointer.length; ii++){
            if(ii == 0)
                startPointer[ii] = 0;
            else {
                startPointer[ii] = startPointer[ii-1] + lls.get(ii-1).size();
            }
        }

        List<Integer> check = ListInteger.splitInterval(trueAnchorage, lls.size());
        for(int i = 0; i + 1 < check.size(); i += 2){
            double avgDistance = 0;

            int startPos = startPointer[check.get(i)];
            int endPos = startPointer[check.get(i+1)+1];

            //第一种
            if(distances != null){
                for(int ii = startPos; ii < endPos; ii++){
                    avgDistance += distances[ii];
                }
                avgDistance /= (endPos - startPos);

                if(avgDistance <= distanceThreshold) {
                    for(int j = check.get(i); j <= check.get(i+1); j++)
                        trueAnchorage.add(j);
                }
            } else if (distancesWithinShore != null) {
                boolean[] tmp = new boolean[endPos - startPos];
                for(int ii = startPos; ii < endPos; ii++){
                    tmp[ii - startPos] = distancesWithinShore[ii];
                }
                if(Array1DBoolean.mode(tmp)){
                    for(int j = check.get(i); j <= check.get(i+1); j++)
                        trueAnchorage.add(j);
                }
            }

        }
        boolean[] mark = ListInteger.toBoolean(trueAnchorage, lls.size());
        int[] mark2 = Array1DBoolean.falseNegativeTruePositive(mark);

        String[] answers = new String[length];
        int pointer = 0;
        int counter = 1;
        for(int i = 0; i < lls.size(); i++){
            if(i > 0 && ((mark2[i]>0) ^ (mark2[i-1]>0)))
                counter++;
            String label = (String.format("%03d", counter) + (mark2[i] > 0 ? "-anchor" : "-sailing"));
            for(String s : lls.get(i)){
                answers[pointer] = label;
                pointer++;
            }
        }
        return answers;
    }

    public boolean[] labelNoiseTrueFalse(){
        int i = 0;
        boolean[] answer = new boolean[length];
        while (i < length){
            int j = i;
            int tolerance = 0;
            int noiseCounter = 0;
            int left = i;
            while (j + 1 < length){
                int right = (j + 1);
                if(Math.hypot(xs[left] - xs[right], ys[left] - ys[right]) > radius){
                    tolerance++;

                    if (tolerance > toleranceMax){
                        j -= toleranceMax;
                        tolerance = 0;
                        break;
                    }
                    j++;
                }else {
                    noiseCounter += tolerance;
                    tolerance = 0;

                    j++;
                }
            }
            j -= tolerance;

            boolean bigTimeGap = false;
            for(int _ = i + 1; _ <= j; _++){
                if(TwoTimestamp.diffInSeconds(timestamps[_], timestamps[_-1], TwoTimestamp.formatter1) > timeThreshold){
                    bigTimeGap = true;
                    break;
                }
            }
            if(bigTimeGap || (j - i + 1) - noiseCounter > minpts){
                for(int _ = i; _ <= j; _++){
                    answer[_] = true;
                }
                i = j;
            }else {
                i++;
            }
        }
        return answer;
    }

    public static void main(String[] args) throws SQLException {
//        DenmarkCoast denmarkCoast = new DenmarkCoast("localhost",
//                "bmda22",
//                "postgres",
//                "wusong",
//                25832,
//                100,
//                "denmark_administrative_national_boundary",
//                "geom25832");
//        System.out.println(denmarkCoast.getDistanceToShore(891602.8556426356, 6118102.93948347));
//        System.out.println(denmarkCoast.getDenmarkCoordinate(5, 50));
    }
}
