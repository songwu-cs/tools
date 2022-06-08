package io.bigdata;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockFileReader implements Closeable, Iterable<List<String>> {
    private final BufferedReader bufferedReader;
    private final int blockSize;

    private boolean ended;

    public BlockFileReader(String path, String splitter, boolean withHeader, int blockSize) throws IOException {
        this.bufferedReader = new BufferedReader(new FileReader(path));
        if (withHeader)
            bufferedReader.readLine();
        this.blockSize = blockSize;
    }

    public List<String> readBatch() throws IOException {
        if(ended)
            return null;
        List<String> answer = new ArrayList<>();

        int counter = 0;
        String line = "";
        while ((line = bufferedReader.readLine()) != null){
            answer.add(line);
            counter++;
            if(counter == blockSize)
                return answer;
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
        BlockFileReader reader = new BlockFileReader("H:\\UpanSky\\DEDS_Java\\tools\\src\\main\\resources\\1.txt", ",", false, 2);
        for(List<String> lines : reader)
            System.out.println(lines);
    }
}
