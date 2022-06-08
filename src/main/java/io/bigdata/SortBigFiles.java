package io.bigdata;
import calculation.ListGeneric;
import calculation.UnitStringSorter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class SortBigFiles {
    private String baseDir;
    private Predicate<String> fileFilter;
    private int[] sortColumns;
    private boolean[] sortAscending;
    private int blockSize;
    private boolean withHeader;

    public SortBigFiles() {
    }

    public SortBigFiles setBlockSize(int blockSize) {
        this.blockSize = blockSize; return this;
    }

    public SortBigFiles setBaseDir(String baseDir) {
        this.baseDir = baseDir; return this;
    }

    public SortBigFiles setFileFilter(Predicate<String> fileFilter) {
        this.fileFilter = fileFilter; return this;
    }

    public SortBigFiles setSortColumns(int[] sortColumns) {
        this.sortColumns = sortColumns; return this;
    }

    public SortBigFiles setSortAscending(boolean[] sortAscending) {
        this.sortAscending = sortAscending; return this;
    }

    public SortBigFiles setWithHeader(boolean withHeader){
        this.withHeader = withHeader; return this;
    }

    public void go() throws IOException {
        File dir = new File(baseDir);
        List<String> files = ListGeneric.filter(Arrays.asList(dir.list()), fileFilter);
        UnitStringSorter fileSorter = new UnitStringSorter(sortColumns, sortAscending);

        int counter = 0;
        for(String file : files){
            try(BlockFileReader reader = new BlockFileReader(baseDir + "\\" + file, ",", true, blockSize)){
                for(List<String> block : reader){
                    block.sort(fileSorter);
                    try(PrintWriter writer = new PrintWriter(baseDir + "\\" + "tmp" + counter)){
                        for(String line : block){
                            writer.write(line);
                            writer.write("\n");
                        }
                    }
                    counter++;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SortBigFiles sortBigFiles = new SortBigFiles();
        sortBigFiles.setBaseDir("H:\\UpanSky\\DEDS_DenmarkAIS_May_2022")
                .setBlockSize(4000000)
                .setFileFilter(s->s.startsWith("aisdk"))
                .setWithHeader(true)
                .setSortColumns(new int[]{2,0})
                .setSortAscending(new boolean[]{true,true});

        sortBigFiles.go();
    }
}

