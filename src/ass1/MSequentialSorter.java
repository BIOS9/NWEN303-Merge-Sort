package ass1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MSequentialSorter implements Sorter {

    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        if(list == null || list.size() == 0)
            return Collections.emptyList();

        if (list.size() == 1)
            return list;

        int midIndex = list.size() / 2;
        List<T> firstHalf = list.subList(0, midIndex);
        List<T> secondHalf = list.subList(midIndex, list.size());

        List<T> sortedFirstHalf = sort(firstHalf);
        List<T> sortedSecondHalf = sort(secondHalf);

        return merge(sortedFirstHalf, sortedSecondHalf);
    }

    /**
     * Merges two sorted lists together to form a single sorted list.
     * @param list1 A list of sorted T elements.
     * @param list2 A list of sorted T elements.
     * @param <T> A comparable type.
     * @return A sorted list.
     */
    private <T extends Comparable<? super T>> List<T> merge(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();

        int index1 = 0, index2 = 0;
        for (int i = 0; i < list1.size() + list2.size(); ++i) {

            // If all elements in list1 have been added
            // Add the rest of the elements in list2
            if(index1 == list1.size()) {
                result.addAll(list2.subList(index2, list2.size()));
                break;
            }

            // If all elements in list2 have been added
            // Add the rest of the elements in list1
            if(index2 == list2.size()) {
                result.addAll(list1.subList(index1, list1.size()));
                break;
            }

            T item1 = list1.get(index1);
            T item2 = list2.get(index2);

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