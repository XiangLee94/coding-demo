package indi.xianglee94.javacode.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestForkJoinSimple {
    private static final int NARRAY = 16; //For demo only
    long[] array = new long[NARRAY];
    Random rand = new Random();

    public void setUp() {
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextLong()%100; //For demo only
        }
        System.out.println("Initial Array: " + Arrays.toString(array));
    }


    public void testSort() throws Exception {
        ForkJoinTask sort = new SortTask(array);
        ForkJoinPool fjpool = new ForkJoinPool();
        fjpool.submit(sort);
        fjpool.shutdown();

        fjpool.awaitTermination(30, TimeUnit.SECONDS);

        assertTrue(checkSorted(array));
    }

    boolean checkSorted(long[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > (a[i + 1])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        TestForkJoinSimple testForkJoinSimple =new TestForkJoinSimple();
        testForkJoinSimple.setUp();
        try {
            testForkJoinSimple.testSort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SortTask extends RecursiveAction {
    final long[] array;
    final int lo;
    final int hi;
    private int THRESHOLD = 0; //For demo only

    public SortTask(long[] array) {
        this.array = array;
        this.lo = 0;
        this.hi = array.length - 1;
    }

    public SortTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    protected void compute() {
        if (hi - lo < THRESHOLD)
            sequentiallySort(array, lo, hi);
        else {
            int pivot = partition(array, lo, hi);
            System.out.println("\npivot = " + pivot + ", low = " + lo + ", high = " + hi);
            System.out.println("array" + Arrays.toString(array));
            new SortTask(array, lo, pivot - 1).fork();
            new SortTask(array, pivot + 1, hi).fork();
        }
    }

    private int partition(long[] array, int lo, int hi) {
        long x = array[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (array[j] <= x) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, hi);
        return i + 1;
    }

    private void swap(long[] array, int i, int j) {
        if (i != j) {
            long temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private void sequentiallySort(long[] array, int lo, int hi) {
        Arrays.sort(array, lo, hi + 1);
    }
}