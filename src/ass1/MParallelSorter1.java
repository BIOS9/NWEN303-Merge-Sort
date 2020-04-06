package ass1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MParallelSorter1 implements Sorter {
    private static final ExecutorService pool = Executors.newWorkStealingPool(); // Was getting thread exhaustion with fixed thread pool.

    /**
     * Sorts a list of items using merge parallel merge sort implemented using futures.
     *
     * Futures allow greater control over the fork/join process than using a parallel stream does.
     * A major problem with futures in a nested/recursive arrangement is that lots of futures end up doing nothing
     * except waiting for other futures to complete. This can cause thread exhaustion with a fixed thread pool.
     *
     * I had never encountered thread exhaustion before, so this was a good learning experience to debug the program when
     * it occurred.
     */
    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        if (list == null)
            return Collections.emptyList();

        if (list.size() < 20) {
            return MSequentialSorter.doSort(list);
        }

        int midIndex = list.size() / 2;
        final List<T> firstHalf = list.subList(0, midIndex);
        final List<T> secondHalf = list.subList(midIndex, list.size());

        Future<List<T>> sortedFirstHalf = pool.submit(() -> sort(firstHalf)); // Fork first half
        List<T> sortedSecondHalf = sort(secondHalf); // Continue second half on current thread

        return MSequentialSorter.merge(get(sortedFirstHalf), sortedSecondHalf); // Wait and join here
    }

    /**
     * Provides error handling for Futures by converting checked exceptions to
     * unchecked exceptions.
     *
     * This method was taken from the NWEN303 2020 lecture 4 slides.
     */
    public static <T> T get(Future<T> f) {
        try {
            return f.get();
        } catch (InterruptedException e) { //we do not expect it
            Thread.currentThread().interrupt(); //just do it :(
            throw new Error(e); //turn it into an error
        } catch (ExecutionException e) {
            Throwable t = e.getCause();//propagate unchecked exceptions
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }//note: CancellationExceptionis a RuntimeException
            if (t instanceof Error) {
                throw (Error) t;
            }
            throw new Error("Unexpected Checked Exception", t);//our callable/closure did throw a checked exception
        }
    }
}