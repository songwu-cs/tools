package calculation;

import java.util.Comparator;

public class UnitStringSorter implements Comparator<String> {
    private int[] sortColumns;
    private boolean[] sortAscending;

    public UnitStringSorter(int[] sortColumns, boolean[] sortAscending) {
        this.sortColumns = sortColumns;
        this.sortAscending = sortAscending;
    }

    @Override
    public int compare(String o1, String o2) {
        String[] parts1 = o1.split(",");
        String[] parts2 = o2.split(",");
        for(int i = 0; i < sortColumns.length; i++){
            int result = parts1[sortColumns[i]].compareTo(parts2[sortColumns[i]]) * (sortAscending[i] ? 1 : -1);
            if(result != 0)
                return result;
        }
        return 0;
    }
}
