package io.bigdata;

import calculation.UnitString;

import java.io.*;

public class ExtractColumns {
    private boolean withHeader;

    public ExtractColumns(boolean withHeader) {
        this.withHeader = withHeader;
    }

    public void extract(String sourceFile, String destFile, String header, int... cols) throws IOException {
        try(PrintWriter writer = new PrintWriter(destFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile))){
            writer.write(header); writer.write("\n");
            if(withHeader)
                bufferedReader.readLine();

            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                writer.write(UnitString.subset(line, ",", cols));
                writer.write("\n");
            }
        }
    }
}
