package io.bigdata;

public class WideLineIndex {
    public final int fileId;
    public final String columnName;

    public WideLineIndex(int fileId, String columnName) {
        this.fileId = fileId;
        this.columnName = columnName;
    }
}
