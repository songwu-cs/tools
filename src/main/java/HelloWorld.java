import db.pg.DenmarkCoast;
import io.bigdata.WideCSV;
import io.bigdata.WideLine;
import io.bigdata.WideLineIndex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class HelloWorld {
    public static void main(String[] args) throws SQLException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\TJUer\\Desktop\\1.txt"));
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        reader.close();
    }
}
