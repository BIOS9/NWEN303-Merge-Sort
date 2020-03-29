package ass1;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MParallelSorter2 implements Sorter {

    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        return MParallelSorter1.get(sortAsync(list));
    }

    private <T extends Comparable<? super T>> CompletableFuture<List<T>> sortAsync(final List<T> list) {
        if (list == null)
            return CompletableFuture.supplyAsync(() -> Collections.emptyList());

        if (list.size() < 20) {
            return CompletableFuture.supplyAsync(() -> MSequentialSorter.doSort(list));
        }

        int midIndex = list.size() / 2;
        final List<T> firstHalf = list.subList(0, midIndex);
        final List<T> secondHalf = list.subList(midIndex, list.size());

        return sortAsync(firstHalf).thenCombine(sortAsync(secondHalf), (sortedFirstHalf, sortedSecondHalf) ->
                MSequentialSorter.merge(sortedFirstHalf, sortedSecondHalf));
    }
}