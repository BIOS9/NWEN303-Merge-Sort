package ass1;

import java.util.ArrayList;
import java.util.List;

public class MSequentialSorter implements Sorter {

    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        if (list.size() <= 1)
            return list;

        int midIndex = list.size() / 2;
        List<T> firstHalf = list.subList(0, midIndex);
        List<T> secondHalf = list.subList(midIndex - 1, list.size() - 1);

        List<T> sortedFirstHalf = sort(firstHalf);
        List<T> sortedSecondHalf = sort(secondHalf);

        return merge(sortedFirstHalf, sortedSecondHalf);
    }

    private <T extends Comparable<? super T>> List<T> merge(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();

        int index1 = 0, index2 = 0;
        for (int i = 0; i < list1.size() + list2.size(); ++i) {
            T item1 = (index1 < list1.size()) ? list1.get(index1) : null;
            T item2 = (index2 < list2.size()) ? list2.get(index2) : null;

            if (item1 == null) {
                result.add(item2);
                ++index2;
                continue;
            }

            if (item2 == null) {
                result.add(item1);
                ++index1;
                continue;
            }

            if (item1.compareTo(item2) > 0) {
                result.add(item2);
                ++index2;
            } else if (item1.compareTo(item2) < 0) {
                result.add(item1);
                ++index1;
            } else {
                result.add(item1);
                ++index1;
            }
        }

        return result;
    }
}