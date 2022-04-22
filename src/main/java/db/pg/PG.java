package db.pg;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class PG implements Closeable {
    protected final String ip;
    protected final String dbname;
    protected final String user;
    protected final String password;
    protected Connection conn;
    protected Statement stmt;

    public PG(String ip, String dbname, String user, String password) {
        this.ip = ip;
        this.dbname = dbname;
        this.user = user;
        this.password = password;

        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + "/" + dbname,
                    user,password);
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
