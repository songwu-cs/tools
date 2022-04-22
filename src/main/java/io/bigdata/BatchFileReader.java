package io.bigdata;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BatchFileReader implements Closeable, Iterable<List<String>> {
    private static final String VOID = "dummy";
    private final BufferedReader bufferedReader;
    private final String splitter;
    private final int[] keys;

    private boolean ended;
    private String previousKey = VOID;
    private String lineHelp = VOID;

    public BatchFileReader(String path, String splitter, boolean withHeader, int... indices) throws IOException {
        this.bufferedReader = new BufferedReader(new FileReader(path));
        this.splitter = splitter;
        keys = indices;
        if (withHeader)
            bufferedReader.readLine();
    }

    public List<String> readBatch() throws IOException {
        if(ended)
            return null;
        List<String> answer = new ArrayList<>();
        if (! lineHelp.equals(VOID))
            answer.add(lineHelp);

        String line = "";
        while ((line = bufferedReader.readLine()) != null){
            String[] parts = line.split(splitter);
            String key_ = "";
            for(int i : keys)
                key_ += parts[i];

            if(previousKey.equals(VOID)){
                previousKey = key_;
            }
            if(key_.equals(previousKey)){
                answer.add(line);
            }else {
                previousKey = key_;
                lineHelp = line;
                return answer;
            }
        }

        ended = true;
        if(answer.size() > 0)
            return answer;
        else
            return null;
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }

    @Override
    public Iterator<List<String>> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<List<String>>{
        List<String> lines;

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
        public List<String> next() {
            return lines;
        }
    }

    public static void main(String[] args) throws IOException {
        BatchFileReader reader = new BatchFileReader("H:\\UpanSky\\DEDS_Java\\tools\\src\\main\\resources\\1.txt", ",", true, 0,2);
        for(List<String> lines : reader)
            System.out.println(lines);
    }
}
