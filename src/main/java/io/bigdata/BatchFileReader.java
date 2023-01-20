package io.bigdata;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BatchFileReader implements Closeable, Iterable<List<String>> {
    private static final String VOID = "dummy";
    private final BufferedReader bufferedReader;
    private final List<BufferedReader> extraReaders;
    private BufferedReader currentReader;
    private int currentIndex = -1;
    private boolean valid;
    private final String splitter;
    private final int[] keys;

    private boolean ended;
    private String previousKey = VOID;
    private String lineHelp = VOID;

    public static void union(String outfile, String header, List<String> paths, String splitter, boolean withHeader, int... indices) throws IOException {
        try(BatchFileReader reader = new BatchFileReader(paths.get(0), splitter, withHeader, indices);
            PrintWriter writer = new PrintWriter(outfile)) {

            writer.write(header);
            writer.write("\n");

            for (String path : paths.subList(1, paths.size())){
                reader.addPath(path, withHeader);
            }

            int counter = 0;
            for (List<String> lines : reader){
                writer.write(String.join("\n", lines));
                writer.write("\n");
                counter += lines.size();
            }
            System.out.println("Total # of lines: " + counter);
        }
    }

    public BatchFileReader(String path, String splitter, boolean withHeader, int... indices) throws IOException {
        this.bufferedReader = new BufferedReader(new FileReader(path));
        currentReader = bufferedReader;
        extraReaders = new ArrayList<>();
        keys = indices;
        valid = true;
        this.splitter = splitter;
        if (withHeader)
            bufferedReader.readLine();
    }

    public BatchFileReader addPath(String path, boolean withHeader) throws IOException {
        if(! valid){
            throw new RuntimeException("Error: Trying to add new files after already reading data !!!");
        }

        extraReaders.add(new BufferedReader(new FileReader(path)));
        if(withHeader)
            extraReaders.get(extraReaders.size()-1).readLine();
        return this;
    }

    public List<String> readBatch() throws IOException {
        valid = false;

        if(ended)
            return null;
        List<String> answer = new ArrayList<>();
        if (! lineHelp.equals(VOID))
            answer.add(lineHelp);

        String line = "";
        while (true){
            while ((line = currentReader.readLine()) != null){
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

            currentIndex++;
            if(extraReaders.size() > currentIndex){
                currentReader = extraReaders.get(currentIndex);
            }else {
                break;
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
        for(BufferedReader reader : extraReaders)
            reader.close();
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
//        BatchFileReader reader = new BatchFileReader("H:\\UpanSky\\DEDS_Java\\tools\\src\\main\\resources\\1.txt", ",", true, 0,2);

        BatchFileReader reader = new BatchFileReader("C:\\Users\\TJUer\\Desktop\\1.txt", ",", false, 0);
        reader.addPath("C:\\Users\\TJUer\\Desktop\\2.txt", false);
        reader.addPath("C:\\Users\\TJUer\\Desktop\\3.txt", false);

        for(List<String> lines : reader)
            System.out.println(lines);
    }
}
