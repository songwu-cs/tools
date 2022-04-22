package clustering;

import calculation.ArrayDoubleTwo;

import java.util.*;

public class DBSCAN {
    public final int minpts;
    public final double epsilon;

    public DBSCAN(int minpts, double epsilon) {
        this.minpts = minpts;
        this.epsilon = epsilon;
    }

    //簇之间不会有重叠
    public List<Set<String>> cluster(Map<String, double[]> points){
        Set<String> visited = new HashSet<>();
        Set<String> added = new HashSet<>();
        List<Set<String>> answer = new ArrayList<>();
        for(String p : points.keySet()){
            if(! visited.contains(p)){
                visited.add(p);
                List<String> neighbors = neighbors(points, p);
                if(neighbors.size() >= minpts){
                    for(int i = 0; i < neighbors.size(); i++){
                        if(! visited.contains(neighbors.get(i))){
                            visited.add(neighbors.get(i));
                            List<String> newNeighbors = neighbors(points, neighbors.get(i));
                            if(newNeighbors.size() >= minpts)
                                neighbors.addAll(newNeighbors);
                        }
                    }
                    neighbors.removeAll(added);
                    answer.add(new HashSet<>(neighbors));
                    added.addAll(answer.get(answer.size()-1));
                }
            }
        }
        return answer;
    }

    //邻居包括自己
    public List<String> neighbors(Map<String, double[]> points, String center){
        double[] coords = points.get(center);
        List<String> answer = new ArrayList<>();
        for(String p : points.keySet()){
            if(ArrayDoubleTwo.euclidean(coords, points.get(p)) <= epsilon)
                answer.add(p);
        }
        return answer;
    }

    public static void main(String[] args) {
        Map<String, double[]> points = new HashMap<>();
        points.put("a", new double[]{0,0});
        points.put("b", new double[]{1,0});
        points.put("c", new double[]{2,0});
        points.put("d", new double[]{1,1});
        points.put("e", new double[]{0,2});
        points.put("f", new double[]{1,2});
        points.put("g", new double[]{2,2});

        DBSCAN dbscan = new DBSCAN(3, 1);
        for(Set<String> clu : dbscan.cluster(points))
            System.out.println(clu);
    }
}
