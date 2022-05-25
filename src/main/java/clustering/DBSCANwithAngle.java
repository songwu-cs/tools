package clustering;

import angle.Angle;
import calculation.ArrayDoubleTwo;
import datetime.OneTimestamp;
import model.Trajectory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBSCANwithAngle extends DBSCAN{
    private Map<String, Double> traj2angle;
    private double angleThreshold;

    public DBSCANwithAngle(int minpts, double epsilon, double angleThreshold) {
        super(minpts, epsilon);
        this.angleThreshold = angleThreshold;
        traj2angle = new HashMap<>();
    }

    //那些没有前驱的点默认返回0
    @Override
    public void preprocess(List<Trajectory> trajs, String ts, int gapInSeconds) {
        traj2angle.clear();
        String lastTS = OneTimestamp.add(ts, 0, 0, -1 * gapInSeconds, OneTimestamp.formatter1);
        for(Trajectory traj : trajs){
            if(traj.contains(ts) && traj.contains(lastTS)){
                double[] from = traj.getVector2(lastTS);
                double[] to = traj.getVector2(ts);
                traj2angle.put(traj.getID(), ArrayDoubleTwo.heading(from, to));
            }
            else
                traj2angle.put(traj.getID(), 0.0);
        }
    }

    @Override
    public List<String> neighbors(Map<String, double[]> points, String center) {
        double[] coords = points.get(center);
        List<String> answer = new ArrayList<>();
        for(String p : points.keySet()){
            if(ArrayDoubleTwo.euclidean(coords, points.get(p)) <= epsilon
                    && Angle.difference(traj2angle.get(center), traj2angle.get(p)) <= angleThreshold)
                answer.add(p);
        }
        return answer;
    }
}
