package ass1;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MParallelSorter2 implements Sorter {

    /**
     * This method is the only blocking method.
     */
    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        return sortAsync(list).join();
    }

    /**
     * Parallel merge sort implemented using CompletableFutures.
     * Completable futures can use threads much more efficiently than regular futures because a new thread
     * is not created until the old thread has finished. This means that there are never any threads waiting
     * for other threads to finish.
     * It is very easy to control the order of operations when using CompletableFutures, for example you can
     * easily have one thread depend on the completion of two or more other threads (which is used in this method).
     *
     * I learned that CompletableFutures are a lot easier to use than they originally seemed, and provide a very fast
     * and efficient way to do parallelization.
     */
    private <T extends Comparable<? super T>> CompletableFuture<List<T>> sortAsync(final List<T> list) {
        if (list == null)
            return CompletableFuture.supplyAsync(() -> Collections.emptyList());

        if (list.size() < 20)
            return CompletableFuture.supplyAsync(() -> MSequentialSorter.doSort(list));

        int midIndex = list.size() / 2;
        final List<T> firstHalf = list.subList(0, midIndex);
        final List<T> secondHalf = list.subList(midIndex, list.size());

        return sortAsync(firstHalf).thenCombine(sortAsync(secondHalf), (sortedFirstHalf, sortedSecondHalf) ->
                MSequentialSorter.merge(sortedFirstHalf, sortedSecondHalf));
    }
}