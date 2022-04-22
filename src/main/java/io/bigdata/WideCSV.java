package io.bigdata;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class WideCSV implements Closeable, Iterable<List<WideLine>>{
    //文件必须有header
    private List<BatchFileReader> readers = new ArrayList<>();
    private int numberOfFiles = 0;
    private String splitter;
    List<Map<String,Integer>> columnName2Index = new ArrayList<>();

    public WideCSV addFile(String path, String splitter, int... indices) throws IOException {
        BatchFileReader fileReader = new BatchFileReader(path, splitter, false, indices);
        String header = fileReader.readBatch().get(0);
        String[] columns = header.split(splitter);
        int counter = 0;
        Map<String,Integer> map = new HashMap<>();
        for(String column : columns)
            map.put(column, counter++);

        this.splitter = splitter;
        readers.add(fileReader);
        numberOfFiles++;
        columnName2Index.add(map);
        return this;
    }

    public List<WideLine> readBatch() throws IOException {
        List<List<String>> lls = new ArrayList<>();
        for(BatchFileReader reader : readers)
            lls.add(reader.readBatch());
        if(lls.get(0) == null)
            return null;

        int numberOfLines = lls.get(0).size();
        List<WideLine> wideLineList = new ArrayList<>();
        for(int i = 0; i < numberOfLines; i++){
            List<String[]> lsArray = new ArrayList<>();
            for(List<String> lsL : lls){
                lsArray.add(lsL.get(i).split(splitter));
            }
            wideLineList.add(new WideLine(lsArray, this));
        }
        return wideLineList;
    }

    @Override
    public void close() throws IOException {
        for(BatchFileReader reader : readers)
            reader.close();
    }

    @Override
    public Iterator<List<WideLine>> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<List<WideLine>>{
        List<WideLine> lines;

        @Override
        public boolean hasNext() {
            try {
                lines = readBatch();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines != null;
        }

        @Override
        public List<WideLine> next() {
            return lines;
        }
    }
}
