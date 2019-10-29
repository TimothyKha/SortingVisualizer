package algorithms;

import visualization.VisualizationFrame;
import visualization.Visualizer;

public class QuickSort implements Runnable {
    private Integer[] sortArray;
    private VisualizationFrame frame;

    public QuickSort(Integer[] sortArray, VisualizationFrame frame) {
        this.sortArray = sortArray;
        this.frame = frame;
    }

    public void run() {
        sort();
        frame.linearWipe(sortArray);
        Visualizer.sorting = false;
    }

    private void sort() {
        quickSort(sortArray, 0, sortArray.length - 1);
    }

    private void quickSort(Integer[] arr, int begin, int end) {
        if (begin < end) {
            int partitionI = partition(arr, begin, end);
            quickSort(arr, begin, partitionI - 1);
            quickSort(arr, partitionI + 1, end);
        }
    }

    private void checkPause() {
        synchronized (Visualizer.threadPauser) {
            while (Visualizer.isPaused) {
                try {
                    Visualizer.threadPauser.wait();
                } catch (InterruptedException e) {

                }
            }
        }
    }

    private int partition(Integer[] arr, int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);
        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                frame.reDraw(sortArray, pivot, i, j);
                //frame.pivotDrawArray(sortArray, pivot, j);
                try {
                    Thread.sleep(Visualizer.sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkPause();
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = temp;
        return i + 1;
    }

}
