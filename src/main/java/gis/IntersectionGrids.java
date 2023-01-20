package gis;

import calculation.Array1DNumber;
import datetime.OneTimestamp;
import datetime.SimpleDateFormatExt;
import datetime.TwoTimestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IntersectionGrids {
    double lonMin, latMin, lonMax, latMax;
    double[] allLongitudes, allLatitudes;
    int lonQuantity, latQuantity;
    double lonSize, latSize;
    double tolerance = 0.000001;

    //area of interest
    public IntersectionGrids(double lonMin, double latMin, double lonMax, double latMax) {
        this.lonMin = lonMin;
        this.latMin = latMin;
        this.lonMax = lonMax;
        this.latMax = latMax;
    }

    public IntersectionGrids setTolerance(double tolerance){
        this.tolerance = tolerance;
        return this;
    }

    public IntersectionGrids split(double lonSize, double latSize){
        lonQuantity = (int)((lonMax - lonMin)/lonSize);
        latQuantity = (int)((latMax - latMin)/latSize);

        this.lonSize = lonSize;
        this.latSize = latSize;

        allLongitudes = new double[lonQuantity];
        allLatitudes = new double[latQuantity];

        for (int i = 0; i < lonQuantity; i++){
            allLongitudes[i] = lonMin + lonSize / 2 + i * lonSize;
        }
        for (int i = 0; i < latQuantity; i++){
            allLatitudes[i] = latMin + latSize / 2 + i * latSize;
        }
        return this;
    }

    public int which(double lon, double lat){
        int lonPos = Array1DNumber.closest(allLongitudes, lon) + 1;
        int latPos = Array1DNumber.closest(allLatitudes, lat) + 1;
        return latPos + (lonPos - 1) * latQuantity;
    }

    public IntersectionGridsHelper intersection(String t1, double lon1, double lat1, String t2, double lon2, double lat2, SimpleDateFormatExt format){
        double lonDiff = Math.abs(lon2 - lon1);
        double latDiff = Math.abs(lat2 - lat1);
        int lonCoef = lon1 < lon2 ? 1 : -1;
        int latCoef = lat1 < lat2 ? 1 : -1;
        double lonStep = (lonDiff > latDiff ? tolerance : (tolerance * lonDiff / latDiff)) * lonCoef;
        double latStep = (lonDiff <= latDiff ? tolerance : (tolerance * latDiff / lonDiff)) * latCoef;

        int timeGap = (int) TwoTimestamp.diffInSeconds(t2, t1, format);

        double changeLon = lonStep;
        double changeLat = latStep;

        int startGrid = which(lon1, lat1);
        int endGrid = which(lon2, lat2);
        int previousGrid = startGrid;

        List<String> timestamps = new ArrayList<>();
        List<Double> longitudes = new ArrayList<>();
        List<Double> latitudes = new ArrayList<>();
        List<Integer> grids = new ArrayList<>();
        while (Math.abs(changeLon) < lonDiff && Math.abs(changeLat) < latDiff && previousGrid != endGrid){
            int grid = which(lon1 + changeLon, lat1 + changeLat);
            if(grid != previousGrid){
                longitudes.add(lon1 + changeLon);
                latitudes.add(lat1 + changeLat);
                grids.add(grid); previousGrid = grid;
                timestamps.add(OneTimestamp.add(t1, 0, 0, (int) Math.round(timeGap * Math.abs(changeLon) / lonDiff), format));
            }
            changeLon += lonStep;
            changeLat += latStep;
        }

        return new IntersectionGridsHelper(timestamps, longitudes, latitudes, grids);
    }

    public IntersectionGridsHelperXY intersectionXY(String t1, double lon1, double lat1, double x1, double y1, String t2, double lon2, double lat2, double x2, double y2, SimpleDateFormatExt format){
        double lonDiff = Math.abs(lon2 - lon1);
        double latDiff = Math.abs(lat2 - lat1);
        int lonCoef = lon1 < lon2 ? 1 : -1;
        int latCoef = lat1 < lat2 ? 1 : -1;
        double lonStep = (lonDiff > latDiff ? tolerance : (tolerance * lonDiff / latDiff)) * lonCoef;
        double latStep = (lonDiff <= latDiff ? tolerance : (tolerance * latDiff / lonDiff)) * latCoef;

        int timeGap = (int) TwoTimestamp.diffInSeconds(t2, t1, format);

        double changeLon = lonStep;
        double changeLat = latStep;

        int startGrid = which(lon1, lat1);
        int endGrid = which(lon2, lat2);
        int previousGrid = startGrid;

        List<String> timestamps = new ArrayList<>();
        List<Double> longitudes = new ArrayList<>();
        List<Double> latitudes = new ArrayList<>();
        List<Integer> grids = new ArrayList<>();
        List<Double> Xs = new ArrayList<>();
        List<Double> Ys = new ArrayList<>();
        while (Math.abs(changeLon) < lonDiff && Math.abs(changeLat) < latDiff && previousGrid != endGrid){
            int grid = which(lon1 + changeLon, lat1 + changeLat);
            if(grid != previousGrid){
                longitudes.add(lon1 + changeLon);
                latitudes.add(lat1 + changeLat);
                grids.add(grid); previousGrid = grid;
                timestamps.add(OneTimestamp.add(t1, 0, 0, (int) Math.round(timeGap * Math.abs(changeLon) / lonDiff), format));

                if (lonDiff > latDiff){
                    Xs.add(x1 + (x2 - x1) * (Math.abs(changeLon) / lonDiff));
                    Ys.add(y1 + (y2 - y1) * (Math.abs(changeLon) / lonDiff));
                }else {
                    Xs.add(x1 + (x2 - x1) * (Math.abs(changeLat) / latDiff));
                    Ys.add(y1 + (y2 - y1) * (Math.abs(changeLat) / latDiff));
                }
            }
            changeLon += lonStep;
            changeLat += latStep;
        }

        return new IntersectionGridsHelperXY(timestamps, longitudes, latitudes, grids, Xs, Ys);
    }

    public static class IntersectionGridsHelper{
        public final List<String> timestamps;
        public final List<Double> longitudes;
        public final List<Double> latitudes;
        public final List<Integer> grids;

        public IntersectionGridsHelper(List<String> timestamps, List<Double> longitudes, List<Double> latitudes, List<Integer> grids) {
            this.timestamps = timestamps;
            this.longitudes = longitudes;
            this.latitudes = latitudes;
            this.grids = grids;
        }

        @Override
        public String toString() {
            return String.join("\n", timestamps.toString(), longitudes.toString(), latitudes.toString(), grids.toString());
        }
    }

    public static class IntersectionGridsHelperXY extends IntersectionGridsHelper{
        public final List<Double> X;
        public final List<Double> Y;

        public IntersectionGridsHelperXY(List<String> timestamps, List<Double> longitudes, List<Double> latitudes, List<Integer> grids, List<Double> x, List<Double> y) {
            super(timestamps, longitudes, latitudes, grids);
            X = x;
            Y = y;
        }

        @Override
        public String toString() {
            return super.toString() + "\n" + String.join("\n", X.toString(), Y.toString());
        }
    }

    public static void main(String[] args) {
        IntersectionGrids intersectionGrids = new IntersectionGrids(0,0,10,10);
        System.out.println(intersectionGrids.split(1,1).setTolerance(0.00001).intersection("2022-05-01 00:00:00", 0.5, 0.5, "2022-05-01 01:00:00", 4.5, 2.5, OneTimestamp.formatter1));
        System.out.println(intersectionGrids.split(1,1).setTolerance(0.00001).intersectionXY("2022-05-01 00:00:00", 0.5, 0.5, 0.5, 0.5, "2022-05-01 01:00:00", 4.5, 2.5, 4.5, 2.5, OneTimestamp.formatter1));

    }
}
