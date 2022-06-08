package io.bigdata;

import calculation.ListGeneric;
import calculation.UnitString;
import calculation.UnitStringSorter;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.function.Predicate;

public class MergeSortedFiles {
    private String baseDir;
    private String outfile;
    private String header;
    private int[] retainedColumns;

    private Predicate<String> fileFilter;
    private int[] sortColumns;
    private boolean[] sortAscending;
    private boolean withHeader;

    public MergeSortedFiles() {
    }

    public MergeSortedFiles setBaseDir(String baseDir) {
        this.baseDir = baseDir; return this;
    }

    public MergeSortedFiles setWithHeader(boolean withHeader) {
        this.withHeader = withHeader;
        return this;
    }

    public MergeSortedFiles setRetainedColumns(int[] indices){
        this.retainedColumns = indices; return this;
    }

    public MergeSortedFiles setOutfile(String outfile) {
        this.outfile = outfile; return this;
    }

    public MergeSortedFiles setFileFilter(Predicate<String> fileFilter) {
        this.fileFilter = fileFilter; return this;
    }

    public MergeSortedFiles setSortColumns(int[] sortColumns) {
        this.sortColumns = sortColumns; return this;
    }

    public MergeSortedFiles setSortAscending(boolean[] sortAscending) {
        this.sortAscending = sortAscending; return this;
    }

    public MergeSortedFiles setHeader(String header) {
        this.header = header;return this;
    }

    public void go() throws IOException{
        File dir = new File(baseDir);
        List<String> files = ListGeneric.filter(Arrays.asList(dir.list()), fileFilter);
        UnitStringSorter fileSorter = new UnitStringSorter(sortColumns, sortAscending);

        try(PrintWriter writer = new PrintWriter(baseDir + "\\" + outfile)){
            writer.write(header); writer.write("\n");

            PriorityQueue<String> priorityQueue = new PriorityQueue<>(fileSorter);

            Map<String, List<BufferedReader>> status = new HashMap<>();

            for(String file : files){
                BufferedReader bufferedReader = new BufferedReader(new FileReader(baseDir + "\\" + file));
                if(withHeader)
                    bufferedReader.readLine();

                String firstLine = bufferedReader.readLine();
                priorityQueue.add(firstLine);
                if(! status.containsKey(firstLine))
                    status.put(firstLine, new ArrayList<>());
                status.get(firstLine).add(bufferedReader);
            }

            while (! status.isEmpty()){
                String minimum = priorityQueue.peek();
                List<BufferedReader> readers = status.get(minimum);
                status.remove(minimum);

                for(int i = 0; i < readers.size(); i++){
                    priorityQueue.poll();
                    writer.write(UnitString.subset(minimum, ",", retainedColumns));
                    writer.write("\n");
                }

                String next = "";
                for(BufferedReader reader : readers){
                    if((next = reader.readLine()) != null){
                        priorityQueue.add(next);
                        if(! status.containsKey(next))
                            status.put(next, new ArrayList<>());
                        status.get(next).add(reader);
                    }else
                        reader.close();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MergeSortedFiles mergeSortedFiles = new MergeSortedFiles();
        mergeSortedFiles.setBaseDir("H:\\UpanSky\\DEDS_DenmarkAIS")
                .setWithHeader(false)
                .setRetainedColumns(new int[]{2,0,4,3,13})
                .setHeader("mmsi,timestamp,longitude,latitude,shiptype")
                .setOutfile("aisdk_oneweek_sorted.csv")
                .setFileFilter(s -> s.startsWith("tmp"))
                .setSortColumns(new int[]{2,0})
                .setSortAscending(new boolean[]{true,true});
        mergeSortedFiles.go();
    }
}
