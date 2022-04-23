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
    private int source;
    private int target;
    private MathTransform transformer;

    public TransformCoordinates(int source, int target){
        try {
            from = CRS.decode("EPSG:" + source);
            to = CRS.decode("EPSG:" + target);
            this.source = source;
            this.target = target;
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
        if(source == 4326){
            return JTS.transform(new Coordinate(y, x), null, transformer);
        }else {
            Coordinate coordinate = JTS.transform(new Coordinate(x, y), null, transformer);
            return new Coordinate(coordinate.y, coordinate.x, coordinate.z);
        }
    }

    public Coordinate go(@NotNull TransformCoordinateIF point) throws TransformException {
        if(source == 4326){
            return JTS.transform(new Coordinate(point.latitudeY(), point.longitudeX()), null, transformer);
        }else {
            Coordinate coordinate = JTS.transform(new Coordinate(point.longitudeX(), point.latitudeY()), null, transformer);
            return new Coordinate(coordinate.y, coordinate.x, coordinate.z);
        }
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
        TransformCoordinates transformCoordinates = new TransformCoordinates(4326, 3857);
        System.out.println(transformCoordinates.go(32.326948,31.1637222));

    }
}
