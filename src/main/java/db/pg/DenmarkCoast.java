package db.pg;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DenmarkCoast extends PG{
    private static final String sqlTemplate1 = "with tt(p) as (select ST_SetSRID(st_makepoint({x}, {y}), {crs})) select (exists (select 1 from tt t1, {table} t2 where st_dwithin(t1.p, t2.{field}, {distance})))";
    private static final String sqlTemplate2 = "SELECT ST_Distance(ST_SetSRID(st_makepoint({x}, {y}), {crs}),t.{field}) from {table} t;";
    private String tableName;
    private String fieldName;
    private int crs;
    private double distanceToCoast;

    public void setCrs(int crs) {
        this.crs = crs;
    }

    public void setDistanceToCoast(double distanceToCoast) {
        this.distanceToCoast = distanceToCoast;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DenmarkCoast(String ip, String dbname, String user, String password, int crs, double distanceToCoast, String tableName, String fieldName) {
        super(ip, dbname, user, password);
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.crs = crs;
        this.distanceToCoast = distanceToCoast;
    }

    synchronized public boolean within(double x, double y) throws SQLException {
        String sql = sqlTemplate1.replace("{x}", x+"")
                .replace("{y}", y+"")
                .replace("{crs}", crs+"")
                .replace("{table}", tableName)
                .replace("{field}", fieldName)
                .replace("{distance}", distanceToCoast+"");
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        boolean answer = rs.getBoolean(1);
        rs.close();
        return answer;
    }

    synchronized public double getDistanceToShore(double x, double y) throws SQLException {
        String sql = sqlTemplate2.replace("{x}", x+"")
                .replace("{y}", y+"")
                .replace("{crs}", crs+"")
                .replace("{table}", tableName)
                .replace("{field}", fieldName);
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        double answer = rs.getDouble(1);
        rs.close();
        return answer;
    }


    public static void main(String[] args) throws SQLException, IOException {
        DenmarkCoast denmarkCoast = new DenmarkCoast("172.29.129.234",
                "bmda22",
                "postgres",
                "wusong",
                25832,
                100,
                "denmark_administrative_national_boundary",
                "geom25832");
        System.out.println(denmarkCoast.getDistanceToShore(891602.8556426356, 6118102.93948347));

    }
}
