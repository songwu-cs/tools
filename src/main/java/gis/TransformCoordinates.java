package gis;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.filter.spatial.DWithinImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.filter.spatial.DWithin;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;



public class TransformCoordinates {
    private CoordinateReferenceSystem from;
    private CoordinateReferenceSystem to;
    private MathTransform transformer;

    public TransformCoordinates(int source, int target){
        try {
            from = CRS.decode("EPSG:" + source);
            to = CRS.decode("EPSG:" + target);
            transformer = CRS.findMathTransform(from, to);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    public void setSource(int code) throws FactoryException {
        from = CRS.decode("EPSG:" + code);
        transformer = CRS.findMathTransform(from, to);
    }

    public void setTarget(int code) throws FactoryException {
        to = CRS.decode("EPSG:" + code);
        transformer = CRS.findMathTransform(from, to);
    }

    public Coordinate go(double x, double y) throws TransformException {
        return JTS.transform(new Coordinate(y, x),
                null, transformer);
    }

    public Coordinate go(@NotNull TransformCoordinateIF point) throws TransformException {
        return JTS.transform(new Coordinate(point.latitudeY(), point.longitudeX()),
                null, transformer);
    }

    public List<Coordinate> go(@NotNull List<TransformCoordinateIF> points) throws TransformException {
        List<Coordinate> answer = new ArrayList<>();
        for(TransformCoordinateIF point : points)
            answer.add(go(point));
        return answer;
    }

    public Coordinate[] go(@NotNull TransformCoordinateIF[] points) throws TransformException {
        Coordinate[] answer = new Coordinate[points.length];
        for(int i = 0; i < points.length; i++)
            answer[i] = go(points[i]);
        return answer;
    }

    public static void main(String[] args) throws FactoryException, TransformException {
        TransformCoordinates transformCoordinates = new TransformCoordinates(4326, 25832);
        System.out.println(transformCoordinates.go(15.134207,55.054825));
    }
}
