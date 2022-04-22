package io.bigdata;

import java.util.Arrays;
import java.util.List;

public class WideLine {
    private final List<String[]> lines;
    private WideCSV wideCSV;

    public WideLine(List<String[]> lines, WideCSV wideCSV) {
        this.lines = lines;
        this.wideCSV = wideCSV;
    }

    public String get(WideLineIndex index){
        int columnIndex = wideCSV.columnName2Index.get(index.fileId).get(index.columnName);
        return lines.get(index.fileId)[columnIndex];
    }
}
