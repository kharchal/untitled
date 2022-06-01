package bobo.paralel.merge.sort;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class Runner {

    public static void main(String[] args) {
//        int[] arr = {3, 9, 5, 2, 0, 7, 4, 8, 1, 6};
        int[] arr = generateArray(100_000_000);
        System.out.println(isSorted(arr));
//        System.out.println("arr = " + Arrays.toString(arr));
        ForkJoinPool.commonPool().invoke(new MergeSortAction(arr));
//        System.out.println("arr = " + Arrays.toString(arr));
        System.out.println(isSorted(arr));
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }
        return true;
    }

    private static int[] generateArray(int n) {
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] =ThreadLocalRandom.current().nextInt(n);
        }
        return array;
    }

}

class MergeSortAction extends RecursiveAction {

    private final int[] arr;

    public MergeSortAction(int[] arr) {
        this.arr = arr;
    }

    private void merge(int[] arr, int[] left, int[] right) {
        int leftInd = 0;
        int rightInd = 0;
        int i = 0;
        while (i < arr.length) {
            if (leftInd >= left.length) {
                arr[i++] = right[rightInd++];
            } else if (rightInd >= right.length) {
                arr[i++] = left[leftInd++];
            } else if (left[leftInd] < right[rightInd]) {
                arr[i++] = left[leftInd++];
            } else {
                arr[i++] = right[rightInd++];
            }
        }
    }

    @Override
    protected void compute() {
        if (arr.length <= 1) {
            return;
        }
        int middleIndex = arr.length / 2;
        int[] left = new int[middleIndex];
        System.arraycopy(arr, 0, left, 0, left.length);
        int[] right = new int[arr.length - middleIndex];
        System.arraycopy(arr, middleIndex, right, 0, right.length);

        MergeSortAction leftAction = new MergeSortAction(left);
        MergeSortAction rightAction = new MergeSortAction(right);

        leftAction.fork();
        rightAction.compute();
        leftAction.join();

        merge(arr, left, right);
    }
}
