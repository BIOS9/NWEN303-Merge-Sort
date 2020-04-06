package ass1;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class TestPerformance {
    /**
     * Measures the time to do an operation.
     * Since Java can use lazy loading, objects may not be initialized before the
     * timer starts meaning the initialization will be included in the time.
     * This method uses a warm up period to ensure the JVM has initialized all objects.
     *
     * @param r Runnable that contains the operation to be timed.
     * @param warmUp How many times to run the operation for the JVM warmup.
     * @param runs How many times to run the runnable, gives consistency.
     * @return How long the operations took in milliseconds.
     */
    long timeOf(Runnable r, int warmUp, int runs) {
        System.gc();
        for (int i = 0; i < warmUp; i++) {
            r.run();
        }
        long time0 = System.currentTimeMillis();
        for (int i = 0; i < runs; i++) {
            r.run();
        }
        long time1 = System.currentTimeMillis();
        return time1 - time0;
    }

    /**
     * Sorts the data in each of the dataset-blocks and measures the time for each block.
     *
     * @param s       Sorter to use to sort the data.
     * @param name    Name of the sorter to print in console.
     * @param dataset Contains the data to be sorted.
     * @param <T>     Comparable type.
     */
    <T extends Comparable<? super T>> void msg(Sorter s, String name, T[][] dataset) {
        long time = timeOf(() -> {
            for (T[] l : dataset) {
                s.sort(Arrays.asList(l));
            }
        }, 20000, 200);//realistically 20.000 to make the JIT do his job..
        System.out.println(name + " sort takes " + time / 1000d + " seconds");
    }

    /**
     * Executes the speed test on the dataset using all of the sorter types.
     *
     * @param dataset Dataset containing the data to be sorted.
     * @param <T>     Comparable type
     */
    <T extends Comparable<? super T>> void msgAll(T[][] dataset) {
        //msg(new ISequentialSorter(),"Sequential insertion",TestBigInteger.dataset);//so slow
        //uncomment the former line to include performance of ISequentialSorter
        msg(new MSequentialSorter(), "Sequential merge sort", dataset);
        msg(new MParallelSorter1(), "Parallel merge sort (futures)", dataset);
        msg(new MParallelSorter2(), "Parallel merge sort (completablefutures)", dataset);
        msg(new MParallelSorter2(), "Parallel merge sort (forkJoin)", dataset);
    }

    /**
     * Tests the performance of the BigInteger datatype.
     */
    @Test
    void testBigInteger() {
        System.out.println("On the data type BigInteger");
        msgAll(TestBigInteger.dataset);
    }

    /**
     * Tests the performance of the Float datatype.
     */
    @Test
    void testFloat() {
        System.out.println("On the data type Float");
        msgAll(TestFloat.dataset);
    }

    /**
     * Tests the performance of the Point datatype.
     */
    @Test
    void testPoint() {
        System.out.println("On the data type Point");
        msgAll(TestPoint.dataset);
    }

    /**
     * Tests the performance of the String datatype.
     */
    @Test
    void testString() {
        System.out.println("On the data type Point");
        msgAll(TestPoint.dataset);
    }
}
/*
With the model solutions, on a lab machine we may get those results:
On the data type Float
Sequential merge sort sort takes 1.178 seconds
Parallel merge sort (futures) sort takes 0.609 seconds
Parallel merge sort (completablefutures) sort takes 0.403 seconds
Parallel merge sort (forkJoin) sort takes 0.363 seconds
On the data type Point
Sequential merge sort sort takes 1.373 seconds
Parallel merge sort (futures) sort takes 0.754 seconds
Parallel merge sort (completablefutures) sort takes 0.541 seconds
Parallel merge sort (forkJoin) sort takes 0.48 seconds
On the data type BigInteger
Sequential merge sort sort takes 1.339 seconds
Parallel merge sort (futures) sort takes 0.702 seconds
Parallel merge sort (completablefutures) sort takes 0.452 seconds
Parallel merge sort (forkJoin) sort takes 0.492 seconds
*/