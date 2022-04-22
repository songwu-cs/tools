package io.newcolumns;

import com.vividsolutions.jts.geom.Coordinate;
import gis.TransformCoordinates;
import io.bigdata.WideLine;
import org.opengis.referencing.operation.TransformException;

import java.io.*;
import java.util.Arrays;

public class TransformXY {
    private static String lineDelimiter = System.lineSeparator();
    private boolean withHeader;
    private String splitter;
    private int longitudeIndex;
    private int latitudeIndex;
    private TransformCoordinates transformCoordinates;

    public TransformXY(boolean withHeader, String splitter, int longitudeIndex, int latitudeIndex, int fromCRS, int toCRS) {
        this.withHeader = withHeader;
        this.splitter = splitter;
        this.longitudeIndex = longitudeIndex;
        this.latitudeIndex = latitudeIndex;
        this.transformCoordinates = new TransformCoordinates(fromCRS, toCRS);
    }

    public void setWithHeader(boolean withHeader) {
        this.withHeader = withHeader;
    }

    public void setSplitter(String splitter) {
        this.splitter = splitter;
    }

    public void setLongitudeIndex(int longitudeIndex) {
        this.longitudeIndex = longitudeIndex;
    }

    public void setLatitudeIndex(int latitudeIndex) {
        this.latitudeIndex = latitudeIndex;
    }

    public void addXY(String inputPath, String outputPath) throws IOException, TransformException {
        try(BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            PrintWriter writer = new PrintWriter(outputPath)){
                String line = "";
                if(withHeader){
                    writer.write(String.join(splitter,
                            reader.readLine(), "X", "Y") + lineDelimiter);
                }
                while ((line = reader.readLine()) != null){
                    String[] parts = line.split(splitter);
                    double longitude = Double.parseDouble(parts[longitudeIndex]);
                    double latitude = Double.parseDouble(parts[latitudeIndex]);
                    Coordinate coord = transformCoordinates.go(longitude, latitude);
                    writer.write(String.join(splitter, line, coord.x + "", coord.y + "") + lineDelimiter);
                }
        }
    }

    public static void main(String[] args) throws TransformException, IOException {
        System.out.println(Arrays.toString("a,b,".split(",")));
    }
}
