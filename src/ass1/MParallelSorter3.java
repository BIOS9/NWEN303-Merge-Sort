package ass1;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MParallelSorter3<T extends Comparable<? super T>> extends RecursiveTask<List<T>> implements Sorter {
    public static final ForkJoinPool pool = new ForkJoinPool();
    private final List<T> list;

    public MParallelSorter3() {
        list = null;
    }

    public MParallelSorter3(List<T> list) {
        this.list = list;
    }

    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        return pool.invoke(new MParallelSorter3<>(list));
    }

    /**
     * Implementation of merge sort using ForkJoin library and RecursiveTask.
     * The ForkJoin library provides a very easy way to do recursive tasks.
     * Each recursive task goes into the pool, and then a pool of threads executes tasks.
     * This has the advantage that threads are not used just for waiting, they are executing work
     * tasks most of the time.
     *
     * I learned that the library uses tasks and doesnt use a whole thread for each RecursiveTask.
     */
    @Override
    protected List<T> compute() {
        if (list == null)
            return Collections.emptyList();

        if (list.size() < 20)
            return MSequentialSorter.doSort(list);

        int midIndex = list.size() / 2;
        List<T> firstHalf = list.subList(0, midIndex);
        List<T> secondHalf = list.subList(midIndex, list.size());

        MParallelSorter3<T> sortedFirstHalf = new MParallelSorter3<>(firstHalf);
        MParallelSorter3<T> sortedSecondHalf = new MParallelSorter3<>(secondHalf);
        invokeAll(sortedFirstHalf, sortedSecondHalf);

        return MSequentialSorter.merge(sortedFirstHalf.join(), sortedSecondHalf.join());
    }
}